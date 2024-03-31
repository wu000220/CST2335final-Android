package algonquin.cst2335.ju000013.songApi;
/**
 * Purpose: This file is the view model for the first recycler view.
 * Author: Wei Deng
 * Lab section: 2335-011
 * Date updated: 2024-03-30
 */
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SongViewModel extends ViewModel {
    /** MutableLiveData object holding the list of songs */
    public MutableLiveData<ArrayList<Song>> songs = new MutableLiveData<>();

    /**
     * Retrieves the MutableLiveData object holding the list of songs.
     * @return The MutableLiveData object containing the list of songs.
     */
    public MutableLiveData<ArrayList<Song>> getSongs() {
        return songs;
    }
}

