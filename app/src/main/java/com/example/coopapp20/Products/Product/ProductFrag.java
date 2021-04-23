package com.example.coopapp20.Products.Product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.Data.Objects.ProductObject;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.Products.ProductViewModel;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.FragRecyclerviewBinding;

public class ProductFrag extends Fragment implements ProductAdapter.OnProductClickListener {

    private MainViewModel mainViewModel;
    private ProductViewModel viewModel;
    private FragRecyclerviewBinding Binding;
    private ProductAdapter Adapter;
    private ToolbarFrag toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = FragRecyclerviewBinding.inflate(getLayoutInflater());

        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(ProductViewModel.class);

        viewModel.getProducts().observe(this, productObjects -> Adapter.UpdateData(productObjects));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Binding.TextView.setText(getString(R.string.productfrag_noshowtext));
        CreateToolbar();
        CreateRecyclerView();
        return Binding.getRoot();
    }

    private void CreateToolbar(){
        toolbar = new ToolbarFrag();
        toolbar.setBarcodeBtn(v -> mainViewModel.getMainNavController().navigate(R.id.scannerFrag));
        toolbar.setSearchBtnVisible(true);
        toolbar.setListBtn(v -> mainViewModel.getMainNavController().navigate(R.id.productChangeFrag));
        mainViewModel.getCurrentToolbar().setValue(toolbar);
    }

    private void CreateRecyclerView(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        Binding.Recyclerview.setLayoutManager(layoutManager);
        Adapter = new ProductAdapter( this, getContext().getResources().getDisplayMetrics());
        Binding.Recyclerview.setAdapter(Adapter);
        Binding.TextView.setVisibility(View.VISIBLE);
        toolbar.SearchText.observe(getViewLifecycleOwner(), s -> {
            Adapter.FilterData(s);
            if(Adapter.getItemCount() == 0){ Binding.TextView.setVisibility(View.VISIBLE);
            }else {Binding.TextView.setVisibility(View.INVISIBLE);}
        });
        Adapter.FilterData(null);
    }


    @Override
    public void onProductClick(ProductObject product) {
        viewModel.getSelectedProduct().setValue(product);
        mainViewModel.getMainNavController().navigate(R.id.productDetailFrag);
    }
}
