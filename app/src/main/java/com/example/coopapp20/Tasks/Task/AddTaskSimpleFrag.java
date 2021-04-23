package com.example.coopapp20.Tasks.Task;

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

import com.example.coopapp20.Data.Objects.DepartmentObject;
import com.example.coopapp20.Data.Objects.TaskObject;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;
import com.example.coopapp20.Tasks.TaskViewModel;
import com.example.coopapp20.databinding.FragTaskAddBinding;
import com.example.coopapp20.zOtherFiles.CustomDropdownMenuAdapter;
import com.example.coopapp20.zOtherFiles.DurationFormat;
import com.example.coopapp20.zOtherFiles.TextWatchers;

import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class AddTaskSimpleFrag extends Fragment{

    private MainViewModel mainViewModel;
    private TaskViewModel viewModel;
    private FragTaskAddBinding Binding;

    private List<String> DepartmentNames, Prioritizations = Arrays.asList("NU!", "snart", "inden deadline");
    private List<Integer> DepartmentNrs;
    private String DialogTitle = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = FragTaskAddBinding.inflate(getLayoutInflater());
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(TaskViewModel.class);
    }

    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewModel.getAddTaskDescriptionText().observe(getViewLifecycleOwner(), s -> Binding.DescriptionTextView.setText(s));

        //make sure text input is time parsable
        new TextWatchers().TimeTextWatcher(Binding.TimeTextView);

        setMenus();
        setValues();

        if(viewModel.getEditableTask().getValue() != null){mainViewModel.getCurrentToolbar().setValue(new ToolbarFrag().setDeleteBtn(v -> viewModel.onDeleteTaskBtnClick(getActivity())));}
        Binding.ExecuteBtn.setOnClickListener(v -> AddTaskIf());
        Binding.DescriptionTextView.setOnClickListener(v -> viewModel.onDescriptionTextViewClick(Binding.DescriptionTextView.getText().toString(),getContext()));

        return Binding.getRoot();
    }

    private void setMenus(){
        //Set spinners
        DepartmentNames = viewModel.getAllDepartments().getValue().stream().map(DepartmentObject::getName).collect(Collectors.toList());
        DepartmentNrs = viewModel.getAllDepartments().getValue().stream().map(DepartmentObject::getId).collect(Collectors.toList());
        CustomDropdownMenuAdapter departmentAdapter = new CustomDropdownMenuAdapter(requireContext(), R.menu.menu_item_layout, DepartmentNames);
        CustomDropdownMenuAdapter executionBeforeDeadlineAdapter = new CustomDropdownMenuAdapter(requireContext(), R.menu.menu_item_layout, Prioritizations);
        Binding.PrioritizationDropDown.setAdapter(executionBeforeDeadlineAdapter);
        Binding.DepartmentDropDown.setAdapter(departmentAdapter);
    }

    private void AddTaskIf(){
        Integer DepartmentId    = DepartmentNrs.get(DepartmentNames.indexOf(Binding.DepartmentDropDown.getText().toString()));
        Integer Prioritization  = Prioritizations.indexOf(Binding.PrioritizationDropDown.getText().toString());
        String Title            = Binding.TitleTextView.getText().toString();
        String Time             = Binding.TimeTextView.getText().toString();
        int Days                = 0;
        String Description      = Binding.DescriptionTextView.getText().toString();
        if(!Binding.DaysTextView.getText().toString().equals("")){Days = Integer.parseInt(Binding.DaysTextView.getText().toString());}

        if(!Title.equals("")){

            Integer finalDays = Days;
            new AlertDialog.Builder(requireContext())
                    .setTitle(DialogTitle)
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                        viewModel.onAddTaskBtnClick(Title,Time, null, finalDays,Description,DepartmentId,Prioritization, false);

                        Binding.TitleTextView.getText().clear();
                        Binding.DaysTextView.getText().clear();
                        Binding.TimeTextView.getText().clear();
                        Binding.DescriptionTextView.getText().clear();

                        viewModel.getEditableTask().setValue(null);
                        if (!Binding.StaySwitch.isChecked()) {
                            mainViewModel.getMainNavController().popBackStack(R.id.taskFrag,true);
                            mainViewModel.getMainNavController().navigate(R.id.taskFrag);
                        } else { Toast.makeText(requireContext(), "udført", Toast.LENGTH_SHORT).show();}
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();

        }else {Toast.makeText(requireContext(),getString(R.string.task_must_have_title),Toast.LENGTH_SHORT).show();}

    }

    private void setValues(){
        SharedPreferences SharedPref = requireContext().getSharedPreferences(getString(R.string.BasicPreferences), Context.MODE_PRIVATE);
        TaskObject task = viewModel.getEditableTask().getValue();
        Binding.DepartmentDropDown.setText(task != null && DepartmentNrs.contains(task.getDepartmentId()) ? DepartmentNames.get(DepartmentNrs.indexOf(task.getDepartmentId())) : DepartmentNames.contains(SharedPref.getString("AddTaskSimpleDepartmentDropDown","")) ? SharedPref.getString("AddTaskSimpleDepartmentDropDown","") : !DepartmentNames.isEmpty() ? DepartmentNames.get(0) : "");
        Binding.PrioritizationDropDown.setText(task != null ? Prioritizations.get(task.getPrioritization()) : Prioritizations.contains(SharedPref.getString("AddTaskSimpleExecutionBeforeDeadlineDropDown","")) ? SharedPref.getString("AddTaskSimpleExecutionBeforeDeadlineDropDown","") : !Prioritizations.isEmpty() ? Prioritizations.get(0) : "");
        Binding.TitleTextView.setText(task != null ? task.getTitle() : SharedPref.getString("AddTaskSimpleTitleTextView", ""));
        Binding.DaysTextView.setText(task != null ? String.valueOf(task.getDays()) : SharedPref.getString("AddTaskSimpleDaysTextView", ""));
        Binding.TimeTextView.setText(task != null ? DurationFormat.DurationToTimeString(task.getDuration()) : SharedPref.getString("AddTaskSimpleTimeConsumptionTextView", ""));
        Binding.DescriptionTextView.setText(task != null ? task.getDescription() : SharedPref.getString("AddTaskSimpleDescriptionTextView", ""));
        Binding.ExecuteBtn.setText(task != null ? R.string.add_change : R.string.add_task);

        DialogTitle = getString(R.string.do_you_want_to_change_the_task);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(viewModel.getEditableTask().getValue() == null) {
            SharedPreferences SharedPref = requireContext().getSharedPreferences(getString(R.string.BasicPreferences), Context.MODE_PRIVATE);
            SharedPref.edit().putString("AddTaskSimpleDepartmentDropDown", Binding.DepartmentDropDown.getText().toString()).apply();
            SharedPref.edit().putString("AddTaskSimpleExecutionBeforeDeadlineDropDown", Binding.PrioritizationDropDown.getText().toString()).apply(); SharedPref.edit().putString("AddTaskSimpleTitleTextView", Binding.TitleTextView.getText().toString()).apply();
            SharedPref.edit().putString("AddTaskSimpleDaysTextView", Binding.DaysTextView.getText().toString()).apply();
            SharedPref.edit().putString("AddTaskSimpleTimeConsumptionTextView", Binding.TimeTextView.getText().toString()).apply();
            SharedPref.edit().putString("AddTaskSimpleDescriptionTextView", Binding.DescriptionTextView.getText().toString()).apply();
        }else {viewModel.getEditableTask().setValue(null);}
    }
}