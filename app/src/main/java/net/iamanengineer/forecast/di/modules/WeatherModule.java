package net.iamanengineer.forecast.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import net.iamanengineer.forecast.services.UnitService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by julien on 01-20-17.
 */

@Module
public class WeatherModule {

    public WeatherModule() {
    }

    @Provides
    @Singleton
    UnitService providesUnitService(Context context, SharedPreferences sharedPreferences) {
        return new UnitService(context, sharedPreferences);
    }
}
