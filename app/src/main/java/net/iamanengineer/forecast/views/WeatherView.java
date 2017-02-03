package net.iamanengineer.forecast.views;

import net.iamanengineer.forecast.model.Day;

import java.util.List;

/**
 * Created by julien on 01-20-17.
 */

public interface WeatherView {
    void setActionBarTitle(String title);
    void showError(String error);
    void displayTemperature(String temperature, boolean isWarm);
    void displayConditions(String conditions);
    void displayHourlyWeather(List<Day> days);
    void launchSettings();
}
