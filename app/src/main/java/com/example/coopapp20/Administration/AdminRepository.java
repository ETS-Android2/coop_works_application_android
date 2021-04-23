package com.example.coopapp20.Administration;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.coopapp20.Data.ConnectionCommandObject;
import com.example.coopapp20.Data.Dao.ActiveShiftDao;
import com.example.coopapp20.Data.Dao.DatabaseChangeDao;
import com.example.coopapp20.Data.Dao.DepartmentDao;
import com.example.coopapp20.Data.Dao.MessageDao;
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
import com.example.coopapp20.Data.Objects.MessageObject;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class AdminRepository {

    private DatabaseChangeDao databaseChangeDao;
    private DepartmentDao departmentDao;
    private ScheduleValueDao scheduleValueDao;
    private TaskSortingValueDao taskSortingValueDao;
    private UserDao userDao;
    private SchedulePreferenceDao schedulePreferenceDao;
    private ShiftChangeResponseDao shiftChangeResponseDao;
    private ShiftChangeRequestDao shiftChangeRequestDao;
    private ShiftPreferenceDao shiftPreferenceDao;
    private ShiftDao shiftDao;
    private TaskDao taskDao;
    private MessageDao messageDao;
    private ActiveShiftDao activeShiftDao;

    AdminRepository(Application application){
        RoomDatabase db = RoomDatabase.getDatabase(application);

        databaseChangeDao = db.databaseChangeDao();
        departmentDao = db.departmentDao();
        scheduleValueDao = db.scheduleValueDao();
        taskSortingValueDao = db.taskSortingValueDao();
        userDao = db.userDao();
        schedulePreferenceDao = db.schedulePreferenceDao();
        shiftChangeResponseDao = db.shiftChangeResponseDao();
        shiftChangeRequestDao = db.shiftChangeRequestDao();
        shiftPreferenceDao = db.shiftPreferenceDao();
        shiftDao = db.shiftDao();
        taskDao = db.taskDao();
        messageDao = db.messageDao();
        activeShiftDao = db.activeShiftDao();
    }

    LiveData<List<DepartmentObject>> getAllDepartments(){return departmentDao.getAll();}
    LiveData<List<UserObject>> getAllUsers(){return userDao.getAll();}
    LiveData<List<TaskObject>> getAllRepeatTasks(){return taskDao.getByRepeat(true);}
    LiveData<List<ShiftObject>> getAllShifts(){return shiftDao.getAll();}
    LiveData<ScheduleValueObject> getDepartmentScheduleValues(Integer DepartmentId){return scheduleValueDao.getByDepartment(DepartmentId);}

    List<UserObject> getDepartmentUsers(int DepartmentId){return userDao.getByDepartmentClean(DepartmentId);}
    List<TaskObject> getDepartmentTasks(int DepartmentId){return taskDao.getByDepartmentClean(DepartmentId);}
    List<ShiftObject> getDepartmentShifts(int DepartmentId){return shiftDao.getByDepartmentClean(DepartmentId);}

    List<ActiveShiftObject> getUserActiveShifts(int UserId){return activeShiftDao.getByUserIdAndAfterClean(UserId, LocalDate.now());}

    void AddDepartment(DepartmentObject department){
        RoomDatabase.databaseWriteExecutor.execute(()->{
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
        });
    }
    void AddUser(UserObject user){
        RoomDatabase.databaseWriteExecutor.execute(()->{
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
        });
    }
    void AddTask(TaskObject task){
        RoomDatabase.databaseWriteExecutor.execute(()->{
            int taskId = (int) taskDao.insert(task);
            task.setId(taskId);
            databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Task(ConnectionCommandObject.Add,task).getCommand()));
        });
    }
    void AddShift(ShiftObject shift){
        RoomDatabase.databaseWriteExecutor.execute(()->{
            int shiftId = (int) shiftDao.insert(shift);

            for(UserObject user : userDao.getByDepartmentClean(shift.getDepartmentId())){
                ShiftPreferenceObject preferences = new ShiftPreferenceObject(null,shift.getDay(), shift.getStartTime(), shift.getEndTime(), ShiftPreferenceObject.VALUE_INDIFFERENT, shiftId, user.getId());
                int preferenceId = (int) shiftPreferenceDao.insert(preferences);
                preferences.setId(preferenceId);
                databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ShiftPreference(ConnectionCommandObject.Add,preferences).getCommand()));
            }

            shift.setId(shiftId);
            databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Shift(ConnectionCommandObject.Add,shift).getCommand()));
        });
    }

    void EditDepartment(DepartmentObject department){
        RoomDatabase.databaseWriteExecutor.execute(()->{
            departmentDao.update(department);
            databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Department(ConnectionCommandObject.Edit,department).getCommand()));
        });
    }
    void EditUser(UserObject user){
        RoomDatabase.databaseWriteExecutor.execute(()->{
            if(userDao.getUserClean(user.getId()).getDepartmentId().equals(user.getDepartmentId())){
                RoomDatabase.databaseWriteExecutor.execute(()->{
                    userDao.update(user);
                    databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().User(ConnectionCommandObject.Edit,user).getCommand()));
                });
            }else {
                userDao.update(user);
                databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().User(ConnectionCommandObject.Edit,user).getCommand()));

                //Delete users ShiftChangeRequests and the requests corresponding responses
                for(ShiftChangeRequestObject request : shiftChangeRequestDao.getBySenderClean(user.getId())){
                    shiftChangeRequestDao.delete(request);
                    //databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ShiftChangeRequest(ConnectionCommandObject.Delete,request).getCommand()));
                    for(ShiftChangeResponseObject response : shiftChangeResponseDao.getByRequestClean(request.getId())){
                        shiftChangeResponseDao.delete(response);
                        //databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ShiftChangeResponse(ConnectionCommandObject.Delete,response).getCommand()));
                    }
                }

                //Delete users response to ShiftChangeRequests in old department
                for(ShiftChangeResponseObject response : shiftChangeResponseDao.getByUserClean(user.getId())){
                    shiftChangeResponseDao.delete(response);
                    //databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ShiftChangeResponse(ConnectionCommandObject.Delete,response).getCommand()));
                }

                //create user response for all ShiftChangeRequests in new department
                ArrayList<ShiftChangeRequestObject> requests = new ArrayList<>();
                for(UserObject user1 : userDao.getByDepartmentClean(user.getDepartmentId())){
                    requests.addAll(shiftChangeRequestDao.getBySenderClean(user1.getId()));
                }
                for(ShiftChangeRequestObject request : requests){
                    ShiftChangeResponseObject response = new ShiftChangeResponseObject(null,ShiftChangeResponseObject.SHIFTCHANGESTATUS_PENDING,request.getId(),null,user.getId());
                    shiftChangeResponseDao.insert(response);
                    //databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ShiftChangeResponse(ConnectionCommandObject.Add,response).getCommand()));
                }
                for(ShiftChangeResponseObject response : shiftChangeResponseDao.getByUserClean(user.getId())){
                    shiftChangeResponseDao.delete(response);
                    //databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ShiftChangeResponse(ConnectionCommandObject.Delete,response).getCommand()));
                }

                //Delete users ShiftPreferences for shifts in old department
                for(ShiftPreferenceObject preference : shiftPreferenceDao.getByUserClean(user.getId())){
                    shiftPreferenceDao.delete(preference);
                    databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ShiftPreference(ConnectionCommandObject.Delete,preference).getCommand()));
                }

                //Create user ShiftPreference for shift in new department
                for(ShiftObject shift : shiftDao.getByDepartmentClean(user.getDepartmentId())){
                    ShiftPreferenceObject preference = new ShiftPreferenceObject(null,shift.getDay(),shift.getStartTime(), shift.getEndTime(),ShiftPreferenceObject.VALUE_INDIFFERENT,shift.getId(),user.getId());
                    shiftPreferenceDao.insert(preference);
                    databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ShiftPreference(ConnectionCommandObject.Add,preference).getCommand()));
                }
            }
        });
    }
    void EditTask(TaskObject task){
        RoomDatabase.databaseWriteExecutor.execute(()->{
            taskDao.update(task);
            databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Task(ConnectionCommandObject.Edit,task).getCommand()));
        });
    }
    void EditShift(ShiftObject shift){
        RoomDatabase.databaseWriteExecutor.execute(()->{
            shiftDao.update(shift);
            Log.e("AdminRespository","EditShift - "+shift.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)));

            for(ShiftPreferenceObject preferences : shiftPreferenceDao.getByShiftClean(shift.getId())){
                preferences.setDay(shift.getDay());
                preferences.setStartTime(shift.getStartTime());
                preferences.setEndTime(shift.getEndTime());
                shiftPreferenceDao.update(preferences);
                databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ShiftPreference(ConnectionCommandObject.Edit,preferences).getCommand()));
            }

            databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Shift(ConnectionCommandObject.Edit,shift).getCommand()));
        });
    }
    void EditScheduleValues(ScheduleValueObject scheduleValues){
        RoomDatabase.databaseWriteExecutor.execute(()->{
            scheduleValueDao.update(scheduleValues);
            databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ScheduleValues(ConnectionCommandObject.Edit, scheduleValues).getCommand()));
        });
    }

    void DeleteDepartment(DepartmentObject department){
        RoomDatabase.databaseWriteExecutor.execute(()->{
            departmentDao.delete(department);
            databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Department(ConnectionCommandObject.Delete,department).getCommand()));

            for(MessageObject message : messageDao.getByDepartmentClean(department.getId())){
                messageDao.delete(message);
                databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Message(ConnectionCommandObject.Delete,message).getCommand()));
            }

            ScheduleValueObject scheduleValue = scheduleValueDao.getByDepartmentClean(department.getId());
            scheduleValueDao.delete(scheduleValue);
            databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ScheduleValues(ConnectionCommandObject.Delete,scheduleValue).getCommand()));

            TaskSortingValueObject taskSortingValue = taskSortingValueDao.getByDepartmentClean(department.getId());
            taskSortingValueDao.delete(taskSortingValue);
            databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().TaskSortingValues(ConnectionCommandObject.Delete,taskSortingValue).getCommand()));

        });
    }
    void DeleteUser(UserObject user){
        RoomDatabase.databaseWriteExecutor.execute(()->{
            if(activeShiftDao.getByUserClean(user.getId()).size() == 0) {
                userDao.delete(user);

                SchedulePreferenceObject preferences = schedulePreferenceDao.getByUserClean(user.getId());
                schedulePreferenceDao.delete(preferences);

                databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().User(ConnectionCommandObject.Delete, user).getCommand()));
                databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().SchedulePreference(ConnectionCommandObject.Delete, preferences).getCommand()));

                for (MessageObject message : messageDao.getByUserClean(user.getId())) {
                    messageDao.delete(message);
                    databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Message(ConnectionCommandObject.Delete, message).getCommand()));
                }

                for (ShiftChangeRequestObject request : shiftChangeRequestDao.getBySenderClean(user.getId())) {
                    shiftChangeRequestDao.delete(request);
                    //databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ShiftChangeRequest(ConnectionCommandObject.Delete,request).getCommand()));
                    for (ShiftChangeResponseObject response : shiftChangeResponseDao.getByRequestClean(request.getId())) {
                        shiftChangeResponseDao.delete(response);
                        //databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ShiftChangeResponse(ConnectionCommandObject.Delete,response).getCommand()));
                    }
                }

                for (ShiftChangeResponseObject response : shiftChangeResponseDao.getByUserClean(user.getId())) {
                    shiftChangeResponseDao.delete(response);
                    //databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ShiftChangeResponse(ConnectionCommandObject.Delete,response).getCommand()));
                }

                for (ShiftPreferenceObject preference : shiftPreferenceDao.getByUserClean(user.getId())) {
                    shiftPreferenceDao.delete(preference);
                    databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ShiftPreference(ConnectionCommandObject.Delete, preference).getCommand()));
                }
            }
        });
    }
    void DeleteTask(TaskObject task){
        RoomDatabase.databaseWriteExecutor.execute(()->{
            taskDao.delete(task);
            databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Task(ConnectionCommandObject.Delete,task).getCommand()));
        });
    }
    void DeleteShift(ShiftObject shift){
        RoomDatabase.databaseWriteExecutor.execute(()->{
            shiftDao.delete(shift);

            for(ShiftPreferenceObject preferences : shiftPreferenceDao.getByShiftClean(shift.getId())){
                shiftPreferenceDao.delete(preferences);
                databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ShiftPreference(ConnectionCommandObject.Delete,preferences).getCommand()));
            }

            databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Shift(ConnectionCommandObject.Delete,shift).getCommand()));
        });
    }

}
