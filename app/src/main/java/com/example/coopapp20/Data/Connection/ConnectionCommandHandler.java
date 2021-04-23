package com.example.coopapp20.Data.Connection;

import android.content.Context;
import android.util.Log;

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
import com.example.coopapp20.Data.RoomDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.coopapp20.Data.Connection.ConnectionCommandObject.ActiveShift;
import static com.example.coopapp20.Data.Connection.ConnectionCommandObject.Add;
import static com.example.coopapp20.Data.Connection.ConnectionCommandObject.Delete;
import static com.example.coopapp20.Data.Connection.ConnectionCommandObject.Department;
import static com.example.coopapp20.Data.Connection.ConnectionCommandObject.Edit;
import static com.example.coopapp20.Data.Connection.ConnectionCommandObject.FinishedTask;
import static com.example.coopapp20.Data.Connection.ConnectionCommandObject.Message;
import static com.example.coopapp20.Data.Connection.ConnectionCommandObject.OffDayRequest;
import static com.example.coopapp20.Data.Connection.ConnectionCommandObject.Other;
import static com.example.coopapp20.Data.Connection.ConnectionCommandObject.Product;
import static com.example.coopapp20.Data.Connection.ConnectionCommandObject.ProductChange;
import static com.example.coopapp20.Data.Connection.ConnectionCommandObject.SchedulePreference;
import static com.example.coopapp20.Data.Connection.ConnectionCommandObject.ScheduleValues;
import static com.example.coopapp20.Data.Connection.ConnectionCommandObject.Shift;
import static com.example.coopapp20.Data.Connection.ConnectionCommandObject.ShiftPreference;
import static com.example.coopapp20.Data.Connection.ConnectionCommandObject.Task;
import static com.example.coopapp20.Data.Connection.ConnectionCommandObject.TaskSortingValues;
import static com.example.coopapp20.Data.Connection.ConnectionCommandObject.User;

public class ConnectionCommandHandler {

