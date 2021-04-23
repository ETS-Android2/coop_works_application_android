package com.example.coopapp20.Contact.Contact;

import java.time.LocalDateTime;
import java.util.List;

public class ContactUserObject {

    private String UserName;
    private Integer UserId;
    private Integer UserStatus;

    private Integer DepartmentColor;
    private String DepartmentName;

    private String Message;
    private LocalDateTime MessageDateTime;
    private List<Integer> MessageMissingViews;

    public ContactUserObject(String UserName, Integer UserId, Integer UserStatus, Integer DepartmentColor, String DepartmentName, String Message, LocalDateTime MessageDateTime, List<Integer> MessageMissingViews){
        this.UserName = UserName;
        this.UserId = UserId;
        this.UserStatus = UserStatus;
        this.DepartmentColor = DepartmentColor;
        this.DepartmentName = DepartmentName;
        this.Message = Message;
        this.MessageDateTime = MessageDateTime;
        this.MessageMissingViews = MessageMissingViews;
    }

    public String getUserName() {
        return UserName;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }
}
