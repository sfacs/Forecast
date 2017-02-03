package net.iamanengineer.forecast.views.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import net.iamanengineer.forecast.ForecastApp;
import net.iamanengineer.forecast.adapters.DayAdapter;
import net.iamanengineer.forecast.model.Day;
import net.iamanengineer.forecast.presenters.WeatherPresenter;
import net.iamanengineer.forecast.services.UnitService;
import net.iamanengineer.forecast.views.WeatherView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MainActivity extends AppCompatActivity implements WeatherView {


    @Inject
    WeatherPresenter weatherPresenter;

    @Inject
    UnitService unitService;

    @BindView(net.iamanengineer.forecast.R.id.temperature)
    TextView temperatureView;

    @BindView(net.iamanengineer.forecast.R.id.conditions)
    TextView conditionsView;

    @BindView(net.iamanengineer.forecast.R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(net.iamanengineer.forecast.R.id.days)
    RecyclerView daysView;

    LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private List<Day> days;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(net.iamanengineer.forecast.R.layout.activity_main);
        ((ForecastApp) getApplication()).getAppComponent().inject(this);
        unbinder = ButterKnife.bind(this);

        getSupportActionBar().setElevation(0);
        weatherPresenter.setView(this);


        days = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        adapter = new DayAdapter(this, unitService, days);
        daysView.setLayoutManager(layoutManager);
        daysView.setAdapter(adapter);

        daysView.setNestedScrollingEnabled(false);

    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        weatherPresenter.onResume();
    }

    @Override
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayConditions(String conditions) {
        conditionsView.setText(conditions);

    }

    @Override
    public void displayTemperature(String temperature, boolean isWarm) {
        temperatureView.setText(temperature);
        if (getSupportActionBar() == null)  {
            return;
        }

        if (isWarm) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, net.iamanengineer.forecast.R.color.weather_warm)));
            collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(this, net.iamanengineer.forecast.R.color.weather_warm));
        } else {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, net.iamanengineer.forecast.R.color.weather_cool)));
            collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(this, net.iamanengineer.forecast.R.color.weather_cool));
        }
    }

    @Override
    public void displayHourlyWeather(List<Day> days) {
        this.days.clear();
        this.days.addAll(days);
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(net.iamanengineer.forecast.R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case net.iamanengineer.forecast.R.id.action_settings:
                launchSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void launchSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
