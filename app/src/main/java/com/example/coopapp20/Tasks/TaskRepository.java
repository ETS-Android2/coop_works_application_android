package com.example.coopapp20.Tasks;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.coopapp20.Data.ConnectionCommandObject;
import com.example.coopapp20.Data.Dao.DatabaseChangeDao;
import com.example.coopapp20.Data.Dao.DepartmentDao;
import com.example.coopapp20.Data.Dao.FinishedTaskDao;
import com.example.coopapp20.Data.Dao.TaskDao;
import com.example.coopapp20.Data.Objects.DatabaseChangeObject;
import com.example.coopapp20.Data.Objects.DepartmentObject;
import com.example.coopapp20.Data.Objects.FinishedTaskObject;
import com.example.coopapp20.Data.Objects.TaskObject;
import com.example.coopapp20.Data.RoomDatabase;

import java.util.List;

class TaskRepository {

    private DatabaseChangeDao databaseChangeDao;
    private TaskDao taskDao;
    private FinishedTaskDao finishedTaskDao;
    private DepartmentDao departmentDao;

    private LiveData<List<DepartmentObject>> AllDepartments;

    TaskRepository(Application application){
        RoomDatabase db = RoomDatabase.getDatabase(application);

        databaseChangeDao = db.databaseChangeDao();
        taskDao = db.taskDao();
        finishedTaskDao = db.finishedTaskDao();
        departmentDao = db.departmentDao();

        AllDepartments = departmentDao.getAll();
    }

    LiveData<List<TaskObject>> getDepartmentTasks(Integer DepartmentId){ return taskDao.getByDepartment(DepartmentId);}
    LiveData<List<FinishedTaskObject>> getUserFinishedTasks(Integer UserId){return finishedTaskDao.getByUser(UserId);}
    LiveData<List<DepartmentObject>> getAllDepartments(){return AllDepartments;}

    void AddTask(TaskObject task){
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            long id = taskDao.insert(task);
            task.setId((int)id);
            databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Task(ConnectionCommandObject.Add,task).getCommand()));
        });
    }
    void AddFinishedTask(FinishedTaskObject finishedTask){
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            long id = finishedTaskDao.insert(finishedTask);
            finishedTask.setId((int) id);
            databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().FinishedTask(ConnectionCommandObject.Add,finishedTask).getCommand()));
        });
    }
    void UpdateTask(TaskObject task){
        RoomDatabase.databaseWriteExecutor.execute(() -> taskDao.update(task));
        RoomDatabase.databaseWriteExecutor.execute(()->databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Task(ConnectionCommandObject.Edit,task).getCommand())));
    }
    void DeleteTask(TaskObject task){
        RoomDatabase.databaseWriteExecutor.execute(() -> taskDao.delete(task));
        RoomDatabase.databaseWriteExecutor.execute(()->databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Task(ConnectionCommandObject.Delete,task).getCommand())));
    }
    void DeleteFinishedTask(FinishedTaskObject finishedTask){
        RoomDatabase.databaseWriteExecutor.execute(() -> finishedTaskDao.delete(finishedTask));
        RoomDatabase.databaseWriteExecutor.execute(()-> databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().FinishedTask(ConnectionCommandObject.Delete,finishedTask).getCommand())));
    }
}
