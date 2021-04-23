package com.example.coopapp20.Data.Objects;

import android.graphics.drawable.Drawable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.coopapp20.Contact.Contact.ContactObject;

import java.time.LocalDateTime;
import java.util.List;

@Entity(tableName = "Messages", ignoredColumns = {"MessageDateTimeString","MessageName","MessageBGColor","InteractionDateTimeString","InteractionName","InteractionBGColor","TypeImage","Divider","Contact"})
public class MessageObject {

    @PrimaryKey(autoGenerate = true)
    private Integer Id;

    public static final int TYPE_PRIVATE_P2P_MESSAGE = 0;
    public static final int TYPE_PRIVATE_GROUP_MESSAGE = 1;
    public static final int TYPE_PRIVATE_CALL = 2;
    public static final int TYPE_PROFESSIONAL_P2P_MESSAGE = 3;
    public static final int TYPE_PROFESSIONAL_GROUP_MESSAGE = 4;
    public static final int TYPE_PROFESSIONAL_CALL = 5;


    private Integer Sender;
    private Integer Receiver;
    private LocalDateTime DateTime;
    private List<Integer> MissingViews;
    private Integer Type;
    private String Message;

    public MessageObject(Integer Id, Integer Sender, Integer Receiver, LocalDateTime DateTime, List<Integer> MissingViews, Integer Type, String Message){
        this.Id = Id;
        this.Sender = Sender;
        this.Receiver = Receiver;
        this.DateTime = DateTime;
        this.MissingViews = MissingViews;
        this.Type = Type;
        this.Message = Message;
    }
    public void setViewed(List<Integer> MissingViews){this.MissingViews = MissingViews;}
    public void setId(Integer id) { Id = id; }

    public Integer getId() {
        return Id;
    }
    public Integer getSender() {
        return Sender;
    }
    public Integer getReceiver() {
        return Receiver;
    }
    public LocalDateTime getDateTime() {
        return DateTime;
    }
    public List<Integer> getMissingViews() {
        return MissingViews;
    }
    public Integer getType() {
        return Type;
    }
    public String getMessage() {
        return Message;
    }


    private String MessageDateTimeString;
    private String MessageName;
    private Integer MessageBGColor;
    private String InteractionDateTimeString;
    private String InteractionName;
    private Integer InteractionBGColor;
    private Drawable TypeImage;
    private Boolean Divider;
    private ContactObject Contact;

    public void setInteractionDateTimeString(String interactionDateTimeString) { InteractionDateTimeString = interactionDateTimeString; }
    public void setInteractionName(String interactionName) { InteractionName = interactionName; }
    public void setInteractionBGColor(Integer interactionBGColor) { InteractionBGColor = interactionBGColor; }
    public void setTypeImage(Drawable typeImage) { TypeImage = typeImage; }
    public void setDivider(Boolean divider) { Divider = divider; }
    public void setContact(ContactObject contact) { Contact = contact; }
    public void setMessageDateTimeString(String messageDateTimeString) { MessageDateTimeString = messageDateTimeString; }
    public void setMessageName(String messageName) { MessageName = messageName; }
    public void setMessageBGColor(Integer messageBGColor) { MessageBGColor = messageBGColor;}

    public String getMessageDateTimeString() { return MessageDateTimeString; }
    public String getMessageName() { return MessageName; }
    public Integer getMessageBGColor() { return MessageBGColor; }
    public String getInteractionDateTimeString() { return InteractionDateTimeString; }
    public String getInteractionName() { return InteractionName; }
    public Integer getInteractionBGColor() { return InteractionBGColor; }
    public Boolean getDivider() { return Divider; }
    public ContactObject getContact() { return Contact; }
    public Drawable getTypeImage() { return TypeImage; }
}
