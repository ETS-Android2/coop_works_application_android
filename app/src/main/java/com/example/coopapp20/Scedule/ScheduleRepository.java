package com.example.coopapp20.Scedule;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.coopapp20.Data.ConnectionCommandObject;
import com.example.coopapp20.Data.Dao.ActiveShiftDao;
import com.example.coopapp20.Data.Dao.DatabaseChangeDao;
import com.example.coopapp20.Data.Dao.DepartmentDao;
import com.example.coopapp20.Data.Dao.OffDayRequestDao;
import com.example.coopapp20.Data.Dao.SchedulePreferenceDao;
import com.example.coopapp20.Data.Dao.ScheduleValueDao;
import com.example.coopapp20.Data.Dao.ShiftChangeRequestDao;
import com.example.coopapp20.Data.Dao.ShiftChangeResponseDao;
import com.example.coopapp20.Data.Dao.ShiftDao;
import com.example.coopapp20.Data.Dao.ShiftPreferenceDao;
import com.example.coopapp20.Data.Dao.UserDao;
import com.example.coopapp20.Data.Objects.ActiveShiftObject;
import com.example.coopapp20.Data.Objects.DatabaseChangeObject;
import com.example.coopapp20.Data.Objects.DepartmentObject;
import com.example.coopapp20.Data.Objects.OffDayRequestObject;
import com.example.coopapp20.Data.Objects.SchedulePreferenceObject;
import com.example.coopapp20.Data.Objects.ScheduleValueObject;
import com.example.coopapp20.Data.Objects.ShiftChangeRequestObject;
import com.example.coopapp20.Data.Objects.ShiftChangeResponseObject;
import com.example.coopapp20.Data.Objects.ShiftObject;
import com.example.coopapp20.Data.Objects.ShiftPreferenceObject;
import com.example.coopapp20.Data.Objects.UserObject;
import com.example.coopapp20.Data.RoomDatabase;
import com.example.coopapp20.Scedule.Schedule.ScheduleActiveShiftObject;
import com.example.coopapp20.Scedule.SchedulePref.ScheduleOffDayRequestObject;
import com.example.coopapp20.Scedule.SchedulePref.ScheduleShiftPreferenceObject;
import com.example.coopapp20.Scedule.ScheduleShiftChange.ScheduleShiftChangeRequestObject;
import com.example.coopapp20.Scedule.ScheduleShiftChange.ScheduleShiftChangeResponseObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ScheduleRepository {

    private DatabaseChangeDao databaseChangeDao;
    private UserDao userDao;
    private DepartmentDao departmentDao;
    private ActiveShiftDao activeShiftDao;
    private ShiftChangeRequestDao shiftChangeRequestDao;
    private ShiftChangeResponseDao shiftChangeResponseDao;
    private ScheduleValueDao scheduleValueDao;
    private ShiftPreferenceDao shiftPreferenceDao;
    private SchedulePreferenceDao schedulePreferenceDao;
    private OffDayRequestDao offDayRequestDao;

    public ScheduleRepository(Application application){
        RoomDatabase db = RoomDatabase.getDatabase(application);

        databaseChangeDao = db.databaseChangeDao();
        userDao = db.userDao();
        departmentDao = db.departmentDao();
        activeShiftDao = db.activeShiftDao();
        shiftChangeRequestDao = db.shiftChangeRequestDao();
        shiftChangeResponseDao = db.shiftChangeResponseDao();
        scheduleValueDao = db.scheduleValueDao();
        shiftPreferenceDao = db.shiftPreferenceDao();
        schedulePreferenceDao = db.schedulePreferenceDao();
        offDayRequestDao = db.offDayRequestDao();
    }

    public LiveData<List<DepartmentObject>> getAllDepartments(){
        return departmentDao.getAll();}
    public LiveData<List<ScheduleShiftChangeRequestObject>> getAllShiftChangeRequests() {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            shiftChangeRequestDao.getAllClean().forEach(request -> {
                if(!request.getStatus().equals(ShiftChangeRequestObject.SHIFTCHANGESTATUS_ACCEPTED) && !request.getStatus().equals(ShiftChangeRequestObject.SHIFTCHANGESTATUS_SWITCHED) && !request.getStatus().equals(ShiftChangeRequestObject.SHIFTCHANGESTATUS_DECLINED_AND_ENDED)) {
                    CheckRequestStatues(request.getId());
                }
            });
        });
        return shiftChangeRequestDao.getScheduleShiftChangeRequests();
    }
    public LiveData<List<ScheduleShiftChangeResponseObject>> getAllShiftChangeResponses(Integer RequestId){return shiftChangeResponseDao.getScheduleShiftChangeResponseObjects(RequestId);}

    public LiveData<ScheduleShiftChangeRequestObject> getSelectedScheduleShiftChangeRequest(Integer Requestid){return shiftChangeRequestDao.getScheduleShiftChangeRequestsById(Requestid);}
    public LiveData<List<ScheduleOffDayRequestObject>> getUserOffDayRequests(Integer UserId) {
        return offDayRequestDao.getScheduleOffDayRequests(UserId);
    }
    public LiveData<List<UserObject>> getDepartmentUsers(Integer DepartmentId){return userDao.getByDepartment(DepartmentId);}
    public LiveData<DepartmentObject> getDepartment(Integer DepartmentId){return departmentDao.getDepartment(DepartmentId);}
    public LiveData<UserObject> getUser(Integer UserId){return userDao.getUser(UserId);}
    public LiveData<SchedulePreferenceObject> getSchedulePreferences(Integer UserId){return schedulePreferenceDao.getByUser(UserId);}
    public LiveData<ScheduleValueObject> getScheduleValues(Integer DepartmentId){return scheduleValueDao.getByDepartment(DepartmentId);}
    public LiveData<List<ActiveShiftObject>> getFutureActiveShifts(){return activeShiftDao.getAfter(LocalDate.now());}
    public LiveData<List<ActiveShiftObject>> getUserFutureActiveShifts(Integer UserId){return activeShiftDao.getByUserIdAndAfter(UserId, LocalDate.now());}
    public LiveData<List<ScheduleShiftPreferenceObject>> getUserShiftPreferences(Integer UserId){ return shiftPreferenceDao.getScheduleShiftPreferences(UserId);}
    public LiveData<ShiftChangeResponseObject> getUserShiftChangeResponse(Integer UserId, Integer RequestId){return shiftChangeResponseDao.getByUserRequest(UserId, RequestId);}
    public LiveData<List<ScheduleActiveShiftObject>> getScheduleActiveShifts(LocalDate StartDate, LocalDate EndDate, Integer DepartmentId, Integer UserId, Integer CurrentUserId){
        return activeShiftDao.getScheduleActiveShifts(StartDate, EndDate, DepartmentId, UserId, CurrentUserId);
    }

    public void AddOffDayRequest(OffDayRequestObject request){RoomDatabase.databaseWriteExecutor.execute(() -> {

        long id = offDayRequestDao.insert(request);
        request.setId((int) id);
        databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().OffDayRequest(ConnectionCommandObject.Add,request).getCommand()));
    });}
    public void AddShiftChangeRequest(ShiftChangeRequestObject request){RoomDatabase.databaseWriteExecutor.execute(() -> {
        long id = shiftChangeRequestDao.insert(request);
        request.setId((int) id);

        //add ShiftChangeResponse for users in ReceiverList
        shiftChangeRequestDao.getRelevantUserIds(request.getId()).forEach(ReceiverId -> {
            ShiftChangeResponseObject response = new ShiftChangeResponseObject(null,ShiftChangeResponseObject.SHIFTCHANGESTATUS_PENDING,request.getId(),null,ReceiverId);
            int ResponseId = (int) shiftChangeResponseDao.insert(response);
            //databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Response(ConnectionCommandObject.Add,response).getCommand()));
        });

        //databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().OffDayRequest(ConnectionCommandObject.Add,request).getCommand()));
    });}

    public void UpdateSchedulePreferences(SchedulePreferenceObject preferences){RoomDatabase.databaseWriteExecutor.execute(() -> {
        schedulePreferenceDao.update(preferences);
        databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().SchedulePreference(ConnectionCommandObject.Edit,preferences).getCommand()));
    });}
    public void UpdateShiftPreferences(Integer Value, Integer ShiftPreferenceId){
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            ShiftPreferenceObject preference = shiftPreferenceDao.getByIdClean(ShiftPreferenceId);
            preference.setValue(Value);
            shiftPreferenceDao.update(preference);
        }
    );}
    public void UpdateOffDayRequest(String Comment, LocalDate StartDate, LocalDate EndDate, Integer Id){RoomDatabase.databaseWriteExecutor.execute(() -> {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            OffDayRequestObject request = offDayRequestDao.getByIdClean(Id);
            request.setComment(Comment);
            request.setStartDate(StartDate);
            request.setEndDate(EndDate);
            offDayRequestDao.update(request);
        });
    });}
    public void UpdateShiftChangeResponse(ShiftChangeResponseObject response){
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            shiftChangeResponseDao.update(response);

            CheckRequestStatues(response.getShiftChangeRequestId());
            //databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ShiftChangeRequest(ConnectionCommandObject.Edit,request).getCommand()));
        });
    }
    public void UpdateShiftChangeRequest(Integer RequestId, Integer Necessity, String Comment){
        RoomDatabase.databaseWriteExecutor.execute(()-> {
            ShiftChangeRequestObject request = shiftChangeRequestDao.getByIdClean(RequestId);

            request.setNecessity(Necessity);
            request.setComment(Comment);

            shiftChangeRequestDao.update(request);
        });
    }

    public void DeleteShiftChangeRequest(Integer RequestId){
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            shiftChangeRequestDao.deleteById(RequestId);

            //Delete all corresponding ShiftChangeResponses
            shiftChangeResponseDao.getByRequestClean(RequestId).forEach(response -> {
                shiftChangeResponseDao.delete(response);
                //databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Response(ConnectionCommandObject.Delete,response).getCommand()));

            });

            //databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().ShiftChangeRequest(ConnectionCommandObject.Delete,request).getCommand()));
        });
    }
    public void DeleteOffDayRequest(Integer RequestId){
        RoomDatabase.databaseWriteExecutor.execute(() -> offDayRequestDao.deleteById(RequestId));
    }

    public void CheckRequestStatues(Integer RequestId){
        List<ShiftChangeResponseObject> AllRequestResponses = shiftChangeResponseDao.getByRequestClean(RequestId);

        if(AllRequestResponses.stream().anyMatch(response1 -> response1.getStatus().equals(ShiftChangeResponseObject.SHIFTCHANGESTATUS_ACCEPTED))){
            shiftChangeRequestDao.update(shiftChangeRequestDao.getByIdClean(RequestId).setStatus(ShiftChangeRequestObject.SHIFTCHANGESTATUS_ACCEPTED));
        } else if(AllRequestResponses.stream().anyMatch(response1 -> response1.getStatus().equals(ShiftChangeResponseObject.SHIFTCHANGESTATUS_SWITCHED))){
            shiftChangeRequestDao.update(shiftChangeRequestDao.getByIdClean(RequestId).setStatus(ShiftChangeRequestObject.SHIFTCHANGESTATUS_SWITCHED));
        }else if(LocalDate.now().isAfter(activeShiftDao.getByIdClean(shiftChangeRequestDao.getByIdClean(RequestId).getActiveShiftId()).getDate())){
            shiftChangeRequestDao.update(shiftChangeRequestDao.getByIdClean(RequestId).setStatus(ShiftChangeRequestObject.SHIFTCHANGESTATUS_DECLINED_AND_ENDED));
        } else if(AllRequestResponses.stream().anyMatch(response1 -> response1.getStatus().equals(ShiftChangeResponseObject.SHIFTCHANGESTATUS_SWITCH_SUGGESTED))){
            shiftChangeRequestDao.update(shiftChangeRequestDao.getByIdClean(RequestId).setStatus(ShiftChangeRequestObject.SHIFTCHANGESTATUS_SWITCH_SUGGESTED));
        }else if(AllRequestResponses.stream().anyMatch(response1 -> response1.getStatus().equals(ShiftChangeResponseObject.SHIFTCHANGESTATUS_PENDING)) && AllRequestResponses.stream().allMatch(response1 -> response1.getStatus().equals(ShiftChangeResponseObject.SHIFTCHANGESTATUS_PENDING) || response1.getStatus().equals(ShiftChangeResponseObject.SHIFTCHANGESTATUS_DECLINED))){
            shiftChangeRequestDao.update(shiftChangeRequestDao.getByIdClean(RequestId).setStatus(ShiftChangeRequestObject.SHIFTCHANGESTATUS_PENDING));
        }else if(AllRequestResponses.stream().allMatch(response1 -> response1.getStatus().equals(ShiftChangeResponseObject.SHIFTCHANGESTATUS_DECLINED))){
            shiftChangeRequestDao.update(shiftChangeRequestDao.getByIdClean(RequestId).setStatus(ShiftChangeRequestObject.SHIFTCHANGESTATUS_DECLINED));
        }
    }
}
