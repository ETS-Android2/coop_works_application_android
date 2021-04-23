package com.example.coopapp20.Scedule.Schedule;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.coopapp20.Data.Objects.DepartmentObject;
import com.example.coopapp20.Data.Objects.ShiftChangeRequestObject;
import com.example.coopapp20.Data.Objects.UserObject;
import com.example.coopapp20.R;
import com.example.coopapp20.Scedule.Schedule.ScheduleActiveShiftObject;
import com.example.coopapp20.Scedule.ScheduleRepository;
import com.example.coopapp20.databinding.DialogScheduleShiftchangerequestBinding;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

public class ScheduleViewModel extends AndroidViewModel {

    private ScheduleRepository repository;

    private LiveData<List<ScheduleActiveShiftObject>> ActiveShifts;

    private LiveData<List<DepartmentObject>> AllDepartments;
    private LiveData<List<UserObject>> DepartmentUsers;
    private LiveData<DepartmentObject> SelectedDepartment;
    private LiveData<UserObject> SelectedUser;
    private MutableLiveData<Integer> SelectedDepartmentId = new MutableLiveData<>();
    private MutableLiveData<Integer> SelectedUserId = new MutableLiveData<>();
    private MutableLiveData<LocalDate> SelectedWeekStart = new MutableLiveData<>();
    private MutableLiveData<UserObject> CurrentUser = new MutableLiveData<>();

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy", Locale.ENGLISH);

    private boolean PickerOpen = false;

    public ScheduleViewModel(@NonNull Application application) {
        super(application);
        repository = new ScheduleRepository(getApplication());

        SelectedWeekStart.setValue(LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue()-1));
        SelectedDepartment = Transformations.switchMap(SelectedDepartmentId, input -> repository.getDepartment(input));
        SelectedUser = Transformations.switchMap(SelectedUserId, input -> repository.getUser(input));
        DepartmentUsers = Transformations.switchMap(SelectedDepartmentId, input -> repository.getDepartmentUsers(input));
        AllDepartments = repository.getAllDepartments();

        MediatorLiveData<Void> ActiveShiftMediator = new MediatorLiveData<>();
        ActiveShiftMediator.addSource(SelectedWeekStart, o -> ActiveShiftMediator.setValue(null));
        ActiveShiftMediator.addSource(SelectedDepartmentId, o -> ActiveShiftMediator.setValue(null));
        ActiveShiftMediator.addSource(SelectedUserId, o -> ActiveShiftMediator.setValue(null));

