package net.iamanengineer.forecast.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.iamanengineer.forecast.model.Day;
import net.iamanengineer.forecast.model.ForecastCondition;
import net.iamanengineer.forecast.services.UnitService;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by julien on 01-20-17.
 */

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {

    private final Context context;
    private final UnitService unitService;
    private List<Day> days;

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        RecyclerView hourlyConditionsView;

        ViewHolder(View view) {
            super(view);
            this.title= ButterKnife.findById(view, net.iamanengineer.forecast.R.id.title);
            this.hourlyConditionsView = ButterKnife.findById(view, net.iamanengineer.forecast.R.id.hourlyConditions);
        }
    }

    public DayAdapter(Context context, UnitService unitService, List<Day> days) {
        this.context = context;
        this.days = days;
        this.unitService = unitService;
    }

    @Override
    public DayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(net.iamanengineer.forecast.R.layout.day, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Day day =  days.get(position);
        holder.title.setText(day.title);

        List<ForecastCondition> forecastConditions = day.hourlyConditionsList;
        GridLayoutManager layoutManager = new GridLayoutManager(context, 4);
        holder.hourlyConditionsView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new ForecastConditionsAdapter(unitService, forecastConditions);
        holder.hourlyConditionsView.setAdapter(adapter);
        holder.hourlyConditionsView.setNestedScrollingEnabled(false);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }
}



