package com.example.coopapp20.Contact.ContactInteractionList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coopapp20.Contact.ContactViewModel;
import com.example.coopapp20.Data.Objects.MessageObject;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.FragRecyclerviewBinding;

public class ContactInteractionFrag extends Fragment {

    private MainViewModel mainViewModel;
    private ContactViewModel viewModel;
    private FragRecyclerviewBinding Binding;
    private ContactInteractionAdapter Adapter;
    private ToolbarFrag toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bind xml data to fragment
        Binding = FragRecyclerviewBinding.inflate(getLayoutInflater());

        //get ContactViewModel
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(ContactViewModel.class);

        //update recyclerview if data changes
        viewModel.getInteractions().observe(this, contactInteractionObjects -> Adapter.UpdateData(contactInteractionObjects));

        toolbar = new ToolbarFrag().setSearchBtnVisible(true);
        mainViewModel.getCurrentToolbar().setValue(new ToolbarFrag().setSearchBtnVisible(true));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Adapter = new ContactInteractionAdapter(object -> {
            viewModel.ContactInteractionClick(object.getContact());
            mainViewModel.getMainNavController().navigate(R.id.contactDetailFrag);
        },getResources().getDisplayMetrics());
        Binding.Recyclerview.setAdapter(Adapter);
        Binding.TextView.setText(R.string.no_data);
        toolbar.SearchText.observe(getViewLifecycleOwner(), s -> Adapter.FilterData(s));

        return Binding.getRoot();
    }
}
