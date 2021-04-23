package com.example.coopapp20.Administration.Schedule;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.coopapp20.Administration.AdminViewModel;
import com.example.coopapp20.Data.Objects.DepartmentObject;
import com.example.coopapp20.Data.Objects.ScheduleValueObject;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.FragAdminScheduleParamBinding;
import com.example.coopapp20.zOtherFiles.CustomDropdownMenuAdapter;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class ScheduleAdminParamFrag extends Fragment {

    private MainViewModel mainViewModel;
    private AdminViewModel viewModel;
    private FragAdminScheduleParamBinding Binding;

    private List<String> DepartmentNames;
    private List<Integer> DepartmentNrs;
    private MutableLiveData<LocalDate> ScheduleBeginning = new MutableLiveData<>(), ScheduleCreation = new MutableLiveData<>(), SchedulePreference = new MutableLiveData<>();
    private MutableLiveData<Integer> ScheduleDuration = new MutableLiveData<>();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy", Locale.ENGLISH);
    private boolean PickerOpen = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = FragAdminScheduleParamBinding.inflate(getLayoutInflater());
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(AdminViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewModel.getSelectedScheduleValues().observe(getViewLifecycleOwner(), values -> {
             if(values != null) {
                 Binding.DurationDropDown.setText(String.valueOf(values.getScheduleWeekDuration()));
                 ScheduleDuration.setValue(values.getScheduleWeekDuration());
                 SchedulePreference.setValue(values.getPreferenceDeadline());
                 ScheduleCreation.setValue(values.getCreationDeadline());
                 ScheduleBeginning.setValue(values.getScheduleBeginning());
            }
        });

        viewModel.getAllDepartments().observe(getViewLifecycleOwner(), this::CreateDropDown);
        SchedulePreference.observe(getViewLifecycleOwner(), localDate -> setDates(0));
        ScheduleCreation.observe(getViewLifecycleOwner(), localDate -> setDates(1));
        ScheduleBeginning.observe(getViewLifecycleOwner(), localDate -> setDates(2));

        CreateToolbar();

        Binding.SchedulePreferenceTextView.setOnClickListener(v -> DatePicker(0, getParentFragmentManager(), SchedulePreference));
        Binding.ScheduleCreationTextView.setOnClickListener(v -> DatePicker(1, getParentFragmentManager(), ScheduleCreation));
        Binding.ScheduleBeginningTextView.setOnClickListener(v -> DatePicker(2,getChildFragmentManager(),ScheduleBeginning));

        Binding.ScheduleBeginningInputLayout.setEnabled(viewModel.getSelectedScheduleValues().getValue() == null || !viewModel.getSelectedScheduleValues().getValue().getScheduleOngoing());

        Binding.ExecuteBtn.setOnClickListener(v -> CreateDialog());
        Binding.ResetBtn.setOnClickListener(v -> Reset());

        return Binding.getRoot();
    }

    private void CreateDropDown(List<DepartmentObject> departments){
        List<String> scheduleDurationNrs = Arrays.asList("1","2","3","4","5","6","7","8","9","10");
        DepartmentNames = departments.stream().map(DepartmentObject::getName).collect(Collectors.toList());
        DepartmentNrs = departments.stream().map(DepartmentObject::getId).collect(Collectors.toList());

        Binding.DepartmentDropDown.setAdapter(new CustomDropdownMenuAdapter(requireContext(), R.menu.menu_item_layout, DepartmentNames));
        Binding.DurationDropDown.setAdapter(new CustomDropdownMenuAdapter(requireContext(), R.menu.menu_item_layout, scheduleDurationNrs));
        Binding.DepartmentDropDown.setOnItemClickListener((parent, view, position, id) -> viewModel.getSelectedScheduleValuesDepartmentId().setValue(DepartmentNrs.get(DepartmentNames.indexOf(Binding.DepartmentDropDown.getText().toString()))));
        Binding.DurationDropDown.setOnItemClickListener((parent, view, position, id) -> ScheduleDuration.setValue(Binding.DurationDropDown.getText().toString().matches("-?\\d+") ? Integer.parseInt(Binding.DurationDropDown.getText().toString()) : ScheduleDuration.getValue()));

        ScheduleDuration.observe(getViewLifecycleOwner(), duration -> {
            Binding.DurationDropDown.setText(String.valueOf(duration));
            Binding.DurationDropDown.dismissDropDown();
            if(viewModel.getSelectedScheduleValues().getValue() != null){
                if(duration.equals(viewModel.getSelectedScheduleValues().getValue().getScheduleWeekDuration())){
                    Binding.DurationDropDown.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                }else {
                    Binding.DurationDropDown.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(),R.drawable.sharp_cloud_upload_24),null,null,null);
                }
            }
        });

        setValues();
    }

    private void CreateDialog(){
        if(ScheduleDuration.getValue() != null && SchedulePreference.getValue() != null && ScheduleCreation.getValue() != null && ScheduleBeginning.getValue() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setPositiveButton(android.R.string.yes, ((dialog, which) -> Execute()));
            builder.setNegativeButton(android.R.string.no, null);
            AlertDialog dialog1 = builder.create();
            dialog1.setTitle("VIL DU ÆNDRE SKEMA PARAMETRENE");

            String message = "";
            if (viewModel.getSelectedScheduleValues().getValue() != null) {
                ScheduleValueObject CurrentValues = viewModel.getSelectedScheduleValues().getValue();

                if (ScheduleDuration.getValue() != null && (CurrentValues.getScheduleWeekDuration() == null || !ScheduleDuration.getValue().equals(CurrentValues.getScheduleWeekDuration()))) {
                    message += "skemalængde - før: " + CurrentValues.getScheduleWeekDuration() + "   " + "nu: " + ScheduleDuration.getValue() + "\n";
                }

                if (SchedulePreference.getValue() != null && (CurrentValues.getPreferenceDeadline() == null || !SchedulePreference.getValue().equals(CurrentValues.getPreferenceDeadline()))) {
                    message += "præf. deadline - før: " + (CurrentValues.getPreferenceDeadline() != null ? CurrentValues.getPreferenceDeadline().format(dateFormatter) : "") + "   " + "nu: " + SchedulePreference.getValue().format(dateFormatter) + "\n";
                }

                if (ScheduleCreation.getValue() != null && (CurrentValues.getCreationDeadline() == null || !ScheduleCreation.getValue().equals(CurrentValues.getCreationDeadline()))) {
                    message += "skema deadline - før: " + (CurrentValues.getCreationDeadline() != null ? CurrentValues.getCreationDeadline().format(dateFormatter) : "") + "   " + "nu: " + ScheduleCreation.getValue().format(dateFormatter) + "\n";
                }

                if (ScheduleBeginning.getValue() != null && (CurrentValues.getScheduleBeginning() == null || !ScheduleBeginning.getValue().equals(CurrentValues.getScheduleBeginning()))) {
                    message += "skema deadline - før: " + (CurrentValues.getScheduleBeginning() != null ? CurrentValues.getScheduleBeginning().format(dateFormatter) : "") + "   " + "nu: " + ScheduleBeginning.getValue().format(dateFormatter) + "\n";
                }
            }

            dialog1.setMessage(message);

            dialog1.show();
        }
    }

    private void CreateToolbar(){
        if(mainViewModel.getCurrentToolbar().getValue() == null || mainViewModel.getCurrentToolbar().getValue().getToolbarId() == null || !mainViewModel.getCurrentToolbar().getValue().getToolbarId().equals("ScheduleAdminToolbar")) {
            ToolbarFrag toolbar = new ToolbarFrag();
            toolbar.setToolbarId("ScheduleAdminToolbar");
            toolbar.setTabLayout(position -> {
                if (position == 0) {
                    mainViewModel.getMainNavController().navigate(R.id.scheduleAdminShiftsFrag);
                } else {
                    mainViewModel.getMainNavController().navigate(R.id.scheduleAdminParamFrag);
                }
            }, getString(R.string.schedule_pref_shifts), "parametre", 1);
            mainViewModel.getCurrentToolbar().setValue(toolbar);
        }else {mainViewModel.getCurrentToolbar().getValue().setToolbarSelectedTab(1);}
    }

    private void Execute(){
        if(VerifyDateInput()){
            viewModel.ExecuteScheduleValues(Integer.parseInt(Binding.DurationDropDown.getText().toString()), SchedulePreference.getValue(), ScheduleCreation.getValue(), ScheduleBeginning.getValue());
        }
    }

    private void Reset(){
        if(viewModel.getSelectedScheduleValues().getValue() != null){
            ScheduleDuration.setValue(viewModel.getSelectedScheduleValues().getValue().getScheduleWeekDuration());
            SchedulePreference.setValue(viewModel.getSelectedScheduleValues().getValue().getPreferenceDeadline());
            ScheduleCreation.setValue(viewModel.getSelectedScheduleValues().getValue().getCreationDeadline());
            ScheduleBeginning.setValue(viewModel.getSelectedScheduleValues().getValue().getScheduleBeginning());
        }
    }

    private boolean VerifyDateInput(){
        boolean Accepted = true;

        if(SchedulePreference == null || ScheduleCreation == null || ScheduleBeginning == null){
            Accepted = false;
            Toast.makeText(getContext(),"udfyld alle datoer",Toast.LENGTH_LONG).show(); }

        if(!DepartmentNames.contains(Binding.DepartmentDropDown.getText().toString())){
            Accepted = false;
            Toast.makeText(getContext(),"vælg afdeling",Toast.LENGTH_LONG).show(); }

        if(!Binding.DurationDropDown.getText().toString().matches("\\d")){
            Accepted = false;
            Toast.makeText(getContext(),"vælg skema længde",Toast.LENGTH_LONG).show(); }

        return Accepted;
    }

    private void setDates(int i){
        if(i == 0){
            if(SchedulePreference.getValue() != null && viewModel.getSelectedScheduleValues().getValue() != null){
                Binding.SchedulePreferenceTextView.setText(SchedulePreference.getValue().format(dateFormatter));

                if(ScheduleCreation.getValue() != null && ScheduleCreation.getValue().isBefore(SchedulePreference.getValue().plusDays(3))){
                    ScheduleCreation.setValue(SchedulePreference.getValue().plusDays(3));
                }

                if(!SchedulePreference.getValue().equals(viewModel.getSelectedScheduleValues().getValue().getPreferenceDeadline())){
                    Binding.SchedulePreferenceTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(),R.drawable.sharp_cloud_upload_24),null,null,null);
                }else {
                    Binding.SchedulePreferenceTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                }
            }
        }else if(i == 1){
            if(ScheduleCreation.getValue() != null && viewModel.getSelectedScheduleValues().getValue() != null){
                Binding.ScheduleCreationTextView.setText(ScheduleCreation.getValue().format(dateFormatter));

                if(SchedulePreference.getValue() != null && SchedulePreference.getValue().isAfter(ScheduleCreation.getValue().minusDays(3))){
                    SchedulePreference.setValue(ScheduleCreation.getValue().minusDays(3));
                }

                if(ScheduleBeginning.getValue() != null && ScheduleBeginning.getValue().isBefore(ScheduleCreation.getValue().plusDays(3))){
                    ScheduleBeginning.setValue(ScheduleCreation.getValue().plusDays(3));
                }

                if(!ScheduleCreation.getValue().equals(viewModel.getSelectedScheduleValues().getValue().getCreationDeadline())){
                    Binding.ScheduleCreationTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(),R.drawable.sharp_cloud_upload_24),null,null,null);
                }else {
                    Binding.ScheduleCreationTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                }
            }
        }else if(i == 2){
            if(ScheduleBeginning.getValue() != null && viewModel.getSelectedScheduleValues().getValue() != null){
                Binding.ScheduleBeginningTextView.setText(ScheduleBeginning.getValue().format(dateFormatter));

                if(ScheduleCreation.getValue() != null && ScheduleCreation.getValue().isAfter(ScheduleBeginning.getValue().minusDays(3))){
                    ScheduleCreation.setValue(ScheduleBeginning.getValue().minusDays(3));
                }

                Log.e("ScheduleAdminParamFrag","ScheduleBeginning - SET");
                if(!ScheduleBeginning.getValue().equals(viewModel.getSelectedScheduleValues().getValue().getScheduleBeginning())){
                    Binding.ScheduleBeginningTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(requireContext(),R.drawable.sharp_cloud_upload_24),null,null,null);
                }else {
                    Binding.ScheduleBeginningTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                }
            }
        }
    }

    void setValues(){
        SharedPreferences SharedPref = requireContext().getSharedPreferences(getString(R.string.BasicPreferences), Context.MODE_PRIVATE);
        Binding.DepartmentDropDown.setText(DepartmentNames.contains(SharedPref.getString("ScheduleAdministrationDepartmentName", "")) ? SharedPref.getString("ScheduleAdministrationDepartmentName", "") : !DepartmentNames.isEmpty() ? DepartmentNames.get(0) : "");
        viewModel.getSelectedScheduleValuesDepartmentId().setValue(DepartmentNames.contains(Binding.DepartmentDropDown.getText().toString()) ? DepartmentNrs.get(DepartmentNames.indexOf(Binding.DepartmentDropDown.getText().toString())) : null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences SharedPref = requireContext().getSharedPreferences(getString(R.string.BasicPreferences), Context.MODE_PRIVATE);
        SharedPref.edit().putString("ScheduleAdministrationDepartmentName", Binding.DepartmentDropDown.getText().toString()).apply();
    }

    public void DatePicker(int i, FragmentManager fm, MutableLiveData<LocalDate> DateDestination){
        if(!PickerOpen && viewModel.getSelectedScheduleValues().getValue() != null) {
            PickerOpen = true;
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();

            CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
            constraintsBuilder.setStart(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
            constraintsBuilder.setEnd(LocalDate.now().plusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());

            if (i == 0) {
                // TEST
                if(viewModel.getSelectedScheduleValues().getValue().getScheduleOngoing() && ScheduleBeginning.getValue() != null) {
                    constraintsBuilder.setValidator(DateValidatorPointBackward.before(ScheduleBeginning.getValue().minusDays(6).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()));
                }else {constraintsBuilder.setValidator(DateValidatorPointForward.from(LocalDate.now().plusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())); }

            } else if (i == 1) {
                constraintsBuilder.setValidator(new CalendarConstraints.DateValidator() {
                    @Override
                    public boolean isValid(long date) {
                        LocalDate Date = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate();
                        if(viewModel.getSelectedScheduleValues().getValue().getScheduleOngoing() && ScheduleBeginning.getValue() != null) {
                            return Date.compareTo(LocalDate.now().plusDays(6)) >= 0 && Date.compareTo(ScheduleBeginning.getValue().minusDays(3)) <= 0;
                        }else {return Date.compareTo(LocalDate.now().plusDays(6)) >= 0;}
                    }

                    @Override
                    public int describeContents() {return 0;}
                    @Override
                    public void writeToParcel(Parcel dest, int flags) {}
                });
            } else if(i == 2){
                constraintsBuilder.setValidator(DateValidatorPointForward.from(LocalDate.now().plusDays(9).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()));
            }

            builder.setTheme(resolveOrThrow(requireContext()));
            builder.setCalendarConstraints(constraintsBuilder.build());

            MaterialDatePicker<Long> picker = builder.build();

            picker.addOnDismissListener(v-> PickerOpen = false);

            picker.addOnPositiveButtonClickListener(v -> {
                if(picker.getSelection() != null) {
                    DateDestination.setValue(Instant.ofEpochMilli(picker.getSelection()).atZone(ZoneId.systemDefault()).toLocalDate());
                }
            });
            picker.show(Objects.requireNonNull(fm), picker.toString());
        }
    }

    private static int resolveOrThrow(Context context) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(R.attr.materialCalendarFullscreenTheme, typedValue, true)) {return typedValue.data;}
        throw new IllegalArgumentException(context.getResources().getResourceName(R.attr.materialCalendarFullscreenTheme));
    }
}
