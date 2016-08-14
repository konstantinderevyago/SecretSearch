package by.jetfire.secretsearch;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import lombok.Data;

@Data
public class LocationData {
    private LatLng latLng;
    private String question;
    private String answer;
    private Bitmap bitmap;
    private boolean finished;
    private boolean visible;
    private LocationData nextStep;
    private Marker marker;

    public void initStartLocationData(LatLng position) {
        setQuestion("Где я впервые признался тебе в любви?");
        setAnswer("Понеслась!");
        setLatLng(position);
        setVisible(true);
        initLocationDataPath();
    }

    public void initLocationDataPath() {
        // end
        LocationData endLocationData = new LocationData();
        endLocationData.setQuestion("Ты согласна?");
        endLocationData.setAnswer("Здесь :)");
        endLocationData.setLatLng(new LatLng(53.88304, 27.57322));

        // cinema
        LocationData cinemaLocationData = new LocationData();
        cinemaLocationData.setQuestion("Наше место?");
        cinemaLocationData.setAnswer("Silver Screen");
        cinemaLocationData.setLatLng(new LatLng(53.89062, 27.55328));
        cinemaLocationData.setNextStep(endLocationData);

        // terminal
        LocationData terminalLocationData = new LocationData();
        terminalLocationData.setQuestion("Лучший кинотетр?");
        terminalLocationData.setAnswer("Железнодорожный вакзал");
        terminalLocationData.setLatLng(new LatLng(53.89115, 27.55098));
        terminalLocationData.setNextStep(cinemaLocationData);

        // first kiss
        LocationData firstKissLocationData = new LocationData();
        firstKissLocationData.setQuestion("Где малыш спрятал много 'Love is' для коти?");
        firstKissLocationData.setAnswer("Кружка");
        firstKissLocationData.setLatLng(new LatLng(53.89589, 27.54771));
        firstKissLocationData.setNextStep(terminalLocationData);

        // dinner
        LocationData dinnerLocationData = new LocationData();
        dinnerLocationData.setQuestion("Где мы впервые поцеловались?");
        dinnerLocationData.setAnswer("The View");
        dinnerLocationData.setLatLng(new LatLng(53.90774, 27.55004));
        dinnerLocationData.setNextStep(firstKissLocationData);

        // love lock
        LocationData loveLockLocationData = new LocationData();
        loveLockLocationData.setQuestion("Где можно покушать рядом с птичками и отличным видом?");
        loveLockLocationData.setAnswer("Мост Любви");
        loveLockLocationData.setLatLng(new LatLng(53.90436, 27.57047));
        loveLockLocationData.setNextStep(dinnerLocationData);

        // meet
        LocationData meetLocationData = new LocationData();
        meetLocationData.setQuestion("Где мы 'закрыли' нашу любовь?");
        meetLocationData.setAnswer("Универ");
        meetLocationData.setLatLng(new LatLng(53.89399, 27.54956));
        meetLocationData.setNextStep(loveLockLocationData);

        // I love you
        LocationData iLoveYouLocationData = new LocationData();
        iLoveYouLocationData.setQuestion("Где мы встретились?");
        iLoveYouLocationData.setAnswer("Под деревом :)");
        iLoveYouLocationData.setLatLng(new LatLng(53.88455, 27.53954));
        iLoveYouLocationData.setNextStep(meetLocationData);

        setNextStep(iLoveYouLocationData);
    }
}
