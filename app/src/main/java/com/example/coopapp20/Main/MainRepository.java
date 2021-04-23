package com.example.coopapp20.Main;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.coopapp20.Data.ConnectionCommandObject;
import com.example.coopapp20.Data.Dao.ActiveShiftDao;
import com.example.coopapp20.Data.Dao.DatabaseChangeDao;
import com.example.coopapp20.Data.Dao.DepartmentDao;
import com.example.coopapp20.Data.Dao.FinishedTaskDao;
import com.example.coopapp20.Data.Dao.MessageDao;
import com.example.coopapp20.Data.Dao.OffDayRequestDao;
import com.example.coopapp20.Data.Dao.ProductChangeDao;
import com.example.coopapp20.Data.Dao.ProductDao;
import com.example.coopapp20.Data.Dao.SchedulePreferenceDao;
import com.example.coopapp20.Data.Dao.ScheduleValueDao;
import com.example.coopapp20.Data.Dao.ShiftChangeRequestDao;
import com.example.coopapp20.Data.Dao.ShiftChangeResponseDao;
import com.example.coopapp20.Data.Dao.ShiftDao;
import com.example.coopapp20.Data.Dao.ShiftPreferenceDao;
import com.example.coopapp20.Data.Dao.TaskDao;
import com.example.coopapp20.Data.Dao.TaskSortingValueDao;
import com.example.coopapp20.Data.Dao.UserDao;
import com.example.coopapp20.Data.Objects.ActiveShiftObject;
import com.example.coopapp20.Data.Objects.DatabaseChangeObject;
import com.example.coopapp20.Data.Objects.DepartmentObject;
import com.example.coopapp20.Data.Objects.FinishedTaskObject;
import com.example.coopapp20.Data.Objects.MessageObject;
import com.example.coopapp20.Data.Objects.OffDayRequestObject;
import com.example.coopapp20.Data.Objects.SchedulePreferenceObject;
import com.example.coopapp20.Data.Objects.ScheduleValueObject;
import com.example.coopapp20.Data.Objects.ShiftChangeRequestObject;
import com.example.coopapp20.Data.Objects.ShiftChangeResponseObject;
import com.example.coopapp20.Data.Objects.ShiftObject;
import com.example.coopapp20.Data.Objects.ShiftPreferenceObject;
import com.example.coopapp20.Data.Objects.TaskObject;
import com.example.coopapp20.Data.Objects.TaskSortingValueObject;
import com.example.coopapp20.Data.Objects.UserObject;
import com.example.coopapp20.Data.RoomDatabase;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainRepository {

    private DatabaseChangeDao databaseChangeDao;
    private UserDao userDao;
    private DepartmentDao departmentDao;
    private MessageDao messageDao;
    private ProductDao productDao;
    private ProductChangeDao productChangeDao;
    private TaskDao taskDao;
    private FinishedTaskDao finishedTaskDao;
    private ShiftDao shiftDao;
    private ActiveShiftDao activeShiftDao;
    private ShiftChangeRequestDao shiftChangeRequestDao;
    private ShiftChangeResponseDao shiftChangeResponseDao;
    private SchedulePreferenceDao schedulePreferenceDao;
    private ShiftPreferenceDao shiftPreferenceDao;
    private OffDayRequestDao offDayRequestDao;
    private ScheduleValueDao scheduleValueDao;
    private TaskSortingValueDao taskSortingValueDao;

    private LiveData<List<DatabaseChangeObject>> AllDatabaseChanges;
    private LiveData<List<UserObject>> AllUsers;
    private LiveData<List<DepartmentObject>> AllDepartments;

    public MainRepository(Application application){
        RoomDatabase db = RoomDatabase.getDatabase(application);
        databaseChangeDao = db.databaseChangeDao();
        userDao = db.userDao();
        departmentDao = db.departmentDao();
        messageDao = db.messageDao();
        productDao = db.productDao();
        productChangeDao = db.productChangeDao();
        taskDao = db.taskDao();
        finishedTaskDao = db.finishedTaskDao();
        shiftDao = db.shiftDao();
        activeShiftDao = db.activeShiftDao();
        shiftChangeRequestDao = db.shiftChangeRequestDao();
        shiftChangeResponseDao = db.shiftChangeResponseDao();
        schedulePreferenceDao = db.schedulePreferenceDao();
        shiftPreferenceDao = db.shiftPreferenceDao();
        offDayRequestDao = db.offDayRequestDao();
        scheduleValueDao = db.scheduleValueDao();
        taskSortingValueDao = db.taskSortingValueDao();

        AllDatabaseChanges = databaseChangeDao.getAll();
    }

    public LiveData<List<UserObject>> getAllUsers(){
        if(AllUsers == null){AllUsers = userDao.getAll();}
        return AllUsers;
    }
    public LiveData<List<DepartmentObject>> getAllDepartments(){
        if(AllDepartments == null){AllDepartments = departmentDao.getAll();}
        return AllDepartments;}
    public LiveData<UserObject> getUser(Integer userId){return userDao.getUser(userId);}
    public LiveData<UserObject> getUserByCredentials(String Username, String Password){return userDao.getUserByCredentials(Username,Password);}
    public LiveData<ActiveShiftObject> getActiveShift(Integer UserId){return activeShiftDao.getCurrentActiveShift(UserId,LocalDate.now());}
    public LiveData<List<TaskObject>> getDepartmentTasks(Integer DepartmentId){return taskDao.getByDepartment(DepartmentId);}
    public LiveData<List<FinishedTaskObject>> getFinishedTasksOfToday(Integer UserId){return finishedTaskDao.getByUser(UserId);}
    public LiveData<List<TaskObject>> getCurrentTasks(Integer DepartmentId){return taskDao.getCurrentUserTasks(DepartmentId, LocalDate.now());}
    public LiveData<List<FinishedTaskObject>> getCurrentFinishedTasks(Integer UserId){return finishedTaskDao.getByUserDate(UserId, LocalDate.now());}
    public LiveData<List<DatabaseChangeObject>> getDatabaseChanges(){return AllDatabaseChanges;}
    public void ClearDatabaseChanges(){databaseChangeDao.deleteAll();}

    public ShiftChangeResponseObject getShiftChangeResponseClean(int UserId, int RequestId){return shiftChangeResponseDao.getByUserRequestClean(UserId, RequestId);}
    public TaskObject getTaskClean(int TaskId){return taskDao.getClean(TaskId);}

    public int AddDepartmentClean(DepartmentObject department){
        int departmentId = (int) departmentDao.insert(department);

        //TASKS
        ScheduleValueObject scheduleValueObject = new ScheduleValueObject(null,null,null,null,4,10,30,50,100,100,false, departmentId);
        TaskSortingValueObject taskSortingValueObject = new TaskSortingValueObject(null,null,null,null,null,null,null,null, departmentId);
        int scheduleValueId = (int) scheduleValueDao.insert(scheduleValueObject);
        int taskSortingValueId = (int) taskSortingValueDao.insert(taskSortingValueObject);

        department.setId(departmentId);
        scheduleValueObject.setId(scheduleValueId);
        taskSortingValueObject.setId(taskSortingValueId);

        databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Department(ConnectionCommandObject.Add,department).getCommand()));
        databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ScheduleValues(ConnectionCommandObject.Add, scheduleValueObject).getCommand()));
        databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().TaskSortingValues(ConnectionCommandObject.Add, taskSortingValueObject).getCommand()));

        return departmentId;
    }
    public int AddUserClean(UserObject user){
        //Add user
        int userId = (int) userDao.insert(user);

        //Add additional user data
        SchedulePreferenceObject schedulePref = new SchedulePreferenceObject(null,5,7,userId);
        int schedulePrefId = (int) schedulePreferenceDao.insert(schedulePref);

        for(ShiftObject shift : shiftDao.getByDepartmentClean(user.getDepartmentId())){
            ShiftPreferenceObject shiftPref = new ShiftPreferenceObject(null,shift.getDay(),shift.getStartTime(), shift.getEndTime(),ShiftPreferenceObject.VALUE_INDIFFERENT,shift.getId(),userId);
            int ShiftPrefId = (int) shiftPreferenceDao.insert(shiftPref);
            shiftPref.setId(ShiftPrefId);
            databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ShiftPreference(ConnectionCommandObject.Add,shiftPref).getCommand()));
        }

        for(ShiftChangeRequestObject request : shiftChangeRequestDao.getByReceiverClean(userId)){
            ShiftChangeResponseObject response = new ShiftChangeResponseObject(null,ShiftChangeResponseObject.SHIFTCHANGESTATUS_PENDING,request.getId(),null,userId);
            int ResponseId = (int) shiftChangeResponseDao.insert(response);
            response.setId(ResponseId);
            //databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Response(ConnectionCommandObject.Add,response).getCommand()));
        }

        //Set assigned id to data objects
        user.setId(userId);
        schedulePref.setId(schedulePrefId);

        //add data objects to DataChanges
        databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().User(ConnectionCommandObject.Add,user).getCommand()));
        databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().SchedulePreference(ConnectionCommandObject.Add,schedulePref).getCommand()));

        return userId;
    }
    public int AddTaskClean(TaskObject task){
        int taskId = (int) taskDao.insert(task);
        task.setId(taskId);
        databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Task(ConnectionCommandObject.Add,task).getCommand()));
        return taskId;
    }
    public int AddShiftClean(ShiftObject shift){
        int shiftId = (int) shiftDao.insert(shift);

        for(UserObject user : userDao.getByDepartmentClean(shift.getDepartmentId())){
            ShiftPreferenceObject preferences = new ShiftPreferenceObject(null,shift.getDay(), shift.getStartTime(), shift.getEndTime(), ShiftPreferenceObject.VALUE_INDIFFERENT, shiftId, user.getId());
            int preferenceId = (int) shiftPreferenceDao.insert(preferences);
            preferences.setId(preferenceId);
            databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ShiftPreference(ConnectionCommandObject.Add,preferences).getCommand()));
        }

        shift.setId(shiftId);
        databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Shift(ConnectionCommandObject.Add,shift).getCommand()));
        return shiftId;
    }
    public int AddActiveShiftClean(ActiveShiftObject activeShift){
        int activeShiftId = (int) activeShiftDao.insert(activeShift);
        activeShift.setId(activeShiftId);
        databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ActiveShift(ConnectionCommandObject.Add, activeShift).getCommand()));
        return activeShiftId;
    }
    public int AddShiftChangeRequestClean(ShiftChangeRequestObject request){
        int requestId = (int) shiftChangeRequestDao.insert(request);
        request.setId(requestId);

        userDao.getUserIdByShiftChangeRequestClean(requestId).forEach(ReceiverId -> {
            ShiftChangeResponseObject response = new ShiftChangeResponseObject(null,ShiftChangeResponseObject.SHIFTCHANGESTATUS_PENDING,request.getId(), Collections.emptyList(),ReceiverId);
            int ResponseId = (int) shiftChangeResponseDao.insert(response);
            //databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Response(ConnectionCommandObject.Add,response).getCommand()));
        });

        return requestId;
    }
    public int AddOffDayRequestClean(OffDayRequestObject request){
        int requestId = (int) offDayRequestDao.insert(request);
        request.setId(requestId);
        return requestId;
    }
    public int AddFinishedTaskClean(FinishedTaskObject finishedTask){
        int finishedTaskId = (int) finishedTaskDao.insert(finishedTask);
        finishedTask.setId(finishedTaskId);
        return finishedTaskId;
    }
    public int AddMessageClean(MessageObject message){
        int messageId = (int) messageDao.insert(message);
        message.setId(messageId);
        return messageId;
    }

    public void EditShiftChangeResponse(ShiftChangeResponseObject reponse){
        shiftChangeResponseDao.update(reponse);
    }


    public void UpdateUser(UserObject user){
        userDao.update(user);
        RoomDatabase.databaseWriteExecutor.execute(()->databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().User(ConnectionCommandObject.Edit,user).getCommand())));
    }

    public void ClearDatabaseClean(){
        userDao.deleteAll();
        departmentDao.deleteAll();
        schedulePreferenceDao.deleteAll();
        shiftPreferenceDao.deleteAll();
        offDayRequestDao.deleteAll();
        shiftChangeRequestDao.deleteAll();
        shiftChangeResponseDao.deleteAll();
        taskDao.deleteAll();
        finishedTaskDao.deleteAll();
        shiftDao.deleteAll();
        activeShiftDao.deleteAll();
        productDao.deleteAll();
        productChangeDao.deleteAll();
        messageDao.deleteAll();
        scheduleValueDao.deleteAll();
        taskSortingValueDao.deleteAll();
    }
}
