package com.example.coopapp20.Administration.Department;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.Data.Objects.DepartmentObject;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.ResItemAdminUserBinding;

import java.util.ArrayList;
import java.util.List;

public class DepartmentAdminAdapter extends RecyclerView.Adapter<DepartmentAdminAdapter.DepartmentViewHolder> {
    private List<DepartmentObject> dataSet = new ArrayList<>();
    private List<DepartmentObject> dataSetSorted = new ArrayList<>();
    private OnCustomClickListener Listener;
    private DisplayMetrics Metrics;

    DepartmentAdminAdapter(OnCustomClickListener listener, DisplayMetrics metrics){
        Listener = listener;
        Metrics = metrics;
    }

    class DepartmentViewHolder extends RecyclerView.ViewHolder {
        ResItemAdminUserBinding Binding;

        DepartmentViewHolder(ResItemAdminUserBinding binding) {
            super(binding.getRoot());
            Binding = binding;
        }

        private void setViews(DepartmentObject object){
            Binding.TitleTextView.setText(object.getName());
            Binding.OptionsBtn.setOnClickListener(v -> Listener.onCustomClick(v, object));

            Binding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int) (Metrics.heightPixels*0.1)));
        }
    }

    @NonNull
    @Override
    public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewRoot = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_admin_user,parent,false);
        ResItemAdminUserBinding Binding = ResItemAdminUserBinding.bind(viewRoot);
        return new DepartmentViewHolder(Binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentViewHolder holder, int position) {
        holder.setViews(dataSetSorted.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSetSorted.size();
    }

    public void UpdateData(List<DepartmentObject> UpdatedData){
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
        void onCustomClick(View v, DepartmentObject user);
    }
}
