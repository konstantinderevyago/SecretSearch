package by.jetfire.secretsearch;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Konstantin on 09.08.2016.
 */
public class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private MapsActivity context;

    public MapInfoWindowAdapter(MapsActivity context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LocationData locationData = context.getLocationData(marker);

        View view = context.getLayoutInflater().inflate(R.layout.info_window, null);
        TextView question = (TextView) view.findViewById(R.id.question);
        ImageView photo = (ImageView) view.findViewById(R.id.photo);

        if (locationData.isFinished()) {
            question.setText(locationData.getQuestion());
        }
        photo.setImageBitmap(locationData.getBitmap());

        return view;
    }
}
