package com.example.coopapp20.Data.Objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "DatabaseChange")
public class DatabaseChangeObject {

    @PrimaryKey(autoGenerate = true)
    private Integer Id;

    private ArrayList<String> Command;

    public DatabaseChangeObject(ArrayList<String> Command){
        this.Command = Command;
    }

    public void setId(Integer id) { Id = id; }
    public Integer getId() { return Id; }
    public ArrayList<String> getCommand() { return Command; }
}
