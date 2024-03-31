package algonquin.cst2335.ju000013.sunrisesunsetApi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import algonquin.cst2335.ju000013.R;

/**
 * SunriseSunsetLookupActivity is responsible for providing a user interface to look up sunrise and sunset times.
 * This activity allows users to enter latitude and longitude values and retrieves the corresponding sunrise and sunset times
 * from a remote API. It includes functionalities to save these times along with the date and location information in the database,
 * view saved locations, and navigate through the app's features.
 *
 * Features include:
 * - Input fields for latitude and longitude.
 * - Lookup button to fetch sunrise and sunset times from the API.
 * - Display of the current date and fetched times.
 * - Functionality to save the looked up location details.
 * - Navigation to view saved locations.
 *
 * SharedPreferences are used to store the last entered latitude and longitude.
 * Volley library is used for network requests to the sunrise-sunset API.
 *
 * @author Zhaohong Huang
 * @Labsection: CST2335-011
 * @Creation Date: 2024-03-29
 *
 */
public class SunriseSunsetLookupActivity extends AppCompatActivity {

    // ViewModel for managing database operations related to favourite locations.
    private FavouriteViewModel favouriteViewModel;

    // EditText for user to enter latitude.
    private EditText enterLatitude;

    // EditText for user to enter longitude.
    private EditText enterLongitude;

    // TextView to display the current date.
    private TextView showDate;

    // TextView to display the sunrise time.
    private TextView showSunrise;

    // TextView to display the sunset time.
    private TextView showSunset;

    // TextView to display the entered latitude.
    private TextView showLatitude;

    // TextView to display the entered longitude.
    private TextView showLongitude;

    // SharedPreferences file name for storing user preferences.
    private static final String PREFS_NAME = "SunriseSunsetPrefs";

    // Key for storing latitude in SharedPreferences.
    private static final String LATITUDE_KEY = "latitude";

    // Key for storing longitude in SharedPreferences.
    private static final String LONGITUDE_KEY = "longitude";


    /**
     * Initializes the activity. Sets up the toolbar, input fields, buttons, and ViewModel.
     * Retrieves saved latitude and longitude from SharedPreferences and populates the input fields.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this Bundle contains the most recent data provided by onSaveInstanceState. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunrise_sunset_lookup);

        // Initialize the home page toolbar control
        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);// This sets the toolbar as the app bar for the activity.

        // Initialize UI components for entering latitude and longitude,
        // and for displaying date, sunrise, sunset, latitude, and longitude.
        enterLatitude = findViewById(R.id.enterLatitude);
        enterLongitude = findViewById(R.id.enterLongitude);
        showDate = findViewById(R.id.showDate);
        showSunrise = findViewById(R.id.showSunrise);
        showSunset = findViewById(R.id.showSunset);
        showLatitude = findViewById(R.id.showLatitude);
        showLongitude = findViewById(R.id.showLongitude);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedLatitude = prefs.getString(LATITUDE_KEY, "");
        String savedLongitude = prefs.getString(LONGITUDE_KEY, "");
        enterLatitude.setText(savedLatitude);
        enterLongitude.setText(savedLongitude);


        Button lookupButton = findViewById(R.id.lookUpButton);
        lookupButton.setOnClickListener(v -> handleLookupButtonClick(enterLatitude, enterLongitude));

        // Initialize the ViewModel for handling database operations
        favouriteViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);

        // Initialize Save Location Button and its listener
        Button saveLocationButton = findViewById(R.id.saveLocationButton);
        saveLocationButton.setOnClickListener(v -> saveLocation());

        // Initialize favourite Button and its listener
        Button favouriteButton = findViewById(R.id.favouriteButton);
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SunriseSunsetLookupActivity.this, FavouritePageActivity.class);
                startActivity(intent);
            }
        });

        // Initialize your views and ViewModel here
        Button saveButton = findViewById(R.id.saveLocationButton); // replace with your actual save button ID
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLocation();
            }
        });
    }



    /**
     * Jump to SunriseSunsetLookup page from main home page toolbar!!!!!
     * Initializes the options menu in the toolbar.
     *
     *
     * @param menu The options menu in which items are placed.
     * @return true for the menu to be displayed; false to not show it.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toobar, menu);
        return true;
    }


    /* Help menu AlertDialog */
    /**
     * Help menu AlertDialog~~~~~
     * Handles item selections in the options menu.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sunHelp) {
            // 当用户点击 Help 时弹出 AlertDialog
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.sunHelp)) // 使用getString()获取字符串资源
                    .setPositiveButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_menu_help)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     * "Toast" when the user enters an incorrect latitude or longitude~~~
     * Validates the format of entered latitude and longitude.
     * Displays a toast message if the format is invalid.
     *
     * @param latitude  The entered latitude string to validate.
     * @param longitude The entered longitude string to validate.
     * @return boolean Returns true if both latitude and longitude are valid, false otherwise.
     */
    private boolean checkLocationFormat(String latitude, String longitude) {
        StringBuilder errorMessage = new StringBuilder();
        // 验证纬度格式
        if (!LocationValidator.isValidLatitude(latitude)) {
            errorMessage.append(getString(R.string.invalidLatitude));
        }
        // 验证经度格式
        if (!LocationValidator.isValidLongitude(longitude)) {
            if (errorMessage.length() > 0) {
                errorMessage.append("\n"); // 在两个错误消息之间添加换行符
            }
            errorMessage.append(getString(R.string.invalidLongitude));
        }
        // 如果有错误信息，显示一个包含所有错误的Toast
        if (errorMessage.length() > 0) {
            Toast.makeText(this, errorMessage.toString(), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }



    /**
     * Save Location" button function~~~~~~
     * Saves the current location information including sunrise, sunset, date, latitude, and longitude.
     * Constructs a SaveLocation object and adds it to the database via the ViewModel.
     */
    private void saveLocation() {
        // Get the current date from the TextView
        String date = showDate.getText().toString();

        // Get the sunrise and sunset times from their respective TextViews
        String sunrise = showSunrise.getText().toString();
        String sunset = showSunset.getText().toString();

        // Get the latitude and longitude from their respective EditTexts
        String latitude = enterLatitude.getText().toString().trim();
        String longitude = enterLongitude.getText().toString().trim();

        // Construct the default location name and details
        String locationName = "Location " + latitude + ", " + longitude + " on " + date + " (Sunrise: " + sunrise + ", Sunset: " + sunset + ")";

        // Create a new SaveLocation object with the retrieved information
        SaveLocation newLocation = new SaveLocation(sunrise, sunset, date, locationName, latitude, longitude);

        // Use the ViewModel to add this new location to the database
        favouriteViewModel.addFavourite(newLocation);

        // Show AlertDialog after adding the location
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.locationAddedSuccessfully)) // 正确地获取字符串资源
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }


