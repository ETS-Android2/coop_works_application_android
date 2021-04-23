package com.example.coopapp20.Data.Objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "ShiftChangeResponses")
public class ShiftChangeResponseObject {

    public static final int SHIFTCHANGESTATUS_DECLINED = 0;
    public static final int SHIFTCHANGESTATUS_PENDING = 1;
    public static final int SHIFTCHANGESTATUS_SWITCH_SUGGESTED = 2;
    public static final int SHIFTCHANGESTATUS_SWITCHED = 3;
    public static final int SHIFTCHANGESTATUS_ACCEPTED = 4;

    @PrimaryKey(autoGenerate = true)
    private Integer Id;

    private Integer Status;
    private Integer ShiftChangeRequestId;
    private List<Integer> ShiftIdList;
    private Integer ResponderId;

    public ShiftChangeResponseObject(Integer Id, Integer Status, Integer ShiftChangeRequestId, List<Integer> ShiftIdList, Integer ResponderId){
        this.Id = Id;
        this.Status = Status;
        this.ShiftChangeRequestId = ShiftChangeRequestId;
        this.ShiftIdList = ShiftIdList;
        this.ResponderId = ResponderId;
    }

    public void setId(Integer id) { Id = id; }
    public ShiftChangeResponseObject setStatus(Integer status) { Status = status; return this;}
    public ShiftChangeResponseObject setShiftIdList(List<Integer> shiftIdList) { ShiftIdList = shiftIdList; return this;}

    public Integer getId() { return Id; }
    public Integer getStatus() { return Status; }
    public Integer getShiftChangeRequestId() { return ShiftChangeRequestId; }
    public List<Integer> getShiftIdList() { return ShiftIdList; }
    public Integer getResponderId() { return ResponderId; }

}
