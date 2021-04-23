package com.example.coopapp20.Contact.ContactDetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.Data.Objects.MessageObject;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.ResItemContactMessageBinding;

import java.util.ArrayList;
import java.util.List;

public class ContactDetailAdapter extends RecyclerView.Adapter<ContactDetailAdapter.ContactDetailViewHolder> {
    private ArrayList<MessageObject> dataset = new ArrayList<>();

    static class ContactDetailViewHolder extends RecyclerView.ViewHolder {
        private ResItemContactMessageBinding Binding;

        private ContactDetailViewHolder(ResItemContactMessageBinding binding) {
            super(binding.getRoot());
            Binding = binding;
        }

        private void setViews(MessageObject object){
            Binding.TitleTextview.setText(object.getMessageName());
            Binding.MessageTextview.setText(object.getMessage());
            Binding.DateTextview.setText(object.getMessageDateTimeString());
            Binding.BackgroundLinearLayout.setBackgroundColor(object.getMessageBGColor());
        }
    }

    @NonNull
    @Override
    public ContactDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_contact_message,parent,false);
        ResItemContactMessageBinding Binding = ResItemContactMessageBinding.bind(listItemView);
        return new ContactDetailViewHolder(Binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactDetailViewHolder holder, int position) {
        holder.setViews(dataset.get(position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void UpdateData(List<MessageObject> UpdatedData){
        dataset = new ArrayList<>(UpdatedData);
        this.notifyDataSetChanged();
    }

}
