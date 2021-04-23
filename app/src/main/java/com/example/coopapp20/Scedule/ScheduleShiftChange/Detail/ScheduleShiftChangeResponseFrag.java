package com.example.coopapp20.Scedule.ScheduleShiftChange.Detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavOptions;
import androidx.navigation.NavOptionsBuilder;

import com.example.coopapp20.Data.Objects.ShiftChangeResponseObject;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;
import com.example.coopapp20.Scedule.ScheduleShiftChange.ShiftChangeViewModel;
import com.example.coopapp20.databinding.FragScheduleShiftchangeResponseBinding;

public class ScheduleShiftChangeResponseFrag extends Fragment {

    private MainViewModel mainViewModel;
    private ShiftChangeViewModel viewModel;
    private FragScheduleShiftchangeResponseBinding Binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(ShiftChangeViewModel.class);
        Binding = FragScheduleShiftchangeResponseBinding.inflate(getLayoutInflater());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainViewModel.getCurrentToolbar().setValue(new ToolbarFrag());

        ShiftChangeResponseAdapter ResponseAdapter = new ShiftChangeResponseAdapter();
        Binding.RecyclerView.setAdapter(ResponseAdapter);

        viewModel.getCurrentShiftChangeResponse().observe(getViewLifecycleOwner(), response -> {
            if(response != null) {
                if(response.getStatus() == ShiftChangeResponseObject.SHIFTCHANGESTATUS_DECLINED){Binding.RadioBtnGroup.check(R.id.DeclineRadioBtn);}
                else if(response.getStatus() == ShiftChangeResponseObject.SHIFTCHANGESTATUS_SWITCH_SUGGESTED){Binding.RadioBtnGroup.check(R.id.SwitchRadioBtn);}
                else if(response.getStatus() == ShiftChangeResponseObject.SHIFTCHANGESTATUS_PENDING){Binding.RadioBtnGroup.check(R.id.PendingRadioBtn);}

                //set execute button
                Binding.ExecuteBtn.setOnClickListener(v -> viewModel.getResponseClickListener().onRepsponseBtnClick(requireContext(), response, Binding.RadioBtnGroup.getCheckedRadioButtonId(), mainViewModel, ResponseAdapter.getSelectedItems()));
            }
        });

        viewModel.getCurrentUserFutureActiveShifts().observe(getViewLifecycleOwner(), ResponseAdapter::UpdateData);

        Binding.RadioBtnGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == Binding.SwitchRadioBtn.getId()){
                Binding.RecyclerView.setVisibility(View.VISIBLE);
            }else {Binding.RecyclerView.setVisibility(View.GONE);}
        });

        Binding.CancelBtn.setOnClickListener(v -> {mainViewModel.getMainNavController().popBackStack(R.id.scheduleShiftChangeDetailFrag, true); mainViewModel.getMainNavController().navigate(R.id.scheduleShiftChangeDetailFrag,null);});

        return Binding.getRoot();
    }
}