    private Context context;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss", Locale.ENGLISH);
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy", Locale.ENGLISH);
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);

    public ConnectionCommandHandler(Context context){ this.context = context;}

    public Boolean Execute(String JSONarray){

        boolean CommandArrayAccepted = true;

        //Attempt to execute command, on fail, return false

            //Read GSON parsed command
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ArrayList<String>>>() {}.getType();
            ArrayList<ArrayList<String>> CommandArray = gson.fromJson(JSONarray, type);

            //Prepare local database
            RoomDatabase Rdatabase = RoomDatabase.getDatabase(context);

            //Log.e("ConnectionCommandHandler", "CommandArray.size: "+CommandArray.size());

            CommandArray.forEach(Command -> {
                boolean CommandAccepted = true;
                try {
                    //Log.e("ConnectionCommandHandler", "Command:\n\t\t\t" + Command);

                    switch (Integer.parseInt(Command.get(0))) {
                        case Other:
                            Integer.parseInt(Command.get(1));
                            break;
                        case Add:
                            switch (Integer.parseInt(Command.get(1))) {
                                case User: {
                                    Rdatabase.userDao().insert(
                                            new UserObject(
                                                    Command.get(2) != null && !Command.get(2).equals("") ? Integer.valueOf(Command.get(2)) : null,
                                                    Command.get(3), Command.get(4), Command.get(5),
                                                    Command.get(6) != null && !Command.get(6).equals("") ? Integer.valueOf(Command.get(6)) : null,
                                                    Command.get(7), Command.get(8),
                                                    Command.get(9) != null && !Command.get(9).equals("") ? Integer.valueOf(Command.get(9)) : null,
                                                    Command.get(10),
                                                    Command.get(11) != null && !Command.get(11).equals("") ? Integer.valueOf(Command.get(11)) : null));
                                    break;
                                }
                                case Department: {
                                    Rdatabase.departmentDao().insert(new DepartmentObject(Integer.parseInt(Command.get(2)), Command.get(3), Command.get(4)));
                                    break;
                                }
                                case SchedulePreference: {
                                    Rdatabase.schedulePreferenceDao().insert(new SchedulePreferenceObject(Integer.parseInt(Command.get(2)), Integer.parseInt(Command.get(3)), Integer.parseInt(Command.get(4)), Integer.parseInt(Command.get(5))));
                                    break;
                                }
                                case ShiftPreference: {
                                    Rdatabase.shiftPreferenceDao().insert(new ShiftPreferenceObject(Integer.parseInt(Command.get(2)), Integer.parseInt(Command.get(3)), LocalTime.parse(Command.get(4),timeFormatter), LocalTime.parse(Command.get(4),timeFormatter), Integer.parseInt(Command.get(5)), Integer.parseInt(Command.get(6)), Integer.parseInt(Command.get(7))));
                                    break;
                                }
                                case OffDayRequest: {
                                    Rdatabase.offDayRequestDao().insert(new OffDayRequestObject(Integer.parseInt(Command.get(2)), Integer.parseInt(Command.get(3)), LocalDate.parse(Command.get(4), dateFormatter), LocalDate.parse(Command.get(5), dateFormatter), Integer.parseInt(Command.get(6)),  Command.get(7), Integer.parseInt(Command.get(8)), Integer.parseInt(Command.get(9))));
                                    break;
                                }
                                case Task: {
                                    Rdatabase.taskDao().insert(new TaskObject(Integer.parseInt(Command.get(2)), Command.get(3), Duration.parse(Command.get(4)), Command.get(5).equals("") ? null : LocalTime.parse(Command.get(5), timeFormatter), Integer.parseInt(Command.get(6)), Command.get(7), Integer.parseInt(Command.get(8)), Integer.parseInt(Command.get(9)), Boolean.valueOf(Command.get(10)), LocalDate.parse(Command.get(11), dateFormatter), LocalTime.parse(Command.get(11), timeFormatter)));
                                    break;
                                }
                                case FinishedTask: {
                                    Rdatabase.finishedTaskDao().insert(new FinishedTaskObject(Integer.valueOf(Command.get(2)), LocalDate.parse(Command.get(3), dateFormatter), LocalTime.parse(Command.get(3),timeFormatter), Duration.parse(Command.get(4)), Integer.parseInt(Command.get(5)), Command.get(6), Integer.parseInt(Command.get(7)), Integer.parseInt(Command.get(8)), gson.fromJson(Command.get(9), TaskObject.class)));
                                    break;
                                }
                                case Shift: {
                                    Rdatabase.shiftDao().insert(new ShiftObject(Integer.parseInt(Command.get(2)), Integer.parseInt(Command.get(3)), Integer.parseInt(Command.get(4)), LocalTime.parse(Command.get(5), timeFormatter), LocalTime.parse(Command.get(6), timeFormatter)));
                                    break;
                                }
                                case ActiveShift: {
                                    Rdatabase.activeShiftDao().insert(new ActiveShiftObject(Integer.valueOf(Command.get(2)), LocalDate.parse(Command.get(3), dateFormatter), LocalTime.parse(Command.get(4), timeFormatter), LocalTime.parse(Command.get(5), timeFormatter), Duration.parse(Command.get(6)), Integer.parseInt(Command.get(7)), Integer.parseInt(Command.get(8)), Integer.parseInt(Command.get(9))));
                                    break;
                                }
                                case Product: {
                                    Rdatabase.productDao().insert(new ProductObject( Integer.parseInt(Command.get(2)), Command.get(3), Command.get(4), Float.valueOf(Command.get(5)), Float.valueOf(Command.get(6)), Command.get(7), Integer.parseInt(Command.get(8)), Command.get(9), Command.get(10)));
                                    break;
                                }
                                case ProductChange: {
                                    Rdatabase.productChangeDao().insert(new ProductChangeObject( Integer.parseInt(Command.get(2)), Integer.parseInt(Command.get(3)), Integer.parseInt(Command.get(4)), Integer.parseInt(Command.get(5)), LocalDateTime.parse(Command.get(6),dateTimeFormatter)));
                                    break;
                                }
                                case Message: {
                                    Rdatabase.messageDao().insert(new MessageObject( Integer.parseInt(Command.get(2)), Integer.parseInt(Command.get(3)), Integer.parseInt(Command.get(4)), LocalDateTime.parse(Command.get(5), dateTimeFormatter), null, Integer.parseInt(Command.get(7)), Command.get(8)));
                                    break;
                                }
                                case ScheduleValues: {
                                    Rdatabase.scheduleValueDao().insert(new ScheduleValueObject( Integer.parseInt(Command.get(2)), LocalDate.parse(Command.get(3), dateFormatter), LocalDate.parse(Command.get(4), dateFormatter), LocalDate.parse(Command.get(5), dateFormatter), Integer.parseInt(Command.get(6)), Integer.parseInt(Command.get(7)), Integer.parseInt(Command.get(8)), Integer.parseInt(Command.get(9)), Integer.parseInt(Command.get(10)), Integer.parseInt(Command.get(11)), Boolean.parseBoolean(Command.get(12)), Integer.parseInt(Command.get(13))));
                                    break;
                                }
                                case TaskSortingValues: {
                                    Rdatabase.taskSortingValueDao().insert(new TaskSortingValueObject( Integer.parseInt(Command.get(2)), Integer.parseInt(Command.get(3)), Integer.parseInt(Command.get(4)), Integer.parseInt(Command.get(5)), Integer.parseInt(Command.get(6)), Integer.parseInt(Command.get(7)), Integer.parseInt(Command.get(8)), Integer.parseInt(Command.get(9)), Integer.parseInt(Command.get(10))));
                                    break;
                                }
                            }
                            break;
                        case Edit:
                            switch (Integer.parseInt(Command.get(1))) {
                                case User: {
                                    Rdatabase.userDao().update(new UserObject(Integer.parseInt(Command.get(2)), Command.get(3), Command.get(4), Command.get(5), Integer.parseInt(Command.get(6)), Command.get(7), Command.get(8), Integer.parseInt(Command.get(9)), Command.get(10), Integer.parseInt(Command.get(11))));
                                    break;
                                }
                                case Department: {
                                    Rdatabase.departmentDao().update(new DepartmentObject( Integer.parseInt(Command.get(2)), Command.get(3), Command.get(4)));
                                    break;
                                }
                                case SchedulePreference: {
                                    Rdatabase.schedulePreferenceDao().update(new SchedulePreferenceObject( Integer.parseInt(Command.get(2)), Integer.parseInt(Command.get(3)), Integer.parseInt(Command.get(4)), Integer.parseInt(Command.get(5))));
                                    break;
                                }
                                case ShiftPreference: {
                                    Rdatabase.shiftPreferenceDao().update(new ShiftPreferenceObject( Integer.parseInt(Command.get(2)), Integer.parseInt(Command.get(3)), LocalTime.parse(Command.get(4),timeFormatter), LocalTime.parse(Command.get(4),timeFormatter), Integer.parseInt(Command.get(5)), Integer.parseInt(Command.get(6)), Integer.parseInt(Command.get(7))));
                                    break;
                                }
                                case OffDayRequest: {
                                    Rdatabase.offDayRequestDao().update(new OffDayRequestObject( Integer.parseInt(Command.get(2)), Integer.parseInt(Command.get(3)), LocalDate.parse(Command.get(4), dateFormatter), LocalDate.parse(Command.get(5), dateFormatter), Integer.parseInt(Command.get(6)),  Command.get(7), Integer.parseInt(Command.get(8)), Integer.parseInt(Command.get(9))));
                                    break;
                                }
                                case Task: {
                                    Rdatabase.taskDao().update(new TaskObject( Integer.parseInt(Command.get(2)), Command.get(3), Duration.parse(Command.get(4)), LocalTime.parse(Command.get(5), timeFormatter), Integer.parseInt(Command.get(6)), Command.get(7), Integer.parseInt(Command.get(8)), Integer.parseInt(Command.get(9)), Boolean.valueOf(Command.get(10)), LocalDate.parse(Command.get(11), dateFormatter), LocalTime.parse(Command.get(11), timeFormatter)));
                                    break;
                                }
                                case FinishedTask: {
                                    Rdatabase.finishedTaskDao().update(new FinishedTaskObject( Integer.parseInt(Command.get(2)), LocalDate.parse(Command.get(3), dateFormatter), LocalTime.parse(Command.get(3), timeFormatter), Duration.parse(Command.get(4)), Integer.parseInt(Command.get(5)), Command.get(6), Integer.parseInt(Command.get(7)), Integer.parseInt(Command.get(8)), gson.fromJson(Command.get(9), TaskObject.class)));
                                    break;
                                }
                                case Shift: {
                                    Rdatabase.shiftDao().update(new ShiftObject( Integer.parseInt(Command.get(2)), Integer.parseInt(Command.get(3)), Integer.parseInt(Command.get(4)), LocalTime.parse(Command.get(5), timeFormatter), LocalTime.parse(Command.get(6), timeFormatter)));
                                    break;
                                }
                                case ActiveShift: {
                                    Rdatabase.activeShiftDao().update(new ActiveShiftObject( Integer.parseInt(Command.get(2)), LocalDate.parse(Command.get(3), dateFormatter), LocalTime.parse(Command.get(4), timeFormatter), LocalTime.parse(Command.get(5), timeFormatter), Duration.parse(Command.get(6)), Integer.parseInt(Command.get(7)), Integer.parseInt(Command.get(8)), Integer.parseInt(Command.get(9))));
                                    break;
                                }
                                case Product: {
                                    Rdatabase.productDao().update(new ProductObject( Integer.parseInt(Command.get(2)), Command.get(3), Command.get(4), Float.valueOf(Command.get(5)), Float.valueOf(Command.get(6)), Command.get(7), Integer.parseInt(Command.get(8)), Command.get(9), Command.get(10)));
                                    break;
                                }
                                case ProductChange: {
                                    Rdatabase.productChangeDao().update(new ProductChangeObject( Integer.parseInt(Command.get(2)), Integer.parseInt(Command.get(3)), Integer.parseInt(Command.get(4)), Integer.parseInt(Command.get(5)), LocalDateTime.parse(Command.get(6),dateTimeFormatter)));
                                    break;
                                }
                                case Message: {
                                    Rdatabase.messageDao().update(new MessageObject( Integer.parseInt(Command.get(2)), Integer.parseInt(Command.get(3)), Integer.parseInt(Command.get(4)), LocalDateTime.parse(Command.get(5), dateTimeFormatter), null, Integer.parseInt(Command.get(7)), Command.get(8)));
                                    break;
                                }
                                case ScheduleValues: {
                                    Rdatabase.scheduleValueDao().update(new ScheduleValueObject( Integer.parseInt(Command.get(2)), LocalDate.parse(Command.get(3), dateFormatter), LocalDate.parse(Command.get(4), dateFormatter), LocalDate.parse(Command.get(5), dateFormatter), Integer.parseInt(Command.get(6)), Integer.parseInt(Command.get(7)), Integer.parseInt(Command.get(8)), Integer.parseInt(Command.get(9)), Integer.parseInt(Command.get(10)), Integer.parseInt(Command.get(11)), Boolean.parseBoolean(Command.get(12)), Integer.parseInt(Command.get(13))));
                                    break;
                                }
                                case TaskSortingValues: {
                                    Rdatabase.taskSortingValueDao().update(new TaskSortingValueObject( Integer.parseInt(Command.get(2)), Integer.parseInt(Command.get(3)), Integer.parseInt(Command.get(4)), Integer.parseInt(Command.get(5)), Integer.parseInt(Command.get(6)), Integer.parseInt(Command.get(7)), Integer.parseInt(Command.get(8)), Integer.parseInt(Command.get(9)), Integer.parseInt(Command.get(10))));
                                    break;
                                }
                            }
                            break;
                        case Delete:
                            switch (Integer.parseInt(Command.get(1))) {
                                case User: {
                                    Rdatabase.userDao().delete(new UserObject( Integer.parseInt(Command.get(2)),null,null,null,null,null,null,null,null,null));
                                    break;
                                }
                                case Department: {
                                    Rdatabase.departmentDao().delete(new DepartmentObject( Integer.parseInt(Command.get(2)),null,null));
                                    break;
                                }
                                case SchedulePreference: {
                                    Rdatabase.schedulePreferenceDao().delete(new SchedulePreferenceObject( Integer.parseInt(Command.get(2)), Integer.parseInt(Command.get(3)), Integer.parseInt(Command.get(4)), Integer.parseInt(Command.get(5))));
                                    break;
                                }
                                case ShiftPreference: {
                                    Rdatabase.shiftPreferenceDao().delete(new ShiftPreferenceObject( Integer.parseInt(Command.get(2)),null,null,null,null,null,null));
                                    break;
                                }
                                case OffDayRequest: {
                                    Rdatabase.offDayRequestDao().delete(new OffDayRequestObject( Integer.parseInt(Command.get(2)),null,null,null,null,null,null,null));
                                    break;
                                }
                                case Task: {
                                    Rdatabase.taskDao().delete(new TaskObject( Integer.parseInt(Command.get(2)),null,null,null,null,null,null,null,null,null, null));
                                    break;
                                }
                                case FinishedTask: {
                                    Rdatabase.finishedTaskDao().delete(new FinishedTaskObject( Integer.parseInt(Command.get(2)),null,null,null,null,null,null,null,null));
                                    break;
                                }
                                case Shift: {
                                    Rdatabase.shiftDao().delete(new ShiftObject( Integer.parseInt(Command.get(2)),null,null,null,null));
                                    break;
                                }
                                case ActiveShift: {
                                    Rdatabase.activeShiftDao().delete(new ActiveShiftObject( Integer.parseInt(Command.get(2)),null,null,null,null,null,null,null));
                                    break;
                                }
                                case Product: {
                                    Rdatabase.productDao().delete(new ProductObject( Integer.parseInt(Command.get(2)),null,null,null,null,null,null,null,null));
                                    break;
                                }
                                case ProductChange: {
                                    Rdatabase.productChangeDao().delete(new ProductChangeObject( Integer.parseInt(Command.get(2)),null,null,null,null));
                                    break;
                                }
                                case Message: {
                                    Rdatabase.messageDao().delete(new MessageObject( Integer.parseInt(Command.get(2)),null,null,null,null,null,null));
                                    break;
                                }
                                case ScheduleValues: {
                                    Rdatabase.scheduleValueDao().delete(new ScheduleValueObject( Integer.parseInt(Command.get(2)),null,null,null,null,null,null,null,null,null,null,null));
                                    break;
                                }
                                case TaskSortingValues: {
                                    Rdatabase.taskSortingValueDao().delete(new TaskSortingValueObject( Integer.parseInt(Command.get(2)),null,null,null,null,null,null,null,null));
                                    break;
                                }
                            }
                    }
                }catch (Exception e){CommandAccepted = false;
                    Log.e("DatabaseCommandHandler",e.getMessage());
                    e.printStackTrace();
                }
            });

        return CommandArrayAccepted;
    }
}
