package com.example.coopapp20.Products.ProductDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;

import com.example.coopapp20.databinding.FragProductDisposalBinding;

import org.jetbrains.annotations.NotNull;

public class ProductDetailDisposalFrag extends Fragment {

    private FragProductDisposalBinding Binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Binding = FragProductDisposalBinding.inflate(inflater);


        String[] ReasonSpinnerReasons = {"Ødelagt varer", "Udløbet varer", "Stjålet varer", "Medarbejder brugt varer"};
        Binding.ProductDisposalReasonSpinner.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, ReasonSpinnerReasons));

        Binding.ProductDisposalExecuteBtn.setOnClickListener(v -> {
            if(Integer.parseInt(Binding.ProductDisposalAmountEditText.getText().toString()) > 0){
                Binding.ProductDisposalAmountEditText.getText().clear();
            }
        });

        return Binding.getRoot();
    }
}
