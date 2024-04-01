package algonquin.cst2335.ju000013.songApi;
/**
 * Purpose: This file is the display of database items saved already
 * Author: Wei Deng
 * Lab section: 2335-011
 * Date updated: 2024-03-30
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.ju000013.DictionaryApiActivity;
import algonquin.cst2335.ju000013.R;
import algonquin.cst2335.ju000013.databinding.ActivitySongFavoritesBinding;


public class SongFavoritesActivity extends AppCompatActivity {
    private ActivitySongFavoritesBinding songFavoritesBinding;
    private SongFavoritesViewModel songFavoritesViewModel;
    private SongFavoritesAdapter songFavoritesAdapter;
    private String helpTitle;
    private String instructions;

    /**
     * Inflates the menu resource for the activity's toolbar.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_toolbar_song, menu);
        return true;
    }

    /**
     * Handles the action bar item clicks.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else if (id == R.id.item_2) {
//            Intent intent = new Intent(SongFavoritesActivity.this, RecipeSearchActivity.class);
//            startActivity(intent);
        } else if (id == R.id.item_3) {
            Intent intent = new Intent(SongFavoritesActivity.this, DictionaryApiActivity.class);
            startActivity(intent);
        } else if (id == R.id.item_4) {
            Intent intent = new Intent(SongFavoritesActivity.this, SongSearchActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.item_help) {
            showHelpDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Displays a dialog with help information.
     */
    private void showHelpDialog() {
        helpTitle = getString(R.string.help_title);
        instructions = getString(R.string.instructions);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(helpTitle);
        builder.setMessage(instructions);
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * Initializes the activity, sets up the toolbar, and observes changes in the ViewModel.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songFavoritesBinding = ActivitySongFavoritesBinding.inflate(getLayoutInflater());
        setContentView(songFavoritesBinding.getRoot());

        Toolbar tool_bar = findViewById(R.id.toolbarFavorites);
        setSupportActionBar(tool_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerViewFavorites = findViewById(R.id.rvFavorites);
        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(this));
        songFavoritesAdapter = new SongFavoritesAdapter(new ArrayList<>(), this);
        recyclerViewFavorites.setAdapter(songFavoritesAdapter);

        songFavoritesViewModel = new ViewModelProvider(this).get(SongFavoritesViewModel.class);
        songFavoritesViewModel.getSongs().observe(this, new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                songFavoritesAdapter.setSongs(songs);
            }
        });
    }
}