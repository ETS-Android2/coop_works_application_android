package com.example.coopapp20.Products.Product;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.Data.Objects.ProductObject;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.ResItemProductBinding;

import java.util.ArrayList;
import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private ArrayList<ProductObject> dataSet = new ArrayList<>();
    private ArrayList<ProductObject> dataSetSorted = new ArrayList<>();
    private OnProductClickListener Listener;
    private String Search = null;
    private DisplayMetrics Metrics;

    ProductAdapter(OnProductClickListener listener, DisplayMetrics metrics){
        Listener = listener;
        Metrics = metrics;
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{
        private ResItemProductBinding Binding;

         ProductViewHolder(ResItemProductBinding binding){
             super(binding.getRoot());
             Binding = binding;
        }

        void setViews(ProductObject object){
             Binding.TitleTextView.setText(object.getName());
             Binding.ManufactureTextView.setText(object.getManufacture());
             Binding.AmountTextView.setText(String.valueOf(object.getInStore()));
             Binding.ColorImageView.setImageDrawable(object.getProductImage());

             Binding.getRoot().setOnClickListener(v -> Listener.onProductClick(dataSetSorted.get(getAdapterPosition())));
             ViewGroup.LayoutParams listItemViewParams = Binding.getRoot().getLayoutParams();
             listItemViewParams.height = (int) (Metrics.heightPixels*0.1);
             Binding.getRoot().setLayoutParams(listItemViewParams);
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_product,parent,false);
        ResItemProductBinding Binding = ResItemProductBinding.bind(listItemView);
        return new ProductViewHolder(Binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.setViews(dataSetSorted.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSetSorted.size();
    }

    void UpdateData(List<ProductObject> UpdatedData){
        dataSet = new ArrayList<>(UpdatedData);
        dataSetSorted = new ArrayList<>(UpdatedData);
        FilterData(Search);
    }

    void FilterData(String Search){
        this.Search = Search;
        dataSetSorted.clear();
        if(Search != null && !Search.equals("")) {
            dataSetSorted = new ArrayList<>(dataSet);
            dataSetSorted.removeIf(data -> {
                boolean remove = false;

                //Filter all strings split by " "
                String[] SearchTextParts = Search.split("\\s+");
                for (String searchTextPart : SearchTextParts) {
                    if (!data.getName().toLowerCase().contains(searchTextPart.toLowerCase()) && !data.getManufacture().toLowerCase().contains(searchTextPart.toLowerCase())) {
                        remove = true;
                    }
                }

                return remove;
            });
        }

        notifyDataSetChanged();
    }

    public interface OnProductClickListener{
        void onProductClick(ProductObject object);
    }
}
