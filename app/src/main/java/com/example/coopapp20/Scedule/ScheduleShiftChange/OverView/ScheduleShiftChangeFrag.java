package com.example.coopapp20.Scedule.ScheduleShiftChange.OverView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;
import com.example.coopapp20.Scedule.ScheduleShiftChange.ShiftChangeViewModel;
import com.example.coopapp20.databinding.FragRecyclerviewBinding;

import java.util.Objects;

public class ScheduleShiftChangeFrag extends Fragment {

    private MainViewModel mainViewModel;
    private ShiftChangeViewModel viewModel;
    private FragRecyclerviewBinding Binding;
    private ScheduleShiftChangeAdapter Adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = FragRecyclerviewBinding.inflate(getLayoutInflater());
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(ShiftChangeViewModel.class);
        viewModel.setCurrentUser(mainViewModel.getCurrentUser());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ToolbarFrag toolbar = new ToolbarFrag().setSearchBtnVisible(true);

        mainViewModel.getCurrentToolbar().setValue(toolbar);

        Adapter = new ScheduleShiftChangeAdapter(object -> { viewModel.setSelectedShiftChangeRequestId(object.getRequestId());mainViewModel.getMainNavController().navigate(R.id.scheduleShiftChangeDetailFrag); },getResources().getDisplayMetrics());
        Binding.Recyclerview.setAdapter(Adapter);
        toolbar.SearchText.observe(getViewLifecycleOwner(), s -> Adapter.FilterData(s));

        viewModel.getShiftChangeRequests().observe(getViewLifecycleOwner(), o -> Adapter.UpdateData(o));

        return Binding.getRoot();
    }
}
