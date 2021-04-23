package com.example.coopapp20.Tasks.Task;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coopapp20.Data.Objects.TaskObject;
import com.example.coopapp20.Data.Objects.UserObject;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;
import com.example.coopapp20.Tasks.TaskViewModel;
import com.example.coopapp20.databinding.DialogMultilineTextfieldBinding;
import com.example.coopapp20.databinding.DialogTaskAccelerationBinding;
import com.example.coopapp20.databinding.FragRecyclerviewBinding;
import com.example.coopapp20.zOtherFiles.AdapterSwipeHelper;
import com.example.coopapp20.zOtherFiles.CustomDropdownMenuAdapter;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class TaskFrag extends Fragment {


    private TaskAdapter Adapter;
    private MainViewModel mainViewModel;
    private TaskViewModel viewModel;
    private FragRecyclerviewBinding Binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Binding = FragRecyclerviewBinding.inflate(getLayoutInflater());
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(TaskViewModel.class);
        viewModel.InitiateViewModel(mainViewModel.getCurrentUser());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CreateToolbar();
        CreateRecyclerView();
        viewModel.getTasks().observe(getViewLifecycleOwner(), taskObjects -> Adapter.UpdateData(taskObjects));
        return Binding.getRoot();
    }

    private void CreateToolbar(){
        ToolbarFrag toolbar = new ToolbarFrag();
        toolbar.setAddBtn(v -> mainViewModel.getMainNavController().navigate(R.id.addTaskSimpleFrag));
        toolbar.setSearchBtnVisible(true);
        toolbar.setListBtn(v -> mainViewModel.getMainNavController().navigate(R.id.finishedTaskFrag));
        toolbar.SearchText.observe(getViewLifecycleOwner(), s -> Adapter.FilterData(s));
        mainViewModel.getCurrentToolbar().setValue(toolbar);
    }

    private void CreateRecyclerView() {
        Adapter = new TaskAdapter(object -> {
            viewModel.getSelectedTask().setValue(object);
            mainViewModel.getMainNavController().navigate(R.id.taskDetailFrag);
        }, requireContext().getResources().getDisplayMetrics());
        Binding.Recyclerview.setAdapter(Adapter);

        Drawable LeftIcon = ContextCompat.getDrawable(requireContext(), R.drawable.sharp_check_circle_outline_24);
        Drawable LeftBackground = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_rectangle);
        LeftBackground.mutate();
        LeftIcon.setTint(ContextCompat.getColor(requireContext(), R.color.White));
        LeftBackground.setTint(ContextCompat.getColor(requireContext(), R.color.SlideBackGround));

        Drawable RightIcon = ContextCompat.getDrawable(requireContext(), R.drawable.sharp_access_time_24);
        Drawable RightBackground = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_rectangle);
        RightBackground.mutate();
        RightIcon.setTint(ContextCompat.getColor(requireContext(), R.color.White));
        RightBackground.setTint(ContextCompat.getColor(requireContext(), R.color.LightGray));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new AdapterSwipeHelper(LeftSwipeListener(), LeftIcon, LeftBackground,RightSwipeListener(),RightIcon,RightBackground, getContext().getResources().getDisplayMetrics()));
        itemTouchHelper.attachToRecyclerView(Binding.Recyclerview);
    }

    public AdapterSwipeHelper.onSwipeListener LeftSwipeListener(){
        return position -> {
            //Remove Task ad Position and update Adapter
            TaskObject taskObject = Adapter.getData().get(position);
            Adapter.AddExemptedData(taskObject.getId());

            //Make snackbar to undo update
            Snackbar snackbar = Snackbar.make(Binding.getRoot(), "", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", v -> {});

            //if snackbar is pressed, update won't be done
            snackbar.addCallback(new Snackbar.Callback() {

                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {viewModel.CheckTask(taskObject); }
                    Adapter.RemoveExemptedData(taskObject.getId());
                }
            });

            snackbar.show();
        };
    }

    public AdapterSwipeHelper.onSwipeListener RightSwipeListener(){
        return position -> {
            TaskObject task = Adapter.getData().get(position);
            List<String> days = IntStream.rangeClosed(0,task.getDaysLeft()).boxed().map(Object::toString).collect(Collectors.toList());

            Adapter.notifyItemChanged(position);

            if(days.size() > 1) {
                Dialog dialog = new Dialog(requireContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogTaskAccelerationBinding DialogBinding = DialogTaskAccelerationBinding.inflate(LayoutInflater.from(getContext()));
                dialog.setContentView(DialogBinding.getRoot());
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                CustomDropdownMenuAdapter DropDownAdapter = new CustomDropdownMenuAdapter(requireContext(), R.menu.menu_item_layout, days);
                DialogBinding.DropDown.setAdapter(DropDownAdapter);
                DialogBinding.DropDown.setText(days.get(days.size() - 1));

                DialogBinding.ExecuteBtn.setOnClickListener(v -> {
                    dialog.cancel();
                    task.setLastCompletionDate(LocalDate.now().minusDays(task.getDays() - Integer.parseInt(DialogBinding.DropDown.getText().toString())));
                    viewModel.onTaskAccelerationClick(task);
                });

                dialog.show();
            }else {
                Toast.makeText(requireContext(), "opgave kan ikke fremskyndes yderligere",Toast.LENGTH_LONG).show();
            }
        };
    }
}
