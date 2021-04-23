package com.example.coopapp20.Contact;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.coopapp20.Contact.Contact.ContactObject;
import com.example.coopapp20.Contact.Contact.ContactUserObject;
import com.example.coopapp20.Data.ConnectionCommandObject;
import com.example.coopapp20.Data.Dao.DatabaseChangeDao;
import com.example.coopapp20.Data.Dao.DepartmentDao;
import com.example.coopapp20.Data.Dao.MessageDao;
import com.example.coopapp20.Data.Dao.UserDao;
import com.example.coopapp20.Data.Objects.DatabaseChangeObject;
import com.example.coopapp20.Data.Objects.DepartmentObject;
import com.example.coopapp20.Data.Objects.MessageObject;
import com.example.coopapp20.Data.Objects.UserObject;
import com.example.coopapp20.Data.RoomDatabase;

import java.util.List;
import java.util.stream.Collectors;

public class ContactRepository {

    private DatabaseChangeDao databaseChangeDao;
    private UserDao userDao;
    private DepartmentDao departmentDao;
    private MessageDao messageDao;

    private LiveData<List<DepartmentObject>> AllDepartments;
    private LiveData<List<UserObject>> AllUsers;
    private LiveData<List<MessageObject>> AllMessages;

    public ContactRepository(Application application){
        RoomDatabase db = RoomDatabase.getDatabase(application);

        databaseChangeDao = db.databaseChangeDao();
        userDao = db.userDao();
        departmentDao = db.departmentDao();
        messageDao = db.messageDao();

        AllUsers = userDao.getAll();
        AllDepartments = departmentDao.getAll();
        AllMessages = messageDao.getAll();
    }


    LiveData<List<DepartmentObject>> getAllDepartments() { return AllDepartments;}
    List<UserObject> getDepartmentUsersClean(Integer DepartmentId){return userDao.getByDepartmentClean(DepartmentId);}
    public LiveData<List<UserObject>> getAllUsers() { return AllUsers; }
    LiveData<List<MessageObject>> getAllMessages() { return AllMessages; }
    List<ContactUserObject> getContactUserObjects(Integer UserId){return userDao.getContactUsersByIdClean(UserId);}

    LiveData<List<MessageObject>> getUserInteractions(UserObject user){return messageDao.getUserInterActions(user.getId(), user.getDepartmentId());}
    LiveData<List<MessageObject>> getContactMessages(ContactObject contact, Integer CurrentUserId){
        if(contact.getDataObject() instanceof DepartmentObject){return messageDao.getByDepartment(((DepartmentObject) contact.getDataObject()).getId());}
        else if(contact.getDataObject() instanceof UserObject){ return messageDao.getByUsers(CurrentUserId, ((UserObject) contact.getDataObject()).getId()); }
        return null;
    }

    void AddMessage(MessageObject message){
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            int id = (int) messageDao.insert(message);
            message.setId(id);
            databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Message(ConnectionCommandObject.Add,message).getCommand()));
        });
    }
    void UpdateMessage(MessageObject message){
        RoomDatabase.databaseWriteExecutor.execute(()-> messageDao.update(message));
        RoomDatabase.databaseWriteExecutor.execute(()->databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Message(ConnectionCommandObject.Edit,message).getCommand())));
    }
}
