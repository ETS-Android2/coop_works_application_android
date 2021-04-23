package com.example.coopapp20.Info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;

public class FeedbackFrag extends Fragment {

    private RecyclerView recyclerView;
    private TextView NoShowTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_recyclerview, container, false);

        NoShowTextView = rootView.findViewById(R.id.TextView);
        NoShowTextView.setText("ingen forslag endnu");
        NoShowTextView.setVisibility(View.VISIBLE);

        MainViewModel mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        mainViewModel.getCurrentToolbar().setValue(new ToolbarFrag());

        return rootView;
    }
}
