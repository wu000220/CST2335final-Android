package algonquin.cst2335.ju000013.songApi;
/**
 * Purpose: This file is the DAO between database and UI for operations
 * Author: Wei Deng
 * Lab section: 2335-011
 * Date updated: 2024-03-30
 */
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SongDAO {

    /**
     * Inserts a new song into the database.
     *
     * @param song The song to be inserted.
     * @return The row ID of the newly inserted song.
     */
    @Insert
    long insertSong(Song song);

    /**
     * Retrieves all songs from the database.
     *
     * @return A list containing all songs stored in the database.
     */
    @Query("SELECT * FROM Song")
    List<Song> getAllSongs();

    /**
     * Deletes a song from the database.
     *
     * @param song The song to be deleted.
     */
    @Delete
    void deleteSong(Song song);

    /**
     * Deletes all songs from the database.
     */
    @Query("DELETE FROM Song")
    void deleteAllSongs();

    /**
     * Retrieves a song from the database based on its title.
     *
     * @param title The title of the song to retrieve.
     * @return The song with the specified title, if found; otherwise, null.
     */
    @Query("SELECT * FROM Song WHERE title = :title")
    Song getSongByTitle(String title);
}