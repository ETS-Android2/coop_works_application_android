package com.example.coopapp20.Data.Objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Users", ignoredColumns = {"DepartmentNameString","DepartmentColorString"})
public class UserObject {

    public static final int STATUS_OFF_DUTY = 0;
    public static final int STATUS_BREAK = 1;
    public static final int STATUS_OCCUPIED = 2;
    public static final int STATUS_AVAILABLE = 3;

    @PrimaryKey(autoGenerate = true)
    private Integer Id;

    private String Name;
    private String Email;
    private String PhoneNr;
    private Integer DepartmentId;
    private String UserName;
    private String Password;
    private Integer Status;
    private String IpAddress;
    private Integer PortNr;


    public UserObject( Integer Id, String Name, String Email, String PhoneNr, Integer DepartmentId, String UserName, String Password, Integer Status, String IpAddress, Integer PortNr){
        this.Name = Name;
        this.Email = Email;
        this.PhoneNr = PhoneNr;
        this.DepartmentId = DepartmentId;
        this.UserName = UserName;
        this.Password = Password;
        this.Status = Status;
        this.IpAddress = IpAddress;
        this.PortNr = PortNr;
        this.Id = Id;
    }

    public void setId(Integer id) { Id = id; }
    public void setName(String name) { Name = name; }
    public void setEmail(String email) { Email = email; }
    public void setPhoneNr(String phoneNr) { PhoneNr = phoneNr; }
    public void setDepartmentId(Integer departmentId) { DepartmentId = departmentId; }
    public void setUserName(String userName) { UserName = userName; }
    public void setPassword(String password) { Password = password; }
    public void setStatus(Integer status) {Status = status;}
    public void setIpAddress(String ipAddress) { IpAddress = ipAddress; }
    public void setPortNr(Integer portNr) { PortNr = portNr; }

    public Integer getId(){return Id;}
    public String getName(){return Name;}
    public String getEmail(){return Email;}
    public String getPhoneNr(){return PhoneNr;}
    public Integer getDepartmentId(){return DepartmentId;}
    public String getUserName(){return UserName;}
    public String getPassword(){return Password;}
    public Integer getStatus(){return Status;}
    public String getIpAddress(){return IpAddress;}
    public Integer getPortNr(){return PortNr;}

    String DepartmentNameString;
    int DepartmentColorString;

    public void setDepartmentNameString(String name){DepartmentNameString = name;}
    public void setDepartmentColorString(int departmentColorString) { DepartmentColorString = departmentColorString; }

    public String getDepartmentNameString(){return DepartmentNameString;}
    public int getDepartmentColorString() { return DepartmentColorString; }
}
