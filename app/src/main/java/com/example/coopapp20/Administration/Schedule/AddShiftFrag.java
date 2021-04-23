package com.example.coopapp20.Administration.Schedule;

import android.R.layout;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.coopapp20.Administration.AdminViewModel;
import com.example.coopapp20.Data.Objects.DepartmentObject;
import com.example.coopapp20.Data.Objects.ShiftObject;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.FragAdminScheduleShiftAddBinding;
import com.example.coopapp20.zOtherFiles.CustomDropdownMenuAdapter;
import com.example.coopapp20.zOtherFiles.TextWatchers;

import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class AddShiftFrag extends Fragment {

    public AddShiftFrag(){}

    private MainViewModel mainViewModel;
    private AdminViewModel viewModel;
    private List<String> DepartmentNames, Days;
    private List<Integer> DepartmentNrs;
    private FragAdminScheduleShiftAddBinding Binding;
    private AlertDialog dialog = null;

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = FragAdminScheduleShiftAddBinding.inflate(getLayoutInflater());
        mainViewModel= ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(AdminViewModel.class);
    }

    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        new TextWatchers().TimeTextWatcher(Binding.TimeFromTextView);
        new TextWatchers().TimeTextWatcher(Binding.TimeToTextView);

        CreateDialog();

        mainViewModel.getCurrentToolbar().setValue(new ToolbarFrag());

        viewModel.getAllDepartments().observe(getViewLifecycleOwner(), this::CreateDropdown);

        Binding.ExecuteBtn.setOnClickListener(v -> dialog.show());

        return Binding.getRoot();
    }

    private void CreateDropdown(List<DepartmentObject> departments){
        //Set Spinner adapters
        if(departments != null) {
            DepartmentNames = viewModel.getAllDepartments().getValue().stream().map(DepartmentObject::getName).collect(Collectors.toList());
            DepartmentNrs = viewModel.getAllDepartments().getValue().stream().map(DepartmentObject::getId).collect(Collectors.toList());
            Days = Arrays.asList(getResources().getStringArray(R.array.day_array));
            Binding.DepartmentDropDown.setAdapter(new CustomDropdownMenuAdapter(requireContext(), layout.simple_spinner_dropdown_item, DepartmentNames));
            Binding.DayDropDown.setAdapter(new CustomDropdownMenuAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, Days));
            setValues();
        }
    }

    private void CreateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setPositiveButton(android.R.string.yes, ((dialog, which) -> Execute()));
        builder.setNegativeButton(android.R.string.no, null);
        dialog = builder.create();
    }

    private void Execute(){
        Integer DepartmentId    = DepartmentNames.contains(Binding.DepartmentDropDown.getText().toString()) ? DepartmentNrs.get(DepartmentNames.indexOf(Binding.DepartmentDropDown.getText().toString())) : null;
        Integer DayNr           = Days.contains(Binding.DayDropDown.getText().toString()) ? Days.indexOf(Binding.DayDropDown.getText().toString()) : null;
        String StartTimeString   = Binding.TimeFromTextView.getText().toString();
        String EndTimeString     = Binding.TimeToTextView.getText().toString();

        if(VerifyShiftInput(DepartmentId, DayNr, StartTimeString, EndTimeString)) {
            viewModel.ExecuteShift(DepartmentId, DayNr, StartTimeString, EndTimeString);

            viewModel.getSelectedShift().setValue(null);
            if (!Binding.StaySwitch.isChecked()) {
                mainViewModel.getMainNavController().popBackStack(R.id.scheduleAdminShiftsFrag, true);
                mainViewModel.getMainNavController().navigate(R.id.scheduleAdminShiftsFrag);
            } else { Toast.makeText(getContext(), "opgave oprettet", Toast.LENGTH_SHORT).show(); }
        }
    }

    private boolean VerifyShiftInput(Integer departmentNr, Integer dayNr, String timeFromString, String timeToString){
        boolean Accepted = true;

        if(departmentNr == null){
            Accepted = false;
            Toast.makeText(getContext(),"Afdelingen eksistere ikke",Toast.LENGTH_LONG).show(); }
        if(dayNr == null){
            Accepted = false;
            Toast.makeText(getContext(),"dag eksistere ikke",Toast.LENGTH_LONG).show(); }
        if(!timeFromString.matches("\\d\\d:\\d\\d") || !timeToString.matches("\\d\\d:\\d\\d")){
            Accepted = false;
            Toast.makeText(getContext(),"angiv tid på formen xx:xx",Toast.LENGTH_LONG).show(); }
        else if(LocalTime.parse(timeFromString).plusMinutes(60).isAfter(LocalTime.parse(timeToString))){
            Accepted = false;
            Toast.makeText(getContext(),"minimum 60 minutters vagt",Toast.LENGTH_LONG).show(); }

        return Accepted;
    }

    private void setValues(){
        SharedPreferences SharedPref = requireContext().getSharedPreferences(getString(R.string.BasicPreferences), Context.MODE_PRIVATE);
        ShiftObject shift = viewModel.getSelectedShift().getValue();

        Binding.DepartmentDropDown.setText(shift != null && DepartmentNrs.contains(shift.getDepartmentId())? DepartmentNames.get(DepartmentNrs.indexOf(shift.getDepartmentId())) : DepartmentNames.contains(SharedPref.getString("AddShiftDepartment", "")) ? SharedPref.getString("AddShiftDepartment", "") : !DepartmentNames.isEmpty() ? DepartmentNames.get(0) : "");
        Binding.DayDropDown.setText(shift != null && Days.size() >= shift.getDay() ? Days.get(shift.getDay()) : Days.contains(SharedPref.getString("AddShiftDay", "")) ? SharedPref.getString("AddShiftDay", "") : !Days.isEmpty() ? Days.get(0) : "");
        Binding.TimeFromTextView.setText(shift != null ? shift.getStartTime().format(timeFormatter) : SharedPref.getString("AddShiftFromTime",""));
        Binding.TimeToTextView.setText(shift != null ? shift.getEndTime().format(timeFormatter) : SharedPref.getString("AddShiftToTime",""));
        Binding.ExecuteBtn.setText(shift != null ? getString(R.string.add_change) : getString(R.string.add));
        dialog.setTitle(shift != null ? getString(R.string.do_you_want_to_add_the_task) : getString(R.string.do_you_want_to_change_the_task));
        Binding.AddShiftDepartmentInputLayout.setEnabled(shift == null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(viewModel.getSelectedShift().getValue() == null) {
            SharedPreferences SharedPref = requireContext().getSharedPreferences(getString(R.string.BasicPreferences), Context.MODE_PRIVATE);
            SharedPref.edit().putString("AddShiftDepartment", Binding.DepartmentDropDown.getText().toString()).apply();
            SharedPref.edit().putString("AddShiftDay", Binding.DayDropDown.getText().toString()).apply();
            SharedPref.edit().putString("AddShiftFromTime", Binding.TimeFromTextView.getText().toString()).apply();
            SharedPref.edit().putString("AddShiftToTime", Binding.TimeToTextView.getText().toString()).apply();
        }else {viewModel.getSelectedShift().setValue(null);}
    }
}