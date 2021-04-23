package com.example.coopapp20.Scedule.ScheduleShiftChange;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.coopapp20.Contact.ContactViewModel;
import com.example.coopapp20.Data.Objects.ActiveShiftObject;
import com.example.coopapp20.Data.Objects.ShiftChangeRequestObject;
import com.example.coopapp20.Data.Objects.ShiftChangeResponseObject;
import com.example.coopapp20.Data.Objects.UserObject;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.R;
import com.example.coopapp20.Scedule.ScheduleRepository;
import com.example.coopapp20.databinding.DialogScheduleShiftchangerequestBinding;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ShiftChangeViewModel extends AndroidViewModel {


    private ScheduleRepository repository;

    private LiveData<List<ScheduleShiftChangeRequestObject>> ShiftChangeRequests;
    private LiveData<List<ScheduleShiftChangeResponseObject>> ShiftChangeResponses;
    private LiveData<List<ActiveShiftObject>> ShiftChangeSwitches;
    private LiveData<List<UserObject>> CurrentDepartmentUsers;

    private MutableLiveData<Integer> SelectedShiftChangeRequestId = new MutableLiveData<>();
    private LiveData<ScheduleShiftChangeRequestObject> SelectedShiftChangeRequest;
    private LiveData<ShiftChangeResponseObject> SelectedShiftChangeResponse;
    private LiveData<UserObject> CurrentUser;

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy", Locale.ENGLISH);

    public ShiftChangeViewModel(@NonNull Application application) {
        super(application);
        repository = new ScheduleRepository(application);

        //map values to selected ShiftChangeRequest
        ShiftChangeRequests = Transformations.map(repository.getAllShiftChangeRequests(), this::Sort);
        ShiftChangeResponses = Transformations.switchMap(SelectedShiftChangeRequestId, input -> repository.getAllShiftChangeResponses(input));
        ShiftChangeSwitches = Transformations.switchMap(ShiftChangeResponses, input -> Transformations.map(repository.getFutureActiveShifts(), input1 -> CreateShiftChangeSwitches(input,input1)));
        SelectedShiftChangeResponse = Transformations.switchMap(SelectedShiftChangeRequestId, input -> repository.getUserShiftChangeResponse(CurrentUser.getValue().getId(), input));
        SelectedShiftChangeRequest = Transformations.switchMap(SelectedShiftChangeRequestId, input -> repository.getSelectedScheduleShiftChangeRequest(input));

        CurrentDepartmentUsers = Transformations.switchMap(CurrentUser, input -> repository.getDepartmentUsers(input.getDepartmentId()));
    }

    public LiveData<List<ScheduleShiftChangeRequestObject>> getShiftChangeRequests() { return ShiftChangeRequests; }
    public LiveData<List<ScheduleShiftChangeResponseObject>> getShiftChangeResponses() { return ShiftChangeResponses; }
    public LiveData<List<ActiveShiftObject>> getShiftChangeSwitches() { return ShiftChangeSwitches; }
    public LiveData<ScheduleShiftChangeRequestObject> getSelectedShiftChangeRequest() { return SelectedShiftChangeRequest; }
    public LiveData<ShiftChangeResponseObject> getCurrentShiftChangeResponse(){ return SelectedShiftChangeResponse; }
    public LiveData<List<ActiveShiftObject>> getCurrentUserFutureActiveShifts(){ return repository.getUserFutureActiveShifts(CurrentUser.getValue().getId());}
    public LiveData<UserObject> getCurrentUser() { return CurrentUser; }
    public void setCurrentUser(LiveData<UserObject> user){
        CurrentUser = user;
        CurrentDepartmentUsers = Transformations.switchMap(user, input -> repository.getDepartmentUsers(input.getDepartmentId()));
    }
    public void setSelectedShiftChangeRequestId(Integer RequestId){SelectedShiftChangeRequestId.setValue(RequestId);}

    public void Observe(LifecycleOwner owner){
        CurrentDepartmentUsers.observe(owner, userObjects -> {});
    }

    public onSwitchCLickListener getSwitchClickListener(){
        return (activeShift, v, mainViewModel, contactViewModel) -> {
            PopupMenu popupMenu = new PopupMenu(getApplication(), v);
            popupMenu.getMenu().add("giv svar");
            popupMenu.getMenu().add("gå til chat");
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.menu1:
                        //Accept suggested switch
                        //set request status
                        //set response status
                        //switch shift

                        return true;
                    case R.id.menu2:
                        //go to contact chat
                        contactViewModel.setSelectedContactId(activeShift.getCurrentShiftHolder());
                        mainViewModel.getMainNavController().navigate(R.id.contactDetailFrag);
                        return true;
                    default:
                        return false;
                }
            });
            popupMenu.show();
        };
    }
    public onReceiverCLickListener getReceiverClickListener(){
        return (response, mainViewModel, contactViewModel) -> {
            if(SelectedShiftChangeRequest.getValue() != null && CurrentUser.getValue() != null) {
                if (SelectedShiftChangeRequest.getValue().getRequestSenderId().equals(CurrentUser.getValue().getId())){
                    contactViewModel.setSelectedContactId(response.getResponderId());
                    mainViewModel.getMainNavController().navigate(R.id.contactDetailFrag);
                }
            }
        };
    }
    public onDeleteBtnListener getDeleteBtnListener(){
        return (context,mainViewModel) -> {
            if(SelectedShiftChangeRequest.getValue() != null) {
                new AlertDialog.Builder(context)
                        .setTitle("VIL DU SLETTE VAGTBYTTE ANMODNINGEN?")
                        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                            repository.DeleteShiftChangeRequest(SelectedShiftChangeRequest.getValue().getRequestId());
                            mainViewModel.getMainNavController().popBackStack(R.id.scheduleShiftChangeFrag,true);
                            mainViewModel.getMainNavController().navigate(R.id.scheduleShiftChangeFrag);
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        };
    }
    public onSettingsBtnClickListener getSettingsBtnClickListener(){
        return context -> {
            if (SelectedShiftChangeRequest.getValue() != null && CurrentDepartmentUsers.getValue() != null) {
                //create dialog with custom layout
                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogScheduleShiftchangerequestBinding Binding = DialogScheduleShiftchangerequestBinding.inflate(dialog.getLayoutInflater());
                dialog.setContentView(Binding.getRoot());
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

                Binding.NecessitySwitch.setChecked(SelectedShiftChangeRequest.getValue().getRequestNecessity().equals(ShiftChangeRequestObject.NECESSITY_NEEDED));
                Binding.CommentTextView.setText(SelectedShiftChangeRequest.getValue().getRequestComment());
                Binding.ExecuteBtn.setText(context.getResources().getString(R.string.add_change));

                //set execute button
                Binding.ExecuteBtn.setOnClickListener(v ->
                        new AlertDialog.Builder(context)
                                .setTitle("VIL DU ÆNDRE ANMODNINGEN?")
                                .setPositiveButton(android.R.string.yes, (d, whichButton) -> {
                                    //Set new receiverlist and comment
                                    ScheduleShiftChangeRequestObject request = SelectedShiftChangeRequest.getValue();
                                    repository.UpdateShiftChangeRequest(request.getRequestId(), Binding.NecessitySwitch.isChecked() ? ShiftChangeRequestObject.NECESSITY_NEEDED : ShiftChangeRequestObject.NECESSITY_WANTED, Binding.CommentTextView.getText().toString());
                                    dialog.cancel();
                                })
                                .setNegativeButton(android.R.string.no, null).show());


                dialog.show();
            }
        };
    }
    public onOptionsBtnClickListener getOptionsBtnClickListener(){
        return (context, v, mainViewModel) -> {
            PopupMenu popupMenu = new PopupMenu(getApplication(), v);
            popupMenu.getMenu().add("besvar");
            popupMenu.getMenu().add("chat");

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("besvar")) { mainViewModel.getMainNavController().navigate(R.id.scheduleShiftChangeResponseFrag); }
                else if (item.getTitle().equals("chat")) { mainViewModel.getMainNavController().navigate(R.id.contactDetailFrag); }
                return true;
            });

            popupMenu.show();

        };
    }
    public onResponseBtnClickListener getResponseClickListener(){
        return (context, response, responseType, mainViewModel, SelectedActiveShifts) -> {
            switch (responseType) {
                case R.id.AcceptRadioBtn:
                    new AlertDialog.Builder(context)
                            .setTitle("VIL DU TAGE VAGT?")
                            .setPositiveButton(android.R.string.yes, (d, whichButton) -> {
                                response.setStatus(ShiftChangeResponseObject.SHIFTCHANGESTATUS_ACCEPTED);
                                repository.UpdateShiftChangeResponse(response);
                                // set new ActiveShift owner
                                mainViewModel.getMainNavController().popBackStack(R.id.scheduleShiftChangeDetailFrag, true);
                                mainViewModel.getMainNavController().navigate(R.id.scheduleShiftChangeDetailFrag);
                            })
                            .setNegativeButton(android.R.string.no, null).show();

                    break;
                case R.id.SwitchRadioBtn:
                    if(!SelectedActiveShifts.isEmpty()) {
                        new AlertDialog.Builder(context)
                                .setTitle("VIL DU FORSLÅ AT BYTTE VAGT?")
                                .setPositiveButton(android.R.string.yes, (d, whichButton) -> {
                                    response.setStatus(ShiftChangeResponseObject.SHIFTCHANGESTATUS_SWITCH_SUGGESTED);
                                    response.setShiftIdList(SelectedActiveShifts);
                                    repository.UpdateShiftChangeResponse(response);
                                    mainViewModel.getMainNavController().popBackStack(R.id.scheduleShiftChangeDetailFrag, true);
                                    mainViewModel.getMainNavController().navigate(R.id.scheduleShiftChangeDetailFrag);
                                })
                                .setNegativeButton(android.R.string.no, null).show();
                    }
                    break;
                case R.id.PendingRadioBtn:
                    response.setStatus(ShiftChangeResponseObject.SHIFTCHANGESTATUS_PENDING);
                    repository.UpdateShiftChangeResponse(response);
                    mainViewModel.getMainNavController().popBackStack(R.id.scheduleShiftChangeDetailFrag, true);
                    mainViewModel.getMainNavController().navigate(R.id.scheduleShiftChangeDetailFrag);
                    break;
                case R.id.DeclineRadioBtn:
                    new AlertDialog.Builder(context)
                            .setTitle("VIL DU AFVISE AT TAGE VAGT?")
                            .setPositiveButton(android.R.string.yes, (d, whichButton) -> {
                                response.setStatus(ShiftChangeResponseObject.SHIFTCHANGESTATUS_DECLINED);
                                repository.UpdateShiftChangeResponse(response);
                                mainViewModel.getMainNavController().popBackStack(R.id.scheduleShiftChangeDetailFrag, true);
                                mainViewModel.getMainNavController().navigate(R.id.scheduleShiftChangeDetailFrag);
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                    break;
            }
        };
    }

    private List<ScheduleShiftChangeRequestObject> Sort(List<ScheduleShiftChangeRequestObject> list) {
        if(list != null) {

            //sort by date
            Collections.sort(list, (o1,o2) -> o2.getShiftDate().compareTo(o1.getShiftDate()));

            //sort by active ShiftChangeRequest
            Collections.sort(list, (o1,o2) -> Boolean.compare(o2.getRequestStatus().equals(ShiftChangeRequestObject.SHIFTCHANGESTATUS_PENDING) || o2.getRequestStatus().equals(ShiftChangeRequestObject.SHIFTCHANGESTATUS_SWITCH_SUGGESTED), o1.getRequestStatus().equals(ShiftChangeRequestObject.SHIFTCHANGESTATUS_PENDING) || o1.getRequestStatus().equals(ShiftChangeRequestObject.SHIFTCHANGESTATUS_SWITCH_SUGGESTED)));

            //Sort by switch suggested to CurrentUser's ShiftChangeRequest
            Collections.sort(list, ((o1, o2) -> Boolean.compare(o2.getRequestStatus().equals(ShiftChangeRequestObject.SHIFTCHANGESTATUS_SWITCH_SUGGESTED) && o2.getRequestSenderId().equals(CurrentUser.getValue().getId()), o1.getRequestStatus().equals(ShiftChangeRequestObject.SHIFTCHANGESTATUS_SWITCH_SUGGESTED) && o1.getRequestSenderId().equals(CurrentUser.getValue().getId()))));

            return list;

        }else {return null;}
    }
    private List<ActiveShiftObject> CreateShiftChangeSwitches(List<ScheduleShiftChangeResponseObject> responses, List<ActiveShiftObject> activeShifts){
        if(responses != null && activeShifts != null && SelectedShiftChangeRequest.getValue() != null) {

            ArrayList<ActiveShiftObject> switches = new ArrayList<>();


            for(ScheduleShiftChangeResponseObject response : responses){
                if(response.getResponseStatus().equals(ShiftChangeResponseObject.SHIFTCHANGESTATUS_SWITCH_SUGGESTED) || response.getResponseStatus().equals(ShiftChangeResponseObject.SHIFTCHANGESTATUS_SWITCHED)){
                    for(Integer id : response.getResponseShiftIdList()){
                        for(ActiveShiftObject a : activeShifts){
                            if(a.getId().equals(id) && response.getResponderId().equals(a.getCurrentShiftHolder())){
                                a.setDayString(getApplication().getResources().getStringArray(R.array.day_array)[a.getDate().getDayOfWeek().getValue()-1]);
                                a.setDateString(a.getDate().format(dateFormatter));
                                a.setTimeString(a.getStartTime().format(timeFormatter) + " - " + a.getEndTime().format(timeFormatter));
                                a.setSwitchStatusColor(ContextCompat.getColor(getApplication(),R.color.StatusPending));
                                a.setSwitchActive(SelectedShiftChangeRequest.getValue().getRequestStatus().equals(ShiftChangeRequestObject.SHIFTCHANGESTATUS_SWITCH_SUGGESTED));
                                a.setShiftHolderName(response.getResponderName());
                                switches.add(a);
                            }
                        }
                    }
                }
            }

            return switches;
        }else {return null;}
    }

    public interface onSwitchCLickListener{ void onSwitchClick(ActiveShiftObject activeShift, View v, MainViewModel mainViewModel, ContactViewModel contactViewModel);}
    public interface onReceiverCLickListener{void onReceiverClick(ScheduleShiftChangeResponseObject response, MainViewModel mainViewModel, ContactViewModel contactViewModel);}
    public interface  onDeleteBtnListener{void onDeleteBtnClick(Context context, MainViewModel mainViewModel);}
    public interface onSettingsBtnClickListener{ void onSettingsBtnClick(Context context);}
    public interface onOptionsBtnClickListener{ void onOptionsBtnClick(Context context, View v, MainViewModel mainViewModel);}
    public interface onResponseBtnClickListener{ void onRepsponseBtnClick(Context context, ShiftChangeResponseObject response, Integer responseType, MainViewModel mainViewModel, List<Integer> SelectedActiveShifts);}
}
