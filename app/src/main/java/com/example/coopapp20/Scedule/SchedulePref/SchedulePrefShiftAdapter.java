package com.example.coopapp20.Scedule.SchedulePref;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.Data.Objects.ShiftPreferenceObject;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.ResItemSchedulePrefShiftBinding;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SchedulePrefShiftAdapter extends RecyclerView.Adapter<SchedulePrefShiftAdapter.DetailViewHolder> {
    private ArrayList<ScheduleShiftPreferenceObject> dataSet = new ArrayList<>();
    private OnAddPreferenceShiftListClickListener OnCustomClickListener;
    private DisplayMetrics Metrics;

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);

    class DetailViewHolder extends RecyclerView.ViewHolder{

        ResItemSchedulePrefShiftBinding Binding;

        DetailViewHolder(ResItemSchedulePrefShiftBinding binding){
            super(binding.getRoot());
            Binding = binding;
        }

        void setViews(ScheduleShiftPreferenceObject shiftPreference){
            Binding.DayTextView.setText(Binding.getRoot().getContext().getResources().getStringArray(R.array.day_array)[shiftPreference.getDay()]);
            Binding.TimeTextView.setText(shiftPreference.getStartTime().format(timeFormatter) + " - " + shiftPreference.getEndTime().format(timeFormatter));
            Binding.ColorImageView.setColorFilter(shiftPreference.getColor(Binding.getRoot().getContext()));

            Binding.getRoot().setOnClickListener(view -> OnCustomClickListener.onShiftClick(shiftPreference));

            Binding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Metrics.heightPixels*0.1)));
        }

    }

    SchedulePrefShiftAdapter(OnAddPreferenceShiftListClickListener onCustomClickListener, DisplayMetrics metrics){
        OnCustomClickListener = onCustomClickListener;
        Metrics = metrics;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_schedule_pref_shift,parent,false);
        ResItemSchedulePrefShiftBinding Binding =  ResItemSchedulePrefShiftBinding.bind(listItemView);
        return new DetailViewHolder(Binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        holder.setViews(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void UpdateData(List<ScheduleShiftPreferenceObject> UpdatedData){
        dataSet = new ArrayList<>(UpdatedData);
        notifyDataSetChanged();
    }

    public interface OnAddPreferenceShiftListClickListener{
        void onShiftClick(ScheduleShiftPreferenceObject object);
    }
}




