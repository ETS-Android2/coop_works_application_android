package com.example.coopapp20.Scedule.ScheduleShiftChange.Detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coopapp20.Contact.ContactViewModel;
import com.example.coopapp20.Data.Objects.ShiftChangeRequestObject;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;
import com.example.coopapp20.Scedule.ScheduleShiftChange.ScheduleShiftChangeRequestObject;
import com.example.coopapp20.Scedule.ScheduleShiftChange.ShiftChangeViewModel;
import com.example.coopapp20.databinding.FragScheduleShiftchangeDetailBinding;

import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class ScheduleShiftChangeDetailFrag extends Fragment {


    private MainViewModel mainViewModel;
    private ShiftChangeViewModel viewModel;
    private ContactViewModel contactViewModel;
    private FragScheduleShiftchangeDetailBinding Binding;
    private ShiftChangeReceiverAdapter ReceiverAdapter;
    private ShiftChangeSwitchAdapter SwitchAdapter;

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy", Locale.ENGLISH);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = FragScheduleShiftchangeDetailBinding.inflate(getLayoutInflater());
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(ShiftChangeViewModel.class);
        contactViewModel = ViewModelProviders.of(requireActivity()).get(ContactViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainViewModel.getCurrentToolbar().setValue(new ToolbarFrag());
        viewModel.getSelectedShiftChangeRequest().observe(getViewLifecycleOwner(), this::CreateToolbar);

        viewModel.Observe(getViewLifecycleOwner());

        CreateRecyclerViews();

        viewModel.getShiftChangeResponses().observe(getViewLifecycleOwner(), s -> ReceiverAdapter.UpdateData(s));
        viewModel.getShiftChangeSwitches().observe(getViewLifecycleOwner(), a -> SwitchAdapter.UpdateData(a));
        viewModel.getSelectedShiftChangeRequest().observe(getViewLifecycleOwner(), a -> setTextViews());

        return Binding.getRoot();
    }

    private void CreateToolbar(ScheduleShiftChangeRequestObject request){
        if(request != null && viewModel.getCurrentUser().getValue() != null){
            ToolbarFrag toolbar = new ToolbarFrag();

            if(!request.getRequestStatus().equals(ShiftChangeRequestObject.SHIFTCHANGESTATUS_ACCEPTED) && !request.getRequestStatus().equals(ShiftChangeRequestObject.SHIFTCHANGESTATUS_DECLINED_AND_ENDED) && !request.getRequestStatus().equals(ShiftChangeRequestObject.SHIFTCHANGESTATUS_SWITCHED)) {
                if (request.getRequestSenderId().equals(viewModel.getCurrentUser().getValue().getId())) {
                    toolbar.setDeleteBtn(v -> viewModel.getDeleteBtnListener().onDeleteBtnClick(requireContext(), mainViewModel));
                    toolbar.setSettingsBtn(v -> viewModel.getSettingsBtnClickListener().onSettingsBtnClick(requireContext()));
                } else {
                    toolbar.setOptionsBtn(v -> viewModel.getOptionsBtnClickListener().onOptionsBtnClick(requireContext(), v, mainViewModel));
                }
            }
            mainViewModel.getCurrentToolbar().setValue(toolbar);
        }
    }

    private void CreateRecyclerViews(){
        ReceiverAdapter = new ShiftChangeReceiverAdapter(object -> viewModel.getReceiverClickListener().onReceiverClick(object, mainViewModel, contactViewModel));
        Binding.ReceiverRecyclerView.setAdapter(ReceiverAdapter);

        SwitchAdapter = new ShiftChangeSwitchAdapter((object, optionsbtn) -> viewModel.getSwitchClickListener().onSwitchClick(object, optionsbtn, mainViewModel, contactViewModel),getResources().getDisplayMetrics());
        Binding.SwitchRecyclerView.setAdapter(SwitchAdapter);
    }

    private void setTextViews(){
        if(viewModel.getSelectedShiftChangeRequest().getValue() != null) {
            ScheduleShiftChangeRequestObject request = viewModel.getSelectedShiftChangeRequest().getValue();

            String Day = getResources().getStringArray(R.array.day_array)[request.getShiftDate().getDayOfWeek().getValue()-1];
            String Week = "Uge Nr: " + request.getShiftDate().get(WeekFields.of(Locale.ENGLISH).weekOfYear());

            Binding.DayTextView.setText(Day);
            Binding.WeekTextView.setText(Week);
            Binding.DateTextView.setText(request.getShiftDate().format(dateFormatter));
            Binding.CommentTextView.setText(request.getRequestComment());
        }
    }
}
