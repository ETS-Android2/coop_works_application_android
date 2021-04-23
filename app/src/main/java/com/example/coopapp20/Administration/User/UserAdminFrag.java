package com.example.coopapp20.Administration.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.coopapp20.Administration.AdminViewModel;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.FragRecyclerviewBinding;

public class UserAdminFrag extends Fragment {

    private MainViewModel mainViewModel;
    private AdminViewModel viewModel;
    private FragRecyclerviewBinding Binding;
    private UserAdminAdapter Adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Binding = FragRecyclerviewBinding.inflate(getLayoutInflater());
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(AdminViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        CreateToolbar();
        CreateRecyclerView();
        viewModel.getAllUsers().observe(getViewLifecycleOwner(), userObjects -> Adapter.UpdateData(userObjects));

        return Binding.getRoot();
    }

    private void CreateToolbar(){
        ToolbarFrag toolbar = new ToolbarFrag();
        toolbar.setAddBtn(v -> mainViewModel.getMainNavController().navigate(R.id.addContactFrag));
        toolbar.setSearchBtnVisible(true);
        mainViewModel.getCurrentToolbar().setValue(toolbar);
    }

    private void CreateRecyclerView(){
        Adapter = new UserAdminAdapter((view, user) -> viewModel.UserItemOptions(view,user,mainViewModel, requireContext()),requireContext().getResources().getDisplayMetrics());
        Binding.Recyclerview.setAdapter(Adapter);
    }
}
