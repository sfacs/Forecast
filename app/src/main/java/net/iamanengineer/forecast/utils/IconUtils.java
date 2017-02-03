package net.iamanengineer.forecast.utils;

import net.iamanengineer.forecast.model.CurrentObservation;

/**
 * API for getting icon URLs for weather conditions
 *
 */
public class IconUtils {

    /**
     * Get the URL to an icon given by Weather Underground
     * @param icon The name of the icon provided by Weather Underground (e.g. "clear").
     *      {@see {@link CurrentObservation#icon}}
     * @return A URL to an icon
     */
    static public String getUrlForIcon(String icon) {
        return String.format("https://icons.wxug.com/i/c/k/%s.gif", icon);
    }

}
