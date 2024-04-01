package algonquin.cst2335.ju000013.songApi;
/**
 * Purpose: This file is to display songs details and add, delete or display database items
 * Author: Wei Deng
 * Lab section: 2335-011
 * Date updated: 2024-03-30
 */
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import algonquin.cst2335.ju000013.DictionaryApiActivity;
import algonquin.cst2335.ju000013.R;
import algonquin.cst2335.ju000013.databinding.ActivitySongDetailsBinding;


public class SongDetailsActivity extends AppCompatActivity {
    /**
     * private attributes to be used.
     */
    private ActivitySongDetailsBinding songDetailsBinding;
    private SongDatabase songDatabase;
    private SongDAO songDAO;

    private String helpTitle;
    private String instructions;

    /**
     * To create the option menu
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
     * To select the options on the menu
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
//            Intent intent = new Intent(SongDetailsActivity.this, RecipeSearchActivity.class);
//            startActivity(intent);
        } else if (id == R.id.item_3) {
            Intent intent = new Intent(SongDetailsActivity.this, DictionaryApiActivity.class);
            startActivity(intent);
        } else if (id == R.id.item_4) {
            Intent intent = new Intent(SongDetailsActivity.this, SongSearchActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.item_help) {
            showHelpDialog(); // Show help dialog when help menu item is clicked
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * To show the help dialog
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
     * All method when you creates
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songDetailsBinding = ActivitySongDetailsBinding.inflate(getLayoutInflater());
        setContentView(songDetailsBinding.getRoot());

        Toolbar tool_bar = findViewById(R.id.toolbarDetails);
        setSupportActionBar(tool_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        songDatabase = Room.databaseBuilder(getApplicationContext(),
                SongDatabase.class, "song-database").build();
        songDAO = songDatabase.sDAO();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            /**
             * Check the source of the extras
             */

