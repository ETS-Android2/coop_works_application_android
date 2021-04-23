package com.example.coopapp20.Products.ProductChangeList;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coopapp20.Data.Objects.ProductChangeObject;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.ResItemProductPastBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductChangeAdapter extends RecyclerView.Adapter<ProductChangeAdapter.ProductViewHolder> {
    private ArrayList<ProductChangeObject> dataSet = new ArrayList<>();
    private ArrayList<ProductChangeObject> dataSetSorted = new ArrayList<>();
    private OnProductChangeClickListener OnCustomClickListener;
    private DisplayMetrics Metrics;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ProductViewHolder extends RecyclerView.ViewHolder {
        ResItemProductPastBinding Binding;

        ProductViewHolder(ResItemProductPastBinding binding) {
            super(binding.getRoot());
            Binding = binding;
        }
        void setViews(ProductChangeObject object){
            Binding.TitleTextView.setText(object.getProductName());
            Binding.TypeTextView.setText(object.getChangeType());

            Binding.ProductPastActivitiesRecyclerViewItemOptionsBtn.setOnClickListener(v->OnCustomClickListener.onProductChangeClick(getAdapterPosition()));
            ViewGroup.LayoutParams listItemViewParams = Binding.getRoot().getLayoutParams();
            listItemViewParams.height = (int) (Metrics.heightPixels*0.1);
            Binding.getRoot().setLayoutParams(listItemViewParams);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ProductChangeAdapter(OnProductChangeClickListener onCustomClickListener, DisplayMetrics metrics){
        OnCustomClickListener = onCustomClickListener;
        Metrics = metrics;
    }

    @NonNull
    @Override
    // Create new views (invoked by the layout manager)
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_product_past,parent,false);
        ResItemProductPastBinding Binding = ResItemProductPastBinding.bind(listItemView);
        return new ProductChangeAdapter.ProductViewHolder(Binding);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ProductChangeAdapter.ProductViewHolder holder, int position) {
        holder.setViews(dataSetSorted.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSetSorted.size();
    }

    void UpdateData(List<ProductChangeObject> UpdatedData){
        dataSet = new ArrayList<>(UpdatedData);
        dataSetSorted = new ArrayList<>(UpdatedData);
        notifyDataSetChanged();
    }

    void FilterData(String Search){
        dataSetSorted.clear();
        dataSetSorted = new ArrayList<>(dataSet);
        dataSetSorted.removeIf(data -> {
           boolean remove = false;

           if(data.getProductName().toLowerCase().contains(Search.toLowerCase())){remove = true;}

           return remove;
        });
    }

    public interface OnProductChangeClickListener{
        void onProductChangeClick(int position);
    }

}

