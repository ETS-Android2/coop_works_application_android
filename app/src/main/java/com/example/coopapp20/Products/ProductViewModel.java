package com.example.coopapp20.Products;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.coopapp20.Data.Objects.ProductChangeObject;
import com.example.coopapp20.Data.Objects.ProductObject;
import com.example.coopapp20.R;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private ProductRepository repository;

    private LiveData<List<ProductObject>> Products;
    private LiveData<List<ProductChangeObject>> ProductChanges;
    private MutableLiveData<ProductObject> SelectedProduct = new MutableLiveData<>();
    private MutableLiveData<ProductObject> EditableProduct = new MutableLiveData<>();
    private MutableLiveData<String> ScannedBarcode = new MutableLiveData<>();


    public ProductViewModel(@NonNull Application application) {
        super(application);
        repository = new ProductRepository(application);

        Products = Transformations.map(repository.getAllProducts(), this::CreateProductObjects);
        ProductChanges = Transformations.map(repository.getUserProductChanges(), this::CreateProductChangeObjects);
    }

    public LiveData<List<ProductObject>> getProducts(){ return Products; }
    public LiveData<List<ProductChangeObject>> getProductChanges(){return ProductChanges;}
    public MutableLiveData<ProductObject> getSelectedProduct(){return SelectedProduct;}
    MutableLiveData<ProductObject> getEditableProduct(){return EditableProduct;}

    void onAddProductBtnClick(String Name, String Manufacture, String Price, String Cost, String InStore, String Barcode){

        if(EditableProduct.getValue() != null){
            ProductObject product = EditableProduct.getValue();

            //Check if barcode is available
            //Edit ProductObject

            repository.updateProduct(product);
        }
    }
    void onDeleteProductBtnClick(){
        if(EditableProduct.getValue() != null){
            //Delete product
        }
    }

    private List<ProductObject> CreateProductObjects(List<ProductObject> products){
        if(products != null){
            products.forEach(p -> p.setProductImage(ContextCompat.getDrawable(getApplication(),R.drawable.sharp_shopping_basket_24)));
            return products;
        }else {return null;}
    }
    private List<ProductChangeObject> CreateProductChangeObjects(List<ProductChangeObject> productChanges){
        if(productChanges != null && Products.getValue() != null){
            productChanges.forEach(pc -> Products.getValue().stream().filter(p -> p.getId().equals(pc.getProductId())).findAny().ifPresent(p -> pc.setProductName(p.getName())));
            return productChanges;
        }else {return null;}
    }

    boolean SelectProductByBarcode(String barcode){
        if(Products.getValue() != null){ SelectedProduct.setValue(Products.getValue().stream().filter(o -> o.getBarcode().equals(barcode)).findAny().orElse(null)); }
        return Products.getValue() != null;
    }
}
