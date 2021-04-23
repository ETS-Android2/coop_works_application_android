package com.example.coopapp20.Data.Objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ShiftChangeRequests")
public class ShiftChangeRequestObject {

    public static final int SHIFTCHANGESTATUS_DECLINED_AND_ENDED = 0;
    public static final int SHIFTCHANGESTATUS_DECLINED = 1;
    public static final int SHIFTCHANGESTATUS_PENDING = 2;
    public static final int SHIFTCHANGESTATUS_SWITCH_SUGGESTED = 3;
    public static final int SHIFTCHANGESTATUS_SWITCHED = 4;
    public static final int SHIFTCHANGESTATUS_ACCEPTED = 5;

    public static final int NECESSITY_WANTED = 0;
    public static final int NECESSITY_NEEDED = 1;

    @PrimaryKey(autoGenerate = true)
    private Integer Id;

    private Integer ActiveShiftId;
    private Integer SenderId;
    private Integer Necessity;
    private String Comment;
    private Integer Status;

    public ShiftChangeRequestObject(Integer Id, Integer ActiveShiftId, Integer SenderId, Integer Necessity, String Comment, Integer Status){
        this.Id = Id;
        this.ActiveShiftId = ActiveShiftId;
        this.SenderId = SenderId;
        this.Necessity = Necessity;
        this.Comment = Comment;
        this.Status = Status;
    }

    public void setId(Integer id) { Id = id; }

    public void setNecessity(Integer necessity) { Necessity = necessity; }
    public void setComment(String comment) { Comment = comment; }
    public ShiftChangeRequestObject setStatus(Integer status) { Status = status; return this;}

    public Integer getId() { return Id; }
    public Integer getActiveShiftId() { return ActiveShiftId; }
    public Integer getSenderId() { return SenderId; }
    public Integer getNecessity() { return Necessity; }
    public String getComment() { return Comment; }
    public Integer getStatus() { return Status; }

}