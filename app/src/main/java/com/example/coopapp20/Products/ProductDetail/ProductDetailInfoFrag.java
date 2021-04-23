package com.example.coopapp20.Products.ProductDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.coopapp20.Data.Objects.ProductObject;
import com.example.coopapp20.Products.ProductViewModel;
import com.example.coopapp20.databinding.FragProductInfoBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ProductDetailInfoFrag extends Fragment {
    public ProductDetailInfoFrag(){}

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragProductInfoBinding Binding = FragProductInfoBinding.inflate(inflater);

        ProductViewModel viewModel = ViewModelProviders.of(requireActivity()).get(ProductViewModel.class);

        viewModel.getSelectedProduct().observe(getViewLifecycleOwner(), object -> {
            ProductObject SelectedProduct = viewModel.getSelectedProduct().getValue();
            if(SelectedProduct != null) {
                Binding.NameTextView.setText(SelectedProduct.getName());
                Binding.ManufactureEditText.setText(SelectedProduct.getManufacture());
                Binding.PriceEditText.setText(String.valueOf(SelectedProduct.getPrice()));
                Binding.CostEditText.setText(String.valueOf(SelectedProduct.getCost()));
                Binding.InStoreEditText.setText(String.valueOf(SelectedProduct.getInStore()));
                Binding.DiscountEditText.setText(SelectedProduct.getDiscount());
                Binding.DescriptionEditText.setText(SelectedProduct.getInformation());
                Binding.BarcodeEditText.setText(SelectedProduct.getBarcode());
                Binding.ProductIdTextView.setText(String.valueOf(SelectedProduct.getId()));
            }
        });

        return Binding.getRoot();
    }
}


