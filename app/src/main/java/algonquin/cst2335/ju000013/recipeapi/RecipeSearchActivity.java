package algonquin.cst2335.ju000013.recipeapi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.ju000013.R;
import algonquin.cst2335.ju000013.databinding.ActivityRecipeSearchBinding;
import algonquin.cst2335.ju000013.databinding.SearchResultBinding;

/**
 * This page is a search page for recipe from https://spoonacular.com
 * After search, user can see lists of related recipes and add favorite recipe to local database.
 * User can view saved recipe as needed.
 *
 * @author Fei Wu
 * @version 1.0
 * @Labsection: CST2335-011
 * @CreationDate: 2024-03-29
 */
public class RecipeSearchActivity extends AppCompatActivity {

    ActivityRecipeSearchBinding binding;
    private RecyclerView.Adapter searchAdapter;
    RecipeSearchedViewModel searchModel;
    private ArrayList<RecipeSearched> searchedRecipes;
    private RecipeSearchedDAO sDAO;
    EditText editSearchText;
    Button buttonSearch;
    RecyclerView searchResultsRecycle;
    Button savedViewButton;
    TextView validateRecipeSearchText;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String searchText;
    private final String TAG = getClass().getSimpleName();
    private final String MY_KEY = "83ade364f8424054ae67e5f15fe178ec";
    private final String URL_REQUEST_DATA = "https://api.spoonacular.com/recipes/complexSearch?query=";
    private final String URL_API_PARAM = "&apiKey=" + MY_KEY;
    protected RequestQueue queue;
    private final String URL_DETAIL_DATA = "https://api.spoonacular.com/recipes/";
    private final String URL_DETAIL_PARAM = "/information?apiKey=" + MY_KEY;

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* open database */
        RecipeSavedDatabase db = Room.databaseBuilder(getApplicationContext(), RecipeSavedDatabase.class, "database-name").build();
        sDAO = db.rsDAO();

        /* achieve all the widgets */
        editSearchText = binding.editRecipeSearchText;
        buttonSearch = binding.buttonSearchRecipe;
        searchResultsRecycle = binding.searchRecipeRecycler;
        savedViewButton = binding.viewSavedRecipeButton;
        validateRecipeSearchText = binding.validateRecipeSearchText;

        /* save search text to SharedPreference to show automatically next time. */
        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        editor = prefs.edit();

        queue = Volley.newRequestQueue(this);
        /* To specify a single column scrolling in a Vertical direction */
        searchResultsRecycle.setLayoutManager(new LinearLayoutManager(this));

        /* initialize the ArrayList<> that it is storing */
        searchModel = new ViewModelProvider(this).get(RecipeSearchedViewModel.class);
        searchedRecipes = searchModel.searchedRecipes.getValue();
        if (searchedRecipes == null) {
            searchModel.searchedRecipes.postValue(searchedRecipes = new ArrayList<>());
        }

