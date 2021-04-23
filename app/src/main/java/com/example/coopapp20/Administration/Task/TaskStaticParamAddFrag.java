package com.example.coopapp20.Administration.Task;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.coopapp20.Administration.AdminViewModel;
import com.example.coopapp20.Data.Objects.DepartmentObject;
import com.example.coopapp20.Data.Objects.TaskObject;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.DialogMultilineTextfieldBinding;
import com.example.coopapp20.databinding.FragAdminTaskStaticAddBinding;
import com.example.coopapp20.zOtherFiles.CustomDropdownMenuAdapter;
import com.example.coopapp20.zOtherFiles.DurationFormat;
import com.example.coopapp20.zOtherFiles.TextWatchers;

import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class TaskStaticParamAddFrag extends Fragment{

    private MainViewModel mainViewModel;
    private AdminViewModel viewModel;
    private FragAdminTaskStaticAddBinding Binding;
    private List<String> DepartmentNames, ExecutionBeforeDeadlineOptions = Arrays.asList("nødvendigt", "eventuelt", "unødvendigt");
    private List<Integer> DepartmentNrs;
    private DateTimeFormatter TimeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
    private AlertDialog dialog = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = FragAdminTaskStaticAddBinding.inflate(getLayoutInflater());
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(AdminViewModel.class);
    }

    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //make sure text input is time parsable
        new TextWatchers().DurationTextWatcher(Binding.TimeTextView);
        new TextWatchers().DurationToFromTextWatcher(Binding.IdealExecutionTimeTextView);

        CreateToolbar();
        CreateDialog();

        viewModel.getAllDepartments().observe(getViewLifecycleOwner(), this::CreateDropDown);

        Binding.ExecuteBtn.setOnClickListener(v -> dialog.show());
        Binding.DescriptionTextView.setOnClickListener(v -> ShowDescriptionDialog());

        return Binding.getRoot();
    }

    private void CreateToolbar(){
        ToolbarFrag toolbar = new ToolbarFrag();
        toolbar.setTabLayout(position -> {
            if(position == 0){mainViewModel.getMainNavController().navigate(R.id.DynamicToStatic);}
            else {mainViewModel.getMainNavController().navigate(R.id.StaticToDynamic);}
        },"statisk","dynamisk", 0);
        mainViewModel.getCurrentToolbar().setValue(toolbar);
    }

    private void CreateDropDown(List<DepartmentObject> departments){
        //Set spinner adapters
        if(departments != null) {
            DepartmentNames = departments.stream().map(DepartmentObject::getName).collect(Collectors.toList());
            DepartmentNrs = departments.stream().map(DepartmentObject::getId).collect(Collectors.toList());
            CustomDropdownMenuAdapter departmentAdapter = new CustomDropdownMenuAdapter(requireContext(), R.menu.menu_item_layout, DepartmentNames);
            CustomDropdownMenuAdapter executionBeforeDeadlineAdapter = new CustomDropdownMenuAdapter(requireContext(), R.menu.menu_item_layout, ExecutionBeforeDeadlineOptions);
            Binding.PrioritizationDropDown.setAdapter(executionBeforeDeadlineAdapter);
            Binding.DepartmentDropDown.setAdapter(departmentAdapter);
            setValues();
        }
    }

    private void CreateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setPositiveButton(android.R.string.yes, ((dialog, which) -> Execute()));
        builder.setNegativeButton(android.R.string.no, null);
        dialog = builder.create();
    }

    private void ShowDescriptionDialog(){
        //create dialog with custom layout
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogMultilineTextfieldBinding DialogBinding = DialogMultilineTextfieldBinding.inflate(LayoutInflater.from(getContext()));
        dialog.setContentView(DialogBinding.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        //reference dialog layout
        DialogBinding.MultilineTextDialogTitle.setText("opgave beskrivelse");
        DialogBinding.MultilineTextDialogTextView.setText(Binding.DescriptionTextView.getText());
        dialog.setOnCancelListener(dialog1 -> Binding.DescriptionTextView.setText(DialogBinding.MultilineTextDialogTextView.getText().toString()));

        dialog.show();
    }

    private void Execute(){
        Integer DepartmentId    = DepartmentNames.contains(Binding.DepartmentDropDown.getText().toString()) ? DepartmentNrs.get(DepartmentNames.indexOf(Binding.DepartmentDropDown.getText().toString())) : null;
        int Prioritization      = ExecutionBeforeDeadlineOptions.indexOf(Binding.PrioritizationDropDown.getText().toString());
        String Title            = Binding.TitleTextView.getText().toString();
        String Time             = Binding.TimeTextView.getText().toString();
        String IdealTime        = Binding.IdealExecutionTimeTextView.getText().toString();
        String Days             = Binding.DaysTextView.getText().toString();
        String Description      = Binding.DescriptionTextView.getText().toString();

        if (VerifyTaskInput(DepartmentId, Title, Time,IdealTime, Days, Description)) {
            viewModel.ExecuteTask(DepartmentId, Prioritization,Title, Time, IdealTime,Days, Description);

            Binding.TitleTextView.getText().clear();
            Binding.DaysTextView.getText().clear();
            Binding.TimeTextView.getText().clear();
            Binding.DescriptionTextView.getText().clear();

            viewModel.getSelectedTask().setValue(null);
            if (!Binding.StaySwitch.isChecked()) {
                mainViewModel.getMainNavController().popBackStack(R.id.taskAdministration, true);
                mainViewModel.getMainNavController().navigate(R.id.taskAdministration);
            } else { Toast.makeText(getContext(), "opgave oprettet", Toast.LENGTH_SHORT).show(); }
        }
    }

    private boolean VerifyTaskInput(Integer departmentId, String title, String time, String IdealTime, String days, String description){
        boolean Accepted = true;

        if(departmentId == null){
            Accepted = false;
            Toast.makeText(getContext(),"Afdelingen eksistere ikke",Toast.LENGTH_LONG).show(); }

        if(title.length() < 10){
            Accepted = false;
            Toast.makeText(getContext(),"angiv beskrivende titel (minimum 10 tegn)",Toast.LENGTH_LONG).show(); }

        if(!time.matches("\\d\\d:\\d\\d")){
            Accepted = false;
            Toast.makeText(getContext(),"angiv tid på formen xx:xx",Toast.LENGTH_LONG).show(); }

        if(IdealTime.equals("") || !IdealTime.matches("\\d\\d:\\d\\d - \\d\\d:\\d\\d")){
            Accepted = false;
            Toast.makeText(getContext(),"angiv ideal tid på formen xx:xx - xx:xx",Toast.LENGTH_LONG).show(); }

        if(days.equals("")){
            Accepted = false;
            Toast.makeText(getContext(),"angiv antal dage mellem udførelse",Toast.LENGTH_LONG).show(); }

        if(description.length() < 50){
            Accepted = false;
            Toast.makeText(getContext(),"angiv beskrivelse (minimum 50 tegn)",Toast.LENGTH_LONG).show(); }

        return Accepted;
    }

    private void setValues(){
        SharedPreferences SharedPref = requireContext().getSharedPreferences(getString(R.string.BasicPreferences), Context.MODE_PRIVATE);
        TaskObject task = viewModel.getSelectedTask().getValue();

        Binding.DepartmentDropDown.setText(task != null && DepartmentNrs.contains(task.getDepartmentId()) ? DepartmentNames.get(DepartmentNrs.indexOf(task.getDepartmentId())) : DepartmentNames.contains(SharedPref.getString("AddTaskAdvancedDepartmentDropDown", "")) ? SharedPref.getString("AddTaskAdvancedDepartmentDropDown", "") : "");
        Binding.PrioritizationDropDown.setText(task != null ? ExecutionBeforeDeadlineOptions.get(task.getPrioritization()) : SharedPref.getString("AddTaskAdvancedExecutionBeforeDeadlineDropDown",""));
        Binding.TitleTextView.setText(task != null ? task.getTitle() : SharedPref.getString("AddTaskAdvancedTitleTextView", ""));
        Binding.DaysTextView.setText(task != null ? String.valueOf(task.getDays()) : SharedPref.getString("AddTaskAdvancedDaysTextView", ""));
        Binding.TimeTextView.setText(task != null ? DurationFormat.DurationToTimeString(task.getDuration()) : SharedPref.getString("AddTaskAdvancedTimeConsumptionTextView", ""));
        Binding.IdealExecutionTimeTextView.setText(task != null ? task.getIdealExecutionTime().format(TimeFormatter) : SharedPref.getString("AddTaskAdvancedIdealExecutionTimeTextView", ""));
        Binding.DescriptionTextView.setText(task != null ? task.getDescription() : SharedPref.getString("AddTaskAdvancedDescriptionTextView", ""));
        Binding.ExecuteBtn.setText(task != null ? R.string.add_change : R.string.add_task);
        dialog.setTitle(task != null ? getString(R.string.do_you_want_to_add_the_task) : getString(R.string.do_you_want_to_change_the_task));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(viewModel.getSelectedTask().getValue() == null) {
            SharedPreferences SharedPref = requireContext().getSharedPreferences(getString(R.string.BasicPreferences), Context.MODE_PRIVATE);
            SharedPref.edit().putString("AddTaskAdvancedDepartmentDropDown", Binding.DepartmentDropDown.getText().toString()).apply();
            SharedPref.edit().putString("AddTaskAdvancedExecutionBeforeDeadlineDropDown", Binding.PrioritizationDropDown.getText().toString()).apply();
            SharedPref.edit().putString("AddTaskAdvancedTitleTextView", Binding.TitleTextView.getText().toString()).apply();
            SharedPref.edit().putString("AddTaskAdvancedDaysTextView", Binding.DaysTextView.getText().toString()).apply();
            SharedPref.edit().putString("AddTaskAdvancedTimeConsumptionTextView", Binding.TimeTextView.getText().toString()).apply();
            SharedPref.edit().putString("AddTaskAdvancedIdealExecutionTimeTextView", Binding.IdealExecutionTimeTextView.getText().toString()).apply();
            SharedPref.edit().putString("AddTaskAdvancedDescriptionTextView", Binding.DescriptionTextView.getText().toString()).apply();
        }else {viewModel.getSelectedTask().setValue(null);}
    }
}