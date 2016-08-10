package by.jetfire.secretsearch;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener {

    private static final int PERMISSION_REQUEST_ALL_NEEDED = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int DEFAULT_ZOOM = 15;

//    private static final LatLng[] locations = {
//            new LatLng(53.89, 27.54),
//            new LatLng(53.9, 27.54),
//            new LatLng(53.88, 27.54),
//            new LatLng(53.90, 27.53),
//            new LatLng(53.90, 27.55),
//            new LatLng(53.88, 27.53),
//            new LatLng(53.88, 27.55),
//            new LatLng(53.89, 27.53),
//            new LatLng(53.89, 27.55),
//
//            new LatLng(53.905, 27.5405),
//            new LatLng(53.885, 27.545),
//            new LatLng(53.905, 27.535),
//            new LatLng(53.905, 27.555),
//            new LatLng(53.885, 27.535),
//            new LatLng(53.885, 27.555),
//            new LatLng(53.895, 27.535),
//            new LatLng(53.895, 27.555),
////            new LatLng(53.9, 27.5),
////            new LatLng(54, 27.6),
////            new LatLng(54, 27.4),
////            new LatLng(53.8, 27.6),
////            new LatLng(53.8, 27.4),
////            new LatLng(54, 27),
////            new LatLng(55, 28),
////            new LatLng(55, 26),
////            new LatLng(53, 28),
////            new LatLng(53, 26)
//    };

    String[] neededPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Bind(R.id.location_scroll)
    protected ScrollView locationScroll;
    @Bind(R.id.location_text)
    protected TextView locationText;
    @Bind(R.id.arrow)
    protected ImageView arrow;

    private GoogleMap googleMap;
    private MapInfoWindowAdapter mapInfoWindowAdapter;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location location;

    private Map<Marker, LocationData> locationInfo;
    private Marker currentMarker;
    private Marker photoMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(neededPermissions, PERMISSION_REQUEST_ALL_NEEDED);
        }

        mapInfoWindowAdapter = new MapInfoWindowAdapter(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        buildGoogleApiClient();
    }

    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }

        googleMap.setInfoWindowAdapter(mapInfoWindowAdapter);
        googleMap.setOnInfoWindowClickListener(this);

//        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                updateArrow();
//                return false;
//            }
//        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        photoMarker = marker;
        takePhoto();
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "by.jetfire.secretsearch",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//            }
        }
    }

//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault()).format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
////        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
//        return image;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (photoMarker != null) {
                LocationData locationData = getLocationData(photoMarker);
                locationData.setFinished(true);
                locationData.setBitmap(imageBitmap);
                saveImageFile(imageBitmap, locationData.getAnswer());
                photoMarker.showInfoWindow();
            }
        }
    }

    private void saveImageFile(Bitmap bitmap, String fileName) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(getExternalFilesDir(null), fileName + ".png"));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_ALL_NEEDED) {
            boolean haveAllPermissions = true;
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    haveAllPermissions = false;
                }
            }

            if (haveAllPermissions) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                }
            } else {
                finish();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(100);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if (location != null && locationInfo == null) {
                LatLng position = new LatLng(location.getLatitude(), location.getLongitude());

                LocationData startLocationData = new LocationData();
                startLocationData.setQuestion("Where did I say 'I love you' in first time?");
                startLocationData.setAnswer("Let's start!");
                startLocationData.setLatLng(position);
                List<LocationData> locationDataPath = startLocationData.getLocationDataPath();

                locationInfo = new HashMap<>();
                for (int i = 0; i < locationDataPath.size(); i++) {
                    LocationData locationData = locationDataPath.get(i);

                    MarkerOptions markerOptions = new MarkerOptions().position(locationData.getLatLng());
                    Marker marker = googleMap.addMarker(markerOptions);
                    marker.setVisible(i == 0);
                    if (i == 0) {
                        currentMarker = marker;
                    }
                    locationData.setMarker(marker);
                    locationInfo.put(marker, locationData);
                }

                CameraUpdate center = CameraUpdateFactory.newLatLngZoom(position, DEFAULT_ZOOM);
                googleMap.animateCamera(center);

                startEmulation();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }

    @Override
    public void onLocationChanged(Location location) {
//        this.location = location;
//        updateArrow();

        locationText.append("[" + location.getLatitude() + ", " + location.getLongitude() + "]\n");

        locationScroll.post(new Runnable() {
            @Override
            public void run() {
                locationScroll.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private Timer timer;

    private void startEmulation() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                location.setLatitude(location.getLatitude() + 0.0001);
                location.setLongitude(location.getLongitude() + 0.0001);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateArrow();
                    }
                });
            }
        }, 0, 100);
    }

    private LatLng getNextLocation() {
        LocationData locationData = getLocationData(currentMarker);
        LocationData next = locationData.getNextStep();
        if (next != null) {
            return next.getLatLng();
        }

        return null;
    }

    private void updateArrow() {
        LatLng latLng = getNextLocation();
        if (location != null && latLng != null) {
            updateArrowRotation(latLng);
            updateArrowColor(latLng);
//        } else {
//            buildGoogleApiClient();
        }
    }

    private void updateArrowRotation(LatLng latLng) {
        double y = latLng.latitude - location.getLatitude();
        double x = latLng.longitude - location.getLongitude();
        double tg = x / y;
        float arctg = (float) Math.toDegrees(Math.atan(tg));
        if (y < 0) {
            arctg += 180;
        }
        arrow.setRotation(arctg);
    }

    private void updateArrowColor(LatLng latLng) {
        float[] result = new float[1];
        Location.distanceBetween(location.getLatitude(), location.getLongitude(), latLng.latitude, latLng.longitude, result);
        float distance = result[0] / 2000;
        if (distance > 1f) {
            distance = 1f;
        }
        if (distance <= 0.12) {
            changeTarget();
        }
        int color = (int) new ArgbEvaluator().evaluate(distance, Color.GREEN, Color.RED);

        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        arrow.setColorFilter(colorFilter);
    }

    private void changeTarget() {
        LocationData locationData = getLocationData(currentMarker);
        LocationData nextLocationData = locationData.getNextStep();
        if (nextLocationData != null) {
            currentMarker = nextLocationData.getMarker();
            currentMarker.setVisible(true);
            currentMarker.showInfoWindow();
            updateArrow();
        } else {
            if (timer != null) {
                timer.cancel();
            }
        }
    }

    public LocationData getLocationData(Marker marker) {
        return locationInfo.get(marker);
    }
}
