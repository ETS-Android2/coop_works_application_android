package com.example.coopapp20.Administration.Schedule;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.Data.Objects.ShiftObject;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.ResItemScheduleShiftBinding;

import java.util.ArrayList;
import java.util.List;

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.DetailViewHolder> {
    private ArrayList<ShiftObject> dataSet = new ArrayList<>();
    private OnShiftDetailClickListener OnCustomClickListener;
    private DisplayMetrics Metrics;

    ShiftAdapter(OnShiftDetailClickListener onCustomClickListener, DisplayMetrics metrics){
        OnCustomClickListener = onCustomClickListener;
        Metrics = metrics;
    }

     class DetailViewHolder extends RecyclerView.ViewHolder {

         ResItemScheduleShiftBinding Binding;

        DetailViewHolder(ResItemScheduleShiftBinding binding) {
            super(binding.getRoot());
            Binding = binding;
        }

        void setViews(ShiftObject shift){
            Binding.DepartmentTextview.setText(shift.getDepartmentName());
            Binding.DayTextview.setText(shift.getDayString());
            Binding.Timetextview.setText(shift.getTimeString());
            Binding.ColorImageView.setColorFilter(shift.getDepartmentColor());

            Binding.OptionsBtn.setOnClickListener(v -> OnCustomClickListener.onShiftBtnClick(dataSet.get(getAdapterPosition()), v));

            ViewGroup.LayoutParams params = Binding.getRoot().getLayoutParams();
            params.height = (int) (Metrics.heightPixels*0.1);
            Binding.getRoot().setLayoutParams(params);
        }

    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_schedule_shift,parent,false);
        ResItemScheduleShiftBinding Binding = ResItemScheduleShiftBinding.bind(listItemView);
        return new ShiftAdapter.DetailViewHolder(Binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        holder.setViews(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void UpdateData(List<ShiftObject> UpdatedData){
        if(UpdatedData != null) {
            dataSet = new ArrayList<>(UpdatedData);
            this.notifyDataSetChanged();
        }
    }

    public interface OnShiftDetailClickListener{
        void onShiftBtnClick(ShiftObject object, View optionsbtn);
    }
}
