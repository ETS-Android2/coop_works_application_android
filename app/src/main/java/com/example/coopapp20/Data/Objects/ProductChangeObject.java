package com.example.coopapp20.Data.Objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "ProductChanges", ignoredColumns = {"ProductName"})
public class ProductChangeObject {

    @PrimaryKey(autoGenerate = true)
    private Integer Id;

    private Integer ChangeType;
    private Integer Amount;
    private Integer ProductId;
    private LocalDateTime DateTime;

    public ProductChangeObject(Integer Id, Integer ChangeType, Integer Amount, Integer ProductId, LocalDateTime DateTime){
        this.Id = Id;
        this.ChangeType = ChangeType;
        this.Amount = Amount;
        this.ProductId = ProductId;
        this.DateTime = DateTime;
    }

    public Integer getId() { return Id; }
    public Integer getChangeType() { return ChangeType; }
    public Integer getAmount() { return Amount; }
    public Integer getProductId() { return ProductId; }
    public LocalDateTime getDateTime() { return DateTime; }


    private String ProductName;
    public void setProductName(String name){ProductName = name;}
    public String getProductName(){return ProductName;}
}
