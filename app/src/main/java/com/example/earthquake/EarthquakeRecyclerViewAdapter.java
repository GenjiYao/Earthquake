package com.example.earthquake;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.earthquake.databinding.ListItemEarthquakeBinding;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class EarthquakeRecyclerViewAdapter extends RecyclerView.Adapter<EarthquakeRecyclerViewAdapter.ViewHolder> {
    private  final List<Earthquake> mEarthquakes;


    public EarthquakeRecyclerViewAdapter(List<Earthquake> mEarthquakes) {
        this.mEarthquakes = mEarthquakes;
    }

    @Override
    public int getItemCount() {
        return mEarthquakes.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_earthquake,parent,false);
         ListItemEarthquakeBinding binding = ListItemEarthquakeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Earthquake earthquake = mEarthquakes.get(position);
        holder.binding.setEarthquake(earthquake);
        holder.binding.executePendingBindings();
        //        holder.date.setText(TIME_FORMAT.format(earthquake.getMDate()));
//        holder.details.setText(earthquake.getMDetails());
//        holder.magnitude.setText(MAGNITUDE_FORMAT.format(earthquake.getMMagnitude()));
    }

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.US);
    private static final NumberFormat MAGNITUDE_FORMAT = new DecimalFormat("0.0");

    public class ViewHolder extends RecyclerView.ViewHolder{


//        public final TextView date;
//        public final TextView details;
//        public final TextView magnitude;
        public final ListItemEarthquakeBinding binding;


        public ViewHolder(ListItemEarthquakeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.setMagnitudeformat(MAGNITUDE_FORMAT);
            binding.setTimeformat(TIME_FORMAT);
//            date = itemView.findViewById(R.id.date);
//            details = itemView.findViewById(R.id.details);
//            magnitude = itemView.findViewById(R.id.magnitude);
        }

//        @Override
//        public String toString() {
//            return super.toString() + "'" + detailsView.getText() + "'";
//        }
    }
}
