/**
 * SavedWordsAdapter is an adapter for the RecyclerView in SavedWordsActivity. It manages the display
 * of saved words, including their definition and the date they were saved. It also allows for selection
 * of items for potential deletion.
 *
 * @author Jungmin Ju
 * @labSection CST2335 011
 * @creationDate 2024-03-29
 */

package algonquin.cst2335.ju000013;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SavedWordsAdapter extends RecyclerView.Adapter<SavedWordsAdapter.ViewHolder> {
    private final List<WordEntity> words;
    private final HashSet<Long> selectedIds = new HashSet<>();

    /**
     * Constructor for SavedWordsAdapter.
     * @param words List of WordEntity objects to be displayed.
     */
    public SavedWordsAdapter(List<WordEntity> words) {
        this.words = words;
    }

    /**
     * Updates the dataset of the adapter and refreshes the RecyclerView.
     * @param newWords List of new WordEntity objects to be displayed.
     */
    public void updateData(List<WordEntity> newWords) {
        words.clear();
        words.addAll(newWords);
        notifyDataSetChanged();
    }

    /**
     * Retrieves a set of selected word IDs for deletion or manipulation.
     * @return HashSet of selected word IDs.
     */
    public HashSet<Long> getSelectedWordIds() {
        return new HashSet<>(selectedIds);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WordEntity wordEntity = words.get(position);
        holder.wordTextView.setText(wordEntity.word);
        holder.definitionTextView.setText(wordEntity.definition);
        holder.dateTextView.setText(DateFormat.getDateTimeInstance().format(new Date(wordEntity.savedDate)));
        holder.checkBox.setOnCheckedChangeListener(null); // Clear existing listeners
        holder.checkBox.setChecked(selectedIds.contains(wordEntity.id));
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedIds.add(wordEntity.id);
            } else {
                selectedIds.remove(wordEntity.id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    /**
     * Removes items from the adapter based on a set of IDs and refreshes the RecyclerView.
     * @param idsToRemove Set of WordEntity IDs to remove.
     */
    public void removeItems(Set<Long> idsToRemove) {
        words.removeIf(word -> idsToRemove.contains(word.id));
        notifyDataSetChanged();
        selectedIds.removeAll(idsToRemove);
    }

    /**
     * ViewHolder class for RecyclerView items. Holds references to the word, definition,
     * date text views, and the selection checkbox.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView wordTextView;
        final TextView definitionTextView;
        final TextView dateTextView;
        final CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.wordTextView);
            definitionTextView = itemView.findViewById(R.id.definitionTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}