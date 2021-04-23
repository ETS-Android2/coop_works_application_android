package com.example.coopapp20.Scedule.SchedulePref;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.Data.Objects.OffDayRequestObject;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.ResItemSchedulePrefOffdayrequestBinding;

import java.util.ArrayList;
import java.util.List;

public class SchedulePrefOffDayRequestAdapter extends RecyclerView.Adapter<SchedulePrefOffDayRequestAdapter.DetailViewHolder> {
    private ArrayList<ScheduleOffDayRequestObject> dataSet = new ArrayList<>();
    private OnOffDayRequestClickListener OnCustomClickListener;
    private DisplayMetrics Metrics;

    class DetailViewHolder extends RecyclerView.ViewHolder {

        ResItemSchedulePrefOffdayrequestBinding Binding;

        DetailViewHolder(ResItemSchedulePrefOffdayrequestBinding binding){
            super(binding.getRoot());
            Binding = binding;
        }

        void setViews(ScheduleOffDayRequestObject offDayRequest){

            Binding.TypeTextView.setText(offDayRequest.getTypeString(Binding.getRoot().getContext()));
            Binding.DateTextView.setText(offDayRequest.getDateString());
            Binding.ColorImageView.setColorFilter(offDayRequest.getStatusColor(Binding.getRoot().getContext()));

            Binding.OptionsBtn.setOnClickListener(v -> OnCustomClickListener.onOffDayRequestClick(dataSet.get(getAdapterPosition()),v));

            Binding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(Metrics.heightPixels *0.1)));
        }

    }

    SchedulePrefOffDayRequestAdapter(OnOffDayRequestClickListener onCustomClickListener, DisplayMetrics metrics){
        OnCustomClickListener = onCustomClickListener;
        Metrics = metrics;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_schedule_pref_offdayrequest,parent,false);
        ResItemSchedulePrefOffdayrequestBinding Binding = ResItemSchedulePrefOffdayrequestBinding.bind(listItemView);
        return new DetailViewHolder(Binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        holder.setViews(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void UpdateData(List<ScheduleOffDayRequestObject> UpdatedData){
        dataSet = new ArrayList<>(UpdatedData);
        this.notifyDataSetChanged();
    }

    public interface OnOffDayRequestClickListener{
        void onOffDayRequestClick(ScheduleOffDayRequestObject object, View OptionsBtn);
    }
}




