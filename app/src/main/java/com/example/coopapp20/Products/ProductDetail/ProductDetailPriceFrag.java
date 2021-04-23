package com.example.coopapp20.Products.ProductDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.coopapp20.R;

public class ProductDetailPriceFrag extends Fragment {
    public ProductDetailPriceFrag(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_product_price, container, false);
        return rootView;
    }
}

