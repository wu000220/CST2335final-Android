package algonquin.cst2335.ju000013.sunrisesunsetApi;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * MessageDatabase is a Room Database class that encapsulates a SQLite database for storing favourite locations.
 * It defines the database's structure and serves as the main access point for the underlying connection to the app's persisted data.
 *
 * This class includes:
 * - Singleton pattern to ensure only one instance of the database is created.
 * - ExecutorService for performing database operations asynchronously.
 * - A method to get the database instance.
 * - DAOs for accessing the database.
 *
 * @author Zhaohong Huang
 * @Labsection: CST2335-011
 * @Creation Date: 2024-03-29
 *
 */
@Database(entities = {SaveLocation.class}, version = 1, exportSchema = false)
public abstract class MessageDatabase extends RoomDatabase {

    // Singleton instance of the database to prevent multiple instances of the database opening at the same time.
    private static volatile MessageDatabase INSTANCE;


    /**
     * Abstract method to get an instance of the FavouriteDao.
     * This method is automatically implemented by Room to access the database.
     *
     * @return Instance of FavouriteDao.
     */
    public abstract FavouriteDao favouriteDao();

    // The number of threads to be used for asynchronous database operations.
    private static final int NUMBER_OF_THREADS = 4;

    // ExecutorService with a fixed thread pool for database write operations.
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    /**
     * Returns the singleton instance of the MessageDatabase.
     * If the instance is null, initializes a new database instance.
     * This method uses a synchronized block to avoid creating multiple instances in a multi-threaded environment.
     *
     * @param context The Context for accessing system resources and services.
     * @return The singleton instance of MessageDatabase.
     */
    public static MessageDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MessageDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MessageDatabase.class, "message_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this simplified code sample.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
