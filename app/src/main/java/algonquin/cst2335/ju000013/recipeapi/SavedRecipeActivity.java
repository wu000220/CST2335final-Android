package algonquin.cst2335.ju000013.recipeapi;

import static algonquin.cst2335.ju000013.recipeapi.RecipeSearchActivity.getBitmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.ju000013.R;
import algonquin.cst2335.ju000013.databinding.ActivitySavedRecipeBinding;
import algonquin.cst2335.ju000013.databinding.SaveResultBinding;

/** This page is a saved page for displaying saved recipes. User can see all the recipes saved in a recyclerView.
 * User can click on each object to see details and delete as needed.
 * There is a garage icon in the up-right corner to clear all saved in the account.
 * There also a question icon in the up-right corner to show displays an AlertDialog with instructions for how to use the APP.
 *
 * @author Fei Wu
 * @version 1.0
 * */
public class SavedRecipeActivity extends AppCompatActivity {
    ActivitySavedRecipeBinding binding;
    private  RecyclerView.Adapter savedAdapter;
    RecipeSavedViewModel savedModel;
    private ArrayList<RecipeSearched> savedRecipes;
    private RecipeSearchedDAO sDAO;
    RecyclerView recipeSavedRecycler;

    /**
     * Initializes the options menu for the activity.
     *
     * @param menu The options menu in which you place your items.
     * @return true for the menu to be displayed; false it will not be shown.
     */
    /* The point of this function is to load a Menu layout file. */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.saved_menu, menu);
        return true;
    }

    /**
     * Handles menu item selection.
     *
     * @param item The menu item that was selected.
     * @return true if the selection was handled, otherwise false.
     */
    /* When the user clicks on a menu item, Android will call this function. */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        AlertDialog.Builder builder = new AlertDialog.Builder( SavedRecipeActivity.this );
        if (id == R.id.recipe_delete_all){
            // delete all in database
            builder.setTitle(R.string.recipe_remove_all)
                    .setNegativeButton(getString(R.string.recipe_no), (dialog, cl) -> {})
                    .setPositiveButton(getString(R.string.recipe_yes), (dialog, cl) -> {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            sDAO.deleteAll(); // clear database
                            savedRecipes.clear(); // clear arraylist
                            // update the Adapter object that something's been removed so the RecyclerView can update itself
                            runOnUiThread(() -> savedAdapter.notifyDataSetChanged());
                        });
                        Snackbar.make(this.getCurrentFocus(), getString(R.string.recipe_clear_confirm), Snackbar.LENGTH_LONG).show();
                    })
                    .create()
                    .show();
        } else if (id == R.id.recipe_help) {
            // help menu item that displays an AlertDialog with instructions for how to use the recipe app
            builder.setMessage(R.string.recipe_help_content)
                    .create()
                    .show();

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* set toolbar */
        Toolbar recipeToolbar = binding.recipeToolbar;
        setSupportActionBar(recipeToolbar);
        recipeToolbar.setSubtitle(R.string.recipe_search);

        /* open a database */
        RecipeSavedDatabase db = Room.databaseBuilder(getApplicationContext(), RecipeSavedDatabase.class, "database-name").build();
        sDAO = db.rsDAO();

        /* achieve all the widgets */
        recipeSavedRecycler = binding.recipeSavedRecycler;
        /* To specify a single column scrolling in a Vertical direction */
        recipeSavedRecycler.setLayoutManager(new LinearLayoutManager(this));

        /* initialize and retrieve the ArrayList<> that it is storing in database */
        savedModel = new ViewModelProvider(this).get(RecipeSavedViewModel.class);
        savedRecipes = savedModel.savedRecipes.getValue();
        if (savedRecipes == null){
            savedModel.savedRecipes.postValue(savedRecipes = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                savedRecipes.addAll( sDAO.getAllSavedResults() ); // get the data from database to arrayList

                runOnUiThread( () ->  binding.recipeSavedRecycler.setAdapter( savedAdapter )); //load the RecyclerView
            });
        }

        /* set adapter to recycleView */
        recipeSavedRecycler.setAdapter(savedAdapter = new RecyclerView.Adapter<MySavedRowHolder>() {
            @NonNull
            @Override
            public MySavedRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SaveResultBinding saveResultBinding = SaveResultBinding.inflate(getLayoutInflater());
                return new MySavedRowHolder(saveResultBinding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MySavedRowHolder holder, int position) {
                RecipeSearched recipeSaved = savedRecipes.get(position);
                holder.recipe_saved_title.setText(recipeSaved.getTitle());
                holder.recipe_saved_url.setText(recipeSaved.getSource_url());
                // set image (String) to imageView
                String image_url = recipeSaved.getImage_url();
                new Thread(() -> {
                    try {
                        Bitmap bitmap = getBitmap(image_url);
                        // Set the loaded bitmap to the ImageView on the UI thread
                        holder.recipe_saved_url.post(() -> holder.result_image.setImageBitmap(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

            }

            @Override
            public int getItemCount() {
                return savedRecipes.size();
            }
        });

    }

    /**
     * Inner class representing a row holder for the RecyclerView adapter.
     */
    class MySavedRowHolder extends RecyclerView.ViewHolder {
        TextView recipe_saved_url;
        ImageView result_image;
        TextView recipe_saved_title;

        /**
         * Constructs a new MySavedRowHolder with the given View.
         *
         * @param itemView The View associated with the row holder.
         */
        public MySavedRowHolder(@NonNull View itemView) {
            super(itemView);
            recipe_saved_url = itemView.findViewById(R.id.recipe_saved_url_text);
            result_image = itemView.findViewById(R.id.recipe_result_image);
            recipe_saved_title = itemView.findViewById(R.id.recipe_saved_title_text);

            /* show detail when you click the item (row). */
            itemView.setOnClickListener(click -> {
                // tell you which row (position) this row is currently in the adapter object.
                int position = getAbsoluteAdapterPosition();

                /* show detail and ask for delete or not */
                RecipeSearched recipeDetail = savedRecipes.get(position);
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
                        runOnUiThread(() -> showDeleteAlertDialog(title, sourceUrl, finalDrawable, recipeDetail));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            });
        }

        /**
         * Shows an AlertDialog for deleting a saved recipe.
         *
         * @param title The title of the recipe.
         * @param sourceUrl The source URL of the recipe.
         * @param drawable The Drawable image of the recipe.
         * @param recipeDetail The RecipeSearched object representing the recipe details.
         */
        private void showDeleteAlertDialog(String title, String sourceUrl, Drawable drawable, RecipeSearched recipeDetail) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SavedRecipeActivity.this);
            builder.setTitle(getString(R.string.recipe_remove_alert))
                    .setIcon(drawable)
                    .setMessage(title + "\n" +"\n" +  sourceUrl)
                    .setNegativeButton(getString(R.string.recipe_no), (dialog, cl) -> {})
                    .setPositiveButton(getString(R.string.recipe_yes), (dialog, cl) -> {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            sDAO.deleteSavedResult(recipeDetail); // delete from database
                            int position = savedRecipes.indexOf(recipeDetail); // get the position of this object
                            savedRecipes.remove(position); // delete from arrayList
                            // update the Adapter object that something's been removed so the RecyclerView can update itself
                            runOnUiThread( () -> savedAdapter.notifyItemRemoved(position));
                        });
                        Toast.makeText(SavedRecipeActivity.this, getString(R.string.removed_alert), Toast.LENGTH_SHORT).show();
                    })
                    .show();
        }
    }
}