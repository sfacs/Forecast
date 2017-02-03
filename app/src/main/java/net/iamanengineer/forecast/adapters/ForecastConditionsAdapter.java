package net.iamanengineer.forecast.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.iamanengineer.forecast.model.ForecastCondition;
import net.iamanengineer.forecast.services.UnitService;
import net.iamanengineer.forecast.utils.IconUtils;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by julien on 01-20-17.
 */

class ForecastConditionsAdapter extends RecyclerView.Adapter<ForecastConditionsAdapter.ViewHolder> {


    private final UnitService unitService;
    private List<ForecastCondition> forecastConditions;

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView time;
        public TextView temperature;
        public ImageView icon;

        ViewHolder(View view) {
            super(view);
            this.time = ButterKnife.findById(view, net.iamanengineer.forecast.R.id.time);
            this.temperature = ButterKnife.findById(view, net.iamanengineer.forecast.R.id.temperature);
            this.icon= ButterKnife.findById(view, net.iamanengineer.forecast.R.id.icon);
        }
    }

    ForecastConditionsAdapter(UnitService unitService, List<ForecastCondition> forecastConditions) {
        this.forecastConditions= forecastConditions;
        this.unitService = unitService;
    }

    @Override
    public ForecastConditionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(net.iamanengineer.forecast.R.layout.forecast_condition, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ForecastCondition forecastCondition = forecastConditions.get(position);
        holder.time.setText(forecastCondition.displayTime);

        holder.temperature.setText(unitService.getTemperatureString(forecastCondition));


        int color = holder.icon.getContext().getColor(net.iamanengineer.forecast.R.color.day_title_color);
        // if the temperature is both highest and lowest, don't color
        if (forecastCondition.isHighestTemp && !forecastCondition.isLowestTemp) {
            color = holder.icon.getContext().getColor(net.iamanengineer.forecast.R.color.weather_warm);
            holder.icon.setColorFilter(color);
        } else if (forecastCondition.isLowestTemp && !forecastCondition.isHighestTemp) {
            color = holder.icon.getContext().getColor(net.iamanengineer.forecast.R.color.weather_cool);
            holder.icon.setColorFilter(color);
        }

        holder.time.setTextColor(color);
        holder.temperature.setTextColor(color);

        Glide.with(holder.icon.getContext())
                .load(IconUtils.getUrlForIcon(forecastCondition.icon))
                .into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return forecastConditions.size();
    }
}




