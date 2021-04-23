package com.example.coopapp20.Contact.Contact;

import android.graphics.Color;
import android.util.Log;

public class ContactObject {

    private String Name;
    private int DepartmentColor;
    private String DepartmentName;
    private String Message;
    private String DateOrTime;
    private boolean Unviewed;
    private boolean Active;
    private Integer UserNr;
    private Object DataObject;

    public ContactObject(String name, String departmentname, String departmentcolor, String message, String dateortime, boolean unviewed, boolean active, Integer usernr, Object dataObject){
        Name = name;
        DepartmentName = departmentname;
        DepartmentColor = Color.parseColor(departmentcolor);
        Message = message;
        DateOrTime = dateortime;
        Unviewed = unviewed;
        Active = active;
        UserNr = usernr;
        DataObject = dataObject;
    }

    public String getName(){
        return Name;
    }
    public int getDepartmentColor(){
        return DepartmentColor;
    }
    public String getDepartmentName(){return DepartmentName;}
    public String getMessage(){
        return Message;
    }
    public String getMessageDateOrTime(){return DateOrTime;}
    public boolean getUnviewed(){return Unviewed;}
    public Boolean getActive(){return Active;}
    public Integer getUserNr(){return UserNr;}
    public Object getDataObject(){return DataObject;}
}
