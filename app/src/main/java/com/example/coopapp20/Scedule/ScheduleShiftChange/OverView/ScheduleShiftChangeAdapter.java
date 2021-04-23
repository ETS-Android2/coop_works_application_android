package com.example.coopapp20.Scedule.ScheduleShiftChange.OverView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.R;
import com.example.coopapp20.Scedule.ScheduleShiftChange.ScheduleShiftChangeRequestObject;
import com.example.coopapp20.databinding.ResItemDividerDateBinding;
import com.example.coopapp20.databinding.ResItemScheduleShiftchangeBinding;
import com.example.coopapp20.zOtherFiles.DividerObject;

import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ScheduleShiftChangeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Object> dataSet = new ArrayList<>();
    private OnShiftChangeClickListener Listener;
    private DisplayMetrics Metrics;

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy", Locale.ENGLISH);
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);

    ScheduleShiftChangeAdapter(OnShiftChangeClickListener listener, DisplayMetrics metrics){
        Listener = listener;
        Metrics = metrics;
    }

    class ShiftChangeViewHolder extends RecyclerView.ViewHolder{

        ResItemScheduleShiftchangeBinding Binding;

        ShiftChangeViewHolder(ResItemScheduleShiftchangeBinding binding) {
            super(binding.getRoot());
            Binding = binding;
        }

        private void setViews(ScheduleShiftChangeRequestObject object){
            Binding.ShiftHolderNameTextView.setText(object.getShiftHolderName());
            Binding.ResponderNameTextView.setText(object.getResponderName());
            Binding.DateTextView.setText(object.getShiftDate().format(dateFormatter));
            Binding.TimeTextView.setText(object.getShiftStartTime().format(timeFormatter) + " - " + object.getShiftEndTime().format(timeFormatter));
            Binding.StatusImageView.setImageDrawable(object.getRequestDrawable(Binding.getRoot().getContext()));
            Binding.ResponseImageView.setImageDrawable(object.getResponseDrawable());

            Binding.getRoot().setOnClickListener(v -> Listener.onShiftChangeClick((ScheduleShiftChangeRequestObject) dataSet.get(getAdapterPosition())));

            Binding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Metrics.heightPixels*0.1)));
        }
    }
    class ShiftChangeDividerViewHolder extends RecyclerView.ViewHolder {

        private ResItemDividerDateBinding Binding;

        ShiftChangeDividerViewHolder(ResItemDividerDateBinding binding) {
            super(binding.getRoot());
            Binding = binding;
        }

        private void setViews(DividerObject object) {
            Binding.DividerText.setText(object.getDate().format(dateFormatter));

            Binding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Metrics.heightPixels*0.07)));
        }
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType){
            case 0:
                View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_schedule_shiftchange,parent,false);
                ResItemScheduleShiftchangeBinding b1 = ResItemScheduleShiftchangeBinding.bind(v1);
                return new ShiftChangeViewHolder(b1);
            case 1:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_divider_date,parent,false);
                ResItemDividerDateBinding b2 = ResItemDividerDateBinding.bind(v2);
                return new ShiftChangeDividerViewHolder(b2);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(dataSet.get(position).getClass() == ScheduleShiftChangeRequestObject.class){
            ScheduleShiftChangeRequestObject object = (ScheduleShiftChangeRequestObject) dataSet.get(position);
            ShiftChangeViewHolder ViewHolder = (ShiftChangeViewHolder) holder;
            ViewHolder.setViews(object);
        }else {
            DividerObject object = (DividerObject) dataSet.get(position);
            ShiftChangeDividerViewHolder ViewHolder = (ShiftChangeDividerViewHolder) holder;
            ViewHolder.setViews(object);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position).getClass() == ScheduleShiftChangeRequestObject.class ? 0 : 1;
    }

    void UpdateData(List<ScheduleShiftChangeRequestObject> UpdatedData){
        if(UpdatedData != null) {
            dataSet = new ArrayList<>(UpdatedData);
            this.notifyDataSetChanged();
        }
    }

    void FilterData(String searchtext){

    }

    public interface OnShiftChangeClickListener{
        void onShiftChangeClick(ScheduleShiftChangeRequestObject object);
    }
}
