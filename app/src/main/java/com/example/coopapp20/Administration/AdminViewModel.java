package com.example.coopapp20.Administration;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.coopapp20.Data.Objects.ActiveShiftObject;
import com.example.coopapp20.Data.Objects.DepartmentObject;
import com.example.coopapp20.Data.Objects.ScheduleValueObject;
import com.example.coopapp20.Data.Objects.ShiftObject;
import com.example.coopapp20.Data.Objects.TaskObject;
import com.example.coopapp20.Data.Objects.UserObject;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.R;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AdminViewModel extends AndroidViewModel {

    private AdminRepository repository;

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);

    private LiveData<List<DepartmentObject>> AllDepartments;
    private LiveData<List<UserObject>> AllUsers;
    private LiveData<List<TaskObject>> AllRepeatTasks;
    private LiveData<List<ShiftObject>> AllShifts;

    private MutableLiveData<DepartmentObject> SelectedDepartment = new MutableLiveData<>();
    private MutableLiveData<UserObject> SelectedUser = new MutableLiveData<>();
    private MutableLiveData<TaskObject> SelectedTask = new MutableLiveData<>();
    private MutableLiveData<ShiftObject> SelectedShift = new MutableLiveData<>();
    private LiveData<ScheduleValueObject> SelectedScheduleValues;

    private MutableLiveData<Integer> SelectedScheduleValuesDepartmentId = new MutableLiveData<>();

    public AdminViewModel(@NonNull Application application) {
        super(application);
        repository = new AdminRepository(application);
        AllDepartments = repository.getAllDepartments();

        SelectedScheduleValues = Transformations.switchMap(SelectedScheduleValuesDepartmentId, id -> repository.getDepartmentScheduleValues(id));
    }

    public LiveData<List<DepartmentObject>> getAllDepartments() {
        return AllDepartments;
    }
    public LiveData<List<UserObject>> getAllUsers(){
        if(AllUsers == null){AllUsers = repository.getAllUsers();}
        return AllUsers;
    }
    public LiveData<List<TaskObject>> getAllTasks() {
        if(AllRepeatTasks == null){AllRepeatTasks = repository.getAllRepeatTasks();}
        return AllRepeatTasks; }
    public LiveData<List<ShiftObject>> getAllShifts(){
        if(AllShifts == null){
            ShiftMediator Mediator = new ShiftMediator();
            Mediator.addSource(repository.getAllShifts(), Mediator::setShifts);
            Mediator.addSource(repository.getAllDepartments(), Mediator::setDepartments);
            AllShifts = Transformations.map(Mediator, value -> setShifts(Mediator.getShifts(), Mediator.getDepartments()));
        }
        return AllShifts;
    }
    private List<ShiftObject> setShifts(List<ShiftObject> shifts, List<DepartmentObject> departments){
        if(shifts != null && departments != null){

            Collections.sort(shifts, (o1, o2) -> o1.getDay().compareTo(o2.getDay()));
            Collections.sort(shifts, (o1, o2) -> o1.getDepartmentId().compareTo(o2.getDepartmentId()));

            shifts.forEach(s ->{
                departments.stream().filter(d -> d.getId().equals(s.getDepartmentId())).findAny().ifPresent(d -> {s.setDepartmentName(d.getName()); s.setDepartmentColor(Color.parseColor(d.getColor()));});

                String DayString = getApplication().getResources().getStringArray(R.array.day_array)[s.getDay()];

                s.setDayString(DayString);
                String TimeString = s.getStartTime().format(timeFormatter) + " - " + s.getEndTime().format(timeFormatter);
                s.setTimeString(TimeString);
            });

            return shifts;
        }else {return null;}
    }

    public MutableLiveData<DepartmentObject> getSelectedDepartment(){return SelectedDepartment;}
    public MutableLiveData<UserObject> getSelectedUser(){return SelectedUser;}
    public MutableLiveData<TaskObject> getSelectedTask(){return SelectedTask;}
    public MutableLiveData<ShiftObject> getSelectedShift() { return SelectedShift; }
    public LiveData<ScheduleValueObject> getSelectedScheduleValues() {return SelectedScheduleValues;}
    public MutableLiveData<Integer> getSelectedScheduleValuesDepartmentId(){return SelectedScheduleValuesDepartmentId;}

    public void UserItemOptions(View view, UserObject user, MainViewModel mainViewModel, Context context){
        PopupMenu popupMenu = new PopupMenu(getApplication(), view);
        popupMenu.getMenu().add(getApplication().getString(R.string.edit));
        popupMenu.getMenu().add(getApplication().getString(R.string.delete));

        popupMenu.setOnMenuItemClickListener(item -> {
            if(item.getTitle().equals(getApplication().getString(R.string.edit))){
                SelectedUser.setValue(user);
                mainViewModel.getMainNavController().navigate(R.id.addContactFrag);
            }else if (item.getTitle().equals(getApplication().getString(R.string.delete))) {
                new AlertDialog.Builder(context)
                        .setTitle("VIL DU SLETTE BRUGEREN?")
                        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> new DeleteUser(user, context).execute())
                        .setNegativeButton(android.R.string.no, null).show();
            }
            return true;
        });

        popupMenu.show();
    }
    public void DepartmentItemOptions(View view, DepartmentObject department, MainViewModel mainViewModel, Context context){
        PopupMenu popupMenu = new PopupMenu(getApplication(), view);
        popupMenu.getMenu().add(getApplication().getString(R.string.edit));
        popupMenu.getMenu().add(getApplication().getString(R.string.delete));

        popupMenu.setOnMenuItemClickListener(item -> {
            if(item.getTitle().equals(getApplication().getString(R.string.edit))){
                SelectedDepartment.setValue(department);
                mainViewModel.getMainNavController().navigate(R.id.addDepartmentFrag);
            }else if (item.getTitle().equals(getApplication().getString(R.string.delete))){
            new AlertDialog.Builder(context)
                    .setTitle("VIL DU SLETTE AFDELINGEN?")
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> new DeleteDepartment(department,context).execute())
                    .setNegativeButton(android.R.string.no, null).show();
        }
            return true;
        });

        popupMenu.show();
    }
    public void TaskItemOptions(View view, TaskObject task, MainViewModel mainViewModel, Context context){
        PopupMenu popupMenu = new PopupMenu(getApplication(), view);
        popupMenu.getMenu().add(getApplication().getString(R.string.edit));
        popupMenu.getMenu().add(getApplication().getString(R.string.delete));

        popupMenu.setOnMenuItemClickListener(item -> {
            if(item.getTitle().equals(getApplication().getString(R.string.edit))){
                SelectedTask.setValue(task);
                mainViewModel.getMainNavController().navigate(R.id.addTaskStaticParameterFrag);
            }else if (item.getTitle().equals(getApplication().getString(R.string.delete))){
                new AlertDialog.Builder(context)
                        .setTitle("VIL DU SLETTE OPGAVEN?")
                        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> repository.DeleteTask(task))
                        .setNegativeButton(android.R.string.no, null).show();
            }
            return true;
        });

        popupMenu.show();
    }
    public void ShiftItemOptions(View view, ShiftObject shift, MainViewModel mainViewModel, Context context){
        PopupMenu popupMenu = new PopupMenu(getApplication(), view);
        popupMenu.getMenu().add(getApplication().getString(R.string.edit));
        popupMenu.getMenu().add(getApplication().getString(R.string.delete));

        popupMenu.setOnMenuItemClickListener(item -> {
            if(item.getTitle().equals(getApplication().getString(R.string.edit))){
                SelectedShift.setValue(shift);
                mainViewModel.getMainNavController().navigate(R.id.addShiftFrag);
            }else if (item.getTitle().equals(getApplication().getString(R.string.delete))){
                new AlertDialog.Builder(context)
                        .setTitle("VIL DU SLETTE VAGTEN?")
                        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> repository.DeleteShift(shift))
                        .setNegativeButton(android.R.string.no, null).show();
            }
            return true;
        });

        popupMenu.show();
    }

    public void ExecuteDepartment(String name, String color){
        if(SelectedDepartment.getValue() == null) {
            repository.AddDepartment(new DepartmentObject(null,  name, color));
        }else {
            SelectedDepartment.getValue().setName(name);
            SelectedDepartment.getValue().setColor(color);
            repository.EditDepartment(SelectedDepartment.getValue());
        }
    }
    public void ExecuteUser(String name, String email,String phonenr,Integer departmentId, String username, String password, Context context){
        if(SelectedUser.getValue() == null) {
            repository.AddUser(new UserObject(null, name, email, phonenr, departmentId, username, password, null, null, null));
        }else {
            SelectedUser.getValue().setName(name);
            SelectedUser.getValue().setEmail(email);
            SelectedUser.getValue().setPhoneNr(phonenr);
            SelectedUser.getValue().setUserName(username);
            SelectedUser.getValue().setPassword(password);
            if(departmentId.equals(SelectedUser.getValue().getDepartmentId())){
                repository.EditUser(SelectedUser.getValue());
            }else {
                SelectedUser.getValue().setDepartmentId(departmentId);
                new EditUser(SelectedUser.getValue(),context).execute();
            }
        }
    }
    public void ExecuteTask(int departmentId, int prioritization, String title, String duration, String idealTime, String days, String description){
        if(SelectedTask.getValue() == null){
            repository.AddTask(new TaskObject(null,title, Duration.between(LocalTime.MIN,LocalTime.parse(duration,timeFormatter)),LocalTime.parse(idealTime.substring(0,5)),Integer.parseInt(days),description,departmentId,prioritization,true, LocalDate.now(), LocalTime.now()));
        } else {
            SelectedTask.getValue().setTitle(title);
            SelectedTask.getValue().setDuration(Duration.between(LocalTime.MIN,LocalTime.parse(duration,timeFormatter)));
            SelectedTask.getValue().setIdealExecutionTime(LocalTime.parse(idealTime.substring(0,5)));
            SelectedTask.getValue().setDays(Integer.parseInt(days));
            SelectedTask.getValue().setDescription(description);
            SelectedTask.getValue().setDepartmentId(departmentId);
            SelectedTask.getValue().setPrioritization(prioritization);
            repository.EditTask(SelectedTask.getValue());
        }
    }
    public void ExecuteShift(int departmentId, int dayNr, String startTime, String endTime){
        if(SelectedShift.getValue() == null){
            repository.AddShift(new ShiftObject(null,departmentId,dayNr,LocalTime.parse(startTime,timeFormatter),LocalTime.parse(endTime,timeFormatter)));
        }else {
            SelectedShift.getValue().setDay(dayNr);
            SelectedShift.getValue().setStartTime(LocalTime.parse(startTime,timeFormatter));
            SelectedShift.getValue().setEndTime(LocalTime.parse(endTime,timeFormatter));
            repository.EditShift(SelectedShift.getValue());
        }
    }
    public void ExecuteScheduleValues(int WeekDuration, LocalDate PreferenceDeadline, LocalDate ScheduleCreation, LocalDate ScheduleBeginning){
        if(SelectedScheduleValues.getValue() != null && ScheduleBeginning != null && ScheduleCreation != null && PreferenceDeadline != null){
            SelectedScheduleValues.getValue().setScheduleWeekDuration(WeekDuration);
            SelectedScheduleValues.getValue().setPreferenceDeadline(PreferenceDeadline);
            SelectedScheduleValues.getValue().setCreationDeadline(ScheduleCreation);
            if(!SelectedScheduleValues.getValue().getScheduleOngoing()){SelectedScheduleValues.getValue().setScheduleBeginning(ScheduleBeginning);}
            repository.EditScheduleValues(SelectedScheduleValues.getValue());
        }
    }

    class DeleteDepartment extends AsyncTask<Void,Void,Void>{
        int users = 0;
        int tasks = 0;
        int shifts = 0;
        DepartmentObject Department;
        Context Context;

        DeleteDepartment(DepartmentObject department, Context context){
            Department = department;
            Context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Check if department contain users
            List<UserObject> DepartmentUsers = repository.getDepartmentUsers(Department.getId());
            users = DepartmentUsers.size();

            //Check if Department contain tasks
            List<TaskObject> DepartmentTasks = repository.getDepartmentTasks(Department.getId());
            tasks = DepartmentTasks.size();

            //Check if department contain shifts
            List<ShiftObject> DepartmentShifts = repository.getDepartmentShifts(Department.getId());
            shifts = DepartmentShifts.size();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(users != 0 || tasks != 0 || shifts != 0){
                new AlertDialog.Builder(Context)
                        .setTitle("Afdeling ikke klar til afvikling")
                        .setMessage("følgende data skal fjernes \nBrugere: "+users+" \nopgaver: "+tasks+" \nvagter: "+shifts)
                        .setNeutralButton("ok",null)
                        .show();
            }else {
                repository.DeleteDepartment(Department);
                Toast.makeText(Context,"deleted",Toast.LENGTH_SHORT).show();
            }
        }
    }

    class EditUser extends AsyncTask<Void,Void,Void>{

        int activeShifts = 0;
        UserObject User;
        Context Context;

        EditUser(UserObject user, Context context){
            User = user;
            Context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<ActiveShiftObject> ActiveShifts = repository.getUserActiveShifts(User.getId());
            activeShifts = ActiveShifts.size();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(activeShifts != 0){
                new AlertDialog.Builder(Context)
                        .setTitle("Bruger ikke klar til flytning")
                        .setMessage("følgende data skal fjernes \nVagt: "+activeShifts)
                        .setNeutralButton("ok",null)
                        .show();
            }else {
                repository.EditUser(User);
                Toast.makeText(Context,"ændret",Toast.LENGTH_SHORT).show();
            }
        }
    }

    class DeleteUser extends AsyncTask<Void,Void,Void>{

        int activeShifts = 0;
        UserObject User;
        Context Context;

        DeleteUser(UserObject user, Context context){
            User = user;
            Context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            List<ActiveShiftObject> ActiveShifts = repository.getUserActiveShifts(User.getId());
            activeShifts = ActiveShifts.size();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(activeShifts != 0){
                new AlertDialog.Builder(Context)
                        .setTitle("Afdeling ikke klar til afvikling")
                        .setMessage("følgende data skal fjernes \nVagt: "+activeShifts)
                        .setNeutralButton("ok",null)
                        .show();
            }else {
                repository.DeleteUser(User);
                Toast.makeText(Context,"deleted",Toast.LENGTH_SHORT).show();
            }
        }
    }

    class ShiftMediator extends MediatorLiveData<Void>{
        private List<ShiftObject> shifts = null;
        private List<DepartmentObject> departments = null;

        ShiftMediator setShifts(List<ShiftObject> shifts) {
            this.shifts = shifts;
            setValue(null);
            return this;
        }

        public ShiftMediator setDepartments(List<DepartmentObject> departments) {
            this.departments = departments;
            setValue(null);
            return this;
        }

        private List<ShiftObject> getShifts() {
            return shifts;
        }

        private List<DepartmentObject> getDepartments() {
            return departments;
        }
    }
}
