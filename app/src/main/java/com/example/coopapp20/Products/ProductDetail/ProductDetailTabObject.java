package com.example.coopapp20.Products.ProductDetail;

public class ProductDetailTabObject {

    private String Name;
    private String Hint;
    private Integer Input;
    private Integer MaxLength;

    public ProductDetailTabObject(String name, String hint, Integer input, Integer maxLength){
        Name = name;
        Hint = hint;
        Input = input;
        MaxLength = maxLength;
    }

    public String getName() {
        return Name;
    }

    public String getHint() {
        return Hint;
    }

    public Integer getInput() {
        return Input;
    }

    public Integer getMaxLength() {
        return MaxLength;
    }
}
