package by.jetfire.secretsearch;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocationData {
    private LatLng latLng;
    private String question;
    private Bitmap bitmap;
    private boolean finished;
}