        /* set adapter to recycleView */
        searchResultsRecycle.setAdapter(searchAdapter = new RecyclerView.Adapter<MySearchRowHolder>() {
            @NonNull
            @Override
            public MySearchRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SearchResultBinding binding1 = SearchResultBinding.inflate(getLayoutInflater());
                return new MySearchRowHolder(binding1.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MySearchRowHolder holder, int position) {
                RecipeSearched recipeSearched = searchedRecipes.get(position);
                holder.result_title_text.setText(recipeSearched.getTitle());
                holder.result_id_text.setText(String.format(Locale.CANADA, "%d", recipeSearched.getId()));
                // set image (String) to imageView
                String image_url = recipeSearched.getImage_url();
                new Thread(() -> {
                    try {
                        Bitmap bitmap = getBitmap(image_url);
                        // Set the loaded bitmap to the ImageView on the UI thread
                        holder.result_image.post(() -> holder.result_image.setImageBitmap(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }

            @Override
            public int getItemCount() {
                return searchedRecipes.size();
            }
        });

        /* search function */
        buttonSearch.setOnClickListener(click -> {
            searchText = editSearchText.getText().toString();
            try {
                if (validateRecipeSearch(searchText)) {
                    validateRecipeSearchText.setText(R.string.recipe_validate_correct_msg);
                    // Clear the previous search results from arrayList
                    searchedRecipes.clear();
                    searchAdapter.notifyDataSetChanged();// Notify the adapter that the data set has changed

                    String url = URL_REQUEST_DATA + URLEncoder.encode(searchText, getString(R.string.recipe_utf_8)) + URL_API_PARAM;

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            (response -> {
                                try {
                                    Log.d(TAG, getString(R.string.recipe_web_response) + response.toString());
                                    JSONArray results = response.getJSONArray(getString(R.string.recipe_json_array_name));
                                    for (int i = 0; i < results.length(); i++) {
                                        JSONObject jsonObject = results.getJSONObject(i);
                                        String title = jsonObject.getString(getString(R.string.recipe_json_title));
                                        String image = jsonObject.getString(getString(R.string.recipe_json_image));
                                        int id = jsonObject.getInt(getString(R.string.recipe_json_id));
                                        String sourceUrl = URL_DETAIL_DATA + id + URL_DETAIL_PARAM;
                                        /* add to search result arraylist*/
                                        searchedRecipes.add(new RecipeSearched(title, image, id, sourceUrl));

                                    }
                                    searchAdapter.notifyItemInserted(searchedRecipes.size() - 1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }),
                            (error -> {
                                Log.e(TAG, "Error:" + error.getMessage());
                            }));
                    queue.add(request);
                } else {
                    validateRecipeSearchText.setText(R.string.recipe_validate_error_msg);
                }
            } catch (Exception e) {
                Log.e(TAG, getString(R.string.error_encoding_recipe_name));
            }
            /* save search text to SharedPreference */
            editor.putString(getString(R.string.recipe_search_content), searchText);
            editor.apply();
        });

        /* achieve search text from SharedPreference */
        String searchText = prefs.getString(getString(R.string.recipe_search_content), "");
        editSearchText.setText(searchText);

        /* view saved favorite recipes button. jump to the next page. */
        Intent savedRecipePage = new Intent(RecipeSearchActivity.this, SavedRecipeActivity.class);
        savedViewButton.setOnClickListener(click -> {
            startActivity(savedRecipePage);
        });
    }

    /**
     * This function check if this string has a special symbol.
     * If has a special symbol, then show a Toast message saying not validate.
     *
     * @param searchText The String object that we are checking.
     * @return Return true if the string not contain special symbol, and false if it is has.
     */
    private boolean validateRecipeSearch(String searchText) {
        for (int i = 0; i < searchText.length(); i++) {
            char c = searchText.charAt(i);
            if (containSpecialCharacter(c)) {
                Toast.makeText(this, R.string.recipe_search_text_contain_special_character, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    /**
     * This function check if there is a special character.
     *
     * @param c The character that we are checking.
     * @return Return true if there is a special character, and false if not.
     */
    private boolean containSpecialCharacter(char c) {
        switch (c) {
            case '#':
            case '@':
            case '?':
            case '!':
            case '*':
            case '&':
            case '^':
            case '%':
            case '$':
                return true;
            default:
                return false;
        }
    }

    /**
     * Utility method to retrieve a bitmap image from a URL.
     *
     * @param url The URL of the image.
     * @return The Bitmap image retrieved from the URL.
     * @throws IOException If an I/O error occurs while retrieving the image.
     */
    /* transfer String url to bitmap. */
    public static Bitmap getBitmap(String url) throws IOException {
        URL url1 = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream inputStream = connection.getInputStream();
        return BitmapFactory.decodeStream(inputStream);
    }

    /**
     * Inner class representing a row holder for the RecyclerView adapter.
     */
    class MySearchRowHolder extends RecyclerView.ViewHolder {
        TextView result_title_text;
        ImageView result_image;
        TextView result_id_text;

        /**
         * Constructs a new MySearchRowHolder with the given View.
         *
         * @param itemView The View associated with the row holder.
         */
        public MySearchRowHolder(@NonNull View itemView) {
            super(itemView);
            result_title_text = itemView.findViewById(R.id.recipe_title_text);
            result_image = itemView.findViewById(R.id.recipe_result_image);
            result_id_text = itemView.findViewById(R.id.recipe_result_id_text);

            /* show detail when you click the item (row). */
            itemView.setOnClickListener(click -> {
                // tell you which row (position) this row is currently in the adapter object.
                int position = getAbsoluteAdapterPosition();

                /* show detail and ask for save or not */
                RecipeSearched recipeDetail = searchedRecipes.get(position);
                String title = recipeDetail.getTitle();
                String image_url = recipeDetail.getImage_url();
                String sourceUrl = recipeDetail.getSource_url();
                Executor thread = Executors.newSingleThreadExecutor();
                thread.execute(() ->
                {
                    try {
                        Bitmap bitmap = getBitmap(image_url);
                        // Convert Bitmap to Drawable
                        Drawable drawable = null;
                        if (bitmap != null) {
                            drawable = new BitmapDrawable(getResources(), bitmap);
                        }
                        // Show AlertDialog on UI thread
                        Drawable finalDrawable = drawable;
                        runOnUiThread(() -> showSaveAlertDialog(title, sourceUrl, finalDrawable, recipeDetail));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            });
        }

        /**
         * Shows an AlertDialog for saving a recipe.
         *
         * @param title        The title of the recipe.
         * @param sourceUrl    The source URL of the recipe.
         * @param drawable     The Drawable image of the recipe.
         * @param recipeDetail The RecipeSearched object representing the recipe details.
         */
        private void showSaveAlertDialog(String title, String sourceUrl, Drawable drawable, RecipeSearched recipeDetail) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RecipeSearchActivity.this);
            builder.setTitle(getString(R.string.recipe_save_alert))
                    .setIcon(drawable)
                    .setMessage(title + "\n" + "\n" + sourceUrl)
                    .setNegativeButton(getString(R.string.recipe_no), (dialog, cl) -> {
                    })
                    .setPositiveButton(getString(R.string.recipe_yes), (dialog, cl) -> {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            sDAO.insertRecipe(recipeDetail); // insert into database
                        });
                        Toast.makeText(RecipeSearchActivity.this, getString(R.string.recipe_add_alert), Toast.LENGTH_SHORT).show();
                    })
                    .show();
        }
    }
}