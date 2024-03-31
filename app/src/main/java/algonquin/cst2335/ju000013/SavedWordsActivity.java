/**
 * SavedWordsActivity provides an interface for users to view, delete, and manage words that have been
 * saved from previous searches. It utilizes a RecyclerView to display the list of saved words and offers
 * functionality to delete individual or multiple selected words.
 *
 * @author Jungmin Ju
 * @labSection CST2335 011
 * @creationDate 2024-03-29
 */

package algonquin.cst2335.ju000013;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SavedWordsActivity extends AppCompatActivity {
    private RecyclerView savedWordsRecyclerView;
    private SavedWordsAdapter savedWordsAdapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_words);
        Toolbar toolbar = findViewById(R.id.toolbar_saved_words);
        setSupportActionBar(toolbar);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "dictionary_db").build();
        savedWordsRecyclerView = findViewById(R.id.savedWordsRecyclerView);
        savedWordsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        savedWordsAdapter = new SavedWordsAdapter(new ArrayList<>());
        savedWordsRecyclerView.setAdapter(savedWordsAdapter);

        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<Long> selectedIds = savedWordsAdapter.getSelectedWordIds();
                new DeleteWordsTask().execute(selectedIds.toArray(new Long[0]));
            }
        });

        Button goBackButton = findViewById(R.id.go_back_button);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SavedWordsActivity.this, DictionaryApiActivity.class));
            }
        });

        loadSavedWords();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            showHelpDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Displays a help dialog to provide guidance to users on how to interact with the saved words interface.
     */
    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.help_dialog_title)
                .setMessage(R.string.help_dialog_message)
                .setPositiveButton("OK", null)
                .show();
    }



    /**
     * Initiates the process of loading saved words from the database asynchronously.
     */
    private void loadSavedWords() {
        new LoadWordsTask().execute();
    }

    private class LoadWordsTask extends AsyncTask<Void, Void, List<WordEntity>> {
        @Override
        protected List<WordEntity> doInBackground(Void... voids) {
            return db.wordDao().getAllWords();
        }

        @Override
        protected void onPostExecute(List<WordEntity> savedWords) {
            savedWordsAdapter.updateData(savedWords);
        }
    }

    /**
     * AsyncTask for deleting selected words from the database. On completion, updates the RecyclerView adapter to reflect changes.
     */
    private class DeleteWordsTask extends AsyncTask<Long, Void, Set<Long>> {
        @Override
        protected Set<Long> doInBackground(Long... ids) {
            db.wordDao().deleteWordsByIds(new ArrayList<>(Arrays.asList(ids)));
            return new HashSet<>(Arrays.asList(ids));
        }

        @Override
        protected void onPostExecute(Set<Long> deletedIds) {
            savedWordsAdapter.removeItems(deletedIds);
            Toast.makeText(SavedWordsActivity.this, R.string.words_deleted, Toast.LENGTH_SHORT).show();
        }
    }
}