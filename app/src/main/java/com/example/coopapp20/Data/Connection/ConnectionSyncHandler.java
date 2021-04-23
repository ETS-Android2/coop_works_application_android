package com.example.coopapp20.Data.Connection;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.coopapp20.Data.Objects.DatabaseChangeObject;
import com.example.coopapp20.Main.MainRepository;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectionSyncHandler {

    private Timer timer = new Timer();
    private Observer observer = databaseChangeObjects -> {};
    private LiveData<List<DatabaseChangeObject>> DatabaseChanges;

    public ConnectionSyncHandler(Application application){
        MainRepository mainRepository = new MainRepository(application);

        DatabaseChanges = mainRepository.getDatabaseChanges();

        DatabaseChanges.observeForever(observer);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                ConnectionHandler connection = new ConnectionHandler(application);

                //Attempt to retrieve changes made to local database
                ConnectionCommandObject command = new ConnectionCommandObject();
                command.getDataChanges();
                if(connection.Command(command)){
                    ConnectionCommandHandler handler = new ConnectionCommandHandler(application);
                    String Return = connection.getStringReturn();
                    handler.Execute(Return);
                }

                if(DatabaseChanges.getValue() != null && DatabaseChanges.getValue().size() > 0){
                    Log.e("ConnectionSyncHandler","Sent command: "+DatabaseChanges.getValue());
                    if(connection.CommandChanges(DatabaseChanges.getValue())){
                        mainRepository.ClearDatabaseChanges();
                        Log.e("DatabaseDataTransfer","Table deleted");
                    }
                }
            }
        }, 0, 1000);
    }

    public void cancel(){timer.cancel();timer.purge();DatabaseChanges.removeObserver(observer);}
}
