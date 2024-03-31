package algonquin.cst2335.ju000013.songApi;
/**
 * Purpose: This file is the view model for the second recycler view
 * Author: Wei Deng
 * Lab section: 2335-011
 * Date updated: 2024-03-30
 */
import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class SongFavoritesViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Song>> songs;
    private final SongDAO songDAO;

    /**
     * Constructor for the SongFavoritesViewModel.
     *
     * @param application The application context.
     */
    public SongFavoritesViewModel(@NonNull Application application) {
        super(application);
        SongDatabase db = SongDatabase.getDatabase(application);
        songDAO = db.sDAO();
        songs = new MutableLiveData<>();
        loadSongs();
    }

    /**
     * Retrieves the LiveData object containing the list of favorite songs.
     *
     * @return LiveData object containing the list of favorite songs.
     */
    public LiveData<List<Song>> getSongs() {
        return songs;
    }

    /**
     * Loads the list of favorite songs from the database.
     */
    private void loadSongs() {
        // Run on a background thread
        new Thread(() -> {
            List<Song> songList = songDAO.getAllSongs();
            // Post the result to the UI thread
            new Handler(Looper.getMainLooper()).post(() -> songs.setValue(songList));
        }).start();
    }
}