package com.example.coopapp20.Scedule.SchedulePref;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coopapp20.Data.Objects.SchedulePreferenceObject;
import com.example.coopapp20.Data.Objects.ShiftPreferenceObject;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.FragSchedulePrefShiftsBinding;
import com.example.coopapp20.zOtherFiles.CustomDropdownMenuAdapter;

import java.util.Arrays;
import java.util.List;

public class SchedulePrefShiftFrag extends Fragment {

    private MainViewModel mainViewModel;
    private SchedulePrefViewModel viewModel;
    private FragSchedulePrefShiftsBinding Binding;
    private SchedulePrefShiftAdapter Adapter;
    private List<String> WorkDays = Arrays.asList("1","2","3","4","5","6","7");
    private MutableLiveData<Integer> PrefWorkDays = new MutableLiveData<>(), MaxWorkDays = new MutableLiveData<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = FragSchedulePrefShiftsBinding.inflate(getLayoutInflater());
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(SchedulePrefViewModel.class);
        viewModel.setCurrentUser(mainViewModel.getCurrentUser().getValue());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Adapter = new SchedulePrefShiftAdapter(object -> viewModel.onSchedulePrefShiftClick(object), getResources().getDisplayMetrics());
        Binding.Recyclerview.setAdapter(Adapter);

        CreateDropDowns();
        CreateToolbar();

        viewModel.getShiftPreferences().observe(getViewLifecycleOwner(), s -> Adapter.UpdateData(s));
        viewModel.getSchedulePreferences().observe(getViewLifecycleOwner(), preferences -> {
            if(preferences != null) {
                PrefWorkDays.setValue(preferences.getPrefDays());
                MaxWorkDays.setValue(preferences.getMaxDays());
            }
        });

        Binding.ExecuteBtn.setOnClickListener(v -> viewModel.onSchedulePrefExecuteBtnClick(Integer.parseInt(Binding.PrefDaysDropDown.getText().toString()),Integer.parseInt(Binding.MaxDaysDropDown.getText().toString())));

        return Binding.getRoot();
    }

    private void CreateDropDowns(){
        Binding.PrefDaysDropDown.setAdapter(new CustomDropdownMenuAdapter(requireContext(), R.menu.menu_item_layout, WorkDays));
        Binding.MaxDaysDropDown.setAdapter(new CustomDropdownMenuAdapter(requireContext(), R.menu.menu_item_layout, WorkDays));
        Binding.PrefDaysDropDown.setOnItemClickListener((parent, view, position, id) -> PrefWorkDays.setValue(Integer.parseInt(Binding.PrefDaysDropDown.getText().toString())));
        Binding.MaxDaysDropDown.setOnItemClickListener((parent, view, position, id) -> MaxWorkDays.setValue(Integer.parseInt(Binding.MaxDaysDropDown.getText().toString())));
        Binding.PrefDaysDropDown.setText(WorkDays.get(WorkDays.size()-1));
        Binding.MaxDaysDropDown.setText(WorkDays.get(WorkDays.size()-1));

        PrefWorkDays.observe(getViewLifecycleOwner(), prefWorkDays -> {
            Binding.PrefDaysDropDown.setText(String.valueOf(prefWorkDays));
            Binding.PrefDaysDropDown.dismissDropDown();
            if(viewModel.getSchedulePreferences().getValue() != null){
                if(prefWorkDays.equals(viewModel.getSchedulePreferences().getValue().getPrefDays())){
                    Binding.PrefDaysDropDown.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                }else {
                    Binding.PrefDaysDropDown.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(),R.drawable.sharp_calendar_today_24),null,null,null);
                }
            }
        });

        MaxWorkDays.observe(getViewLifecycleOwner(), maxWorkDays -> {
            Binding.MaxDaysDropDown.setText(String.valueOf(maxWorkDays));
            Binding.MaxDaysDropDown.dismissDropDown();
            if(viewModel.getSchedulePreferences().getValue() != null){
                if(maxWorkDays.equals(viewModel.getSchedulePreferences().getValue().getMaxDays())){
                    Binding.MaxDaysDropDown.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                }else {
                    Binding.MaxDaysDropDown.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(),R.drawable.sharp_calendar_today_24),null,null,null);
                }
            }
        });
    }

    private void CreateToolbar(){
        if(mainViewModel.getCurrentToolbar().getValue() == null || mainViewModel.getCurrentToolbar().getValue().getToolbarId() == null || !mainViewModel.getCurrentToolbar().getValue().getToolbarId().equals("SchedulePrefToolbar")) {
            ToolbarFrag toolbar = new ToolbarFrag();
            toolbar.setToolbarId("SchedulePrefToolbar");
            toolbar.setTabLayout(position -> {
                if (position == 0) {
                    mainViewModel.getMainNavController().popBackStack(R.id.scheduleFrag, false);
                    mainViewModel.getMainNavController().navigate(R.id.schedulePrefShiftFrag);
                } else {
                    mainViewModel.getMainNavController().popBackStack(R.id.scheduleFrag, false);
                    mainViewModel.getMainNavController().navigate(R.id.schedulePrefOffDayRequestFrag);
                }
            }, "vagter", "fridage", 0);
            mainViewModel.getCurrentToolbar().setValue(toolbar);
        }else {mainViewModel.getCurrentToolbar().getValue().setToolbarSelectedTab(0);}
    }

}
