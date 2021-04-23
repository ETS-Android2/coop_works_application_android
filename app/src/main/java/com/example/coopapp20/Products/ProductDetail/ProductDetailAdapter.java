package com.example.coopapp20.Products.ProductDetail;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.R;
import com.example.coopapp20.databinding.ResItemProductDetailBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailAdapter extends RecyclerView.Adapter<ProductDetailAdapter.TabViewHolder>  {
    private ArrayList<ProductDetailTabObject> dataSet = new ArrayList<>();

    class TabViewHolder extends RecyclerView.ViewHolder{
        private ResItemProductDetailBinding Binding;

        TabViewHolder(ResItemProductDetailBinding binding) {
            super(binding.getRoot());
            Binding = binding;
        }

        void setViews(ProductDetailTabObject object){
            Binding.TabName.setText(object.getName());
            Binding.TabField.setHint(object.getHint());
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(object.getMaxLength());
            Binding.TabField.setFilters(fArray);

            switch (object.getInput()){

            }
        }
    }

    @NonNull
    @Override
    public TabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewRoot = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_product_detail,parent,false);
        ResItemProductDetailBinding Binding = ResItemProductDetailBinding.bind(viewRoot);
        return new TabViewHolder(Binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TabViewHolder holder, int position) {
        holder.setViews(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void UpdateData(List<ProductDetailTabObject> UpdatedData){
        dataSet = new ArrayList<>(UpdatedData);
        notifyDataSetChanged();
    }
}
