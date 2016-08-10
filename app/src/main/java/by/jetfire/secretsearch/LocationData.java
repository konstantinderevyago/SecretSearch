package by.jetfire.secretsearch;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class LocationData {
    private LatLng latLng;
    private String question;
    private String answer;
    private Bitmap bitmap;
    private boolean finished;
    private LocationData nextStep;
    private Marker marker;

    public List<LocationData> getLocationDataPath() {
        List<LocationData> locationData = new ArrayList<>();

        // end
        LocationData endLocationData = new LocationData();
        endLocationData.setQuestion("Are you agree?");
        endLocationData.setAnswer("Here :)");
        endLocationData.setLatLng(new LatLng(53.897, 27.547));

        // cinema
        LocationData cinemaLocationData = new LocationData();
        cinemaLocationData.setQuestion("Our favorite place?");
        cinemaLocationData.setAnswer("Silver Screen");
        cinemaLocationData.setLatLng(new LatLng(53.896, 27.546));
        cinemaLocationData.setNextStep(endLocationData);

        // terminal
        LocationData terminalLocationData = new LocationData();
        terminalLocationData.setQuestion("The best move place?");
        terminalLocationData.setAnswer("Railway Station");
        terminalLocationData.setLatLng(new LatLng(53.895, 27.545));
        terminalLocationData.setNextStep(cinemaLocationData);

        // first kiss
        LocationData firstKissLocationData = new LocationData();
        firstKissLocationData.setQuestion("Where my babe saved many 'Love is' for me?");
        firstKissLocationData.setAnswer("Кружка");
        firstKissLocationData.setLatLng(new LatLng(53.894, 27.544));
        firstKissLocationData.setNextStep(terminalLocationData);

        // dinner
        LocationData dinnerLocationData = new LocationData();
        dinnerLocationData.setQuestion("Where was our first kiss?");
        dinnerLocationData.setAnswer("The View");
        dinnerLocationData.setLatLng(new LatLng(53.893, 27.543));
        dinnerLocationData.setNextStep(firstKissLocationData);

        // love lock
        LocationData loveLockLocationData = new LocationData();
        loveLockLocationData.setQuestion("Where we can eat near birds?");
        loveLockLocationData.setAnswer("Love bridge");
        loveLockLocationData.setLatLng(new LatLng(53.892, 27.542));
        loveLockLocationData.setNextStep(dinnerLocationData);

        // meet
        LocationData meetLocationData = new LocationData();
        meetLocationData.setQuestion("Where did we lock our love?");
        meetLocationData.setAnswer("University");
        meetLocationData.setLatLng(new LatLng(53.891, 27.541));
        meetLocationData.setNextStep(loveLockLocationData);

        // I love you
        LocationData iLoveYouLocationData = new LocationData();
        iLoveYouLocationData.setQuestion("Where did we meet?");
        iLoveYouLocationData.setAnswer("Under the tree");
        iLoveYouLocationData.setLatLng(new LatLng(53.89, 27.54));
        iLoveYouLocationData.setNextStep(meetLocationData);

        setNextStep(iLoveYouLocationData);

        locationData.add(this);
        locationData.add(iLoveYouLocationData);
        locationData.add(meetLocationData);
        locationData.add(loveLockLocationData);
        locationData.add(dinnerLocationData);
        locationData.add(firstKissLocationData);
        locationData.add(terminalLocationData);
        locationData.add(cinemaLocationData);
        locationData.add(endLocationData);

        return locationData;
    }
}
