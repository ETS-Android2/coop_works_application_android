package com.example.coopapp20.Tasks.TaskDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;
import com.example.coopapp20.Tasks.TaskViewModel;
import com.example.coopapp20.databinding.FragTaskDetailBinding;

import org.jetbrains.annotations.NotNull;

public class TaskDetailFrag extends Fragment {

    private MainViewModel mainViewModel;
    private TaskViewModel viewModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragTaskDetailBinding Binding = FragTaskDetailBinding.inflate(inflater);

        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(TaskViewModel.class);

        viewModel.getSelectedTask().observe(getViewLifecycleOwner(), taskObject -> {
            Binding.TaskDetailTitleTextview.setText(taskObject.getTitle());
            Binding.TaskDetailDaysTextview.setText("Dage: " +taskObject.getDaysLeft()+ "/" + taskObject.getDays());
            Binding.TaskDetailTimeTextview.setText(taskObject.getTimeString());
            Binding.TaskDetailActivityDescriptionTextview.setText(taskObject.getDescription());
            Binding.TaskDetailHeaderLinearLayout.setBackgroundColor(taskObject.getColor());
        });

        CreateToolbar();

        return Binding.getRoot();
    }

    private void CreateToolbar(){
        ToolbarFrag toolbar = new ToolbarFrag();
        if(viewModel.getSelectedTask().getValue() != null && !viewModel.getSelectedTask().getValue().getRepeat()) {

            //Only show button if user is authorized

            toolbar.setSettingsBtn(v -> {
                viewModel.getEditableTask().setValue(viewModel.getSelectedTask().getValue());
                mainViewModel.getMainNavController().navigate(R.id.addTaskSimpleFrag);
            });
        }
        mainViewModel.getCurrentToolbar().setValue(toolbar);
    }
}
