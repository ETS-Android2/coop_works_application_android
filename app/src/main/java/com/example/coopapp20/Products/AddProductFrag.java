package com.example.coopapp20.Products;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.coopapp20.Data.Objects.ProductObject;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.FragAdminProductAddBinding;

import org.jetbrains.annotations.NotNull;


public class AddProductFrag extends Fragment {

    private FragAdminProductAddBinding Binding;
    private MainViewModel mainViewModel;
    private ProductViewModel viewModel;
    private ToolbarFrag toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = FragAdminProductAddBinding.inflate(getLayoutInflater());

        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(ProductViewModel.class);

    }

    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Binding.ExecuteBtn.setOnClickListener(v -> AddProductIf());

        Binding.BarcodeBtn.setOnClickListener(v -> mainViewModel.getMainNavController().navigate(R.id.scannerFrag));

        CreateToolbar();

        return Binding.getRoot();
    }

    private void CreateToolbar(){
        toolbar = new ToolbarFrag();
        if(viewModel.getEditableProduct().getValue() != null){toolbar.setDeleteBtn(v -> viewModel.onDeleteProductBtnClick());}
        mainViewModel.getCurrentToolbar().setValue(toolbar);
    }

    private void AddProductIf() {
        String Name = Binding.NameTextView.getText().toString();
        String Manufacture = Binding.ManufactureTextView.getText().toString();
        String Price = Binding.PriceTextView.getText().toString();
        String Cost = Binding.CostTextView.getText().toString();
        String InStore = Binding.InStoreTextView.getText().toString();
        String Barcode = Binding.BarcodeTextView.getText().toString();

        if (!Name.equals("") && !Manufacture.equals("") && !Price.equals("") && !Cost.equals("") && !InStore.equals("") && !Barcode.equals("")) {
            String DialogTitle;
            if(viewModel.getEditableProduct().getValue() != null){DialogTitle = getString(R.string.do_you_want_to_change_the_product);}
            else {DialogTitle = getString(R.string.do_you_want_to_change_the_product);}

            new AlertDialog.Builder(requireContext())
                    .setTitle(DialogTitle)
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                        viewModel.onAddProductBtnClick(Name, Manufacture, Price, Cost, InStore, Barcode);

                        Binding.NameTextView.getText().clear();
                        Binding.ManufactureTextView.getText().clear();
                        Binding.PriceTextView.getText().clear();
                        Binding.CostTextView.getText().clear();
                        Binding.InStoreTextView.getText().clear();
                        Binding.BarcodeTextView.getText().clear();

                        if (!Binding.StaySwitch.isChecked()) { requireActivity().onBackPressed(); }
                        else { Toast.makeText(getContext(), getString(R.string.done), Toast.LENGTH_SHORT).show(); }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        } else {Toast.makeText(getContext(), "Fill out whole application", Toast.LENGTH_SHORT).show();}
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // else set text to previously written information
        if(viewModel.getEditableProduct().getValue() == null) {
            SharedPreferences SharedPref = getContext().getSharedPreferences(getString(R.string.BasicPreferences), Context.MODE_PRIVATE);

            Binding.NameTextView.setText(SharedPref.getString("AddProductEdittextName", ""));
            Binding.ManufactureTextView.setText(SharedPref.getString("AddProductEdittextManufacture", ""));
            Binding.PriceTextView.setText(SharedPref.getString("AddProductEdittextStoreprice", ""));
            Binding.CostTextView.setText(SharedPref.getString("AddProductEdittextCostprice", ""));
            Binding.InStoreTextView.setText(SharedPref.getString("AddProductEdittextAmount", ""));
            Binding.BarcodeTextView.setText(SharedPref.getString("AddProductEdittextBarcode", ""));
            Binding.ExecuteBtn.setText(getString(R.string.add_product));
        }
        else  {
            ProductObject product = viewModel.getEditableProduct().getValue();

            Binding.NameTextView.setText(product.getName());
            Binding.ManufactureTextView.setText(product.getManufacture());
            Binding.PriceTextView.setText(String.valueOf(product.getPrice()));
            Binding.CostTextView.setText(String.valueOf(product.getCost()));
            Binding.InStoreTextView.setText(product.getInStore());
            Binding.BarcodeTextView.setText(product.getBarcode());
            Binding.ExecuteBtn.setText(getString(R.string.edit_product));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (viewModel.getEditableProduct().getValue() == null) {
            SharedPreferences SharedPref = getContext().getSharedPreferences(getString(R.string.BasicPreferences), Context.MODE_PRIVATE);
            SharedPref.edit().putString("AddProductEdittextName", Binding.NameTextView.getText().toString()).apply();
            SharedPref.edit().putString("AddProductEdittextManufacture", Binding.ManufactureTextView.getText().toString()).apply();
            SharedPref.edit().putString("EdittextStoreprice", Binding.PriceTextView.getText().toString()).apply();
            SharedPref.edit().putString("AddProductEdittextCostprice", Binding.CostTextView.getText().toString()).apply();
            SharedPref.edit().putString("AddProductEdittextAmount", Binding.InStoreTextView.getText().toString()).apply();
            SharedPref.edit().putString("AddProductEdittextBarcode", Binding.BarcodeTextView.getText().toString()).apply();
        }else {viewModel.getEditableProduct().setValue(null);}
    }


}