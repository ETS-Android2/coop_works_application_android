package com.example.coopapp20.Contact.Contact;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.R;
import com.example.coopapp20.databinding.ResItemContactBinding;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<ContactObject> dataSet = new ArrayList<>();
    private List<ContactObject> dataSetSorted = new ArrayList<>();
    private OnCustomClickListener Listener;
    private DisplayMetrics Metrics;

    // Provide a suitable constructor (depends on the kind of dataset)
    ContactAdapter(OnCustomClickListener listener, DisplayMetrics metrics){
        Listener = listener;
        Metrics = metrics;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ContactViewHolder extends RecyclerView.ViewHolder {
        ResItemContactBinding Binding;

        ContactViewHolder(ResItemContactBinding binding) {
            super(binding.getRoot());
            Binding = binding;
        }

        private void setViews(ContactObject object){
            Binding.NameTextView.setText(object.getName());
            Binding.MessageTextView.setText(object.getMessage());
            Binding.DateTextView.setText(object.getMessageDateOrTime());
            Binding.FrameImageView.setColorFilter(object.getDepartmentColor());

            if(object.getUnviewed()){Binding.ViewedIndicatorImageView.setVisibility(View.VISIBLE);}else {Binding.ViewedIndicatorImageView.setVisibility(View.INVISIBLE);}
            if(object.getActive()){Binding.FilterImageView.setAlpha(0.0f);}else {Binding.FilterImageView.setAlpha(0.5f);}
            Binding.getRoot().setOnClickListener(v->Listener.onCustomClick(object));

            ViewGroup.LayoutParams params = Binding.getRoot().getLayoutParams();
            params.height = (int) (Metrics.heightPixels*0.1);
            Binding.getRoot().setLayoutParams(params);
        }
    }

    @NonNull
    @Override
    // Create new views (invoked by the layout manager)
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewRoot = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_contact,parent,false);
        ResItemContactBinding Binding  = ResItemContactBinding.bind(viewRoot);
        return new ContactViewHolder(Binding);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.setViews(dataSetSorted.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSetSorted.size();
    }

    public void UpdateData(List<ContactObject> UpdatedData){
        if(UpdatedData != null) {
            dataSet = new ArrayList<>(UpdatedData);
            FilterData(null);
        }
    }

    public void FilterData(String searchtext){
        dataSetSorted = new ArrayList<>(dataSet);

        if(searchtext != null && !searchtext.equals("")){
            //Filter all strings split by " "
            String[] SearchTextParts = searchtext.split("\\s+");
            for(String searchTextPart : SearchTextParts){
                dataSetSorted.removeIf(ContactObject -> !ContactObject.getName().toLowerCase().contains(searchTextPart.toLowerCase()) && !ContactObject.getDepartmentName().toLowerCase().contains(searchTextPart.toLowerCase()));
            }
        }

        notifyDataSetChanged();
    }

    public interface OnCustomClickListener{
        void onCustomClick(ContactObject contact);
    }
}
