package algonquin.cst2335.ju000013.sunrisesunsetApi;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * FavouriteViewModel extends AndroidViewModel and is responsible for managing the data for the UI layer,
 * specifically for operations related to favourite locations saved in the database.
 * It provides methods to add, delete, retrieve, and update favourite locations, and manage user preferences.
 *
 * @author Zhaohong Huang
 * @Labsection: CST2335-011
 * @Creation Date: 2024-03-29
 *
 */
public class FavouriteViewModel extends AndroidViewModel {

    // Dao for accessing and manipulating favourite location data in the database.
    private FavouriteDao favouriteDao;

    // LiveData representing a list of saved favourite locations.
    private LiveData<List<SaveLocation>> favouritesList;

    /**
     * Constructor for FavouriteViewModel. Initializes the database and the DAO, and retrieves the initial list of favourites.
     *
     * @param application The application context, used to initialize the database and shared preferences.
     */
    public FavouriteViewModel(Application application) {
        super(application);
        // Constructor implementation
        MessageDatabase db = MessageDatabase.getDatabase(application);
        favouriteDao = db.favouriteDao();
        favouritesList = favouriteDao.getFavourites(); // for RecyclerView
    }

    /**
     * Deletes a favourite location by its ID.
     *
     * @param favouriteId The ID of the favourite location to be deleted.
     */
    public void deleteFavouriteById(int favouriteId) {
        // Method implementation
        MessageDatabase.databaseWriteExecutor.execute(() -> {
            favouriteDao.deleteFavouriteById(favouriteId);
        });
    }

    /**
     * Adds a new favourite location.
     *
     * @param saveLocation The location to be saved as a favourite.
     */
    public void addFavourite(SaveLocation saveLocation) {
        // Method implementation
        MessageDatabase.databaseWriteExecutor.execute(() -> {
            favouriteDao.insertFavourite(saveLocation);
        });
    }

    /**
     * Returns the LiveData list of favourite locations.
     *
     * @return A LiveData object containing the list of saved favourite locations.
     */
    public LiveData<List<SaveLocation>> getFavourites() {
        // Method implementation
        return favouritesList;
    }


    /**
     * This method is used to re-check the sunrise and sunset times based on the location of the collection clicked by the user
     *
     * Refreshes the sunrise and sunset times for a specific favourite location.
     * This could involve fetching the latest data from a remote API and updating the local database.
     *
     * @param favouriteId The ID of the favourite location to refresh.
     */
    public void refreshFavourite(int favouriteId) {
        // Method implementation
    }

    /**
     * Saves the most recent search term to SharedPreferences.
     * This can be used to persist the last search between app sessions.
     *
     * @param searchTerm The search term to be saved.
     */
    public void saveSearchTerm(String searchTerm) {
        SharedPreferences prefs = getApplication().getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("search_term", searchTerm);
        editor.apply(); // 使用 apply() 而不是 commit() 以异步保存数据
    }

    /**
     * Retrieves the saved search term from SharedPreferences.
     * Returns null if no search term is saved.
     *
     * @return The last saved search term or null if none exists.
     */
    public String getSavedSearchTerm() {
        SharedPreferences prefs = getApplication().getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        return prefs.getString("search_term", null); // 如果没有找到，则返回null
    }

    /**
     * Updates an existing favourite location in the database.
     * Typically used to reflect changes made to the location's details.
     *
     * @param saveLocation The updated location object to save.
     */
    public void updateFavourite(SaveLocation saveLocation) {
        MessageDatabase.databaseWriteExecutor.execute(() -> {
            favouriteDao.updateFavourite(saveLocation);
        });
    }

    /**
     * Inserts a new favourite location into the database.
     * This method is typically used when a user saves a new location as a favourite.
     *
     * @param saveLocation The location to be inserted into the database.
     */
    public void insertFavourite(SaveLocation saveLocation) {
        MessageDatabase.databaseWriteExecutor.execute(() -> {
            favouriteDao.insertFavourite(saveLocation);
        });
    }
}
