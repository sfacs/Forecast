package net.iamanengineer.forecast.services.weather;

import net.iamanengineer.forecast.BuildConfig;
import net.iamanengineer.forecast.model.WeatherData;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Retrofit interface for fetching weather data
 *
 */
public interface WeatherApi {

    /**
     * Get the forecast for a given zip code
     */
    @GET("/api/" + BuildConfig.API_KEY + "/conditions/hourly/q/{zip}.json")
    Observable<WeatherData> getForecastForZip(@Path("zip") String zipCode);
}
