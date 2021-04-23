package com.example.coopapp20.Data;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Ignore;
import androidx.room.Room;
import androidx.room.TypeConverters;

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
import com.example.coopapp20.Data.Objects.ProductChangeObject;
import com.example.coopapp20.Data.Objects.ProductObject;
import com.example.coopapp20.Data.Objects.SchedulePreferenceObject;
import com.example.coopapp20.Data.Objects.ScheduleValueObject;
import com.example.coopapp20.Data.Objects.ShiftChangeRequestObject;
import com.example.coopapp20.Data.Objects.ShiftChangeResponseObject;
import com.example.coopapp20.Data.Objects.ShiftObject;
import com.example.coopapp20.Data.Objects.ShiftPreferenceObject;
import com.example.coopapp20.Data.Objects.TaskObject;
import com.example.coopapp20.Data.Objects.TaskSortingValueObject;
import com.example.coopapp20.Data.Objects.UserObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {UserObject.class, DepartmentObject.class, MessageObject.class, ProductObject.class,
        ProductChangeObject.class, TaskObject.class, FinishedTaskObject.class, ShiftObject.class,
        ShiftPreferenceObject.class, ScheduleValueObject.class, SchedulePreferenceObject.class,
        OffDayRequestObject.class, TaskSortingValueObject.class, ActiveShiftObject.class,
        ShiftChangeRequestObject.class, ShiftChangeResponseObject.class, DatabaseChangeObject.class},
        version = 24, exportSchema = false)
@TypeConverters({RoomTypeConverter.class})
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    @Ignore
    private static final String DATABASE_FILE_NAME = "RoomDatabase.db";

    public abstract DatabaseChangeDao databaseChangeDao();
    public abstract UserDao userDao();
    public abstract DepartmentDao departmentDao();
    public abstract MessageDao messageDao();
    public abstract ProductDao productDao();
    public abstract ProductChangeDao productChangeDao();
    public abstract TaskDao taskDao();
    public abstract FinishedTaskDao finishedTaskDao();
    public abstract ShiftDao shiftDao();
    public abstract ShiftPreferenceDao shiftPreferenceDao();
    public abstract ScheduleValueDao scheduleValueDao();
    public abstract SchedulePreferenceDao schedulePreferenceDao();
    public abstract OffDayRequestDao offDayRequestDao();
    public abstract TaskSortingValueDao taskSortingValueDao();
    public abstract ActiveShiftDao activeShiftDao();
    public abstract ShiftChangeRequestDao shiftChangeRequestDao();
    public abstract ShiftChangeResponseDao shiftChangeResponseDao();

    private static volatile RoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static RoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDatabase.class) {
                if (INSTANCE == null) {
                    Log.e("RoomDatabase","CREATED");
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDatabase.class, DATABASE_FILE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