            String source = extras.getString("SOURCE");
            if ("FAVORITES".equals(source)) {
                /**
                 * Extras are from SongFavoritesActivity
                 */
                String title = extras.getString("SONG_TITLE");
                int duration = extras.getInt("SONG_DURATION");
                String albumName = extras.getString("SONG_ALBUM_NAME");
                String albumCoverUrl = extras.getString("SONG_ALBUM_COVER_URL");
                /**
                 * Set the details in the views
                 */
                songDetailsBinding.tvSongTitleDetail.setText(title);
                songDetailsBinding.tvSongDurationDetail.setText(String.valueOf(duration));
                songDetailsBinding.tvSongAlbumNameDetail.setText(albumName);

                new Thread(() -> {
                    try {
                        URL detailUrl = new URL(albumCoverUrl);
                        HttpURLConnection connection = (HttpURLConnection) detailUrl.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream inputStream = connection.getInputStream();
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        /**
                         * Update ImageView on the main UI thread
                          */

                        runOnUiThread(() -> songDetailsBinding.ivSongAlbumCoverDetail.setImageBitmap(bitmap));
                    } catch (IOException e) {
                        Log.e(null, "Error downloading image: " + e.getMessage());
                    }
                }).start();

                songDetailsBinding.btnAddFavorite.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        new Thread(() -> {
                            Song existingSong = songDAO.getSongByTitle(title);
                            if (existingSong == null) {
                                /**
                                 * Song does not exist, insert it into the database
                                 */
                                insertSongIntoDatabase(title, duration, albumName, albumCoverUrl);
                                runOnUiThread(() -> {
                                    /**
                                     * Show a message indicating that the song was added to favorites
                                      */
                                    String message = getString(R.string.songAdded) + " " + title;
                                    showSnackbar(message);
                                });
                            } else {
                                /**
                                 * Song already exists in the database
                                 */
                                runOnUiThread(() -> {
                                    /**
                                     * Show a message indicating that the song is already in favorites
                                     */
                                    String message = getString(R.string.songAlreadyExist) + " " + title;
                                    showSnackbar(message);
                                });
                            }
                        }).start();
                    }
                });

                songDetailsBinding.btnShowFavorites.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SongDetailsActivity.this, SongFavoritesActivity.class);
                        startActivity(intent);
                    }
                });

                songDetailsBinding.btnDeleteFavorite.setOnClickListener(v -> {
                    /**
                     * Check if the song exists in the database
                      */

                    new Thread(() -> {
                        Song existingSong = songDAO.getSongByTitle(title);
                        if (existingSong != null) {
                            /**
                             * Song exists, delete it from the database
                             */
                            songDAO.deleteSong(existingSong);
                            /**
                             * Update UI or perform any other actions if needed
                             */
                            runOnUiThread(() -> {
                                /**
                                 * For example, show a message indicating deletion
                                 */
                                String message = getString(R.string.songDeleted) + " " + title;
                                showSnackbar(message);
                            });
                        }
                    }).start();
                });

            } else {
                /**
                 * Extras are from SongSearchActivity
                 */
                String title = extras.getString("title");
                int duration = extras.getInt("duration");
                String albumName = extras.getString("albumName");
                String albumCoverUrl = extras.getString("albumCoverUrl");

                /**
                 * Set the details in the views
                 */
                songDetailsBinding.tvSongTitleDetail.setText(title);
                songDetailsBinding.tvSongDurationDetail.setText(String.valueOf(duration));
                songDetailsBinding.tvSongAlbumNameDetail.setText(albumName);

                new Thread(() -> {
                    try {
                        URL detailUrl = new URL(albumCoverUrl);
                        HttpURLConnection connection = (HttpURLConnection) detailUrl.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream inputStream = connection.getInputStream();
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        /**
                         * Update ImageView on the main UI thread
                         */
                        runOnUiThread(() -> songDetailsBinding.ivSongAlbumCoverDetail.setImageBitmap(bitmap));
                    } catch (IOException e) {
                        Log.e(null, "Error downloading image: " + e.getMessage());
                    }
                }).start();

                songDetailsBinding.btnAddFavorite.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        new Thread(() -> {
                            Song existingSong = songDAO.getSongByTitle(title);
                            if (existingSong == null) {
                                /**
                                 * Song does not exist, insert it into the database
                                 */
                                insertSongIntoDatabase(title, duration, albumName, albumCoverUrl);
                                runOnUiThread(() -> {
                                    /**
                                     * Show a message indicating that the song was added to favorites
                                     */
                                    String message = getString(R.string.songAdded) + " " + title;
                                    showSnackbar(message);
                                });
                            } else {
                                /**
                                 * Song already exists in the database
                                 */
                                runOnUiThread(() -> {
                                    /**
                                     * Show a message indicating that the song is already in favorites
                                      */
                                    String message = getString(R.string.songAlreadyExist) + " " + title;
                                    showSnackbar(message);
                                });
                            }
                        }).start();
                    }
                });

                songDetailsBinding.btnShowFavorites.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SongDetailsActivity.this, SongFavoritesActivity.class);
                        startActivity(intent);
                    }
                });

                songDetailsBinding.btnDeleteFavorite.setOnClickListener(v -> {
                    /**
                     * Check if the song exists in the database
                     */
                    new Thread(() -> {
                        Song existingSong = songDAO.getSongByTitle(title);
                        if (existingSong != null) {
                            /**
                             * Song exists, delete it from the database
                             */
                            songDAO.deleteSong(existingSong);
                            /**
                             * Update UI or perform any other actions if needed
                              */
                            runOnUiThread(() -> {
                                /**
                                 * For example, show a message indicating deletion
                                 */
                                String message = getString(R.string.songDeleted) + " " + title;
                                showSnackbar(message);
                            });
                        }
                    }).start();
                });
            }
        }
    }

    private void insertSongIntoDatabase(String title, int duration, String albumName, String albumCoverUrl){
        Song song = new Song(title, duration ,albumName, albumCoverUrl);
        new Thread(()->{
            songDAO.insertSong(song);
        }).start();
    }

    private void showSnackbar(String message) {
        Snackbar.make(songDetailsBinding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

}
