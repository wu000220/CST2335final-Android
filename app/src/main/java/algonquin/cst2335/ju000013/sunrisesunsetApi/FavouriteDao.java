package algonquin.cst2335.ju000013.sunrisesunsetApi;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import algonquin.cst2335.ju000013.sunrisesunsetApi.SaveLocation;

import java.util.List;

/**
 * FavouriteDao defines the data access object (DAO) for the favourites table.
 * This interface provides methods for performing various operations such as
 * inserting, querying, deleting, and updating records in the favourites table.
 *
 * @author Zhaohong Huang
 * @Labsection: CST2335-011
 * @Creation Date: 2024-03-29
 *
 */
@Dao
public interface FavouriteDao {

    /**
     * Inserts a new favourite location into the database.
     *
     * @param saveLocation The location object to be inserted.
     * @return The row ID of the newly inserted location, as a long.
     */
    @Insert
    Long insertFavourite(SaveLocation saveLocation);

    /**
     * Retrieves all saved favourite locations.
     *
     * @return LiveData containing a list of all saved locations.
     */
    @Query("SELECT * FROM favourites")
    LiveData<List<SaveLocation>> getFavourites();

    /**
     * Deletes a favourite location based on its ID.
     *
     * @param favouriteId The ID of the location to delete.
     * @return The number of rows affected by the delete operation.
     */
    @Query("DELETE FROM favourites WHERE id = :favouriteId")
    int deleteFavouriteById(int favouriteId);

    /**
     * Retrieves a specific favourite location by its ID.
     *
     * @param favouriteId The ID of the location to retrieve.
     * @return The SaveLocation object corresponding to the specified ID.
     */
    @Query("SELECT * FROM favourites WHERE id = :favouriteId")
    SaveLocation getFavouriteById(int favouriteId);

    /**
     * Updates an existing favourite location in the database.
     *
     * @param saveLocation The SaveLocation object with updated information.
     * @return The number of rows affected by the update operation.
     */
    @Update
    int updateFavourite(SaveLocation saveLocation);
}
