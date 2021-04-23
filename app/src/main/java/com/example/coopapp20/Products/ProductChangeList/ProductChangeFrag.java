package com.example.coopapp20.Products.ProductChangeList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.Products.ProductViewModel;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.FragRecyclerviewBinding;

import java.util.Objects;

public class ProductChangeFrag extends Fragment{

    private MainViewModel mainViewModel;
    private ProductViewModel viewModel;
    private ProductChangeAdapter Adapter;
    private FragRecyclerviewBinding Binding;
    private ToolbarFrag toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = FragRecyclerviewBinding.inflate(getLayoutInflater());

        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(ProductViewModel.class);

        viewModel.getProductChanges().observe(this, productChangeObject -> Adapter.UpdateData(productChangeObject));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Binding.TextView.setText(R.string.no_data);
        CreateToolbar();
        CreateRecyclerView();
        return Binding.getRoot();
    }

    private void CreateToolbar(){
        toolbar = new ToolbarFrag();
        toolbar.setSearchBtnVisible(true);
        mainViewModel.getCurrentToolbar().setValue(toolbar);
    }

    private void CreateRecyclerView(){
        Adapter = new ProductChangeAdapter( position -> {}, getResources().getDisplayMetrics());
        Binding.Recyclerview.setLayoutManager( new LinearLayoutManager(getContext()));
        Binding.Recyclerview.setAdapter(Adapter);
        toolbar.SearchText.observe(getViewLifecycleOwner(), s -> Adapter.FilterData(s));
    }
}
