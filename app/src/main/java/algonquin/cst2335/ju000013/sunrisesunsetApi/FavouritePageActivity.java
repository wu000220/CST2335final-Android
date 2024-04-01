package algonquin.cst2335.ju000013.sunrisesunsetApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;

import algonquin.cst2335.ju000013.R;

/**
 * FavouritePageActivity provides a user interface to view and manage a list of saved favourite locations.
 * This activity includes functionalities such as displaying saved locations, allowing users to delete
 * favourites, and updating the list when new locations are added.
 *
 * Key Features:
 * - Displays a list of saved locations using a RecyclerView.
 * - Allows users to delete locations from the list.
 * - Updates the display when new locations are saved.
 *
 * This activity utilizes the FavouriteViewModel for managing and observing data changes,
 * and uses the FavouriteAdapter for presenting the data in a RecyclerView.
 *
 * @author Zhaohong Huang
 * @Labsection: CST2335-011
 * @Creation Date: 2024-03-29
 *
 */
public class FavouritePageActivity extends AppCompatActivity {
    // RecyclerView for displaying the list of favourite locations.
    private RecyclerView favouritesRecyclerView;

    // ViewModel used for accessing and managing data related to favourite locations.
    private FavouriteViewModel favouriteViewModel;

    // Adapter for the RecyclerView to display data.
    private FavouriteAdapter adapter;

    // Button for initiating the delete mode, allowing users to remove favourite locations.
    private Button deleteButton;

    /**
     * Initializes the activity, sets up the RecyclerView with the adapter and defines the delete functionality.
     * Observes changes in the LiveData from FavouriteViewModel to update the UI accordingly.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this Bundle contains the most recent data provided by onSaveInstanceState. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_page);

        favouritesRecyclerView = findViewById(R.id.recycleView);
        favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        favouriteViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);

        // Initialize the adapter with the context and an empty list
        adapter = new FavouriteAdapter(this, favouriteViewModel, new ArrayList<>(), favouritesRecyclerView); // 'this' is the Context

        favouritesRecyclerView.setAdapter(adapter);

        // Observe the LiveData list of favourites and update the adapter with new data
        favouriteViewModel.getFavourites().observe(this, newFavourites -> {
            if (newFavourites != null) {
                adapter.setFavourites(newFavourites);
            }
        });

        // Set up the delete button
        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> adapter.setDeleteMode(true));

        // Handling deletion from the adapter
        adapter.setOnItemDeleteListener((location, position) -> {
            new AlertDialog.Builder(FavouritePageActivity.this)
                    .setMessage(getString(R.string.deleteLocationAlert))
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        favouriteViewModel.deleteFavouriteById(location.getId());
                        adapter.removeItem(position);
                        adapter.setDeleteMode(false); // Exit delete mode
                    })
                    .setNegativeButton(getString(R.string.no), null)
                    .show();
        });
    }

    /**
     * This method is used to add a new favourite location to the ViewModel.
     * It can be invoked when a new location needs to be saved to the favourites list.
     *
     * @param newFavourite The new location object to be added to the list of favourites.
     */
    public void onNewFavouriteSaved(SaveLocation newFavourite) {
        // This method can be used to add a new favourite location
        favouriteViewModel.addFavourite(newFavourite);
    }
}