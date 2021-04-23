package com.example.coopapp20.Scedule.ScheduleShiftChange;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.room.Ignore;

import com.example.coopapp20.Data.Objects.ShiftChangeResponseObject;
import com.example.coopapp20.R;

import java.util.List;

public class ScheduleShiftChangeResponseObject {
    
    private String ResponderName;
    private Integer ResponderId;
    
    private Integer ResponseStatus;
    private List<Integer> ResponseShiftIdList;
    private Integer ResponseId;
    
    @Ignore
    private Drawable ResponseDrawable;
    
    public ScheduleShiftChangeResponseObject(String ResponderName, Integer ResponderId, Integer ResponseStatus, List<Integer> ResponseShiftIdList, Integer ResponseId){
        this.ResponderName = ResponderName;
        this.ResponderId = ResponderId;
        this.ResponseStatus = ResponseStatus;
        this. ResponseShiftIdList = ResponseShiftIdList;
        this.ResponseId = ResponseId;
    }

    public String getResponderName() {
        return ResponderName;
    }
    public Integer getResponderId() {
        return ResponderId;
    }
    public Integer getResponseStatus() {
        return ResponseStatus;
    }
    public List<Integer> getResponseShiftIdList() {
        return ResponseShiftIdList;
    }
    public Integer getResponseId() {
        return ResponseId;
    }

    public Drawable getResponseDrawable(Context context) {
        if(ResponseDrawable == null){
            switch (ResponseStatus) {
                case ShiftChangeResponseObject.SHIFTCHANGESTATUS_PENDING:
                    ResponseDrawable = ContextCompat.getDrawable(context, R.drawable.round_pending_icon);
                    ResponseDrawable.setTint(ContextCompat.getColor(context, R.color.StatusPending));
                    break;
                case ShiftChangeResponseObject.SHIFTCHANGESTATUS_SWITCH_SUGGESTED:
                    ResponseDrawable = ContextCompat.getDrawable(context, R.drawable.round_switch_icon);
                    ResponseDrawable.setTint(ContextCompat.getColor(context, R.color.StatusPending));
                    break;
                case ShiftChangeResponseObject.SHIFTCHANGESTATUS_SWITCHED:
                    ResponseDrawable = ContextCompat.getDrawable(context, R.drawable.round_switch_icon);
                    ResponseDrawable.setTint(ContextCompat.getColor(context, R.color.StatusAccepted));
                    break;
                case ShiftChangeResponseObject.SHIFTCHANGESTATUS_ACCEPTED:
                    ResponseDrawable = ContextCompat.getDrawable(context, R.drawable.sharp_check_circle_outline_24);
                    ResponseDrawable.setTint(ContextCompat.getColor(context, R.color.StatusAccepted));
                    break;
                case ShiftChangeResponseObject.SHIFTCHANGESTATUS_DECLINED:
                    ResponseDrawable = ContextCompat.getDrawable(context, R.drawable.sharp_highlight_off_24);
                    ResponseDrawable.setTint(ContextCompat.getColor(context, R.color.StatusDenied));
                    break;
            }
        }
        return ResponseDrawable;
    }
}
