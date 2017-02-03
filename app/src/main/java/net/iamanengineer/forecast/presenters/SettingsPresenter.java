package net.iamanengineer.forecast.presenters;

import android.content.Context;
import android.content.SharedPreferences;

import net.iamanengineer.forecast.BuildConfig;
import net.iamanengineer.forecast.R;
import net.iamanengineer.forecast.views.SettingsView;

import javax.inject.Inject;

/**
 * This presenter handle the save & restore of zipcode and unit settings.
 * It will disable the user to go back to the homescreen until the zipcode is valid.
 * Created by julien on 01-20-17.
 */

public class SettingsPresenter {


    private final Context context;
    private SettingsView view;
    private SharedPreferences sharedPreferences;
    private String zipcode;

    @Inject
    public SettingsPresenter(Context context, SharedPreferences sharedPreferences) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    public void setView(SettingsView view) {
        this.view = view;
    }

    public void onResume() {
        String zipcode = sharedPreferences.getString(context.getString(R.string.zipcode_pref), "");
        view.setZipcode(zipcode);


        int unit = sharedPreferences.getInt(context.getString(R.string.unit_pref), 0);
        view.setUnit(unit);
    }

    public void onZipcodeEdited(String zipcode) {
        this.zipcode = zipcode;
    }

    public boolean onBackButton() {
        if (zipcode.isEmpty()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(context.getString(R.string.zipcode_pref), BuildConfig.DEFAULT_ZIP).apply();
            return true;
        }
        if (zipcode.matches("[0-9]{5}")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(context.getString(R.string.zipcode_pref), zipcode).apply();
            return true;
        } else {
            view.showError(context.getString(R.string.enter_valid_zipcode));
            return false;
        }
    }


    public void onUnitSelected(int unit) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.unit_pref), unit).apply();
    }
}
