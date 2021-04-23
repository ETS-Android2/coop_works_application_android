package com.example.coopapp20.Scedule.ScheduleShiftChange;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.room.Ignore;

import com.example.coopapp20.Data.Objects.ShiftChangeRequestObject;
import com.example.coopapp20.R;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleShiftChangeRequestObject {

    private String ShiftHolderName;
    private String ResponderName;

    private LocalDate ShiftDate;
    private LocalTime ShiftStartTime;
    private LocalTime ShiftEndTime;

    private String RequestComment;
    private Integer RequestStatus;
    private Integer RequestNecessity;
    private Integer RequestSenderId;
    private Integer RequestId;

    @Ignore
    private Drawable RequestStatusDrawable;
    @Ignore
    private Drawable ResponseDrawable;

    public ScheduleShiftChangeRequestObject(String ShiftHolderName, String ResponderName, LocalDate ShiftDate, LocalTime ShiftStartTime, LocalTime ShiftEndTime, String RequestComment, Integer RequestStatus, Integer RequestNecessity, Integer RequestSenderId, Integer RequestId){
        this.ShiftHolderName = ShiftHolderName;
        this.ResponderName = ResponderName;
        this.ShiftDate = ShiftDate;
        this.ShiftStartTime = ShiftStartTime;
        this.ShiftEndTime = ShiftEndTime;
        this.RequestComment = RequestComment;
        this.RequestStatus = RequestStatus;
        this.RequestNecessity = RequestNecessity;
        this.RequestSenderId = RequestSenderId;
        this.RequestId = RequestId;
    }

    public String getShiftHolderName() {
        return ShiftHolderName;
    }
    public String getResponderName() {
        return ResponderName;
    }
    public LocalDate getShiftDate() {
        return ShiftDate;
    }
    public LocalTime getShiftStartTime() {
        return ShiftStartTime;
    }
    public LocalTime getShiftEndTime() {
        return ShiftEndTime;
    }
    public String getRequestComment() {
        return RequestComment;
    }
    public Integer getRequestStatus() {
        return RequestStatus;
    }
    public Integer getRequestNecessity() { return RequestNecessity; }
    public Integer getRequestSenderId() {
        return RequestSenderId;
    }
    public Integer getRequestId() {
        return RequestId;
    }

    public Drawable getRequestDrawable(Context context) {
        if(RequestStatusDrawable == null){
            switch (RequestStatus) {
                case ShiftChangeRequestObject.SHIFTCHANGESTATUS_DECLINED_AND_ENDED:
                    RequestStatusDrawable = ContextCompat.getDrawable(context, R.drawable.round_cancel_out_of_time_icon);
                    RequestStatusDrawable.setTint(ContextCompat.getColor(context, R.color.StatusDenied));
                    break;
                case ShiftChangeRequestObject.SHIFTCHANGESTATUS_DECLINED:
                    RequestStatusDrawable = ContextCompat.getDrawable(context, R.drawable.round_cancel_icon);
                    RequestStatusDrawable.setTint(ContextCompat.getColor(context, R.color.StatusDenied));
                    break;
                case ShiftChangeRequestObject.SHIFTCHANGESTATUS_PENDING:
                    RequestStatusDrawable = ContextCompat.getDrawable(context, R.drawable.round_pending_icon);
                    RequestStatusDrawable.setTint(ContextCompat.getColor(context, R.color.StatusPending));
                    break;
                case ShiftChangeRequestObject.SHIFTCHANGESTATUS_SWITCH_SUGGESTED:
                    RequestStatusDrawable = ContextCompat.getDrawable(context, R.drawable.round_switch_icon);
                    RequestStatusDrawable.setTint(ContextCompat.getColor(context, R.color.StatusPending));
                    break;
                case ShiftChangeRequestObject.SHIFTCHANGESTATUS_SWITCHED:
                    RequestStatusDrawable = ContextCompat.getDrawable(context, R.drawable.round_switch_icon);
                    RequestStatusDrawable.setTint(ContextCompat.getColor(context, R.color.StatusAccepted));
                    break;
                case ShiftChangeRequestObject.SHIFTCHANGESTATUS_ACCEPTED:
                    RequestStatusDrawable = ContextCompat.getDrawable(context, R.drawable.sharp_check_circle_outline_24);
                    RequestStatusDrawable.setTint(ContextCompat.getColor(context, R.color.StatusAccepted));
                    break;
            }
        }
        
        return RequestStatusDrawable;
    }

    public Drawable getResponseDrawable() {
        return ResponseDrawable;
    }
}
