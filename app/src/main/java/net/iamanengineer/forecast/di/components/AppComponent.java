package net.iamanengineer.forecast.di.components;

import net.iamanengineer.forecast.di.modules.AppModule;
import net.iamanengineer.forecast.di.modules.NetworkModule;
import net.iamanengineer.forecast.di.modules.WeatherModule;
import net.iamanengineer.forecast.views.activities.MainActivity;
import net.iamanengineer.forecast.views.activities.SettingsActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by julien on 01-20-17.
 */

@Singleton
@Component(modules = {
        AppModule.class,
        NetworkModule.class,
        WeatherModule.class,
        })
public interface AppComponent
{
        void inject(MainActivity mainActivity);

        void inject(SettingsActivity settingsActivity);
}
