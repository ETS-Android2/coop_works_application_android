package com.example.coopapp20.Main;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
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
import androidx.navigation.NavController;

import com.example.coopapp20.Data.Objects.ActiveShiftObject;
import com.example.coopapp20.Data.Objects.DepartmentObject;
import com.example.coopapp20.Data.Objects.FinishedTaskObject;
import com.example.coopapp20.Data.Objects.TaskObject;
import com.example.coopapp20.Data.Objects.UserObject;
import com.example.coopapp20.Main.LoginActivity.LoginActivity;
import com.example.coopapp20.R;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private NavController SuperNavController;
    private NavController MainNavController;
    private MainRepository repository;
    private LiveData<UserObject> CurrentUser;
    private LiveData<ActiveShiftObject> CurrentShift;
    private LiveData<List<TaskObject>> CurrentTasks;
    private LiveData<List<FinishedTaskObject>> CurrentFinishedTasks;
    private MutableLiveData<ToolbarFrag> CurrentToolbar = new MutableLiveData<>();
    private LiveData<List<UserObject>> AllUsers;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new MainRepository(getApplication());

        MediatorLiveData<Void> UserListMediator = new MediatorLiveData<>();
        UserListMediator.addSource(repository.getAllUsers(), users -> UserListMediator.setValue(null));
        UserListMediator.addSource(repository.getAllDepartments(), departments -> UserListMediator.setValue(null));

        AllUsers = Transformations.map(UserListMediator, input -> CreateUserObjects());

    }

    public LiveData<UserObject> getCurrentUser() { return CurrentUser; }
    public LiveData<ActiveShiftObject> getCurrentShift(){return CurrentShift;}
    public LiveData<List<TaskObject>> getCurrentTasks() { return CurrentTasks; }
    public LiveData<List<FinishedTaskObject>> getFinishedTasks() { return CurrentFinishedTasks; }
    public LiveData<List<UserObject>> getAllUsers() { return AllUsers; }

    public MutableLiveData<ToolbarFrag> getCurrentToolbar() { return CurrentToolbar; }
    public NavController getSuperNavController(){return SuperNavController;}
    public NavController getMainNavController() { return MainNavController; }

    public void setCurrentUser(LiveData<UserObject> currentUser) {
        CurrentUser = currentUser;
        CurrentShift = Transformations.switchMap(currentUser, user -> user != null ? repository.getActiveShift(user.getId()) : null);
        CurrentTasks = Transformations.switchMap(currentUser, user -> user != null ? repository.getCurrentTasks(user.getDepartmentId()): null);
        CurrentFinishedTasks = Transformations.switchMap(currentUser, user -> user != null ? repository.getCurrentFinishedTasks(user.getId()) : null);
    }
    public void setSuperNavController(NavController navController){SuperNavController = navController;}
    public void setMainNavController(NavController navController){MainNavController = navController;}

    public void onLogoutBtnClick(Context context){
        //Set logout btn to delete current user and go back to login activity
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("VIL DU LOGGE UD?");
        dialog.setPositiveButton(android.R.string.yes, (d, whichButton) -> {
            SharedPreferences SharedPref = context.getSharedPreferences(context.getString(R.string.BasicPreferences), Context.MODE_PRIVATE);
            SharedPref.edit().putBoolean(context.getString(R.string.LoggedIn),false).apply();
            SharedPref.edit().putString(context.getString(R.string.CurrentUsername), null).apply();

            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.addFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        });
        dialog.setNegativeButton(android.R.string.no, null);
        dialog.show();
    }
    public void onStatusBtnClick(Context context, View view){
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenu().add(0,0,3,getApplication().getString(R.string.navigation_active_menu_available));
        popupMenu.getMenu().add(0,0,2,getApplication().getString(R.string.navigation_active_menu_occupied));
        popupMenu.getMenu().add(0,0,1,getApplication().getString(R.string.navigation_active_menu_on_break));
        popupMenu.setOnMenuItemClickListener(item -> {
            if(getCurrentUser().getValue() != null){
                getCurrentUser().getValue().setStatus(item.getOrder());
                new Thread(()-> repository.UpdateUser(getCurrentUser().getValue())).start();
            }else { Toast.makeText(context, "CurrentUser not found", Toast.LENGTH_SHORT).show(); }
            return true;
        });
        popupMenu.show();
    }
    public boolean onNavigationDrawerItemClick(int ItemId){
        switch (ItemId){
            case R.id.item1:
                getMainNavController().navigate(R.id.guideFrag);
                break;
            case R.id.item2:
                getMainNavController().navigate(R.id.feedbackFrag);
                break;
            case R.id.item3:
                getMainNavController().navigate(R.id.devInfoFrag);
                break;
            case R.id.item4:
                getMainNavController().navigate(R.id.settingsFrag);
                break;
            case R.id.item5:
                getMainNavController().navigate(R.id.scheduleAdminShiftsFrag);
                break;
            case R.id.item6:
                getMainNavController().navigate(R.id.taskAdministration);
                break;
            case R.id.item7:
                getMainNavController().navigate(R.id.userAdministration);
                break;
            case R.id.item8:
                getMainNavController().navigate(R.id.departmentAdministration);
                break;
        }
        return true;
    }

    public List<UserObject> CreateUserObjects() {
        if (repository.getAllUsers().getValue() != null && repository.getAllDepartments().getValue() != null) {
            List<UserObject> users = repository.getAllUsers().getValue();
            List<DepartmentObject> departments = repository.getAllDepartments().getValue();

            users.forEach(user -> {
                user.setDepartmentNameString(departments.stream().filter(department -> department.getId().equals(user.getDepartmentId())).findAny().map(DepartmentObject::getName).orElse("N/A"));
                user.setDepartmentColorString(Color.parseColor(departments.stream().filter(department -> department.getId().equals(user.getDepartmentId())).findAny().map(DepartmentObject::getColor).orElse("#919191")));
            });

            return users;

        }else {return null;}

    }
}
