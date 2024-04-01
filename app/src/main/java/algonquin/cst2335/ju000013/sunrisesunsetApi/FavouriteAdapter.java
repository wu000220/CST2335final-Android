package algonquin.cst2335.ju000013.sunrisesunsetApi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import algonquin.cst2335.ju000013.R;

/**
 * Adapter class for managing a list of saved locations in a RecyclerView.
 * This class handles the display and management of favourite locations,
 * including deletion and adding new items.
 *
 *
 * @author Zhaohong Huang
 * @Labsection: CST2335-011
 * @Creation Date: 2024-03-29
 *
 */
public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {
    /** List of favourite locations to be displayed in the RecyclerView. */
    private List<SaveLocation> favourites;

    /** The context in which the adapter is being used, typically the current activity. */
    private Context context;

    /** ViewModel associated with this adapter to manage data operations. */
    private FavouriteViewModel favouriteViewModel;

    /** The RecyclerView instance this adapter is attached to. */
    private RecyclerView recyclerView;

    /** Flag to determine if the adapter is in delete mode (allows item deletion). */
    private boolean isDeleteMode = false;

    /**
     * Constructs a FavouriteAdapter.
     *
     * @param context The context of the adapter.
     * @param viewModel The ViewModel that will be used to manage data.
     * @param favourites A list of favourite locations.
     * @param recyclerView The RecyclerView that this adapter is attached to.
     */
    public FavouriteAdapter(Context context, FavouriteViewModel viewModel, List<SaveLocation> favourites, RecyclerView recyclerView) {
        this.context = context;
        this.favouriteViewModel = viewModel;
        this.favourites = favourites;
        this.recyclerView = recyclerView;
    }

    /**
     * Removes an item from the favourites list.
     *
     * @param position The position of the item in the list to be removed.
     */
    public void removeItem(int position) {
        if (position < favourites.size()) {
            favourites.remove(position);
            notifyItemRemoved(position);
        }
    }


    /**
     * Updates the list of favourite locations that the adapter displays.
     * After updating the list, it notifies the RecyclerView that the data set has changed.
     *
     * @param newFavourites The new list of favourite locations to be displayed.
     */
    public void setFavourites(List<SaveLocation> newFavourites) {
        this.favourites = newFavourites;
        notifyDataSetChanged();
    }

    /**
     * Interface for handling deletion of items from the list.
     */
    public interface OnItemDeleteListener {

        /**
         * Called when an item is to be deleted.
         *
         * @param location The location that is being deleted.
         * @param position The position of the item in the list.
         */
        void onItemDelete(SaveLocation location, int position);
    }

    /** Listener for handling the deletion of items from the favourites list. */
    private OnItemDeleteListener onItemDeleteListener;

    /**
     * Sets the listener that will be notified when an item is deleted from the favourites list.
     *
     * @param listener The listener that handles the item deletion.
     */
    public void setOnItemDeleteListener(OnItemDeleteListener listener) {
        this.onItemDeleteListener = listener;
    }

    /**
     * Sets the delete mode for the adapter.
     * In delete mode, clicking an item will trigger its deletion.
     *
     * @param isDeleteMode Boolean value indicating whether the adapter is in delete mode.
     */
    public void setDeleteMode(boolean isDeleteMode) {

        this.isDeleteMode = isDeleteMode;
    }

    /**
     * Constructs a new FavouriteAdapter with the provided parameters.
     *
     * @param favourites The list of favourite locations to be managed by this adapter.
     * @param viewModel The ViewModel that will be used to manage data operations.
     * @param recyclerView The RecyclerView to which this adapter is bound.
     */
    public FavouriteAdapter(List<SaveLocation> favourites, FavouriteViewModel viewModel, RecyclerView recyclerView) {
        this.favourites = favourites;
        this.favouriteViewModel = viewModel;
        this.recyclerView = recyclerView;
    }


    /**
     * Called when RecyclerView needs a new {@link FavouriteViewHolder} of the given type to represent
     * an item. This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. The new ViewHolder will be used to display items of the adapter using
     * onBindViewHolder(ViewHolder, int, List).
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     * @return A new FavouriteViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_item, parent, false);
        return new FavouriteViewHolder(itemView);
    }


    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link FavouriteViewHolder#itemView} to reflect the item at the
     * given position.
     *
     * In this method, the contents of the ViewHolder are set based on the SaveLocation object's
     * properties, such as date, sunrise, sunset, latitude, and longitude.
     *
     * @param holder The FavouriteViewHolder which should be updated to represent the contents of the
     *               item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        SaveLocation location = favourites.get(position);
        holder.tvDate.setText(location.getDate());
        holder.tvSunrise.setText(location.getSunrise());
        holder.tvSunset.setText(location.getSunset());
        holder.tvLatitude.setText(String.format(context.getString(R.string.resault_latitude), location.getLatitude()));
        holder.tvLongitude.setText(String.format(context.getString(R.string.resault_longitude), location.getLongitude()));
    }


    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The size of the favourites list, representing the number of items in the adapter.
     */
    @Override
    public int getItemCount() {

        return favourites.size();
    }


    /**
     * Provides a reference to the views for each data item.
     * Complex data items may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder.
     */
    public class FavouriteViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvSunrise, tvSunset, tvLatitude, tvLongitude;

        /**
         * Constructor for FavouriteViewHolder. Initializes the TextViews used to display the data
         * and sets up an onClickListener to handle item clicks. The behavior of the onClickListener
         * depends on whether the adapter is in delete mode or not. In delete mode, clicking an item
         * triggers its deletion. Otherwise, it adds a new location based on the current one.
         *
         * @param itemView The View for the individual item in the RecyclerView.
         */
        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvSunrise = itemView.findViewById(R.id.tvSunrise);
            tvSunset = itemView.findViewById(R.id.tvSunset);
            tvLatitude = itemView.findViewById(R.id.tvLatitude);
            tvLongitude = itemView.findViewById(R.id.tvLongitude);

            itemView.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();// choose a data
                if (position != RecyclerView.NO_POSITION) { //NO_POSITION is -1
                    SaveLocation selectedLocation = favourites.get(position);
                    if (isDeleteMode && onItemDeleteListener != null) {
                        // Call the method from the interface to handle the delete
                        onItemDeleteListener.onItemDelete(selectedLocation, position);
                    } else {
                        // Not in delete mode, add a new location based on the current one
                        // String newTime = getCurrentTime(); // Use the method to get the current time
                        SaveLocation newLocation = new SaveLocation(
                                selectedLocation.getSunrise(),
                                selectedLocation.getSunset(),
                                getCurrentTime(), // new time
                                selectedLocation.getLocation(), // same location name
                                selectedLocation.getLatitude(),
                                selectedLocation.getLongitude()
                        );

                        // Insert the new location into the database and update RecyclerView
                        favouriteViewModel.insertFavourite(newLocation);
                        favourites.add(newLocation);
                        notifyItemInserted(favourites.size() - 1);


                        // Show confirmation message using the itemView's context to find the root layout
                        View parentLayout = itemView.getRootView().findViewById(android.R.id.content);
                        Snackbar.make(parentLayout, context.getString(R.string.new_location_added), Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }


        /**
         * Helper method to get the current time in a specific format.
         *
         * @return The current time as a formatted string.
         */
        private String getCurrentTime() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return sdf.format(new Date());
        }
    }
}


