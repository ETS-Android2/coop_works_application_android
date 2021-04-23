package com.example.coopapp20.Administration.Schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coopapp20.Administration.AdminViewModel;
import com.example.coopapp20.Data.Objects.ShiftObject;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.FragRecyclerviewBinding;

public class ScheduleAdminShiftsFrag extends Fragment{

    private MainViewModel mainViewModel;
    private AdminViewModel viewModel;
    private ShiftAdapter Adapter;
    private FragRecyclerviewBinding Binding;

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

        //Observe ViewModel values
        viewModel.getAllShifts().observe(getViewLifecycleOwner(), shiftObjects -> Adapter.UpdateData(shiftObjects));

        //Set Recyclerview
        Binding.Recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        Adapter = new ShiftAdapter((ShiftObject shift, View optionsbtn) -> viewModel.ShiftItemOptions(optionsbtn,shift,mainViewModel,requireContext()), getResources().getDisplayMetrics());
        Binding.Recyclerview.setAdapter(Adapter);

        //set onClickListeners
        Binding.AddBtn.setVisibility(View.VISIBLE);
        Binding.AddBtn.setOnClickListener(v -> mainViewModel.getMainNavController().navigate(R.id.addShiftFrag));

        return Binding.getRoot();
    }

    private void CreateToolbar(){
        if(mainViewModel.getCurrentToolbar().getValue() == null || mainViewModel.getCurrentToolbar().getValue().getToolbarId() == null || !mainViewModel.getCurrentToolbar().getValue().getToolbarId().equals("ScheduleAdminToolbar")) {
            ToolbarFrag toolbar = new ToolbarFrag();
            toolbar.setToolbarId("ScheduleAdminToolbar");
            toolbar.setTabLayout(position -> {
                if (position == 0) {
                    mainViewModel.getMainNavController().popBackStack(R.id.scheduleAdminParamFrag,true);
                    mainViewModel.getMainNavController().navigate(R.id.scheduleAdminShiftsFrag);
                } else {
                    mainViewModel.getMainNavController().popBackStack(R.id.scheduleAdminShiftsFrag,true);
                    mainViewModel.getMainNavController().navigate(R.id.scheduleAdminParamFrag);
                }
            }, getString(R.string.schedule_pref_shifts), "parametre", 0);
            mainViewModel.getCurrentToolbar().setValue(toolbar);
        }else {mainViewModel.getCurrentToolbar().getValue().setToolbarSelectedTab(0);}
    }
}
