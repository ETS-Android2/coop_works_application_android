package com.example.coopapp20.Info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;

public class GuideFrag extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_resources_guide, container, false);

        MainViewModel mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        mainViewModel.getCurrentToolbar().setValue(new ToolbarFrag());

        return rootView;
    }
}
