package net.iamanengineer.forecast.services;

import android.content.Context;
import android.content.SharedPreferences;

import net.iamanengineer.forecast.model.CurrentObservation;
import net.iamanengineer.forecast.model.ForecastCondition;

/**
 * Created by julien on 01-20-17.
 */

public class UnitService {

    public final static int FAHRENHEIT = 0;
    public final static int CELCIUS = 1;
    private final Context context;
    private SharedPreferences sharedPreferences;

    public UnitService(Context context, SharedPreferences sharedPreferences) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    public int getTemperature(ForecastCondition forecastCondition) {
        float temperature;
        if (isUnitFahrenheit()) {
            temperature = forecastCondition.tempFahrenheit;
        } else {
            temperature = forecastCondition.tempCelsius;
        }
        return Math.round(temperature);
    }

    public int getTemperature(CurrentObservation currentObservation) {
        float temperature;
        if (isUnitFahrenheit()) {
            temperature = currentObservation.tempFahrenheit;
        } else {
            temperature = currentObservation.tempCelsius;
        }
        return Math.round(temperature);
    }

    public String getTemperatureString(ForecastCondition forecastCondition) {
        float temperature;
        if (isUnitFahrenheit()) {
            temperature = forecastCondition.tempFahrenheit;
        } else {
            temperature = forecastCondition.tempCelsius;
        }
        return String.format("%d\u00B0", Math.round(temperature));
    }

    public String getTemperatureString(CurrentObservation currentObservation) {
        float temperature;
        if (isUnitFahrenheit()) {
            temperature = currentObservation.tempFahrenheit;
        } else {
            temperature = currentObservation.tempCelsius;
        }
        return String.format("%d\u00B0", Math.round(temperature));
    }

    private boolean isUnitFahrenheit() {
        return sharedPreferences.getInt(context.getString(net.iamanengineer.forecast.R.string.unit_pref), 0) == FAHRENHEIT;
    }



}
