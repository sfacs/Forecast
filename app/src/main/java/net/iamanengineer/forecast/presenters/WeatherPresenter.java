package net.iamanengineer.forecast.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import net.iamanengineer.forecast.BuildConfig;
import net.iamanengineer.forecast.model.Day;
import net.iamanengineer.forecast.model.ForecastCondition;
import net.iamanengineer.forecast.model.WeatherData;
import net.iamanengineer.forecast.services.UnitService;
import net.iamanengineer.forecast.services.weather.WeatherService;
import net.iamanengineer.forecast.transformers.Transformers;
import net.iamanengineer.forecast.views.WeatherView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by julien on 01-20-17.
 */

public class WeatherPresenter {

    private static final float MIN_WARM_TEMPERATURE = 60;
    private final Context context;
    private final SharedPreferences sharedPreferences;
    private WeatherView view;
    private String zipcode;

    private WeatherService weatherService;
    private UnitService unitService;

    @Inject
    public WeatherPresenter(Context context, WeatherService weatherService, UnitService unitService, SharedPreferences sharedPreferences) {
        this.context = context;
        this.weatherService = weatherService;
        this.unitService = unitService;
        this.sharedPreferences = sharedPreferences;
    }

    public void setView(WeatherView view) {
        this.view = view;
    }

    public void onResume() {
        this.zipcode = sharedPreferences.getString(context.getString(net.iamanengineer.forecast.R.string.zipcode_pref), BuildConfig.DEFAULT_ZIP);

        weatherService.getForecastForZip(zipcode)
                .compose(Transformers.io())
                .subscribe(this::onWeatherLoaded,
                        this::onError);

    }

    private void onWeatherLoaded(WeatherData weatherData) {
        // If we don't have the currentObservation or displayLocation at this point, we can assume an error
        // and go back to zipcode. Alternatively, we could just display the zpcode, and keep going trying
        // to display the rest of the data (i.e. Temperature...)
        if (weatherData.currentObservation == null ||
                weatherData.currentObservation.displayLocation == null) {
            onError(new Throwable("Data incomplete."));
            return;
        }

        view.setActionBarTitle(String.format(context.getString(net.iamanengineer.forecast.R.string.location_format),
                weatherData.currentObservation.displayLocation.city,
                weatherData.currentObservation.displayLocation.state));

        view.displayTemperature(unitService.getTemperatureString(weatherData.currentObservation),
                isTemperatureWarm(weatherData.currentObservation.tempFahrenheit));

        view.displayConditions(weatherData.currentObservation.weather);

        List<Day> days = getDays(weatherData.forecast);

        view.displayHourlyWeather(days);

    }

    private List<Day> getDays(List<ForecastCondition> forecasts) {

        // We use a TreeMap to make a list of unique days at the end of this method
        Map<Integer, Day> days = new TreeMap<>();

        Calendar calendar = Calendar.getInstance();

        // Pair index - temperature to record the highest and lowest temperature in a day
        Pair<Integer, Integer> highestTemperature = new Pair<>(-1, null);
        Pair<Integer, Integer> lowestTemperature = new Pair<>(-1, null);

        // Reset after every day, to keep track of all the temperature of the previous day
        int indexForecastInDay = 0;
        // Keep track of the previous day
        int previousKey = -1;

        Date today = new Date();
        calendar.setTime(today);
        int todayKey = calendar.get(Calendar.DAY_OF_YEAR);

        for(ForecastCondition forecast : forecasts) {

            calendar.setTime(forecast.time);
            int key = calendar.get(Calendar.DAY_OF_YEAR);
            Day day;

            // If the day has already been added to the day list, we retrieve it, so that we can add more
            // hourly forecasts to it
            if (days.containsKey(key)) {
                day = days.get(key);
            } else {
                // If have already processed a day, set that day's min/max temperature
                if (previousKey != -1) {
                    days.get(previousKey).hourlyConditionsList.get(highestTemperature.first).isHighestTemp = true;
                    days.get(previousKey).hourlyConditionsList.get(lowestTemperature.first).isLowestTemp = true;
                }

                //Reset for a new day
                indexForecastInDay = 0;
                day = new Day();
                highestTemperature = new Pair<>(-1, null);
                lowestTemperature = new Pair<>(-1, null);

            }

            // Is the temperature higher than the one on record?
            if (highestTemperature.second == null || unitService.getTemperature(forecast) > highestTemperature.second) {
                highestTemperature = new Pair<>(indexForecastInDay, unitService.getTemperature(forecast));
            }

            // Is the temperature lower than the one on record?
            if (lowestTemperature.second == null || unitService.getTemperature(forecast) < lowestTemperature.second) {
                lowestTemperature = new Pair<>(indexForecastInDay, unitService.getTemperature(forecast));
            }

            // Display "Monday", "Tuesday" ...
            SimpleDateFormat format = new SimpleDateFormat("EEEE");
            if (todayKey == key) {
                day.title = context.getString(net.iamanengineer.forecast.R.string.today);
            } else if (todayKey + 1 == key ) {
                day.title = context.getString(net.iamanengineer.forecast.R.string.tomorrow);
            } else {
                day.title = format.format(forecast.time);
            }
            // Add the hourly forecast to the day
            day.hourlyConditionsList.add(forecast);

            indexForecastInDay++;
            previousKey = key;

            //Add the day to the list of days
            days.put(key, day);
        }
        //Setup last day
        days.get(previousKey).hourlyConditionsList.get(highestTemperature.first).isHighestTemp = true;
        days.get(previousKey).hourlyConditionsList.get(lowestTemperature.first).isLowestTemp = true;

        return new ArrayList<>(days.values());

    }

    private boolean isTemperatureWarm(float temperatureFarenheit) {
        return temperatureFarenheit > MIN_WARM_TEMPERATURE;
    }

    private void onError(Throwable throwable) {
        Timber.e(throwable.getMessage());
        view.showError(String.format(context.getString(net.iamanengineer.forecast.R.string.unable_to_load_weather), zipcode));
        view.launchSettings();
    }


}
