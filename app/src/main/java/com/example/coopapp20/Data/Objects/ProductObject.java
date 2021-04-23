package com.example.coopapp20.Data.Objects;

import android.graphics.drawable.Drawable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Products", ignoredColumns = {"ProductImage"})
public class ProductObject {

    @PrimaryKey(autoGenerate = true)
    private Integer Id;

    private String Name;
    private String Manufacture;
    private Float Price;
    private Float Cost;
    private String Discount;
    private Integer InStore;
    private String Information;
    private String Barcode;

    public ProductObject(Integer Id, String Name, String Manufacture, Float Price, Float Cost, String Discount, Integer InStore, String Information, String Barcode){
        this.Id = Id;
        this.Name = Name;
        this.Manufacture = Manufacture;
        this.Price = Price;
        this.Cost = Cost;
        this.Discount = Discount;
        this.InStore = InStore;
        this.Information = Information;
        this.Barcode = Barcode;
    }

    public Integer getId() {return Id;}
    public String getName() {return Name; }
    public String getManufacture() { return Manufacture; }
    public Float getPrice() { return Price; }
    public Float getCost() { return Cost; }
    public String getDiscount() { return Discount; }
    public Integer getInStore() { return InStore; }
    public String getInformation() { return Information; }
    public String getBarcode() { return Barcode; }


    private Drawable ProductImage;
    public void setProductImage(Drawable image){ProductImage = image;}
    public Drawable getProductImage(){return ProductImage;}
}
