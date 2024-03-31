package algonquin.cst2335.ju000013.songApi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import algonquin.cst2335.ju000013.R;
/**
 * Purpose: This file is the adapter working between search and the first recycler view
 * Author: Wei Deng
 * Lab section: 2335-011
 * Date updated: 2024-03-30
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    /**
     * private attributes
     */
    private final List<Song> songsEntity;
    private final Context context;

    /**
     * get the entity and context
     * @param songsEntity: for storage of song info
     * @param context: indicator
     */
    public SongAdapter(List<Song> songsEntity, Context context) {
        this.songsEntity = songsEntity;
        this.context = context;
    }

    /**
     * create view holder
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return: songViewholder
     */
    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.song_info,null);
        return new SongViewHolder(view);
    }

    /**
     * bind view holder
     * @param songViewHolder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull SongViewHolder songViewHolder, int position) {
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
    }

    /**
     * get item count
     * @return: integer of item number
     */
    @Override
    public int getItemCount() {
        return songsEntity == null ? 0: songsEntity.size();
    }

    /**
     * inner class for view holder
     */
    public class SongViewHolder extends RecyclerView.ViewHolder{
        private final TextView songName;
        private final TextView songDuration;
        private final TextView songAlbumName;
        private final ImageView songAlbumCover;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.textViewSongTitle);
            songDuration = itemView.findViewById(R.id.textViewSongDuration);
            songAlbumName = itemView.findViewById(R.id.textViewSongAlbumName);
            songAlbumCover = itemView.findViewById(R.id.imageViewSongAlbumCover);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Song song = songsEntity.get(position);
                    // Handle click event
                    Toast.makeText(context, context.getString(R.string.itemClicked) + song.getTitle(), Toast.LENGTH_SHORT).show();
                    // Start SongDetailsActivity and pass song details as extras
                    Intent intent = new Intent(context, SongDetailsActivity.class);
                    intent.putExtra("title", song.getTitle());
                    intent.putExtra("duration", song.getDuration());
                    intent.putExtra("albumName", song.getAlbumName());
                    intent.putExtra("albumCoverUrl", song.getAlbumCoverUrl());
                    context.startActivity(intent);
                }
            });
        }
    }
}
