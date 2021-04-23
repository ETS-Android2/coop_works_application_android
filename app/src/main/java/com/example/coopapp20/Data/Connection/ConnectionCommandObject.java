package com.example.coopapp20.Data.Connection;

import com.example.coopapp20.Data.Objects.ActiveShiftObject;
import com.example.coopapp20.Data.Objects.DepartmentObject;
import com.example.coopapp20.Data.Objects.FinishedTaskObject;
import com.example.coopapp20.Data.Objects.MessageObject;
import com.example.coopapp20.Data.Objects.OffDayRequestObject;
import com.example.coopapp20.Data.Objects.ProductChangeObject;
import com.example.coopapp20.Data.Objects.ProductObject;
import com.example.coopapp20.Data.Objects.SchedulePreferenceObject;
import com.example.coopapp20.Data.Objects.ScheduleValueObject;
import com.example.coopapp20.Data.Objects.ShiftObject;
import com.example.coopapp20.Data.Objects.ShiftPreferenceObject;
import com.example.coopapp20.Data.Objects.TaskObject;
import com.example.coopapp20.Data.Objects.TaskSortingValueObject;
import com.example.coopapp20.Data.Objects.UserObject;
import com.google.gson.Gson;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ConnectionCommandObject {

    public static final int Other = 0;
    public static final int Add = 1;
    public static final int Edit = 2;
    public static final int Delete = 3;

    private static final int LoginAttempt = 0;
    private static final int getDataChanges = 1;
    private static final int getDatabase = 2;

    static final int User = 0;
    static final int Department = 1;
    static final int SchedulePreference = 2;
    static final int ShiftPreference = 3;
    static final int OffDayRequest = 4;
    static final int Task = 5;
    static final int FinishedTask = 6;
    static final int Shift = 7;
    static final int ActiveShift = 8;
    static final int Product = 9;
    static final int ProductChange = 10;
    static final int Message = 11;
    static final int ScheduleValues = 12;
    static final int TaskSortingValues = 13;

    public static Integer CurrentUserId = null;
    
    private Boolean CommandAvailable = false;
    private Boolean ReturnExpected = false;
    private ArrayList<String> Command = new ArrayList<>();

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
    private Gson gson = new Gson();

    public void LoginAttempt(String username, String password, String ip, String port){
        if (!CommandAvailable) {
            if (username != null && password != null && ip != null && port != null){
                Command.add(String.valueOf(Other));
                Command.add(String.valueOf(LoginAttempt));
                Command.add(username);
                Command.add(password);
                Command.add(ip);
                Command.add(port);
                CommandAvailable = true;
            }
        }
    }

    void getDataChanges(){
        if (!CommandAvailable) {
            Command.add(String.valueOf(Other));
            Command.add(String.valueOf(getDataChanges));
            Command.add(String.valueOf(CurrentUserId));
            CommandAvailable = true;
            ReturnExpected = true;
        }
    }

    public void getLocalDatabase(){
        if(!CommandAvailable){
            Command.add(String.valueOf(Other));
            Command.add(String.valueOf(getDatabase));
            CommandAvailable = true;
            ReturnExpected = true;
        }
    }

    public ConnectionCommandObject User(int ChangeType, UserObject user) {
        if (!CommandAvailable) {
            Command.add(String.valueOf(ChangeType));
            Command.add(String.valueOf(User));
            Command.add(String.valueOf(CurrentUserId));
            Command.add(String.valueOf(user.getId()));
            Command.add(user.getName());
            Command.add(user.getEmail());
            Command.add(user.getPhoneNr());
            Command.add(String.valueOf(user.getDepartmentId()));
            Command.add(user.getUserName());
            Command.add(user.getPassword());
            Command.add(String.valueOf(user.getStatus()));
            Command.add(user.getIpAddress());
            Command.add(String.valueOf(user.getPortNr()));
            CommandAvailable = true;
        }
        return this;
    }
    public ConnectionCommandObject Department(int ChangeType, DepartmentObject department){
        if(!CommandAvailable) {
            Command.add(String.valueOf(ChangeType));
            Command.add(String.valueOf(Department));
            Command.add(String.valueOf(CurrentUserId));
            Command.add(String.valueOf(department.getId()));
            Command.add(department.getName());
            Command.add(department.getColor());
            CommandAvailable = true;
        }
        return this;
    }
    public ConnectionCommandObject SchedulePreference(int ChangeType, SchedulePreferenceObject preferences){
        if (!CommandAvailable) {
            Command.add(String.valueOf(ChangeType));
            Command.add(String.valueOf(SchedulePreference));
            Command.add(String.valueOf(CurrentUserId));
            Command.add(String.valueOf(preferences.getId()));
            Command.add(String.valueOf(preferences.getPrefDays()));
            Command.add(String.valueOf(preferences.getMaxDays()));
            Command.add(String.valueOf(preferences.getUserId()));
            CommandAvailable = true;
        }
        return this;
    }
    public ConnectionCommandObject ShiftPreference(int ChangeType, ShiftPreferenceObject preferences){
        if (!CommandAvailable) {
            Command.add(String.valueOf(ChangeType));
            Command.add(String.valueOf(ShiftPreference));
            Command.add(String.valueOf(CurrentUserId));
            Command.add(String.valueOf(preferences.getId()));
            Command.add(String.valueOf(preferences.getDay()));
            Command.add(preferences.getStartTime().format(timeFormatter));
            Command.add(String.valueOf(preferences.getValue()));
            Command.add(String.valueOf(preferences.getShiftId()));
            Command.add(String.valueOf(preferences.getUserId()));
            CommandAvailable = true;
        }
        return this;
    }
    public ConnectionCommandObject OffDayRequest(int ChangeType, OffDayRequestObject offDayRequest){
        if (!CommandAvailable) {
            Command.add(String.valueOf(ChangeType));
            Command.add(String.valueOf(OffDayRequest));
            Command.add(String.valueOf(CurrentUserId));
            Command.add(String.valueOf(offDayRequest.getId()));
            Command.add(String.valueOf(offDayRequest.getType()));
            Command.add(offDayRequest.getStartDate().format(dateFormatter));
            Command.add(offDayRequest.getEndDate().format(dateFormatter));
            Command.add(String.valueOf(offDayRequest.getReason()));
            Command.add(offDayRequest.getComment());
            Command.add(String.valueOf(offDayRequest.getUserId()));
            Command.add(String.valueOf(offDayRequest.getStatus()));
            CommandAvailable = true;
        }
        return this;
    }
    public ConnectionCommandObject Task(int ChangeType, TaskObject task){
        if (!CommandAvailable) {
            Command.add(String.valueOf(ChangeType));
            Command.add(String.valueOf(Task));
            Command.add(String.valueOf(CurrentUserId));
            Command.add(String.valueOf(task.getId()));
            Command.add(task.getTitle());
            Command.add(task.getTimeString());
            Command.add(task.getIdealExecutionTime() == null ? "" : task.getIdealExecutionTime().format(timeFormatter));
            Command.add(String.valueOf(task.getDays()));
            Command.add(task.getDescription());
            Command.add(String.valueOf(task.getDepartmentId()));
            Command.add(String.valueOf(task.getPrioritization()));
            Command.add(String.valueOf(task.getRepeat()));
            Command.add(task.getLastCompletionDate().atTime(task.getLastCompletionTime()).format(dateTimeFormatter));
            CommandAvailable = true;
        }
        return this;
    }
    public ConnectionCommandObject FinishedTask(int ChangeType, FinishedTaskObject finishedTask){
        if (!CommandAvailable) {
            Command.add(String.valueOf(ChangeType));
            Command.add(String.valueOf(FinishedTask));
            Command.add(String.valueOf(CurrentUserId));
            Command.add(String.valueOf(finishedTask.getId() == null ? finishedTask.getId() : finishedTask.getId()));
            Command.add(finishedTask.getCompletionDate().atTime(finishedTask.getCompletionTime()).format(dateTimeFormatter));
            //Command.add(finishedTask.getDuration()..format(timeFormatter));
            Command.add(String.valueOf(finishedTask.getEvaluation()));
            Command.add(String.valueOf(finishedTask.getComment()));
            Command.add(String.valueOf(finishedTask.getUserId()));
            Command.add(String.valueOf(finishedTask.getTaskId()));
            Command.add(gson.toJson(finishedTask.getTask()));
            CommandAvailable = true;
        }
        return this;
    }
    public ConnectionCommandObject Shift(int ChangeType, ShiftObject shift){
        if (!CommandAvailable) {
            Command.add(String.valueOf(ChangeType));
            Command.add(String.valueOf(Shift));
            Command.add(String.valueOf(CurrentUserId));
            Command.add(String.valueOf(shift.getId() == null ? shift.getId() : shift.getId()));
            Command.add(String.valueOf(shift.getDepartmentId()));
            Command.add(String.valueOf(shift.getDay()));
            Command.add(shift.getStartTime().format(timeFormatter));
            Command.add(shift.getEndTime().format(timeFormatter));
            CommandAvailable = true;
        }
        return this;
    }
    public ConnectionCommandObject ActiveShift(int ChangeType, ActiveShiftObject activeShift){
        if (!CommandAvailable) {
            Command.add(String.valueOf(ChangeType));
            Command.add(String.valueOf(ActiveShift));
            Command.add(String.valueOf(CurrentUserId));
            Command.add(String.valueOf(activeShift.getId()));
            Command.add(activeShift.getDate().format(dateFormatter));
            Command.add(activeShift.getStartTime().format(timeFormatter));
            Command.add(activeShift.getEndTime().format(timeFormatter));
            //Command.add(activeShift.getBreakTime().format(timeFormatter));
            Command.add(String.valueOf(activeShift.getFirstShiftHolder()));
            Command.add(String.valueOf(activeShift.getCurrentShiftHolder()));
            Command.add(String.valueOf(activeShift.getShiftId()));
            CommandAvailable = true;
        }
        return this;
    }
    public ConnectionCommandObject Product(int ChangeType, ProductObject product){
        if (!CommandAvailable) {
            Command.add(String.valueOf(ChangeType));
            Command.add(String.valueOf(Product));
            Command.add(String.valueOf(CurrentUserId));
            Command.add(String.valueOf(product.getId()));
            Command.add(product.getName());
            Command.add(product.getManufacture());
            Command.add(String.valueOf(product.getPrice()));
            Command.add(String.valueOf(product.getCost()));
            Command.add(product.getDiscount());
            Command.add(String.valueOf(product.getInStore()));
            Command.add(product.getInformation());
            Command.add(product.getBarcode());
            CommandAvailable = true;
        }
        return this;
    }
    public ConnectionCommandObject ProductChange(int ChangeType, ProductChangeObject productChange){
        if (!CommandAvailable) {
            Command.add(String.valueOf(ChangeType));
            Command.add(String.valueOf(ProductChange));
            Command.add(String.valueOf(CurrentUserId));
            Command.add(String.valueOf(productChange.getId()));
            Command.add(String.valueOf(productChange.getChangeType()));
            Command.add(String.valueOf(productChange.getAmount()));
            Command.add(String.valueOf(productChange.getProductId()));
            Command.add(productChange.getDateTime().format(dateFormatter));
            CommandAvailable = true;
        }
        return this;
    }
    public ConnectionCommandObject Message(int ChangeType, MessageObject message){
        if (!CommandAvailable) {
            Command.add(String.valueOf(ChangeType));
            Command.add(String.valueOf(Message));
            Command.add(String.valueOf(CurrentUserId));
            Command.add(String.valueOf(message.getId()));
            Command.add(String.valueOf(message.getSender()));
            Command.add(String.valueOf(message.getReceiver()));
            Command.add(message.getDateTime().format(dateTimeFormatter));
            //Command.add(String.valueOf(message.getViewed()));
            Command.add(String.valueOf(message.getType()));
            Command.add(message.getMessage());
            CommandAvailable = true;
        }
        return this;
    }
    public ConnectionCommandObject ScheduleValues(int ChangeType, ScheduleValueObject scheduleValues){
        if (!CommandAvailable) {
            Command.add(String.valueOf(ChangeType));
            Command.add(String.valueOf(ScheduleValues));
            Command.add(String.valueOf(CurrentUserId));
            Command.add(String.valueOf(scheduleValues.getId()));
            Command.add(scheduleValues.getPreferenceDeadline() != null ? scheduleValues.getPreferenceDeadline().format(dateFormatter) : null);
            Command.add(scheduleValues.getCreationDeadline() != null ? scheduleValues.getCreationDeadline().format(dateFormatter) : null);
            Command.add(scheduleValues.getScheduleBeginning() != null ? scheduleValues.getScheduleBeginning().format(dateFormatter) : null);
            Command.add(String.valueOf(scheduleValues.getScheduleWeekDuration()));
            Command.add(String.valueOf(scheduleValues.getPrefValueOnePoints()));
            Command.add(String.valueOf(scheduleValues.getPrefValueTwoPoints()));
            Command.add(String.valueOf(scheduleValues.getPrefValueThreePoints()));
            Command.add(String.valueOf(scheduleValues.getPrefWorkDaysDistPercent()));
            Command.add(String.valueOf(scheduleValues.getMedianPointsDistPercent()));
            Command.add(String.valueOf(scheduleValues.getScheduleOngoing()));
            Command.add(String.valueOf(scheduleValues.getDepartmentId()));
            CommandAvailable = true;
        }
        return this;
    }
    public ConnectionCommandObject TaskSortingValues(int ChangeType, TaskSortingValueObject taskSortingValues){
        if (!CommandAvailable) {
            Command.add(String.valueOf(ChangeType));
            Command.add(String.valueOf(TaskSortingValues));
            Command.add(String.valueOf(CurrentUserId));
            Command.add(String.valueOf(taskSortingValues.getId()));
            Command.add(String.valueOf(taskSortingValues.getTaskPercentageLeftFactor()));
            Command.add(String.valueOf(taskSortingValues.getTaskDaysLeftFactor()));
            Command.add(String.valueOf(taskSortingValues.getTaskDoneDividingFactor()));
            Command.add(String.valueOf(taskSortingValues.getSingleTaskValueOnePoints()));
            Command.add(String.valueOf(taskSortingValues.getSingleTaskValueTwoPoints()));
            Command.add(String.valueOf(taskSortingValues.getRepeatTaskValueOnePoints()));
            Command.add(String.valueOf(taskSortingValues.getRepeatTaskValueTwoPoints()));
            Command.add(String.valueOf(taskSortingValues.getDepartmentId()));
            CommandAvailable = true;
        }
        return this;
    }

    public Boolean getCommandAvailable() {return CommandAvailable;}

    public ArrayList<String> getCommand(){ return Command; }

    public String getCommandJson(){ return gson.toJson(Command); }

    String getCommandJsonPackage() {
        ArrayList<ArrayList<String>> CommandArray = new ArrayList<>();
        CommandArray.add(Command);
        return gson.toJson(CommandArray);}

    Boolean getReturnExpected() {return ReturnExpected;}

}