        ActiveShifts = Transformations.switchMap(ActiveShiftMediator, input -> repository.getScheduleActiveShifts(SelectedWeekStart.getValue(), SelectedWeekStart.getValue().plusDays(7),SelectedDepartmentId.getValue(), SelectedUserId.getValue(), CurrentUser.getValue().getId()));
    }

    public LiveData<List<ScheduleActiveShiftObject>> getActiveShifts() { return ActiveShifts; }
    public LiveData<DepartmentObject> getSelectedDepartment() { return SelectedDepartment; }
    public LiveData<UserObject> getSelectedUser() { return SelectedUser; }
    public MutableLiveData<LocalDate> getSelectedWeekStart() { return SelectedWeekStart; }

    public void Observe(LifecycleOwner owner){
        DepartmentUsers.observe(owner, userObjects -> {});
        AllDepartments.observe(owner, departmentObjects -> {});
    }

    public LiveData<UserObject> getCurrentUser() { return CurrentUser; }
    public void setCurrentUser(LiveData<UserObject> currentUser) {
        CurrentUser.setValue(currentUser.getValue());
        SelectedDepartmentId.setValue(CurrentUser.getValue() != null ? CurrentUser.getValue().getDepartmentId() : null);
        SelectedUserId.setValue(CurrentUser.getValue() != null ? CurrentUser.getValue().getId() : null);
    }

    public void onShiftChangeBtnClick(Activity activity, ScheduleActiveShiftObject activeShift){
        if(DepartmentUsers.getValue() != null && CurrentUser.getValue() != null){

            //create dialog with custom layout
            Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogScheduleShiftchangerequestBinding Binding = DialogScheduleShiftchangerequestBinding.inflate(dialog.getLayoutInflater());
            dialog.setContentView(Binding.getRoot());
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            //set execute button
            Binding.ExecuteBtn.setOnClickListener(v ->
                    new AlertDialog.Builder(activity)
                            .setTitle("VIL DU BYTTE VAGTEN?")
                            .setPositiveButton(android.R.string.yes, (d, whichButton) -> {
                                Log.e("ScheduleViewModel","ShiftChangeRequestObject("+activeShift.getActiveShiftId()+","+ CurrentUser.getValue().getId()+","+ (Binding.NecessitySwitch.isChecked() ? ShiftChangeRequestObject.NECESSITY_NEEDED : ShiftChangeRequestObject.NECESSITY_WANTED) +","+Binding.CommentTextView.getText().toString() +","+ShiftChangeRequestObject.SHIFTCHANGESTATUS_PENDING+")");
                                repository.AddShiftChangeRequest(new ShiftChangeRequestObject(null,activeShift.getActiveShiftId(), CurrentUser.getValue().getId(),Binding.NecessitySwitch.isChecked() ? ShiftChangeRequestObject.NECESSITY_NEEDED : ShiftChangeRequestObject.NECESSITY_WANTED,Binding.CommentTextView.getText().toString(),ShiftChangeRequestObject.SHIFTCHANGESTATUS_PENDING));
                                dialog.cancel();
                            })
                            .setNegativeButton(android.R.string.no, null).show());

            dialog.show();
        }
    }
    public void onSelectedWeekTextViewClick(FragmentManager manager, Activity activity){
        if(!PickerOpen && SelectedWeekStart.getValue() != null) {
            PickerOpen = true;
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();

            //set range for a year from now, and available dates after end of current schedule
            CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
            constraintsBuilder.setOpenAt(SelectedWeekStart.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
            constraintsBuilder.setStart(LocalDate.now().minusYears(2).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
            constraintsBuilder.setEnd(LocalDate.now().plusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());

            builder.setSelection(SelectedWeekStart.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());

            builder.setTitleText("Calendar");
            builder.setTheme(resolveOrThrow(activity));
            builder.setCalendarConstraints(constraintsBuilder.build());

            MaterialDatePicker<Long> picker = builder.build();
            picker.addOnDismissListener(v -> PickerOpen = false);

            picker.addOnPositiveButtonClickListener(selection -> {
                if(picker.getSelection() != null) {
                    LocalDate SelectedDate = Instant.ofEpochMilli(((Long) picker.getSelection())).atZone(ZoneId.systemDefault()).toLocalDate();
                    SelectedWeekStart.setValue(SelectedDate.minusDays(SelectedDate.getDayOfWeek().getValue() - 1));
                }
            });

            picker.show(manager, picker.toString());
        }
    }
    public void onWeekChangeBtnsClick(boolean forward){
        if(SelectedWeekStart.getValue() != null){
            if(forward) {SelectedWeekStart.setValue(SelectedWeekStart.getValue().plusWeeks(1));}
            else {SelectedWeekStart.setValue(SelectedWeekStart.getValue().minusWeeks(1));}
        }
    }
    public void onDepartmentTextViewClick(View v){
        if(AllDepartments.getValue() != null && CurrentUser.getValue() != null) {
            PopupMenu popup = new PopupMenu(getApplication(), v);

            List<DepartmentObject> departments = AllDepartments.getValue();
            popup.getMenu().add(0, 0, 0, getApplication().getString(R.string.all));
            departments.forEach(d -> popup.getMenu().add(0,0,departments.indexOf(d),d.getName()));

            popup.setOnMenuItemClickListener(item -> {
                if(item.getTitle().equals(getApplication().getString(R.string.all))){ SelectedDepartmentId.setValue(null); }
                else {SelectedDepartmentId.setValue(departments.get(item.getOrder()).getId());}
                SelectedUserId.setValue(null);

                return true;
            });

            popup.show();
        }
    }
    public void onUserTextViewClick(View v){
        if(DepartmentUsers.getValue() != null) {
            PopupMenu popup = new PopupMenu(getApplication(), v);

            List<UserObject> departmentUsers = DepartmentUsers.getValue();
            popup.getMenu().add(0, 0, 0, getApplication().getString(R.string.all));
            departmentUsers.forEach(u -> popup.getMenu().add(0, 0, departmentUsers.indexOf(u), u.getName()));

            popup.setOnMenuItemClickListener(item -> {
                if(item.getTitle().equals(getApplication().getString(R.string.all))){ SelectedUserId.setValue(null);
                }else { SelectedUserId.setValue(departmentUsers.get(item.getOrder()).getId());}
                return true;
            });

            popup.show();
        }
    }

    public String getSelectedWeekText(){
        if(SelectedWeekStart.getValue() != null) {
            WeekFields weekFields = WeekFields.of(Locale.ENGLISH);
            return "UGE: " + SelectedWeekStart.getValue().get(weekFields.weekOfYear()) + "\n" + SelectedWeekStart.getValue().format(dateFormatter) + " - " + SelectedWeekStart.getValue().plusDays(6).format(dateFormatter);
        }
        return null;
    }

    private int resolveOrThrow(Context context) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(R.attr.materialCalendarFullscreenTheme, typedValue, true)) {
            return typedValue.data;
        }
        throw new IllegalArgumentException(context.getResources().getResourceName(R.attr.materialCalendarFullscreenTheme));
    }

}
