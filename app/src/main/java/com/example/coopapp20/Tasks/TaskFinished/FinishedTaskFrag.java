package com.example.coopapp20.Tasks.TaskFinished;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coopapp20.Data.Objects.FinishedTaskObject;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.Tasks.TaskViewModel;
import com.example.coopapp20.databinding.FragRecyclerviewBinding;

import java.util.Objects;

public class FinishedTaskFrag extends Fragment {

    private MainViewModel mainViewModel;
    private TaskViewModel viewModel;
    private FragRecyclerviewBinding Binding;
    private FinishedTaskAdapter Adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = FragRecyclerviewBinding.inflate(getLayoutInflater());
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(TaskViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainViewModel.getCurrentToolbar().setValue(new ToolbarFrag().setSearchBtnVisible(true));

        Adapter = new FinishedTaskAdapter((object, optionsbtn) -> viewModel.onFinishedTaskClick(optionsbtn,object, getActivity()), getResources().getDisplayMetrics());
        Binding.Recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        Binding.Recyclerview.setAdapter(Adapter);
        Objects.requireNonNull(mainViewModel.getCurrentToolbar().getValue()).SearchText.observe(getViewLifecycleOwner(), s -> Adapter.FilterData(s));
        viewModel.getFinishedTasks().observe(getViewLifecycleOwner(), finishedTaskObjects -> Adapter.UpdateData(finishedTaskObjects));

        return Binding.getRoot();
    }
}
