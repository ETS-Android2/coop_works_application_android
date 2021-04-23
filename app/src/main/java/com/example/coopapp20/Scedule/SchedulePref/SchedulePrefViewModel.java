package com.example.coopapp20.Scedule.SchedulePref;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.coopapp20.Data.Objects.OffDayRequestObject;
import com.example.coopapp20.Data.Objects.SchedulePreferenceObject;
import com.example.coopapp20.Data.Objects.ShiftPreferenceObject;
import com.example.coopapp20.Data.Objects.UserObject;
import com.example.coopapp20.R;
import com.example.coopapp20.Scedule.ScheduleRepository;
import com.example.coopapp20.databinding.DialogSchedulePrefOffdayrequestCommentBinding;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

public class SchedulePrefViewModel extends AndroidViewModel {

    private ScheduleRepository repository = new ScheduleRepository(getApplication());

    private Observer observer = o -> {};
    private LiveData<SchedulePreferenceObject> SchedulePreferences;
    private LiveData<List<ScheduleShiftPreferenceObject>> ShiftPreferences;
    private LiveData<List<ScheduleOffDayRequestObject>> OffDayRequests;
    private LiveData<LocalDate> EndOffCurrentSchedule;
    private MutableLiveData<UserObject> CurrentUser = new MutableLiveData<>();

    private boolean PickerOpen = false;

    public SchedulePrefViewModel(@NonNull Application application) {
        super(application);

        OffDayRequests = Transformations.switchMap(CurrentUser, input -> repository.getUserOffDayRequests(input != null ? input.getId() : null));
        ShiftPreferences = Transformations.switchMap(CurrentUser, input -> repository.getUserShiftPreferences(input != null ? input.getId() : null));
        SchedulePreferences = Transformations.switchMap(CurrentUser, input -> repository.getSchedulePreferences(input != null ? input.getId() : null));
        EndOffCurrentSchedule = Transformations.switchMap(CurrentUser, input -> Transformations.map(repository.getScheduleValues(input != null ? input.getDepartmentId() : null), input1 -> input1 != null && input1.getScheduleBeginning() != null ? input1.getScheduleBeginning().plusWeeks(input1.getScheduleWeekDuration()) : null));

        EndOffCurrentSchedule.observeForever(observer);
    }

    LiveData<List<ScheduleShiftPreferenceObject>> getShiftPreferences() { return ShiftPreferences; }
    LiveData<List<ScheduleOffDayRequestObject>> getOffDayRequests() { return OffDayRequests; }
    LiveData<SchedulePreferenceObject> getSchedulePreferences() { return SchedulePreferences; }

    void setCurrentUser(UserObject currentUser) {CurrentUser.setValue(currentUser);}

    void onSchedulePrefExecuteBtnClick(int PrefWorkDays, int MaxWorkDays){
        if(getSchedulePreferences().getValue() != null) {
            SchedulePreferenceObject object = getSchedulePreferences().getValue();
            object.setPrefDays(PrefWorkDays);
            object.setMaxDays(MaxWorkDays);
            repository.UpdateSchedulePreferences(object);
        }
    }
    void onSchedulePrefShiftClick(ScheduleShiftPreferenceObject preference){
        repository.UpdateShiftPreferences(preference.getValue().equals(3) ? 0 : preference.getValue()+1, preference.getId());
    }
    void onSchedulePrefOffDayRequestClick(ScheduleOffDayRequestObject request, View v, FragmentManager fm, Context context){

        PopupMenu popupMenu = new PopupMenu(getApplication(), v);
        popupMenu.getMenu().add(getApplication().getString(R.string.offdayrequest_popupmenuitem_reason));
        popupMenu.getMenu().add(getApplication().getString(R.string.offdayrequest_popupmenuitem_date));
        popupMenu.getMenu().add(getApplication().getString(R.string.offdayrequest_popupmenuitem_delete));
        popupMenu.setOnMenuItemClickListener(item -> {
            if(item.getTitle().toString().equals(getApplication().getString(R.string.offdayrequest_popupmenuitem_reason))){
                //Change OffDayRequest reason
                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogSchedulePrefOffdayrequestCommentBinding Binding = DialogSchedulePrefOffdayrequestCommentBinding.inflate(dialog.getLayoutInflater());
                dialog.setContentView(Binding.getRoot());
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

                //reference dialog layout
                Binding.CommentTextView.setText(request.getComment());
                Binding.executeBtn.setOnClickListener(vv -> {repository.UpdateOffDayRequest(Binding.CommentTextView.getText().toString(), request.getStartDate(), request.getEndDate(), request.getId());dialog.cancel();});

                dialog.show();
            }
            else if(item.getTitle().toString().equals(getApplication().getString(R.string.offdayrequest_popupmenuitem_date))){
                //Change OffDayRequest dates
                openDatePicker(request,null,fm, context);
            }
            else if(item.getTitle().toString().equals(getApplication().getString(R.string.offdayrequest_popupmenuitem_delete))){
                //delete OffDayRequest
                repository.DeleteOffDayRequest(request.getId());
            }

            return true;
        });
        popupMenu.show();
    }

    void openDatePicker(ScheduleOffDayRequestObject offDayRequest, Integer type, FragmentManager fm, Context context) throws IllegalArgumentException {
       if(EndOffCurrentSchedule.getValue() != null && !PickerOpen && CurrentUser.getValue() != null) {
            PickerOpen = true;
            MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();

            //set range for a year from now, and available dates after end of current schedule
            CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
            constraintsBuilder.setStart(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
            constraintsBuilder.setEnd(LocalDate.now().plusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
            constraintsBuilder.setValidator(DateValidatorPointForward.from(EndOffCurrentSchedule.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()));


            builder.setTitleText("Calendar");
            builder.setTheme(resolveOrThrow(context));
            builder.setCalendarConstraints(constraintsBuilder.build());

            MaterialDatePicker<Pair<Long,Long>> picker = builder.build();
            picker.addOnDismissListener(v-> PickerOpen = false);

            picker.addOnPositiveButtonClickListener(v -> {

                LocalDate StartDate = Instant.ofEpochMilli(picker.getSelection().first).atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate EndDate = Instant.ofEpochMilli(picker.getSelection().second).atZone(ZoneId.systemDefault()).toLocalDate();

                if(offDayRequest == null) {
                    //Add OffDayRequest
                    OffDayRequestObject request = new OffDayRequestObject(null,type,StartDate,EndDate,null,null,CurrentUser.getValue().getId(),OffDayRequestObject.STATUS_PENDING);
                    repository.AddOffDayRequest(request);
                }else {
                    //Edit OffDayRequest

                    repository.UpdateOffDayRequest(offDayRequest.getComment(), StartDate, EndDate, offDayRequest.getId());
                }
            });
            picker.show(fm, picker.toString());
        }
    }

    private static int resolveOrThrow(Context context) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(R.attr.materialCalendarFullscreenTheme, typedValue, true)) {return typedValue.data;}
        throw new IllegalArgumentException(context.getResources().getResourceName(R.attr.materialCalendarFullscreenTheme));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        EndOffCurrentSchedule.removeObserver(observer);
    }
}
