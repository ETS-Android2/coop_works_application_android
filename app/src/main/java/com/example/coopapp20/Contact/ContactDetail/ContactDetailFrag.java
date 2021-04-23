package com.example.coopapp20.Contact.ContactDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.Contact.Contact.ContactObject;
import com.example.coopapp20.Contact.ContactViewModel;
import com.example.coopapp20.Contact.ContactVoiceChat.VoiceChatViewModel;
import com.example.coopapp20.Data.Objects.DepartmentObject;
import com.example.coopapp20.Data.Objects.UserObject;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.databinding.FragContactDetailBinding;

import org.jetbrains.annotations.NotNull;


public class ContactDetailFrag extends Fragment {

    private MainViewModel mainViewModel;
    private ContactViewModel viewModel;
    private VoiceChatViewModel voiceChatViewModel;
    private FragContactDetailBinding Binding;
    private ContactDetailAdapter Adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding  = FragContactDetailBinding.inflate(getLayoutInflater());

        //get ContactViewModel
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(ContactViewModel.class);
        voiceChatViewModel = ViewModelProviders.of(requireActivity()).get(VoiceChatViewModel.class);
    }

    @Override
    public View onCreateView (@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //set recyclerview
        Binding.ContactDetailRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,true));
        Adapter = new ContactDetailAdapter();
        Binding.ContactDetailRecyclerView.setAdapter(Adapter);

        //ask ContactViewModel to send text on btn click
        Binding.ContactDetailSendTextBtn.setOnClickListener(v -> {
            viewModel.SendText(Binding.ContactDetailMessageEditText.getText().toString());
            Binding.ContactDetailMessageEditText.getText().clear();
        });

        //update recyclerview if data changes
        viewModel.getMessages().observe(getViewLifecycleOwner(), messageObjects -> {
            Adapter.UpdateData(viewModel.getMessages().getValue());
            Binding.ContactDetailRecyclerView.smoothScrollToPosition(0);
        });

        viewModel.getSelectedContact().observe(getViewLifecycleOwner(), this::CreateToolbar);

        return Binding.getRoot();
    }

    private void CreateToolbar(ContactObject contact){
        ToolbarFrag toolbar = new ToolbarFrag();
        if(contact != null) {
            toolbar.setCallBtn(v -> onCallBtnClick());
            toolbar.setTextLayout(contact.getName(),null,null);
        }
        mainViewModel.getCurrentToolbar().setValue(toolbar);
    }

    private void onCallBtnClick(){
        if(viewModel.getSelectedContact().getValue() != null){
            ContactObject contact = viewModel.getSelectedContact().getValue();

            if(contact.getActive() && contact.getDataObject() instanceof UserObject){
                //Call user
                voiceChatViewModel.setCaller((UserObject) contact.getDataObject());

            }else if(contact.getActive() && contact.getDataObject() instanceof DepartmentObject){
                //Call most available user
                //Sort DepartmentUserList by their STATUS
                //if highest status is on break or lower, inform caller before calling

            }else if(!contact.getActive() && contact.getDataObject() instanceof DepartmentObject){
                Toast.makeText(getContext(), "Ingen medarbejdere på arbejde", Toast.LENGTH_SHORT).show();

            } else if(!contact.getActive() && contact.getDataObject() instanceof UserObject){
                new android.app.AlertDialog.Builder(getContext())
                        .setTitle("Medarbejder er ikke på arbejde")
                        .setMessage("ring kun privat hvis det er meget vigtigt")
                        .setPositiveButton("annuller", (dialog, which) -> {})
                        .setNeutralButton("ring privat", ((dialog, which) -> {}))
                        .show();
            }

        }
    }
}
