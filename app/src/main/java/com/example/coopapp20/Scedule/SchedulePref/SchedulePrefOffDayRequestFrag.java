package com.example.coopapp20.Scedule.SchedulePref;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coopapp20.Data.Objects.OffDayRequestObject;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.FragSchedulePrefOffdayrequestsBinding;

import java.util.Objects;

public class SchedulePrefOffDayRequestFrag extends Fragment{

    private MainViewModel mainViewModel;
    private SchedulePrefViewModel viewModel;
    private FragSchedulePrefOffdayrequestsBinding Binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = FragSchedulePrefOffdayrequestsBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(requireActivity()).get(SchedulePrefViewModel.class);
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SchedulePrefOffDayRequestAdapter adapter = new SchedulePrefOffDayRequestAdapter((object, OptionsBtn) ->   viewModel.onSchedulePrefOffDayRequestClick(object, OptionsBtn, getParentFragmentManager(), getContext()), getResources().getDisplayMetrics());
        Binding.Recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        Binding.Recyclerview.setAdapter(adapter);

        viewModel.getOffDayRequests().observe(getViewLifecycleOwner(), adapter::UpdateData);

        Binding.TabLayout.addTab(Binding.TabLayout.newTab().setText(getString(R.string.schedule_pref_vacation)));
        Binding.TabLayout.addTab(Binding.TabLayout.newTab().setText(getString(R.string.schedule_pref_offdays)));
        Binding.AddBtn.setOnClickListener(v -> viewModel.openDatePicker(null,Binding.TabLayout.getSelectedTabPosition(),getParentFragmentManager(), requireContext()));

        CreateToolbar();

        return Binding.getRoot();
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
            }, "vagter", "fridage", 1);
            mainViewModel.getCurrentToolbar().setValue(toolbar);
        }else {mainViewModel.getCurrentToolbar().getValue().setToolbarSelectedTab(1);}
    }
}
