package net.iamanengineer.forecast.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A day of hourly forecast
 * Created by julien on 01-20-17.
 */

public class Day {
    public String title;
    public List<ForecastCondition> hourlyConditionsList;

    public Day() {
        hourlyConditionsList = new ArrayList<>();
    }
}