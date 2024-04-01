package algonquin.cst2335.ju000013.songApi;
/**
 * Purpose: This file is the database that stores favorite songs
 * Author: Wei Deng
 * Lab section: 2335-011
 * Date updated: 2024-03-30
 */
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Song.class}, version = 1)
public abstract class SongDatabase extends RoomDatabase {

    /**
     * Returns an instance of the SongDAO interface to perform database operations.
     *
     * @return The instance of the SongDAO interface.
     */
    public abstract SongDAO sDAO();

    /**
     * Singleton instance of the SongDatabase.
     */
    private static volatile SongDatabase INSTANCE;

    /**
     * Retrieves an instance of the SongDatabase using the singleton pattern.
     *
     * @param context The application context.
     * @return The singleton instance of the SongDatabase.
     */
    public static synchronized SongDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SongDatabase.class, "song-database")
                    .build();
        }
        return INSTANCE;
    }
}