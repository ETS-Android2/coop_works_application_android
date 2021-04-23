package com.example.coopapp20.Tasks.TaskFinished;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.Data.Objects.FinishedTaskObject;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.ResItemDividerDateBinding;
import com.example.coopapp20.databinding.ResItemTaskPastBinding;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FinishedTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<FinishedTaskObject> dataSet;
    private ArrayList<FinishedTaskObject> dataSetSorted = new ArrayList<>();
    private OnTaskFinishedClickListener Listener;
    private String SearchText = null;
    private DisplayMetrics Metrics;

    FinishedTaskAdapter(OnTaskFinishedClickListener mOnCustomClickListener, DisplayMetrics metrics){
        Listener = mOnCustomClickListener;
        Metrics = metrics;
    }

    class TaskFinishedViewHolder extends RecyclerView.ViewHolder  {

        private ResItemTaskPastBinding Binding;

        TaskFinishedViewHolder(ResItemTaskPastBinding binding){
            super(binding.getRoot());
            Binding = binding;
        }

        void setViews(FinishedTaskObject object){
            Binding.FinishedTaskTitelTextView.setText(object.getTitle());
            Binding.FinishedTaskDateTextView.setText(object.getDateTimeString());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
            LocalTime FinishTime = object.getCompletionTime();
            LocalTime StartTime = FinishTime.minusSeconds(object.getDuration().getSeconds());
            String Time = StartTime.format(formatter) + " - " + FinishTime.format(formatter);
            Binding.FinishedTaskTimeTextView.setText(Time);

            Binding.FinishedTaskOptionsImageButton.setOnClickListener(v -> Listener.onFinishedTaskClick(dataSetSorted.get(getAdapterPosition()),v));

            ViewGroup.LayoutParams Params = Binding.getRoot().getLayoutParams();
            Params.height = (int) (Metrics.heightPixels*0.1);
            Binding.getRoot().setLayoutParams(Params);
        }

    }
    class DateDividerViewHolder extends RecyclerView.ViewHolder{
        ResItemDividerDateBinding Binding;
        DateDividerViewHolder(ResItemDividerDateBinding binding) {
            super(binding.getRoot());
            Binding = binding;
        }
        void setDateTextView(String date){
            Binding.DividerText.setText(date);

            ViewGroup.LayoutParams Params = Binding.getRoot().getLayoutParams();
            Params.height = (int) (Metrics.heightPixels*0.05);
            Binding.getRoot().setLayoutParams(Params);
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType){
            //view is FinishedTaskView
            case 0:
                View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_task_past,parent,false);
                ResItemTaskPastBinding Binding1 = ResItemTaskPastBinding.bind(v1);
                return new TaskFinishedViewHolder(Binding1);

            //view is date divider
            case 1:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_divider_date,parent,false);
                ResItemDividerDateBinding Binding2 = ResItemDividerDateBinding.bind(v2);
                return new DateDividerViewHolder(Binding2);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FinishedTaskObject finishedTask = dataSetSorted.get(position);
        if (!finishedTask.getDivider()) {
            TaskFinishedViewHolder viewHolder = (TaskFinishedViewHolder) holder;
            viewHolder.setViews(finishedTask);
        } else {
            DateDividerViewHolder viewHolder = (DateDividerViewHolder) holder;
            viewHolder.setDateTextView(finishedTask.getDateTimeString().substring(0,8));
        }
    }

    void UpdateData(List<FinishedTaskObject> UpdatedData){
        if(UpdatedData != null) {
            dataSet = new ArrayList<>(UpdatedData);
            dataSetSorted = new ArrayList<>(UpdatedData);
            FilterData(SearchText);
        }
    }

    void FilterData(String searchText){
        SearchText = searchText;
        dataSetSorted = new ArrayList<>(dataSet);

        //Filter all strings split by " "
        if(SearchText != null && !SearchText.equals("")) {
            String[] SearchTextParts = searchText.split("\\s+");
            for (String searchTextPart : SearchTextParts) {
                dataSetSorted.removeIf(FinishedTaskObject -> FinishedTaskObject.getTitle() != null && !FinishedTaskObject.getTitle().toLowerCase().contains(searchTextPart.toLowerCase()));
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataSetSorted.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataSetSorted.get(position).getDivider() ? 1 : 0;
    }

    public interface OnTaskFinishedClickListener{
        void onFinishedTaskClick(FinishedTaskObject object, View optionsbtn);
    }
}
