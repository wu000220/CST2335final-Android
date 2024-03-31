package algonquin.cst2335.ju000013.songApi;
/**
 * Purpose: This file is the entity of song that would store info for song details
 * Author: Wei Deng
 * Lab section: 2335-011
 * Date updated: 2024-03-30
 */

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Song {

    /**
     * The title of the song.
     */
    @ColumnInfo(name = "title")
    private String title;

    /**
     * The duration of the song in seconds.
     */
    @ColumnInfo(name = "duration")
    private int duration;

    /**
     * The name of the album to which the song belongs.
     */
    @ColumnInfo(name = "album")
    private String albumName;

    /**
     * The URL of the album cover for the song.
     */
    @ColumnInfo(name = "cover")
    private String albumCoverUrl;

    /**
     * The unique identifier of the song.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    /**
     * Constructs a new Song object with the specified details.
     *
     * @param title         The title of the song.
     * @param duration      The duration of the song in seconds.
     * @param albumName     The name of the album to which the song belongs.
     * @param albumCoverUrl The URL of the album cover for the song.
     */
    public Song(String title, int duration, String albumName, String albumCoverUrl) {
        this.title = title;
        this.duration = duration;
        this.albumName = albumName;
        this.albumCoverUrl = albumCoverUrl;
    }

    /**
     * Retrieves the title of the song.
     *
     * @return The title of the song.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the song.
     *
     * @param title The title of the song.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves the duration of the song.
     *
     * @return The duration of the song in seconds.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the song.
     *
     * @param duration The duration of the song in seconds.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Retrieves the name of the album to which the song belongs.
     *
     * @return The name of the album.
     */
    public String getAlbumName() {
        return albumName;
    }

    /**
     * Sets the name of the album to which the song belongs.
     *
     * @param albumName The name of the album.
     */
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    /**
     * Retrieves the URL of the album cover for the song.
     *
     * @return The URL of the album cover.
     */
    public String getAlbumCoverUrl() {
        return albumCoverUrl;
    }

    /**
     * Sets the URL of the album cover for the song.
     *
     * @param albumCoverUrl The URL of the album cover.
     */
    public void setAlbumCoverUrl(String albumCoverUrl) {
        this.albumCoverUrl = albumCoverUrl;
    }
}