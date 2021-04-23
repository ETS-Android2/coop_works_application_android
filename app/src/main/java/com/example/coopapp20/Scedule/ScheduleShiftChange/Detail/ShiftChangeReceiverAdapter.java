package com.example.coopapp20.Scedule.ScheduleShiftChange.Detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.Data.Objects.ShiftChangeResponseObject;
import com.example.coopapp20.R;
import com.example.coopapp20.Scedule.ScheduleShiftChange.ScheduleShiftChangeResponseObject;
import com.example.coopapp20.databinding.ResItemScheduleShiftchangeReceiverBinding;

import java.util.ArrayList;
import java.util.List;

public class ShiftChangeReceiverAdapter extends RecyclerView.Adapter<ShiftChangeReceiverAdapter.ShiftChangeReceiverViewHolder> {
    private ArrayList<ScheduleShiftChangeResponseObject> dataSet = new ArrayList<>();
    private OnReceiverClickListener Listener;

    ShiftChangeReceiverAdapter(OnReceiverClickListener listener){
        Listener = listener;
    }

    class ShiftChangeReceiverViewHolder extends RecyclerView.ViewHolder{
        ResItemScheduleShiftchangeReceiverBinding Binding;

        ShiftChangeReceiverViewHolder(ResItemScheduleShiftchangeReceiverBinding binding) {
            super(binding.getRoot());
            Binding = binding;
        }

        private void setViews(ScheduleShiftChangeResponseObject response){
            Binding.NameTextView.setText(response.getResponderName());
            Binding.ImageView.setImageDrawable(response.getResponseDrawable(Binding.getRoot().getContext()));
            Binding.getRoot().setOnClickListener(v -> Listener.onReceiverClick(dataSet.get(getAdapterPosition())));
        }
    }

    @NonNull
    @Override
    public ShiftChangeReceiverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_schedule_shiftchange_receiver,parent,false);
        ResItemScheduleShiftchangeReceiverBinding Binding = ResItemScheduleShiftchangeReceiverBinding.bind(listItemView);
        return new ShiftChangeReceiverViewHolder(Binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftChangeReceiverViewHolder holder, int position) {
        holder.setViews(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    void UpdateData(List<ScheduleShiftChangeResponseObject> UpdatedData){
        if(UpdatedData != null) {
            dataSet = new ArrayList<>(UpdatedData);
            this.notifyDataSetChanged();
        }
    }

    public interface OnReceiverClickListener{
        void onReceiverClick(ScheduleShiftChangeResponseObject object);
    }
}
