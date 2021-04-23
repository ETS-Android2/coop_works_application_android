package com.example.coopapp20.Scedule.Schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.FragScheduleBinding;

import org.jetbrains.annotations.NotNull;


public class ScheduleFrag extends Fragment {

    private MainViewModel mainViewModel;
    private ScheduleViewModel viewModel;
    private ScheduleAdapter Adapter;
    private FragScheduleBinding Binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = FragScheduleBinding.inflate(getLayoutInflater());
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(ScheduleViewModel.class);
        viewModel.setCurrentUser(mainViewModel.getCurrentUser());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        CreateToolbar();

        viewModel.Observe(getViewLifecycleOwner());

        Adapter = new ScheduleAdapter(object -> viewModel.onShiftChangeBtnClick(getActivity(), object), requireContext().getResources().getDisplayMetrics());
        Binding.RecyclerView.setAdapter(Adapter);

        viewModel.getActiveShifts().observe(getViewLifecycleOwner(), a -> Adapter.UpdateData(a, viewModel.getSelectedWeekStart().getValue()));
        viewModel.getSelectedWeekStart().observe(getViewLifecycleOwner(), date -> Binding.SelectedWeekTextView.setText(viewModel.getSelectedWeekText()));
        viewModel.getSelectedUser().observe(getViewLifecycleOwner(), user -> Binding.UserTextView.setText(user == null ? getString(R.string.all) : user.getName()));
        viewModel.getSelectedDepartment().observe(getViewLifecycleOwner(), department -> Binding.DepartmentTextView.setText(department == null ? getString(R.string.all) : department.getName()));

        Binding.NextWeekBtn.setOnClickListener(view -> viewModel.onWeekChangeBtnsClick(true));
        Binding.PreviousWeekBtn.setOnClickListener(view -> viewModel.onWeekChangeBtnsClick(false));
        Binding.SelectedWeekTextView.setOnClickListener(v -> viewModel.onSelectedWeekTextViewClick(getParentFragmentManager(), getActivity()));
        Binding.DepartmentTextView.setOnClickListener(v -> viewModel.onDepartmentTextViewClick(v));
        Binding.UserTextView.setOnClickListener(v -> viewModel.onUserTextViewClick(v));

        return Binding.getRoot();
    }

    private void CreateToolbar(){
        ToolbarFrag toolbar = new ToolbarFrag();
        toolbar.setSettingsBtn(v -> mainViewModel.getMainNavController().navigate(R.id.schedulePrefShiftFrag));
        toolbar.setListBtn(v -> mainViewModel.getMainNavController().navigate(R.id.scheduleShiftChangeFrag));
        mainViewModel.getCurrentToolbar().setValue(toolbar);
    }
}
