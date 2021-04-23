package com.example.coopapp20.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.coopapp20.Contact.ContactVoiceChat.VoiceChatViewModel;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.FragMainBinding;
import com.example.coopapp20.databinding.ResNavHeaderBinding;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class MainFrag extends Fragment {

    private FragMainBinding Binding;
    private MainViewModel viewModel;
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = FragMainBinding.inflate(getLayoutInflater());

        viewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);

        NavHostFragment navfrag = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.MainNavHostFrag);
        NavController navController = Objects.requireNonNull(navfrag).getNavController();
        viewModel.setMainNavController(navController);

        NavigationUI.setupWithNavController(Binding.MenuBottomNavigation, viewModel.getMainNavController());
        Binding.MenuBottomNavigation.getMenu().findItem(R.id.navigationDrawerFrag).setOnMenuItemClickListener(item -> {Binding.NavDrawerLayout.openDrawer(GravityCompat.END);return false;});

        viewModel.getCurrentToolbar().observe(this, tempToolbarFrag -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ToolbarFrameLayout, tempToolbarFrag).commit());

        VoiceChatViewModel voiceChatViewModel = ViewModelProviders.of(this).get(VoiceChatViewModel.class);
        voiceChatViewModel.setMainViewModel(viewModel);
        voiceChatViewModel.setCurrentUser(viewModel.getCurrentUser());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setNavigationMenus();
        setNavigationDrawer();
        return Binding.getRoot();
    }

    private void setNavigationMenus(){
        //set toolbar height to 7% of screen height
        ConstraintLayout.LayoutParams ToolbarParams = (ConstraintLayout.LayoutParams) Binding.ToolbarFrameLayout.getLayoutParams();
        ToolbarParams.height=(int)(getResources().getDisplayMetrics().heightPixels*0.07);
        Binding.ToolbarFrameLayout.setLayoutParams(ToolbarParams);

        //set menu height to 7% of screen height
        ConstraintLayout.LayoutParams MenuParams = (ConstraintLayout.LayoutParams) Binding.MenuBottomNavigation.getLayoutParams();
        MenuParams.height=(int)(getResources().getDisplayMetrics().heightPixels*0.07);
        Binding.MenuBottomNavigation.setLayoutParams(MenuParams);
    }

    private void setNavigationDrawer(){
        //Setting NavigationDrawer Width
        ViewGroup.LayoutParams params = Binding.NavDrawer.getLayoutParams();
        params.width = (int) (getResources().getDisplayMetrics().widthPixels*0.8);
        Binding.NavDrawer.setLayoutParams(params);

        ResNavHeaderBinding navHeaderBinding = ResNavHeaderBinding.bind(Binding.NavDrawer.getHeaderView(0));

        //Setting NavigationDrawer Status
        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if(user != null) {
                navHeaderBinding.NavDrawerHeaderName.setText(user.getName());
                switch (user.getStatus() != null ? user.getStatus() : 0) {
                    case 0:
                        //on break
                        navHeaderBinding.NavHeaderImageFrame.setColorFilter(ContextCompat.getColor(requireContext(), R.color.ActivityNoShift));
                        navHeaderBinding.NavHeaderStatusBtn.setText(R.string.navigation_active_menu_no_shift);
                        break;
                    case 1:
                        //on break
                        navHeaderBinding.NavHeaderImageFrame.setColorFilter(ContextCompat.getColor(requireContext(), R.color.ActivityBreak));
                        navHeaderBinding.NavHeaderStatusBtn.setText(getString(R.string.navigation_active_menu_on_break));
                        break;
                    case 2:
                        //Unavailable
                        navHeaderBinding.NavHeaderImageFrame.setColorFilter(ContextCompat.getColor(requireContext(), R.color.ActivityUnavailable));
                        navHeaderBinding.NavHeaderStatusBtn.setText(getString(R.string.navigation_active_menu_occupied));
                        break;
                    case 3:
                        //available
                        navHeaderBinding.NavHeaderImageFrame.setColorFilter(ContextCompat.getColor(requireContext(), R.color.ActivityAvailable));
                        navHeaderBinding.NavHeaderStatusBtn.setText(getString(R.string.navigation_active_menu_available));
                        break;
                }

                if(user.getStatus() != 0){navHeaderBinding.NavHeaderStatusBtn.setOnClickListener(v -> viewModel.onStatusBtnClick(requireContext(),v));}
            }
        });

        //
        navHeaderBinding.NavDrawerHeaderShiftTime.setText(R.string.navigation_active_menu_no_shift);
        navHeaderBinding.NavDrawerHeaderTaskTime.setText("ingen opgaver");
        navHeaderBinding.NavDrawerHeaderFinishedTaskTime.setText("ingen færdige opgaver");

        viewModel.getCurrentShift().observe(getViewLifecycleOwner(), shiftObject -> {
            if(shiftObject != null){
                navHeaderBinding.NavDrawerHeaderShiftTime.setText(shiftObject.getTimeString());
            }
        });
        viewModel.getCurrentTasks().observe(getViewLifecycleOwner(), taskObjects -> {
            if(taskObjects != null){
                TaskTime taskTime = new TaskTime();
                taskObjects.forEach(task -> taskTime.add((int) task.getDuration().getSeconds()));
                navHeaderBinding.NavDrawerHeaderTaskTime.setText("Manglende:" + LocalTime.MIN.plusSeconds(taskTime.get()).format(timeFormatter));
            }
        });
        viewModel.getFinishedTasks().observe(getViewLifecycleOwner(), taskObjects -> {
            TaskTime taskTime = new TaskTime();
            taskObjects.forEach(task -> taskTime.add((int) task.getDuration().getSeconds()));
            navHeaderBinding.NavDrawerHeaderFinishedTaskTime.setText(LocalTime.MIN.plusSeconds(taskTime.get()).format(timeFormatter));
        });

        //Setting NavigationDrawer navigation
        Binding.NavDrawer.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            Binding.NavDrawerLayout.closeDrawer(GravityCompat.END);
            return viewModel.onNavigationDrawerItemClick(item.getItemId());
        });

        Binding.LogoutBtn.setOnClickListener(v -> viewModel.onLogoutBtnClick(requireContext()));
    }

    private static class TaskTime{
        int Time = 0;
        private void add(int time){Time +=time; }
        private int get(){return Time;}
    }
}

