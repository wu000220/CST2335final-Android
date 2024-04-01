package algonquin.cst2335.ju000013.sunrisesunsetApi;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * SaveLocation represents an entity in the 'favourites' table in the database.
 * This class is used to store the details of a saved location,
 * including latitude, longitude, sunrise, sunset, and other relevant information.
 *
 * @author Zhaohong Huang
 * @Labsection: CST2335-011
 * @Creation Date: 2024-03-29
 *
 */
@Entity(tableName = "favourites")
public class SaveLocation {
    @PrimaryKey(autoGenerate = true)
    private int id; // Unique identifier for the location.

    private String latitude;// The latitude of the location.
    private String longitude;// The latitude of the location.
    private String sunrise;// The time of sunrise at the location.
    private String sunset;// The time of sunset at the location.

    private String date;// The date of the location information.
    private String location;// Additional information or a label for the location.


    /**
     * Constructs a new SaveLocation instance.
     * Initializes the instance with provided sunrise, sunset, date, location, latitude, and longitude values.
     *
     * @param sunrise   The time of sunrise at the location.
     * @param sunset    The time of sunset at the location.
     * @param date      The date of the location information.
     * @param location  Additional information or a label for the location.
     * @param latitude  The latitude of the location.
     * @param longitude The longitude of the location.
     */
    public SaveLocation(String sunrise, String sunset, String date, String location, String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.date = date;
        this.location = location;
    }


    /**
     * Gets the latitude of this location.
     * @return The latitude.
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of this location.
     *
     * @param latitude The new latitude value.
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude of this location.
     *
     * @return The current longitude value.
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of this location.
     *
     * @param longitude The new longitude value.
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the unique identifier for this location.
     *
     * @return The current identifier value.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this location.
     *
     * @param id The new identifier value.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the sunrise time for this location.
     *
     * @return The current sunrise time.
     */
    public String getSunrise() {
        return sunrise;
    }

    /**
     * Sets the sunrise time for this location.
     *
     * @param sunrise The new sunrise time.
     */
    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    /**
     * Gets the sunset time for this location.
     *
     * @return The current sunset time.
     */
    public String getSunset() {
        return sunset;
    }

    /**
     * Sets the sunset time for this location.
     *
     * @param sunset The new sunset time.
     */
    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    /**
     * Gets the date for this location.
     *
     * @return The current date.
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date for this location.
     *
     * @param date The new date.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the location name or label.
     *
     * @return The current location name or label.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location name or label.
     *
     * @param location The new location name or label.
     */
    public void setLocation(String location) {
        this.location = location;
    }
}
