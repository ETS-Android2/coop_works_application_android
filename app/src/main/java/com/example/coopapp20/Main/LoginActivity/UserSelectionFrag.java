package com.example.coopapp20.Main.LoginActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.coopapp20.Data.Objects.DepartmentObject;
import com.example.coopapp20.Data.Objects.UserObject;
import com.example.coopapp20.Main.MainRepository;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.FragLoginUserSelectionBinding;

import java.util.List;
import java.util.Objects;

public class UserSelectionFrag extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragLoginUserSelectionBinding binding = FragLoginUserSelectionBinding.inflate(inflater);
        SharedPreferences SharedPref = requireContext().getSharedPreferences(getString(R.string.BasicPreferences), Context.MODE_PRIVATE);
        MainViewModel viewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        MainRepository repository = new MainRepository(requireActivity().getApplication());

        LoginUserAdapter Adapter = new LoginUserAdapter(user -> {
            Log.e("LoginViewModel","User matching username & password");
            SharedPref.edit().putBoolean(getString(R.string.LoggedIn), true).apply();
            SharedPref.edit().putString(getString(R.string.CurrentUsername), user.getUserName()).apply();
            SharedPref.edit().putString(getString(R.string.CurrentUserPassword), user.getPassword()).apply();
            SharedPref.edit().putInt(getString(R.string.CurrentUserId), user.getId()).apply();
            viewModel.setCurrentUser(repository.getUser(user.getId()));
            Log.e("LoginViewModel","Preferences set");

            viewModel.getSuperNavController().navigate(R.id.SelectionToMain);
        }, getResources().getDisplayMetrics());
        binding.recyclerView.setAdapter(Adapter);

        viewModel.getAllUsers().observe(getViewLifecycleOwner(), Adapter::UpdateData);

        binding.ExecuteBtn.setOnClickListener(v -> new ResetDatabase(repository, requireContext()));

        return binding.getRoot();
    }
}
