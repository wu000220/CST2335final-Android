package algonquin.cst2335.ju000013.songApi;
/**
 * Purpose: This file is to search for songs from album of artist and display with the first recycler view.
 * Author: Wei Deng
 * Lab section: 2335-011
 * Date updated: 2024-03-30
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import algonquin.cst2335.ju000013.DictionaryApiActivity;
import algonquin.cst2335.ju000013.R;
import algonquin.cst2335.ju000013.databinding.ActivitySongSearchBinding;


public class SongSearchActivity extends AppCompatActivity {
    /**
     * private attributes
     */
    ActivitySongSearchBinding songBinding;
    ArrayList<Song> songsEntity;
    Song song;
    SongViewModel songViewModel;
    SongAdapter songAdapter;

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String ARTIST_NAME_KEY = "artist_name";


    private final String URL_REQUEST_DATA = "https://api.deezer.com/search/artist/?q=";
    protected String artistName;
    protected RequestQueue queue;
    private final String TAG = getClass().getSimpleName();

    private String helpTitle;
    private String instructions;

    /**
     * To create menu option by inflating
     * @param menu The options menu in which you place your items.
     *
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_toolbar_song, menu);
        return true;
    }

    /**
     * to select menu options by id
     * @param item The menu item that was selected.
     *
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) { // Handle the Up button click
            onBackPressed(); // Go back to the previous activity
            return true;
        }
        else if (id == R.id.item_1) {

        }
        else if (id == R.id.item_2) {
//            Intent intent = new Intent(SongSearchActivity.this, RecipeSearchActivity.class);
//            startActivity(intent);
        } else if (id == R.id.item_3) {
            Intent intent = new Intent(SongSearchActivity.this, DictionaryApiActivity.class);
            startActivity(intent);
        } else if (id == R.id.item_4) {
            Intent intent = new Intent(SongSearchActivity.this, SongSearchActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.item_help) {
            showHelpDialog(); // Show help dialog when help menu item is clicked
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * to show help dialog when clicked
     */
    private void showHelpDialog() {
        helpTitle = getString(R.string.help_title);
        instructions = getString(R.string.instructions);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(helpTitle);
        builder.setMessage(instructions);
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Handle OK button click
                dialog.dismiss(); // Dismiss the dialog
            }
        });
        builder.show();
    }

    /**
     * methods to apply when instance is loaded.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songBinding = ActivitySongSearchBinding.inflate(getLayoutInflater());
        setContentView(songBinding.getRoot());
/**
 * to use the toolbar
 */
        Toolbar tool_bar = findViewById(R.id.toolbar);
        setSupportActionBar(tool_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable Up button in the toolbar
/**
 * get entity from model
 */
        songViewModel = new ViewModelProvider(this).get(SongViewModel.class);
        songsEntity = songViewModel.songs.getValue();
/**
 * check if entity is null
 */
        if (songsEntity == null) {
            songViewModel.songs.postValue(songsEntity = new ArrayList<>());
        }
/**
 * prepare to get info from volley
 */
        queue = Volley.newRequestQueue(this);
/**
 * initiate shared preference
 */
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        /**
         * get value from shared preference
         */
        artistName = prefs.getString(ARTIST_NAME_KEY, "");
        songBinding.etSong.setText(artistName);
/**
 * onclick listener for search
 */
        songBinding.btnSongSearch.setOnClickListener(click -> {
            artistName = songBinding.etSong.getText().toString();

            if (!isValidArtistName(artistName)) {
                // Show error message in the validation TextView
                songBinding.tvValidation.setText(getString(R.string.enterLettersOnly));
                return;
            } else {
                // Clear the validation message if artist name is valid
                songBinding.tvValidation.setText(getString(R.string.validationPassed));
            }
/**
 * store into shared ref
 */
            //save in shared preference
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(ARTIST_NAME_KEY, artistName);
            editor.apply();
            /**
             * Construct the URL with the artist's name
             */
            String url;
            try {
                url = URL_REQUEST_DATA + URLEncoder.encode(artistName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "Error encoding URL: " + e.getMessage());
                return;
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response) -> {
                        try {
                            /**
                             * Handle successful response
                             * Get the "data" array from the JSON response
                              */
                            JSONArray dataArray = response.getJSONArray("data");
                            songsEntity.clear();
                            String tracklistUrl = null;
                            if (dataArray.length() > 0) {
                                /**
                                 *  Get the first object from the array
                                 */
                                JSONObject firstObject = dataArray.getJSONObject(0);
                                /**
                                 * Get the tracklist URL from the first object
                                 */
                                tracklistUrl = firstObject.getString("tracklist");

                                JsonObjectRequest tracklistRequest = new JsonObjectRequest(Request.Method.GET, tracklistUrl, null,
                                        (tracklistResponse) -> {
                                            try {
                                                /**
                                                 * Handle successful response for tracklist
                                                 * Get the "data" array from the JSON response
                                                 */
                                                JSONArray dataArray2 = tracklistResponse.getJSONArray("data");

                                                for (int i = 0; i < dataArray2.length(); i++) {
                                                    JSONObject songObject = dataArray2.getJSONObject(i);
                                                    String songTitle = songObject.getString("title");
                                                    int duration = songObject.getInt("duration");

                                                    JSONObject albumObject = songObject.getJSONObject("album");
                                                    String albumName = albumObject.getString("title");
                                                    String albumCoverUrl = albumObject.getString("cover");

                                                    /**
                                                     *  Create a new Song instance and add it to the list
                                                     */
                                                    song = new Song(songTitle, duration, albumName, albumCoverUrl);
                                                    songsEntity.add(song);
                                                }
                                                songAdapter.notifyDataSetChanged();
                                                songViewModel.songs.setValue(songsEntity);
                                            } catch (JSONException e) {
                                                Log.e(TAG, "Error parsing tracklist JSON: " + e.getMessage());
                                            }
                                        },
                                        (tracklistError) -> {
                                            /**
                                             * Handle error response for tracklist
                                              */
                                            Log.e(TAG, "Error fetching tracklist: " + tracklistError.getMessage());
                                        });
                                queue.add(tracklistRequest);
                            } else {
                                /**
                                 * No artist found with the given name
                                 */
                                showSnackbar("No tracklist found for the artist: " + artistName);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing JSON: " + e.getMessage());
                        }
                    },
                    (error) -> {
                        /**
                         * Handle error response
                         */
                        Log.e(TAG, "Error fetching artist data: " + error.getMessage());
                    });
            queue.add(request);
        });

        songBinding.rvSongs.setLayoutManager(new LinearLayoutManager(this));
        songAdapter = new SongAdapter(songsEntity,this);//put arraylist into adapter
        songBinding.rvSongs.setAdapter(songAdapter);//set adapter to recycler view
    }

    /**
     * Inside the SongSearchActivity class
     * @param message
     */
    private void showSnackbar(String message) {
        Snackbar.make(songBinding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    private boolean isValidArtistName(String artistName) {
        // Return true if the artistName contains only letters (no numbers or special characters)
        return artistName.matches("[a-zA-Z]+");
    }
}



