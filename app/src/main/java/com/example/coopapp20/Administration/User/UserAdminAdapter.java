package com.example.coopapp20.Administration.User;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.Data.Objects.UserObject;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.ResItemAdminUserBinding;

import java.util.ArrayList;
import java.util.List;

public class UserAdminAdapter extends RecyclerView.Adapter<UserAdminAdapter.UserViewHolder> {
    private List<UserObject> dataSet = new ArrayList<>();
    private List<UserObject> dataSetSorted = new ArrayList<>();
    private OnCustomClickListener Listener;
    private DisplayMetrics Metrics;

    UserAdminAdapter(OnCustomClickListener listener, DisplayMetrics metrics){
        Listener = listener;
        Metrics = metrics;
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        ResItemAdminUserBinding Binding;

        UserViewHolder(ResItemAdminUserBinding binding) {
            super(binding.getRoot());
            Binding = binding;
        }

        private void setViews(UserObject object){
            Binding.TitleTextView.setText(object.getName());
            Binding.OptionsBtn.setOnClickListener(v -> Listener.onCustomClick(v, object));
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewRoot = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_admin_user,parent,false);
        ResItemAdminUserBinding Binding = ResItemAdminUserBinding.bind(viewRoot);
        return new UserViewHolder(Binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setViews(dataSetSorted.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSetSorted.size();
    }

    public void UpdateData(List<UserObject> UpdatedData){
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
        void onCustomClick(View v, UserObject user);
    }
}
