package net.iamanengineer.forecast.views.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import net.iamanengineer.forecast.ForecastApp;
import net.iamanengineer.forecast.presenters.SettingsPresenter;
import net.iamanengineer.forecast.views.SettingsView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

/**
 * Created by julien on 01-20-17.
 */
public class SettingsActivity extends AppCompatActivity implements SettingsView {

    @Inject
    SettingsPresenter presenter;

    @BindView(net.iamanengineer.forecast.R.id.zipcode)
    EditText zipcodeEdit;

    @BindView(net.iamanengineer.forecast.R.id.unit)
    Spinner unitSpinner;


    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(net.iamanengineer.forecast.R.layout.activity_settings);
        ((ForecastApp) getApplication()).getAppComponent().inject(this);
        unbinder = ButterKnife.bind(this);
        presenter.setView(this);

        setupUnitSpinner();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }


    @OnEditorAction(net.iamanengineer.forecast.R.id.zipcode)
    public boolean onEditorAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            hideSoftKeyboard();
            if (presenter.onBackButton()) {
                onBackPressed();
            }
        }
        return true;
    }

    @OnItemSelected(net.iamanengineer.forecast.R.id.unit)
    void onUnitSelected(int position) {
        presenter.onUnitSelected(position);
    }

    private void setupUnitSpinner() {
        List<String> units = new ArrayList<>();
        units.add("Farenheit");
        units.add("Celcius");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(dataAdapter);
    }


    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        presenter.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onTryToExitSettings();
        }

        return super.onOptionsItemSelected(item);
    }


    public void onTryToExitSettings() {
        hideSoftKeyboard();
        if (presenter.onBackButton()) {
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        onTryToExitSettings();
    }

    @OnTextChanged(value = net.iamanengineer.forecast.R.id.zipcode, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onZipcodeEdited(Editable editable) {
        presenter.onZipcodeEdited(editable.toString());

    }

    @Override
    public void setActionBarTitle(String title) {

    }

    @Override
    public void showError(String error) {
        Snackbar.make(zipcodeEdit, error, Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void setZipcode(String zipcode) {
        zipcodeEdit.setText(zipcode);
    }

    private void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void setUnit(int unit) {
        unitSpinner.setSelection(unit);

    }
}
