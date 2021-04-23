package com.example.coopapp20.Administration.Task;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.Data.Objects.TaskObject;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.ResItemAdminUserBinding;

import java.util.ArrayList;
import java.util.List;

public class TaskAdminAdapter extends RecyclerView.Adapter<TaskAdminAdapter.TaskViewHolder> {
private List<TaskObject> dataSet = new ArrayList<>();
private List<TaskObject> dataSetSorted = new ArrayList<>();
private OnCustomClickListener Listener;
private DisplayMetrics Metrics;

    TaskAdminAdapter(OnCustomClickListener listener, DisplayMetrics metrics){
        Listener = listener;
        Metrics = metrics;
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        ResItemAdminUserBinding Binding;

        TaskViewHolder(ResItemAdminUserBinding binding) {
            super(binding.getRoot());
            Binding = binding;
        }

        private void setViews(TaskObject object){
            Binding.TitleTextView.setText(object.getTitle());
            Binding.OptionsBtn.setOnClickListener(v -> Listener.onCustomClick(v, object));

            Binding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int) (Metrics.heightPixels*0.1)));
        }
    }

        @NonNull
        @Override
        public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View viewRoot = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_admin_user,parent,false);
            ResItemAdminUserBinding Binding = ResItemAdminUserBinding.bind(viewRoot);
            return new TaskViewHolder(Binding);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
            holder.setViews(dataSetSorted.get(position));
        }

        @Override
        public int getItemCount() {
            return dataSetSorted.size();
        }

        public void UpdateData(List<TaskObject> UpdatedData){
            if(UpdatedData != null){
                dataSet = new ArrayList<>(UpdatedData);
                FilterData(null);
            }
        }

        public void FilterData(String SearchText){
            dataSetSorted = new ArrayList<>(dataSet);

            if(SearchText != null && !SearchText.equals("")){}

            notifyDataSetChanged();
        }

    public interface OnCustomClickListener{
        void onCustomClick(View v, TaskObject user);
    }
}
