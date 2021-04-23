package com.example.coopapp20.Products.ProductDetail;

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
import com.example.coopapp20.databinding.FragProductDetailBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class ProductDetailFrag extends Fragment {

    private MainViewModel mainViewModel;
    private ToolbarFrag toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragProductDetailBinding Binding = FragProductDetailBinding.inflate(inflater);

        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);

        Binding.ProductChangeMenu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (Binding.ProductChangeMenu.getSelectedTabPosition()){
                    case 0:
                        getFragmentManager().beginTransaction().replace(R.id.ProductChangeFrame, new ProductDetailDisposalFrag()).commit();
                        break;
                    case 1:
                        getFragmentManager().beginTransaction().replace(R.id.ProductChangeFrame, new ProductDetailPriceFrag()).commit();
                        break;
                    case 2:
                        getFragmentManager().beginTransaction().replace(R.id.ProductChangeFrame, new ProductDetailManagementFrag()).commit();
                        break;
                    case 3:
                        getFragmentManager().beginTransaction().replace(R.id.ProductChangeFrame, new ProductDetailInfoFrag()).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        CreateToolbar();

        return Binding.getRoot();
    }

    private void CreateToolbar(){
        toolbar = new ToolbarFrag();
        toolbar.setSearchBtnVisible(true);
        toolbar.setBarcodeBtn(v -> mainViewModel.getMainNavController().navigate(R.id.scannerFrag));
        mainViewModel.getCurrentToolbar().setValue(toolbar);
    }
}
