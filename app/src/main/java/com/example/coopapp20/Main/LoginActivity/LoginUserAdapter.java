package com.example.coopapp20.Main.LoginActivity;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.Data.Objects.UserObject;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.ResItemLoginUserBinding;

import java.util.ArrayList;
import java.util.List;

public class LoginUserAdapter extends RecyclerView.Adapter<LoginUserAdapter.LoginUserViewHolder> {
    private List<UserObject> dataSet = new ArrayList<>();
    private OnLoginUserClickListener Listener;
    private DisplayMetrics Metrics;

    LoginUserAdapter(OnLoginUserClickListener listener, DisplayMetrics metrics){
        Listener = listener;
        Metrics = metrics;
    }

    class LoginUserViewHolder extends RecyclerView.ViewHolder{
        ResItemLoginUserBinding Binding;

        LoginUserViewHolder(ResItemLoginUserBinding binding){
            super(binding.getRoot());
            Binding = binding;
        }

        private void setViews(UserObject user){
            Binding.textView3.setText(user.getName());
            Binding.textView4.setText(user.getDepartmentNameString());
            Binding.imageView7.setColorFilter(user.getDepartmentColorString());

            Binding.getRoot().setOnClickListener(v -> Listener.onLoginUserClick(user));
            Binding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int) (Metrics.heightPixels*0.1)));
        }
    }

    @NonNull
    @Override
    public LoginUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewRoot = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_login_user,parent,false);
        ResItemLoginUserBinding Binding = ResItemLoginUserBinding.bind(viewRoot);
        return new LoginUserViewHolder(Binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LoginUserViewHolder holder, int position) {
        holder.setViews(dataSet.get(position));
    }

    @Override
    public int getItemCount() { return dataSet.size(); }

    public void UpdateData(List<UserObject> UpdateData){
        if(UpdateData != null){
            dataSet = UpdateData;
            notifyDataSetChanged();
        }
    }

    public interface OnLoginUserClickListener{
        void onLoginUserClick(UserObject user);
    }

}
