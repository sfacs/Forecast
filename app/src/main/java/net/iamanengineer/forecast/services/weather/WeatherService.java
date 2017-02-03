package net.iamanengineer.forecast.services.weather;

import net.iamanengineer.forecast.model.WeatherData;

import rx.Observable;

/**
 * Created by julien on 01-20-17.
 */

public class WeatherService {

    private final WeatherApi weatherApi;

    public WeatherService(WeatherApi weatherApi) {
        this.weatherApi = weatherApi;
    }

    public Observable<WeatherData> getForecastForZip(String zipcode) {
        return weatherApi.getForecastForZip(zipcode);

    }
}
