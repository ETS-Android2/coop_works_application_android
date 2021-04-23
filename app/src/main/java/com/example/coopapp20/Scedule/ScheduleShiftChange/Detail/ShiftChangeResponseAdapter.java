package com.example.coopapp20.Scedule.ScheduleShiftChange.Detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.Data.Objects.ActiveShiftObject;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.ResItemScheduleResponseBinding;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ShiftChangeResponseAdapter extends RecyclerView.Adapter<ShiftChangeResponseAdapter.ShiftChangeResponseViewHolder> {
    private ArrayList<ActiveShiftObject> dataSet = new ArrayList<>();

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy", Locale.ENGLISH);

    class ShiftChangeResponseViewHolder extends RecyclerView.ViewHolder {

        ResItemScheduleResponseBinding Binding;

        ShiftChangeResponseViewHolder(ResItemScheduleResponseBinding binding){
            super(binding.getRoot());
            Binding = binding;
        }

        void setViews(ActiveShiftObject object){
            Binding.DayTextView.setText(Binding.getRoot().getContext().getResources().getStringArray(R.array.day_array)[object.getDate().getDayOfWeek().getValue()-1]);
            Binding.DateTextView.setText(object.getDate().format(dateFormatter));
            Binding.TimeTextView.setText(object.getStartTime().format(timeFormatter) + " - " + object.getEndTime().format(timeFormatter));

            setShiftSelectedImageView(object.getResponseShiftSelected());

            Binding.SelectedImageView.setOnClickListener(v ->{dataSet.get(getAdapterPosition()).setResponseShiftSelected();setShiftSelectedImageView(dataSet.get(getAdapterPosition()).getResponseShiftSelected());});
        }

        private void setShiftSelectedImageView(boolean shiftSelected) {
            if(shiftSelected){Binding.SelectedImageView.setImageResource(R.drawable.sharp_check_circle_outline_24);}
            if(!shiftSelected){Binding.SelectedImageView.setImageResource(R.drawable.sharp_add_circle_outline_24);}
        }

    }

    public ShiftChangeResponseAdapter(){}

    @NonNull
    @Override
    public ShiftChangeResponseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_schedule_response,parent,false);
        ResItemScheduleResponseBinding Binding = ResItemScheduleResponseBinding.bind(listItemView);
        return new ShiftChangeResponseViewHolder(Binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftChangeResponseViewHolder holder, int position) {
        holder.setViews(dataSet.get(position));
    }

    public List<Integer> getSelectedItems() {
        return dataSet.stream().filter(ActiveShiftObject::getResponseShiftSelected).map(ActiveShiftObject::getId).collect(Collectors.toList());
    }

    public void UpdateData(List<ActiveShiftObject> Data){
        if(Data != null){
            dataSet = new ArrayList<>(Data);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