    /**
     * Handling query button click events~~~~~~~~~~
     * Handles the action to be taken when the lookup button is clicked.
     * Validates the input latitude and longitude, saves them in SharedPreferences,
     * and makes a request to the sunrise-sunset API to retrieve the times.
     *
     * @param enterLatitude  The EditText view containing the latitude value.
     * @param enterLongitude The EditText view containing the longitude value.
     */
    private void handleLookupButtonClick(EditText enterLatitude, EditText enterLongitude) {
        String latitude = enterLatitude.getText().toString().trim();
        String longitude = enterLongitude.getText().toString().trim();

        // Displaying the current date
        String currentDate = getCurrentDate();
        TextView dateTextView = findViewById(R.id.showDate);
        dateTextView.setText(getString(R.string.resault_date_time, currentDate));

        // 检查用户输入的纬度和经度格式是否正确
        if (checkLocationFormat(latitude, longitude)) {
            // 如果格式正确，将纬度和经度保存到 SharedPreferences
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(LATITUDE_KEY, latitude);
            editor.putString(LONGITUDE_KEY, longitude);
            editor.apply(); // 异步地提交数据到 SharedPreferences

            // Constructing the query URL
            String url = "https://api.sunrise-sunset.org/json?lat=" + latitude + "&lng=" + longitude + "&date=today";

            // Creating Volley's JsonObjectRequest
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    response -> {
                        try {
                            JSONObject results = response.getJSONObject("results");
                            String sunrise = results.getString("sunrise");
                            String sunset = results.getString("sunset");

                            // Displaying the results on the UI
                            TextView sunriseTextView = findViewById(R.id.showSunrise);
                            sunriseTextView.setText(getString(R.string.resault_sunrise, sunrise));
                            TextView sunsetTextView = findViewById(R.id.showSunset);
                            sunsetTextView.setText(getString(R.string.resault_sunset, sunset));

                            // Display the entered latitude and longitude
                            TextView latitudeTextView = findViewById(R.id.showLatitude);
                            TextView longitudeTextView = findViewById(R.id.showLongitude);

                            latitudeTextView.setText(getString(R.string.resault_latitude, latitude));
                            longitudeTextView.setText(getString(R.string.resault_longitude, longitude));
                        } catch (JSONException e) {
                            Toast.makeText(SunriseSunsetLookupActivity.this, "Error parsing data", Toast.LENGTH_LONG).show();
                        }
                    },
                    error -> Toast.makeText(SunriseSunsetLookupActivity.this, "Error fetching data", Toast.LENGTH_LONG).show()
            );

            // 将请求添加到Volley的请求队列中
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjectRequest);

        } else {

        }
    }


    /**
     * show current date~~~~~~~~~~
     * Returns the current date as a string in the format "yyyy-MM-dd HH:mm:ss".
     *
     * @return String Current date and time.
     */
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}





