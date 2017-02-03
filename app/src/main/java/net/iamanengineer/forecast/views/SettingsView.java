package net.iamanengineer.forecast.views;

/**
 * Created by julien on 01-20-17.
 */

public interface SettingsView {
    void setActionBarTitle(String title);
    void showError(String error);

    void setZipcode(String zipcode);

    void setUnit(int unit);
}
