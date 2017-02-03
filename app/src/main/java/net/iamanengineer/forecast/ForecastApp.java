package net.iamanengineer.forecast;

import android.app.Application;

import net.iamanengineer.forecast.di.components.AppComponent;
import net.iamanengineer.forecast.di.components.DaggerAppComponent;
import net.iamanengineer.forecast.di.modules.AppModule;
import net.iamanengineer.forecast.di.modules.NetworkModule;

import timber.log.Timber;

/**
 * Created by julien on 01-20-17.
 */

public class ForecastApp extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Setup logging
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            // we could add a release crash reporting Tree here
        }

        //Initalize AppComponent
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .build();

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
