package com.example.coopapp20.Scedule.Schedule;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.Data.Objects.ActiveShiftObject;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.ResItemScheduleActiveshiftBinding;
import com.example.coopapp20.databinding.ResItemDividerDayBinding;
import com.example.coopapp20.zOtherFiles.DividerObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Object> dataSet = new ArrayList<>();
    private OnActiveShiftClickListener Listener;
    private DisplayMetrics Metrics;

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy", Locale.ENGLISH);
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);

    ScheduleAdapter(OnActiveShiftClickListener listener, DisplayMetrics metrics){
        Listener = listener;
        Metrics = metrics;
    }

    class ScheduleViewHolder extends RecyclerView.ViewHolder {
        ResItemScheduleActiveshiftBinding Binding;

        ScheduleViewHolder(ResItemScheduleActiveshiftBinding binding) {
            super(binding.getRoot());
            Binding = binding;
        }

        void setViews(ScheduleActiveShiftObject activeShift) {
            Binding.NameTextView.setText(activeShift.getShiftHolderName());
            Binding.DepartmentTextView.setText(activeShift.getDepartmentName());
            Binding.DateTextView.setText(activeShift.getDate().format(dateFormatter));
            Binding.TimeTextView.setText(activeShift.getStartTime().format(timeFormatter));

            if(activeShift.getIcon() == ScheduleActiveShiftObject.ICON_REQUEST_BTN){
                Binding.ShiftChangeBtn.setOnClickListener(v -> Listener.onShiftChangeBtnClick(activeShift));
                Binding.ShiftChangeBtn.setVisibility(View.VISIBLE);
            }else {
                Binding.ShiftChangeBtn.setOnClickListener(null);
                Binding.ShiftChangeBtn.setVisibility(View.INVISIBLE);
            }

            Binding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(Metrics.heightPixels*0.07)));
        }
    }
    class DayDividerViewHolder extends RecyclerView.ViewHolder {
        ResItemDividerDayBinding Binding;

        DayDividerViewHolder(ResItemDividerDayBinding binding) {
            super(binding.getRoot());
            Binding = binding;
        }

        void setViews(DividerObject divider){
            Binding.ScheduleDayDividerItemDayTextView.setText(Binding.getRoot().getContext().getResources().getStringArray(R.array.day_array)[divider.getDate().getDayOfWeek().getValue()-1]);
            Binding.ScheduleDayDividerItemDateTextView.setText(divider.getDate().format(dateFormatter));
            Binding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(Metrics.heightPixels*0.05)));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType){
            //view is FinishedTaskView
            case 0:
                View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_schedule_activeshift,parent,false);
                viewHolder = new ScheduleViewHolder(ResItemScheduleActiveshiftBinding.bind(v1));
                break;

            //view is date divider
            case 1:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_divider_day,parent,false);
                viewHolder = new DayDividerViewHolder(ResItemDividerDayBinding.bind(v2));
                break;

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(dataSet.get(position).getClass() == ScheduleActiveShiftObject.class) {
            ScheduleViewHolder Holder = (ScheduleViewHolder) holder;
            Holder.setViews((ScheduleActiveShiftObject) dataSet.get(position));
        } else {
            DayDividerViewHolder Holder = (DayDividerViewHolder) holder;
            Holder.setViews((DividerObject) dataSet.get(position));
        }
    }

    public void UpdateData(List<ScheduleActiveShiftObject> UpdatedData, LocalDate SelectedWeekStart){
        if(UpdatedData != null) {
            dataSet = new ArrayList<>(UpdatedData);
            for( int x = 0 ; x < 7 ; x++){
                dataSet.add(new DividerObject().setDate(SelectedWeekStart.plusDays(x)));
            }

            Collections.sort(dataSet, ((o1, o2) -> {
                LocalDate o1Date;
                LocalDate o2Date;

                if(o1.getClass() == ScheduleActiveShiftObject.class){
                    o1Date = ((ScheduleActiveShiftObject) o1).getDate();
                }else {o1Date = ((DividerObject) o1).getDate();}

                if(o2.getClass() == ScheduleActiveShiftObject.class){
                    o2Date = ((ScheduleActiveShiftObject) o2).getDate();
                }else {o2Date = ((DividerObject) o2).getDate();}

                if(o1Date.compareTo(o2Date) != 0 || o1.getClass() == o2.getClass()){return o1Date.compareTo(o2Date);}
                else if(o1.getClass() == DividerObject.class){return -1;}
                else {return 0;}

            }));

            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position).getClass() != ScheduleActiveShiftObject.class ? 1 : 0;
    }

    public interface OnActiveShiftClickListener{
        void onShiftChangeBtnClick(ScheduleActiveShiftObject object);
    }
}
