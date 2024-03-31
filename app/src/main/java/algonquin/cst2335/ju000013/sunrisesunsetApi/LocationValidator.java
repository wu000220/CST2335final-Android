package algonquin.cst2335.ju000013.sunrisesunsetApi;


import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * LocationValidator provides static methods to validate the format of latitude and longitude strings.
 * It checks if the provided strings conform to specific format constraints for latitude and longitude.
 *
 * @author Zhaohong Huang
 * @Labsection: CST2335-011
 * @Creation Date: 2024-03-29
 *
 */
public class LocationValidator {

    // Validation format
    private static final Pattern LATITUDE_PATTERN =
            Pattern.compile("^\\d{5}$");

    // Validation format for longitude from -180 to +180 with E or W suffix
    private static final Pattern LONGITUDE_PATTERN =
            Pattern.compile("^\\d{6}$");


    /**
     * Validates the format of a latitude string.
     * Latitude must be a 5-digit number.
     *
     * @param latitude The latitude string to be validated.
     * @return true if the latitude is valid, false otherwise.
     */
    public static boolean isValidLatitude(String latitude) {
        return latitude != null && LATITUDE_PATTERN.matcher(latitude).matches();
    }

    /**
     * Validates the format of a longitude string.
     * Longitude must be a 6-digit number.
     *
     * @param longitude The longitude string to be validated.
     * @return true if the longitude is valid, false otherwise.
     */
    public static boolean isValidLongitude(String longitude) {
        return longitude != null && LONGITUDE_PATTERN.matcher(longitude).matches();
    }
}
