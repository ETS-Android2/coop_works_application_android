package com.example.coopapp20.Administration.Task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.coopapp20.Administration.AdminViewModel;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.FragRecyclerviewBinding;

public class TaskAdminFrag extends Fragment {

    private MainViewModel mainViewModel;
    private AdminViewModel viewModel;
    private FragRecyclerviewBinding Binding;
    private TaskAdminAdapter Adapter;

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
        viewModel.getAllTasks().observe(getViewLifecycleOwner(), tasks -> Adapter.UpdateData(tasks));

        return Binding.getRoot();
    }

    private void CreateToolbar(){
        ToolbarFrag toolbar = new ToolbarFrag();
        toolbar.setAddBtn(v -> mainViewModel.getMainNavController().navigate(R.id.addTaskStaticParameterFrag));
        toolbar.setSettingsBtn(v -> Toast.makeText(getContext(),"Task Prioritization Values",Toast.LENGTH_LONG).show());
        mainViewModel.getCurrentToolbar().setValue(toolbar);
    }

    private void CreateRecyclerView(){
        Adapter = new TaskAdminAdapter((view, task) -> viewModel.TaskItemOptions(view, task, mainViewModel, requireContext()), getResources().getDisplayMetrics());
        Binding.Recyclerview.setAdapter(Adapter);
    }
}
