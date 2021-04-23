package com.example.coopapp20.Tasks.Task;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.Data.Objects.TaskObject;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.ResItemTaskBinding;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>  {
    private ArrayList<TaskObject> dataSet = new ArrayList<>();
    private ArrayList<TaskObject> datasetSorted = new ArrayList<>();
    private ArrayList<Integer> ExemptedData = new ArrayList<>();
    private String SearchText;
    private OnTaskClickListener OnTaskClickListener;
    private DisplayMetrics Metrics;

    TaskAdapter(OnTaskClickListener onTaskClickListener, DisplayMetrics metrics){
        OnTaskClickListener = onTaskClickListener;
        Metrics = metrics;
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private ResItemTaskBinding Binding;

        TaskViewHolder(ResItemTaskBinding binding) {
            super(binding.getRoot());
            Binding = binding;
        }

        void setData(com.example.coopapp20.Data.Objects.TaskObject object){
            Binding.TitelTextView.setText(object.getTitle());
            Binding.DaysTextView.setText("Dage " + object.getDaysLeft() + "/" + object.getDays());
            Binding.TimeTextView.setText("ca: " + object.getTimeString());
            Binding.DepartmentTextView.setText(object.getDepartmentName());
            Binding.ColorImageView.setColorFilter(object.getColor());

            Binding.getRoot().setOnClickListener( v -> OnTaskClickListener.onTaskClick(datasetSorted.get(getAdapterPosition())));

            ViewGroup.LayoutParams params = Binding.getRoot().getLayoutParams();
            params.height = (int) (Metrics.heightPixels*0.1);
            Binding.getRoot().setLayoutParams(params);
        }

    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_task,parent,false);
        ResItemTaskBinding Binding = ResItemTaskBinding.bind(listItemView);
        return new TaskViewHolder(Binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.setData(datasetSorted.get(position));
    }

    @Override
    public int getItemCount() {
        return datasetSorted.size();
    }

    void UpdateData(List<TaskObject> UpdatedData){
        if(UpdatedData != null) {
            dataSet = new ArrayList<>(UpdatedData);
            FilterData(SearchText);
        }
    }

    void FilterData(String searchtext){
        SearchText = searchtext;
        datasetSorted = new ArrayList<>(dataSet);

        if(searchtext != null && !searchtext.equals("")) {
            //Filter all strings split by " "
            String[] SearchTextParts = searchtext.split("\\s+");
            for(String searchTextPart : SearchTextParts){
                datasetSorted.removeIf(TaskObject -> !TaskObject.getTitle().toLowerCase().contains(searchTextPart.toLowerCase()));
            }
        }

        datasetSorted.removeIf(o -> ExemptedData.stream().anyMatch(i -> i.equals(o.getId())));

        notifyDataSetChanged();
    }

    void AddExemptedData(Integer TaskId){
        if(!ExemptedData.contains(TaskId)){
            ExemptedData.add(TaskId);}
        FilterData(SearchText);
    }

    void RemoveExemptedData(Integer TaskId){
        ExemptedData.removeIf(i -> i.equals(TaskId));
        FilterData(SearchText);
    }

    ArrayList<TaskObject> getData(){
        return datasetSorted;
    }

    public interface OnTaskClickListener{
        void onTaskClick(TaskObject object);
    }
}
