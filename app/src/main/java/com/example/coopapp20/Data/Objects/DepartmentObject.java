package com.example.coopapp20.Data.Objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Departments")
public class DepartmentObject {

    @PrimaryKey(autoGenerate = true)
    private Integer Id;

    private String Name;
    private String Color;

    public DepartmentObject (Integer Id, String Name, String Color){
        this.Id = Id;
        this.Name = Name;
        this.Color = Color;
    }

    public void setId(Integer id) { Id = id; }
    public void setName(String name) { Name = name; }
    public void setColor(String color) { Color = color; }

    public Integer getId() { return Id; }
    public String getName() { return Name; }
    public String getColor() { return Color; }
}
