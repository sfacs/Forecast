package net.iamanengineer.forecast.di.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.iamanengineer.forecast.BuildConfig;
import net.iamanengineer.forecast.utils.parser.ForecastParser;
import net.iamanengineer.forecast.model.ForecastCondition;
import net.iamanengineer.forecast.services.weather.WeatherApi;
import net.iamanengineer.forecast.services.weather.WeatherService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by julien on 01-20-17.
 */

@Module
public class NetworkModule {

    public NetworkModule() { }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(ForecastCondition.class, new ForecastParser())
                .create();
    }

    /**
     * Not a @Singleton. Create a new base retrofit builder each time to be used by other providers.
     */
    @Provides
    Retrofit.Builder provideRetrofitBuilder(Gson gson) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));
    }


    @Provides
    @Singleton
    WeatherService provideWeatherService(WeatherApi weatherApi) {
        return new WeatherService(weatherApi);
    }


    @Provides
    @Singleton
    WeatherApi provideWeatherApi (Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder
                .baseUrl(BuildConfig.WEATHER_API_URL)
                .build()
                .create(WeatherApi.class);
    }

}
