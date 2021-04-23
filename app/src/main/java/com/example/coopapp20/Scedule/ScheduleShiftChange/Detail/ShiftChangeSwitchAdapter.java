package com.example.coopapp20.Scedule.ScheduleShiftChange.Detail;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.Data.Objects.ActiveShiftObject;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.ResItemScheduleSwitchBinding;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShiftChangeSwitchAdapter extends RecyclerView.Adapter<ShiftChangeSwitchAdapter.ShiftChangeSwitchViewHolder> {
    private ArrayList<ActiveShiftObject> dataSet = new ArrayList<>();
    private OnSwitchClickListener Listener;
    private DisplayMetrics Metrics;

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy", Locale.ENGLISH);

    ShiftChangeSwitchAdapter(OnSwitchClickListener listener, DisplayMetrics metrics){
        Listener = listener;
        Metrics = metrics;
    }

    class ShiftChangeSwitchViewHolder extends RecyclerView.ViewHolder{

        ResItemScheduleSwitchBinding Binding;

        ShiftChangeSwitchViewHolder(ResItemScheduleSwitchBinding binding) {
            super(binding.getRoot());
            Binding = binding;
        }

        private void setTextViews(ActiveShiftObject object){
            Binding.DayTextView.setText(Binding.getRoot().getContext().getResources().getStringArray(R.array.day_array)[object.getDate().getDayOfWeek().getValue()-1]);
            Binding.DateTextView.setText(object.getDate().format(dateFormatter));
            Binding.TimeTextView.setText(object.getStartTime().format(timeFormatter) + " - " + object.getEndTime().format(timeFormatter));
            Binding.NameTextView.setText(object.getShiftHolderName());
            Binding.StatusImageView.setColorFilter(object.getSwitchStatusColor());

            Binding.OptionsBtn.setOnClickListener(v -> Listener.onSwitchClick(dataSet.get(getAdapterPosition()),v));
            Binding.OptionsBtn.setClickable(object.getSwitchActive());

            if(object.getSwitchActive()) { Binding.OptionsBtn.setVisibility(View.VISIBLE);
            }else { Binding.OptionsBtn.setVisibility(View.INVISIBLE); }

            Binding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Metrics.heightPixels*0.1)));
        }
    }

    @NonNull
    @Override
    public ShiftChangeSwitchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_schedule_switch,parent,false);
        ResItemScheduleSwitchBinding Binding = ResItemScheduleSwitchBinding.bind(listItemView);
        return new ShiftChangeSwitchViewHolder(Binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftChangeSwitchViewHolder holder, int position) {
        holder.setTextViews(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    void UpdateData(List<ActiveShiftObject> UpdatedData){
        if(UpdatedData != null) {
            dataSet = new ArrayList<>(UpdatedData);
            this.notifyDataSetChanged();
        }
    }

    interface OnSwitchClickListener{
        void onSwitchClick(ActiveShiftObject object, View optionsbtn);
    }
}
