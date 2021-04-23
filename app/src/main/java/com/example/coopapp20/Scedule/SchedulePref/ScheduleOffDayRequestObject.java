package com.example.coopapp20.Scedule.SchedulePref;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.room.Ignore;

import com.example.coopapp20.Data.Objects.OffDayRequestObject;
import com.example.coopapp20.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ScheduleOffDayRequestObject {

    @Ignore
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy", Locale.ENGLISH);

    private String Comment;
    private Integer Type;
    private LocalDate StartDate;
    private LocalDate EndDate;
    private Integer Status;
    private Integer Id;

    public ScheduleOffDayRequestObject(String Comment, Integer Type, LocalDate StartDate, LocalDate EndDate, Integer Status, Integer Id){
        this.Comment = Comment;
        this.Type = Type;
        this.StartDate = StartDate;
        this.EndDate = EndDate;
        this.Status = Status;
        this.Id = Id;
    }

    public String getComment() {
        return Comment;
    }
    public Integer getType() {
        return Type;
    }
    public LocalDate getStartDate() {
        return StartDate;
    }
    public LocalDate getEndDate() {
        return EndDate;
    }
    public Integer getStatus() {
        return Status;
    }
    public Integer getId() {
        return Id;
    }

    public String getTypeString(Context context){
        if(Type.equals(OffDayRequestObject.TYPE_OFFDAY)){return context.getResources().getString(R.string.schedule_pref_vacation);}
        else { return context.getResources().getString(R.string.schedule_pref_offdays);}
    }
    public String getDateString(){
        if(!StartDate.equals(EndDate)){return StartDate.format(dateFormatter) + " - " + EndDate.format(dateFormatter);
        }else {return StartDate.format(dateFormatter);}
    }
    public Integer getStatusColor(Context context){
        switch (Status){
            case OffDayRequestObject.STATUS_DENIED:return ContextCompat.getColor(context, R.color.StatusDenied);
            case OffDayRequestObject.STATUS_PENDING:return ContextCompat.getColor(context, R.color.StatusPending);
            case OffDayRequestObject.STATUS_ACCEPTED:return ContextCompat.getColor(context, R.color.StatusAccepted);
            default: return null;
        }
    }
}
