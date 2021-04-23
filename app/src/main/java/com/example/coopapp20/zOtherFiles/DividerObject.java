package com.example.coopapp20.zOtherFiles;

import java.time.LocalDate;

public class DividerObject {

    private LocalDate Date;

    public DividerObject(){}

    public DividerObject setDate(LocalDate date){Date = date; return this;}

    public LocalDate getDate() { return Date;}
}
