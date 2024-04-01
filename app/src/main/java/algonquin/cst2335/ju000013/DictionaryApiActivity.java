/**
 * This activity serves as the main interface for the Dictionary API feature of the app. Users can
 * enter a word to search for its definitions using the DictionaryAPI.dev service. The activity allows
 * for the searching of words, displaying their definitions, and provides options to save definitions
 * and navigate to a saved words screen.
 *
 * @author Jungmin Ju
 * @labSection CST2335 011
 * @creationDate 2024-03-29
 */



package algonquin.cst2335.ju000013;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.widget.Toolbar;
import algonquin.cst2335.ju000013.R;
import androidx.appcompat.widget.Toolbar;
public class DictionaryApiActivity extends AppCompatActivity {
    private EditText editTextWord;
    private Button buttonSearch, btnSaveAll, btnGoToSaved;
    private RecyclerView recyclerViewDefinitions;
    private DictionaryAdapter dictionaryAdapter;
    private RequestQueue requestQueue;
    private AppDatabase db;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dictionary_search);

        Toolbar toolbar = findViewById(R.id.toolbar_dictionary);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("DictionaryPreferences", MODE_PRIVATE);
        editTextWord = findViewById(R.id.word_search_input);
        buttonSearch = findViewById(R.id.search_button);
        btnSaveAll = findViewById(R.id.save_all_button);
        btnGoToSaved = findViewById(R.id.go_to_saved_button);
        recyclerViewDefinitions = findViewById(R.id.definition_list);

        recyclerViewDefinitions.setLayoutManager(new LinearLayoutManager(this));
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "dictionary_db").allowMainThreadQueries().build();
        dictionaryAdapter = new DictionaryAdapter(new ArrayList<>(), db);
        recyclerViewDefinitions.setAdapter(dictionaryAdapter);

        requestQueue = Volley.newRequestQueue(this);
        editTextWord.setText(sharedPreferences.getString("lastSearch", ""));

        buttonSearch.setOnClickListener(v -> {
            String word = editTextWord.getText().toString().trim();
            if (!word.isEmpty()) {
                fetchDefinitions(word);
                sharedPreferences.edit().putString("lastSearch", word).apply();
            } else {
                Toast.makeText(DictionaryApiActivity.this, R.string.please_enter_a_word, Toast.LENGTH_SHORT).show();
            }
        });

        btnSaveAll.setOnClickListener(v -> saveAllDefinitions(dictionaryAdapter.getDefinitions()));
        btnGoToSaved.setOnClickListener(v -> startActivity(new Intent(DictionaryApiActivity.this, SavedWordsActivity.class)));
    }



    /**
     * Saves all currently displayed definitions to the database.
     *
     * @param definitions List of definitions to be saved.
     */
    private void saveAllDefinitions(List<Definition> definitions) {
        for (Definition definition : definitions) {
            new InsertWordTask().execute(new WordEntity(definition.getWord(), definition.getDefinition(), System.currentTimeMillis()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dictionary, menu);
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
     * Displays a help dialog to assist users with the functionality of the Dictionary API feature.
     */
    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.help_dialog_title)
                .setMessage(R.string.help_dialog_message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }


    /**
     * Initiates a request to fetch definitions for a given word using the DictionaryAPI.dev API.
     *
     * @param word The word for which definitions are to be fetched.
     */
    private void fetchDefinitions(String word) {
        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                this::parseAndDisplayDefinitions,
                error -> Toast.makeText(this, getString(R.string.error_fetching_definitions, error.getMessage()), Toast.LENGTH_SHORT).show()
        );
        requestQueue.add(jsonArrayRequest);
    }
    /**
     * Parses the JSON response from the API and updates the RecyclerView adapter with new definitions.
     *
     * @param response JSON response containing word definitions from the API.
     */
    private void parseAndDisplayDefinitions(JSONArray response) {
        try {
            List<Definition> definitions = new ArrayList<>();
            for (int i = 0; i < response.length(); i++) {
                JSONObject entry = response.getJSONObject(i);
                JSONArray meanings = entry.getJSONArray("meanings");
                for (int j = 0; j < meanings.length(); j++) {
                    JSONObject meaning = meanings.getJSONObject(j);
                    JSONArray definitionsArray = meaning.getJSONArray("definitions");
                    for (int k = 0; k < definitionsArray.length(); k++) {
                        JSONObject definitionObject = definitionsArray.getJSONObject(k);
                        String definitionText = definitionObject.getString("definition");
                        definitions.add(new Definition(entry.getString("word"), definitionText));
                    }
                }
            }
            runOnUiThread(() -> dictionaryAdapter.updateData(definitions));
        } catch (Exception e) {
            runOnUiThread(() -> Toast.makeText(this, getString(R.string.error_parsing_json_response, e.getMessage()), Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("lastSearch", editTextWord.getText().toString().trim());
    }
    /**
     * Inner class representing a definition object, containing a word and its definition.
     */
    private static class Definition {
        private final String word;
        private final String definition;

        Definition(String word, String definition) {
            this.word = word;
            this.definition = definition;
        }

        public String getWord() {
            return word;
        }

        public String getDefinition() {
            return definition;
        }
    }

    /**
     * AsyncTask class for inserting a word entity into the database.
     */
    private class InsertWordTask extends AsyncTask<WordEntity, Void, Void> {
        @Override
        protected Void doInBackground(WordEntity... wordEntities) {
            db.wordDao().insertWord(wordEntities[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(DictionaryApiActivity.this, R.string.definition_saved, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * RecyclerView adapter class for displaying word definitions in the UI.
     */
    class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.ViewHolder> {
        private List<Definition> definitions;
        private AppDatabase db;

        DictionaryAdapter(List<Definition> definitions, AppDatabase db) {
            this.definitions = definitions;
            this.db = db;
        }

        public List<Definition> getDefinitions() {
            return definitions;
        }

        void updateData(List<Definition> newDefinitions) {
            definitions.clear();
            definitions.addAll(newDefinitions);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.definition_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Definition definition = definitions.get(position);
            holder.textViewDefinition.setText(definition.getDefinition());
        }

        @Override
        public int getItemCount() {
            return definitions.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewDefinition;

            ViewHolder(View itemView) {
                super(itemView);
                textViewDefinition = itemView.findViewById(R.id.definition_text_view);
            }
        }
    }
}
