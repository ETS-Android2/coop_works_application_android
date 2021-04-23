package com.example.coopapp20.Contact.ContactInteractionList;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.Data.Objects.MessageObject;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.ResItemContactPastBinding;
import com.example.coopapp20.databinding.ResItemDividerDateBinding;

import java.util.ArrayList;
import java.util.List;

public class ContactInteractionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private ArrayList<MessageObject> dataSet;
    private ArrayList<MessageObject> dataSetSorted = new ArrayList<>();
    private OnContactInteractionListClickListener Listener;
    private DisplayMetrics Metrics;

    ContactInteractionAdapter(OnContactInteractionListClickListener listener, DisplayMetrics metrics){
        Listener = listener;
        Metrics = metrics;
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        private ResItemContactPastBinding Binding;

        DetailViewHolder(ResItemContactPastBinding binding) {
            super(binding.getRoot());
            Binding = binding;
        }

        void setViews(MessageObject object){
            Binding.TitleTextView.setText(object.getInteractionName());
            Binding.DateTextView.setText(object.getInteractionDateTimeString());
            Binding.MessageTextView.setText(object.getMessage());
            Binding.FrameImageView.setColorFilter(object.getInteractionBGColor());
            Binding.TypeImageView.setImageDrawable(object.getTypeImage());
            if(!object.getMissingViews().isEmpty()){Binding.ViewedIndicatorImageView.setVisibility(View.VISIBLE);}
            else{Binding.ViewedIndicatorImageView.setVisibility(View.INVISIBLE);}

            Binding.getRoot().setOnClickListener(v -> Listener.onContactInteractionClick(dataSetSorted.get(getAdapterPosition())));

            //set ItemView height to 10% percent of screen height
            ViewGroup.LayoutParams v1Params = Binding.getRoot().getLayoutParams();
            v1Params.height = (int) (Metrics.heightPixels*0.1);
            Binding.getRoot().setLayoutParams(v1Params);
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

            //set ItemView height to 2,5% percent of screen height
            ViewGroup.LayoutParams v2Params = Binding.getRoot().getLayoutParams();
            v2Params.height = (int) (Metrics.heightPixels*0.05);
            Binding.getRoot().setLayoutParams(v2Params);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType){
            case 0:
                View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_contact_past,parent,false);
                ResItemContactPastBinding Binding1 = ResItemContactPastBinding.bind(v1);
                viewHolder = new DetailViewHolder(Binding1);
                break;
            case 1:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_divider_date,parent,false);
                ResItemDividerDateBinding Binding2 = ResItemDividerDateBinding.bind(v2);
                viewHolder = new DateDividerViewHolder(Binding2);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MessageObject Interaction = dataSetSorted.get(position);
        if(!Interaction.getDivider()) {
            DetailViewHolder viewHolder = (DetailViewHolder) holder;
            viewHolder.setViews(Interaction);
        }else {
            DateDividerViewHolder viewHolder = (DateDividerViewHolder) holder;
            viewHolder.setDateTextView(Interaction.getInteractionDateTimeString());
        }
    }

    void UpdateData(List<MessageObject> UpdatedData){
        dataSet = new ArrayList<>(UpdatedData);
        dataSetSorted = new ArrayList<>(UpdatedData);
        this.notifyDataSetChanged();
    }

    void FilterData(String searchtext){
        dataSetSorted = new ArrayList<>(dataSet);

        //Filter all strings split by " "
        String[] SearchTextParts = searchtext.split("\\s+");
        for(String searchTextPart : SearchTextParts){
            dataSetSorted.removeIf(object -> {
                boolean Return = true;

                if(!object.getDivider()) {
                    if (object.getInteractionName().toLowerCase().contains(searchTextPart.toLowerCase())) { Return = false; }
                    if (object.getInteractionDateTimeString().contains(searchTextPart.toLowerCase())) { Return = false;}
                    if (object.getMessage().toLowerCase().contains(searchTextPart.toLowerCase())) { Return = false; }
                }else {Return = false;}

                return Return;
            });
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {return dataSetSorted.size();}

    @Override
    public int getItemViewType(int position) {
        return dataSetSorted.get(position).getDivider() ? 1 : 0;
    }

    public interface OnContactInteractionListClickListener{
        void onContactInteractionClick(MessageObject object);
    }

}

