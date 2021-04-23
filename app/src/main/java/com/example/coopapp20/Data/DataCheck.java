package com.example.coopapp20.Data;

import android.app.Application;
import android.os.AsyncTask;

public class DataCheck {

    RoomDatabase db;

    public DataCheck(Application application){
        db = RoomDatabase.getDatabase(application);
    }

    class AddUserDataCheck extends AsyncTask<Void,Void,Void>{

        int UserId;

        public AddUserDataCheck(int userId){UserId = userId;}

        @Override
        protected Void doInBackground(Void... voids) {
            //LiveData<List<SchedulePreferenceObject>> preferences = db.schedulePreferenceDao().getByUser(UserId);

            return null;
        }
    }
}
