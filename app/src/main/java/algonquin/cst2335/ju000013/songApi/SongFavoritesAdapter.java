package algonquin.cst2335.ju000013.songApi;
/**
 * Purpose: This file works between the database and second recycler view.
 * Author: Wei Deng
 * Lab section: 2335-011
 * Date updated: 2024-03-30
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import algonquin.cst2335.ju000013.R;

public class SongFavoritesAdapter extends RecyclerView.Adapter<SongFavoritesAdapter.SongFavoritesViewHolder> {
    private List<Song> songsEntity;
    private final Context context;

    /**
     * Constructor for the SongFavoritesAdapter.
     *
     * @param songsEntity The list of favorite songs.
     * @param context     The context of the activity.
     */
    public SongFavoritesAdapter(List<Song> songsEntity, Context context) {
        this.songsEntity = songsEntity;
        this.context = context;
    }

    /**
     * Sets the list of favorite songs.
     *
     * @param songs The list of favorite songs.
     */
    public void setSongs(List<Song> songs) {
        this.songsEntity = songs;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    @NonNull
    @Override
    public SongFavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.song_info, null);
        return new SongFavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongFavoritesViewHolder songViewHolder, int position) {
        songViewHolder.songName.setText(songsEntity.get(position).getTitle());
        songViewHolder.songDuration.setText(String.valueOf(songsEntity.get(position).getDuration()));
        songViewHolder.songAlbumName.setText(songsEntity.get(position).getAlbumName());
        new Thread(() -> {
            try {
                URL imageUrl = new URL(songsEntity.get(position).getAlbumCoverUrl());
                HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // Update ImageView on the main UI thread
                songViewHolder.itemView.post(() -> songViewHolder.songAlbumCover.setImageBitmap(bitmap));
            } catch (IOException e) {
                Log.e(null, "Error downloading image: " + e.getMessage());
            }
        }).start();

        songViewHolder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, SongDetailsActivity.class);
            intent.putExtra("SONG_TITLE", songsEntity.get(position).getTitle());
            intent.putExtra("SONG_DURATION", songsEntity.get(position).getDuration());
            intent.putExtra("SONG_ALBUM_NAME", songsEntity.get(position).getAlbumName());
            intent.putExtra("SONG_ALBUM_COVER_URL", songsEntity.get(position).getAlbumCoverUrl());
            Bundle extras = new Bundle();
            extras.putString("SOURCE", "FAVORITES");
            Intent prevIntent = ((SongFavoritesActivity) context).getIntent();
            Bundle prevExtras = prevIntent.getExtras();
            if (prevExtras != null) {
                extras.putAll(prevExtras);
            }
            intent.putExtras(extras);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return songsEntity == null ? 0 : songsEntity.size();
    }

    /**
     * ViewHolder class to hold the views for a single item in the RecyclerView.
     */
    public static class SongFavoritesViewHolder extends RecyclerView.ViewHolder {
        private final TextView songName;
        private final TextView songDuration;
        private final TextView songAlbumName;
        private final ImageView songAlbumCover;

        public SongFavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.textViewSongTitle);
            songDuration = itemView.findViewById(R.id.textViewSongDuration);
            songAlbumName = itemView.findViewById(R.id.textViewSongAlbumName);
            songAlbumCover = itemView.findViewById(R.id.imageViewSongAlbumCover);
        }
    }
}