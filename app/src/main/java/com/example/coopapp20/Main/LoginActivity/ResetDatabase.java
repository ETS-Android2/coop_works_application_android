package com.example.coopapp20.Main.LoginActivity;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.coopapp20.Data.Objects.ActiveShiftObject;
import com.example.coopapp20.Data.Objects.DepartmentObject;
import com.example.coopapp20.Data.Objects.FinishedTaskObject;
import com.example.coopapp20.Data.Objects.MessageObject;
import com.example.coopapp20.Data.Objects.OffDayRequestObject;
import com.example.coopapp20.Data.Objects.ShiftChangeRequestObject;
import com.example.coopapp20.Data.Objects.ShiftChangeResponseObject;
import com.example.coopapp20.Data.Objects.ShiftObject;
import com.example.coopapp20.Data.Objects.TaskObject;
import com.example.coopapp20.Data.Objects.UserObject;
import com.example.coopapp20.Data.RoomDatabase;
import com.example.coopapp20.Main.MainRepository;
import com.example.coopapp20.R;

import java.sql.Array;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class ResetDatabase {

    private DateTimeFormatter TimeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddHH:mm", Locale.ENGLISH);

    @SuppressLint("ResourceType")
    public ResetDatabase(MainRepository repository, Context context){
        RoomDatabase.databaseWriteExecutor.execute(()->{
            repository.ClearDatabaseClean();

            //DEPARTMENTS
            int DepartmentId1 = repository.AddDepartmentClean(new DepartmentObject(null,"Servicemedarbejder",context.getResources().getString(R.color.Servicearbejder)));
            int DepartmentId2 = repository.AddDepartmentClean(new DepartmentObject(null,"Afdelingsleder",context.getResources().getString(R.color.Afdelingsleder)));
            int DepartmentId3 = repository.AddDepartmentClean(new DepartmentObject(null,"Bager",context.getResources().getString(R.color.Bager)));
            int DepartmentId4 = repository.AddDepartmentClean(new DepartmentObject(null,"Slagter",context.getResources().getString(R.color.Slagter)));

            //USERS
            int MartinId = repository.AddUserClean(new UserObject(null,"Martin","martin@eksempel.dk","12345678",DepartmentId1,"Martin","Martin",UserObject.STATUS_OFF_DUTY,null,null));
            int SofieId = repository.AddUserClean(new UserObject(null,"Sofie","Sofie@eksempel.dk","12345678",DepartmentId1,"Sofie","Sofie",UserObject.STATUS_OFF_DUTY,null,null));
            int AndreasId = repository.AddUserClean(new UserObject(null,"Andreas","Andreas@eksempel.dk","12345678",DepartmentId1,"Andreas","Andreas",UserObject.STATUS_OFF_DUTY,null,null));
            int AnnaId = repository.AddUserClean(new UserObject(null,"Anna","Anna@eksempel.dk","12345678",DepartmentId1,"Anna","Anna",UserObject.STATUS_OFF_DUTY,null,null));

            int JohanId = repository.AddUserClean(new UserObject(null,"Johan","Johan@eksempel.dk","12345678",DepartmentId2,"Johan","Johan",UserObject.STATUS_OFF_DUTY,null,null));
            repository.AddUserClean(new UserObject(null,"Cecillie","Cecillie@eksempel.dk","12345678",DepartmentId2,"Cecillie","Cecillie",UserObject.STATUS_OFF_DUTY,null,null));
            repository.AddUserClean(new UserObject(null,"Mie","Mie@eksempel.dk","12345678",DepartmentId2,"Mie","Mie",UserObject.STATUS_OFF_DUTY,null,null));
            repository.AddUserClean(new UserObject(null,"Mathilde","Mathilde@eksempel.dk","12345678",DepartmentId2,"Mathilde","Mathilde",UserObject.STATUS_OFF_DUTY,null,null));

            repository.AddUserClean(new UserObject(null,"Peter","Peter@eksempel.dk","12345678",DepartmentId3,"Peter","Peter",UserObject.STATUS_OFF_DUTY,null,null));
            repository.AddUserClean(new UserObject(null,"Thomas","Thomas@eksempel.dk","12345678",DepartmentId3,"Thomas","Thomas",UserObject.STATUS_OFF_DUTY,null,null));
            repository.AddUserClean(new UserObject(null,"Marie","Marie@eksempel.dk","12345678",DepartmentId3,"Marie","Marie",UserObject.STATUS_OFF_DUTY,null,null));
            repository.AddUserClean(new UserObject(null,"Lotte","Lotte@eksempel.dk","12345678",DepartmentId3,"Lotte","Lotte",UserObject.STATUS_OFF_DUTY,null,null));

            repository.AddUserClean(new UserObject(null,"Markus","Markus@eksempel.dk","12345678",DepartmentId4,"Markus","Markus",UserObject.STATUS_OFF_DUTY,null,null));
            repository.AddUserClean(new UserObject(null,"Sebastian","Sebastian@eksempel.dk","12345678",DepartmentId4,"Sebastian","Sebastian",UserObject.STATUS_OFF_DUTY,null,null));
            repository.AddUserClean(new UserObject(null,"Julie","Julie@eksempel.dk","12345678",DepartmentId4,"Julie","Julie",UserObject.STATUS_OFF_DUTY,null,null));
            repository.AddUserClean(new UserObject(null,"Sofia","Sofia@eksempel.dk","12345678",DepartmentId4,"Sofia","Sofia",UserObject.STATUS_OFF_DUTY,null,null));

            //TASKS
            int TaskId1 = repository.AddTaskClean(new TaskObject(null,"Trim gang 1", Duration.ofMinutes(20),null,4,"",DepartmentId1,TaskObject.PERFORM_BEFORE_DEADLINE,true, LocalDate.now().minusDays(5),LocalTime.parse("19:47",TimeFormatter)));
            int TaskId2 = repository.AddTaskClean(new TaskObject(null,"Trim gang 2", Duration.ofMinutes(20),null,7,"",DepartmentId1,TaskObject.PERFORM_BEFORE_DEADLINE,true, LocalDate.now().minusDays(4),LocalTime.parse("15:14",TimeFormatter)));
            int TaskId3 = repository.AddTaskClean(new TaskObject(null,"Trim gang 3", Duration.ofMinutes(10),null,7,"",DepartmentId1,TaskObject.PERFORM_BEFORE_DEADLINE,true, LocalDate.now().minusDays(4),LocalTime.parse("10:11",TimeFormatter)));
            int TaskId4 = repository.AddTaskClean(new TaskObject(null,"Trim gang 4", Duration.ofMinutes(35),null,5,"",DepartmentId1,TaskObject.PERFORM_BEFORE_DEADLINE,true, LocalDate.now().minusDays(4),LocalTime.parse("16:01",TimeFormatter)));
            int TaskId5 = repository.AddTaskClean(new TaskObject(null,"Trim gang 5", Duration.ofMinutes(25),null,7,"",DepartmentId1,TaskObject.PERFORM_BEFORE_DEADLINE,true, LocalDate.now().minusDays(3),LocalTime.parse("18:27",TimeFormatter)));
            int TaskId6 = repository.AddTaskClean(new TaskObject(null,"Trim gang 6", Duration.ofMinutes(20),null,9,"",DepartmentId1,TaskObject.PERFORM_BEFORE_DEADLINE,true, LocalDate.now().minusDays(3),LocalTime.parse("17:55",TimeFormatter)));
            int TaskId7 = repository.AddTaskClean(new TaskObject(null,"Trim gang 7", Duration.ofMinutes(15),null,7,"",DepartmentId1,TaskObject.PERFORM_BEFORE_DEADLINE,true, LocalDate.now().minusDays(2),LocalTime.parse("07:49",TimeFormatter)));
            int TaskId8 = repository.AddTaskClean(new TaskObject(null,"Trim gang 8", Duration.ofMinutes(20),null,10,"",DepartmentId1,TaskObject.PERFORM_BEFORE_DEADLINE,true, LocalDate.now().minusDays(2),LocalTime.parse("14:33",TimeFormatter)));
            int TaskId9 = repository.AddTaskClean(new TaskObject(null,"Trim gang 9", Duration.ofMinutes(20),null,7,"",DepartmentId1,TaskObject.PERFORM_BEFORE_DEADLINE,true, LocalDate.now(),LocalTime.now().minusMinutes(5)));

            repository.AddTaskClean(new TaskObject(null,"Trim ost", Duration.ofMinutes(30),null,9,"",DepartmentId1,TaskObject.PERFORM_BEFORE_DEADLINE,true, LocalDate.now().minusDays(5),LocalTime.parse("19:47",TimeFormatter)));
            repository.AddTaskClean(new TaskObject(null,"Trim mælk", Duration.ofMinutes(20),null,1,"",DepartmentId1,TaskObject.PERFORM_AD_DEADLINE,true, LocalDate.now().minusDays(4),LocalTime.parse("15:14",TimeFormatter)));
            repository.AddTaskClean(new TaskObject(null,"Trim pålæg", Duration.ofMinutes(10),null,7,"",DepartmentId1,TaskObject.PERFORM_AD_DEADLINE,true, LocalDate.now().minusDays(4),LocalTime.parse("10:11",TimeFormatter)));
            repository.AddTaskClean(new TaskObject(null,"Trim æg og fisk", Duration.ofMinutes(35),null,5,"",DepartmentId1,TaskObject.PERFORM_BEFORE_DEADLINE,true, LocalDate.now().minusDays(4),LocalTime.parse("16:01",TimeFormatter)));
            repository.AddTaskClean(new TaskObject(null,"Trim frugt og grønt standere", Duration.ofMinutes(25),null,3,"",DepartmentId1,TaskObject.PERFORM_OFTEN,true, LocalDate.now().minusDays(3),LocalTime.parse("18:27",TimeFormatter)));
            repository.AddTaskClean(new TaskObject(null,"Trim frugt og grønt hylder", Duration.ofMinutes(20),null,8,"",DepartmentId1,TaskObject.PERFORM_OFTEN,true, LocalDate.now().minusDays(3),LocalTime.parse("17:55",TimeFormatter)));
            repository.AddTaskClean(new TaskObject(null,"rengør bananhylder", Duration.ofMinutes(15),null,5,"",DepartmentId1,TaskObject.PERFORM_OFTEN,true, LocalDate.now().minusDays(2),LocalTime.parse("07:49",TimeFormatter)));
            repository.AddTaskClean(new TaskObject(null,"Trim salat og kryderihylder", Duration.ofMinutes(20),null,14,"",DepartmentId1,TaskObject.PERFORM_BEFORE_DEADLINE,true, LocalDate.now().minusDays(2),LocalTime.parse("14:33",TimeFormatter)));
            repository.AddTaskClean(new TaskObject(null,"puds frostlåger", Duration.ofMinutes(20),null,17,"",DepartmentId1,TaskObject.PERFORM_BEFORE_DEADLINE,true, LocalDate.now(),LocalTime.now().minusMinutes(5)));

            repository.AddTaskClean(new TaskObject(null,"puds skabslåger", Duration.ofMinutes(20),null,7,"",DepartmentId1,TaskObject.PERFORM_BEFORE_DEADLINE,true, LocalDate.now().minusDays(3),LocalTime.parse("17:55",TimeFormatter)));
            repository.AddTaskClean(new TaskObject(null,"puds frugt og grønt spejle", Duration.ofMinutes(15),null,12,"",DepartmentId1,TaskObject.PERFORM_BEFORE_DEADLINE,true, LocalDate.now().minusDays(2),LocalTime.parse("07:49",TimeFormatter)));
            repository.AddTaskClean(new TaskObject(null,"fyld op med brød", Duration.ofMinutes(20),null,7,"",DepartmentId1,TaskObject.PERFORM_BEFORE_DEADLINE,true, LocalDate.now().minusDays(2),LocalTime.parse("14:33",TimeFormatter)));
            repository.AddTaskClean(new TaskObject(null,"orden mælkerum", Duration.ofMinutes(20),null,1,"",DepartmentId1,TaskObject.PERFORM_AD_DEADLINE,true, LocalDate.now().minusDays(1),LocalTime.parse("16:27",TimeFormatter)));


            //SHIFTS
            int ShiftId1 = repository.AddShiftClean(new ShiftObject(null,DepartmentId1,ShiftObject.MONDAY,LocalTime.parse("16:00",TimeFormatter),LocalTime.parse("20:00",TimeFormatter)));
            int ShiftId2 = repository.AddShiftClean(new ShiftObject(null,DepartmentId1,ShiftObject.TUESDAY,LocalTime.parse("16:00",TimeFormatter),LocalTime.parse("20:00",TimeFormatter)));
            int ShiftId3 = repository.AddShiftClean(new ShiftObject(null,DepartmentId1,ShiftObject.WEDNESDAY,LocalTime.parse("16:00",TimeFormatter),LocalTime.parse("20:00",TimeFormatter)));
            int ShiftId4 = repository.AddShiftClean(new ShiftObject(null,DepartmentId1,ShiftObject.THURSDAY,LocalTime.parse("16:00",TimeFormatter),LocalTime.parse("20:00",TimeFormatter)));
            int ShiftId5 = repository.AddShiftClean(new ShiftObject(null,DepartmentId1,ShiftObject.FRIDAY,LocalTime.parse("16:00",TimeFormatter),LocalTime.parse("20:00",TimeFormatter)));
            int ShiftId6 = repository.AddShiftClean(new ShiftObject(null,DepartmentId1,ShiftObject.SATURDAY,LocalTime.parse("12:00",TimeFormatter),LocalTime.parse("18:00",TimeFormatter)));
            int ShiftId7 = repository.AddShiftClean(new ShiftObject(null,DepartmentId1,ShiftObject.SUNDAY,LocalTime.parse("12:00",TimeFormatter),LocalTime.parse("18:00",TimeFormatter)));

            //ACTIVESHIFTS
            int ActiveShiftId1 = repository.AddActiveShiftClean(new ActiveShiftObject(null,LocalDate.now().with(DayOfWeek.MONDAY),LocalTime.parse("16:00",TimeFormatter),LocalTime.parse("20:00",TimeFormatter), Duration.ofMinutes(0),MartinId,MartinId,ShiftId1));
            int ActiveShiftId2 = repository.AddActiveShiftClean(new ActiveShiftObject(null,LocalDate.now().with(DayOfWeek.TUESDAY),LocalTime.parse("16:00",TimeFormatter),LocalTime.parse("20:00",TimeFormatter), Duration.ofMinutes(0),MartinId,MartinId,ShiftId2));
            int ActiveShiftId3 = repository.AddActiveShiftClean(new ActiveShiftObject(null,LocalDate.now().with(DayOfWeek.WEDNESDAY),LocalTime.parse("16:00",TimeFormatter),LocalTime.parse("20:00",TimeFormatter), Duration.ofMinutes(0),SofieId,SofieId,ShiftId3));
            int ActiveShiftId4 = repository.AddActiveShiftClean(new ActiveShiftObject(null,LocalDate.now().with(DayOfWeek.THURSDAY),LocalTime.parse("16:00",TimeFormatter),LocalTime.parse("20:00",TimeFormatter), Duration.ofMinutes(0),SofieId,SofieId,ShiftId4));
            int ActiveShiftId5 = repository.AddActiveShiftClean(new ActiveShiftObject(null,LocalDate.now().with(DayOfWeek.FRIDAY),LocalTime.parse("16:00",TimeFormatter),LocalTime.parse("20:00",TimeFormatter), Duration.ofMinutes(0),AndreasId,AndreasId,ShiftId5));
            int ActiveShiftId6 = repository.AddActiveShiftClean(new ActiveShiftObject(null,LocalDate.now().with(DayOfWeek.SATURDAY),LocalTime.parse("12:00",TimeFormatter),LocalTime.parse("18:00",TimeFormatter), Duration.ofMinutes(30),AndreasId,AndreasId,ShiftId6));
            int ActiveShiftId7 = repository.AddActiveShiftClean(new ActiveShiftObject(null,LocalDate.now().with(DayOfWeek.SUNDAY),LocalTime.parse("12:00",TimeFormatter),LocalTime.parse("18:00",TimeFormatter), Duration.ofMinutes(30),AnnaId,AnnaId,ShiftId7));

            int ActiveShiftId8 = repository.AddActiveShiftClean(new ActiveShiftObject(null,LocalDate.now().with(DayOfWeek.MONDAY).plusWeeks(1),LocalTime.parse("16:00",TimeFormatter),LocalTime.parse("20:00",TimeFormatter), Duration.ofMinutes(0),MartinId,MartinId,ShiftId1));
            int ActiveShiftId9 = repository.AddActiveShiftClean(new ActiveShiftObject(null,LocalDate.now().with(DayOfWeek.TUESDAY).plusWeeks(1),LocalTime.parse("16:00",TimeFormatter),LocalTime.parse("20:00",TimeFormatter), Duration.ofMinutes(0),MartinId,MartinId,ShiftId2));
            int ActiveShiftId10 = repository.AddActiveShiftClean(new ActiveShiftObject(null,LocalDate.now().with(DayOfWeek.WEDNESDAY).plusWeeks(1),LocalTime.parse("16:00",TimeFormatter),LocalTime.parse("20:00",TimeFormatter), Duration.ofMinutes(0),SofieId,SofieId,ShiftId3));
            int ActiveShiftId11 = repository.AddActiveShiftClean(new ActiveShiftObject(null,LocalDate.now().with(DayOfWeek.THURSDAY).plusWeeks(1),LocalTime.parse("16:00",TimeFormatter),LocalTime.parse("20:00",TimeFormatter), Duration.ofMinutes(0),SofieId,SofieId,ShiftId4));
            int ActiveShiftId12 = repository.AddActiveShiftClean(new ActiveShiftObject(null,LocalDate.now().with(DayOfWeek.FRIDAY).plusWeeks(1),LocalTime.parse("16:00",TimeFormatter),LocalTime.parse("20:00",TimeFormatter), Duration.ofMinutes(0),AndreasId,AndreasId,ShiftId5));
            int ActiveShiftId13 = repository.AddActiveShiftClean(new ActiveShiftObject(null,LocalDate.now().with(DayOfWeek.SATURDAY).plusWeeks(1),LocalTime.parse("12:00",TimeFormatter),LocalTime.parse("18:00",TimeFormatter), Duration.ofMinutes(30),AndreasId,AnnaId,ShiftId6));
            int ActiveShiftId14 = repository.AddActiveShiftClean(new ActiveShiftObject(null,LocalDate.now().with(DayOfWeek.SUNDAY).plusWeeks(1),LocalTime.parse("12:00",TimeFormatter),LocalTime.parse("18:00",TimeFormatter), Duration.ofMinutes(30),AnnaId,AnnaId,ShiftId7));

            int ActiveShiftId15 = repository.AddActiveShiftClean(new ActiveShiftObject(null,LocalDate.now().with(DayOfWeek.MONDAY).plusWeeks(2),LocalTime.parse("16:00",TimeFormatter),LocalTime.parse("20:00",TimeFormatter), Duration.ofMinutes(0),MartinId,MartinId,ShiftId1));
            int ActiveShiftId16 = repository.AddActiveShiftClean(new ActiveShiftObject(null,LocalDate.now().with(DayOfWeek.TUESDAY).plusWeeks(2),LocalTime.parse("16:00",TimeFormatter),LocalTime.parse("20:00",TimeFormatter), Duration.ofMinutes(0),MartinId,MartinId,ShiftId2));
            int ActiveShiftId17 = repository.AddActiveShiftClean(new ActiveShiftObject(null,LocalDate.now().with(DayOfWeek.WEDNESDAY).plusWeeks(2),LocalTime.parse("16:00",TimeFormatter),LocalTime.parse("20:00",TimeFormatter), Duration.ofMinutes(0),SofieId,SofieId,ShiftId3));
            int ActiveShiftId18 = repository.AddActiveShiftClean(new ActiveShiftObject(null,LocalDate.now().with(DayOfWeek.THURSDAY).plusWeeks(2),LocalTime.parse("16:00",TimeFormatter),LocalTime.parse("20:00",TimeFormatter), Duration.ofMinutes(0),SofieId,SofieId,ShiftId4));
            int ActiveShiftId19 = repository.AddActiveShiftClean(new ActiveShiftObject(null,LocalDate.now().with(DayOfWeek.FRIDAY).plusWeeks(2),LocalTime.parse("16:00",TimeFormatter),LocalTime.parse("20:00",TimeFormatter), Duration.ofMinutes(0),AndreasId,AndreasId,ShiftId5));
            int ActiveShiftId20 = repository.AddActiveShiftClean(new ActiveShiftObject(null,LocalDate.now().with(DayOfWeek.SATURDAY).plusWeeks(2),LocalTime.parse("12:00",TimeFormatter),LocalTime.parse("18:00",TimeFormatter), Duration.ofMinutes(30),AndreasId,AndreasId,ShiftId6));
            int ActiveShiftId21 = repository.AddActiveShiftClean(new ActiveShiftObject(null,LocalDate.now().with(DayOfWeek.SUNDAY).plusWeeks(2),LocalTime.parse("12:00",TimeFormatter),LocalTime.parse("18:00",TimeFormatter), Duration.ofMinutes(30),AnnaId,AnnaId,ShiftId7));

            //SHIFTCHANGEREQUSTS
            int ShiftChangeRequestId1 = repository.AddShiftChangeRequestClean(new ShiftChangeRequestObject(null,ActiveShiftId1,MartinId,ShiftChangeRequestObject.NECESSITY_WANTED,"Jeg glemte en aftale med en kammerat",ShiftChangeRequestObject.SHIFTCHANGESTATUS_SWITCH_SUGGESTED));
            int ShiftChangeRequestId2 = repository.AddShiftChangeRequestClean(new ShiftChangeRequestObject(null,ActiveShiftId5,AndreasId,ShiftChangeRequestObject.NECESSITY_NEEDED,"Jeg har fået en akuttid ved tandlægen",ShiftChangeRequestObject.SHIFTCHANGESTATUS_PENDING));
            int ShiftChangeRequestId3 = repository.AddShiftChangeRequestClean(new ShiftChangeRequestObject(null,ActiveShiftId10,SofieId,ShiftChangeRequestObject.NECESSITY_WANTED,"skal til håndboldstævne",ShiftChangeRequestObject.SHIFTCHANGESTATUS_PENDING));
            int ShiftChangeRequestId4 = repository.AddShiftChangeRequestClean(new ShiftChangeRequestObject(null,ActiveShiftId13,AndreasId,ShiftChangeRequestObject.NECESSITY_WANTED,"",ShiftChangeRequestObject.SHIFTCHANGESTATUS_ACCEPTED));

            //SHIFTCHANERESPONSE
            repository.EditShiftChangeResponse(repository.getShiftChangeResponseClean(AndreasId, ShiftChangeRequestId1).setStatus(ShiftChangeResponseObject.SHIFTCHANGESTATUS_SWITCH_SUGGESTED).setShiftIdList(Arrays.asList(ActiveShiftId5,ActiveShiftId20)));
            repository.EditShiftChangeResponse(repository.getShiftChangeResponseClean(AnnaId, ShiftChangeRequestId1).setStatus(ShiftChangeResponseObject.SHIFTCHANGESTATUS_SWITCH_SUGGESTED).setShiftIdList(Arrays.asList(ActiveShiftId14)));
            repository.EditShiftChangeResponse(repository.getShiftChangeResponseClean(SofieId, ShiftChangeRequestId2).setStatus(ShiftChangeResponseObject.SHIFTCHANGESTATUS_DECLINED).setShiftIdList(Arrays.asList()));
            repository.EditShiftChangeResponse(repository.getShiftChangeResponseClean(SofieId, ShiftChangeRequestId1).setStatus(ShiftChangeResponseObject.SHIFTCHANGESTATUS_DECLINED).setShiftIdList(Arrays.asList()));
            repository.EditShiftChangeResponse(repository.getShiftChangeResponseClean(AnnaId, ShiftChangeRequestId4).setStatus(ShiftChangeResponseObject.SHIFTCHANGESTATUS_ACCEPTED).setShiftIdList(Arrays.asList()));

            //OFFDAYREQUESTS
            repository.AddOffDayRequestClean(new OffDayRequestObject(null,OffDayRequestObject.TYPE_OFFDAY,LocalDate.now().plusMonths(1),LocalDate.now().plusMonths(1).plusDays(3),null,null,MartinId,OffDayRequestObject.STATUS_PENDING));
            repository.AddOffDayRequestClean(new OffDayRequestObject(null,OffDayRequestObject.TYPE_OFFDAY,LocalDate.now().plusMonths(1).plusDays(3),LocalDate.now().plusMonths(1).plusDays(6),null,null,SofieId,OffDayRequestObject.STATUS_PENDING));
            repository.AddOffDayRequestClean(new OffDayRequestObject(null,OffDayRequestObject.TYPE_OFFDAY,LocalDate.now().plusMonths(1).plusDays(6),LocalDate.now().plusMonths(1).plusDays(9),null,null,AndreasId,OffDayRequestObject.STATUS_PENDING));
            repository.AddOffDayRequestClean(new OffDayRequestObject(null,OffDayRequestObject.TYPE_OFFDAY,LocalDate.now().plusMonths(1).plusDays(9),LocalDate.now().plusMonths(1).plusDays(12),null,null,AnnaId,OffDayRequestObject.STATUS_PENDING));

            //MESSAGES
            repository.AddMessageClean(new MessageObject(null,AnnaId,DepartmentId1, LocalDate.now().minusDays(2).atTime(LocalTime.parse("12:41")), Collections.singletonList(AndreasId),MessageObject.TYPE_PROFESSIONAL_GROUP_MESSAGE,"Hvor er den brede kost blevet stillet hen?"));
            repository.AddMessageClean(new MessageObject(null,MartinId,DepartmentId1, LocalDate.now().minusDays(2).atTime(LocalTime.parse("12:45")), Collections.singletonList(AndreasId),MessageObject.TYPE_PROFESSIONAL_GROUP_MESSAGE,"Den var der heller ikke igår"));
            repository.AddMessageClean(new MessageObject(null,AnnaId,DepartmentId1, LocalDate.now().minusDays(2).atTime(LocalTime.parse("12:52")),Arrays.asList(AndreasId,MartinId),MessageObject.TYPE_PROFESSIONAL_GROUP_MESSAGE,"Brugte du den i Torsdags Sofie?"));
            repository.AddMessageClean(new MessageObject(null,SofieId,DepartmentId1, LocalDate.now().minusDays(2).atTime(LocalTime.parse("12:54")),Arrays.asList(AndreasId,MartinId,AnnaId),MessageObject.TYPE_PROFESSIONAL_GROUP_MESSAGE,"Ja, jeg glemte den i flaskerummet"));

            repository.AddMessageClean(new MessageObject(null,JohanId,MartinId, LocalDate.now().minusDays(1).atTime(LocalTime.parse("16:07")), Collections.singletonList(MartinId),MessageObject.TYPE_PROFESSIONAL_P2P_MESSAGE,"Husk der er status næste uge?"));
            repository.AddMessageClean(new MessageObject(null,JohanId,AnnaId, LocalDate.now().minusDays(1).atTime(LocalTime.parse("16:07")), Collections.singletonList(AnnaId),MessageObject.TYPE_PROFESSIONAL_P2P_MESSAGE,"Husk der er status næste uge?"));
            repository.AddMessageClean(new MessageObject(null,JohanId,AndreasId, LocalDate.now().minusDays(1).atTime(LocalTime.parse("16:07")), Collections.singletonList(AndreasId),MessageObject.TYPE_PROFESSIONAL_P2P_MESSAGE,"Husk der er status næste uge?"));
            repository.AddMessageClean(new MessageObject(null,JohanId,SofieId, LocalDate.now().minusDays(1).atTime(LocalTime.parse("16:08")), Collections.singletonList(SofieId),MessageObject.TYPE_PROFESSIONAL_P2P_MESSAGE,"Husk der er status næste uge?"));


            //FINISHEDTASKS
            repository.AddFinishedTaskClean(new FinishedTaskObject(null,LocalDate.now().minusDays(5),LocalTime.parse("19:47",TimeFormatter),Duration.ofMinutes(20),null,null,MartinId,TaskId1,repository.getTaskClean(TaskId1)));
            repository.AddFinishedTaskClean(new FinishedTaskObject(null,LocalDate.now().minusDays(4),LocalTime.parse("15:14",TimeFormatter),Duration.ofMinutes(20),null,null,AndreasId,TaskId2,repository.getTaskClean(TaskId2)));
            repository.AddFinishedTaskClean(new FinishedTaskObject(null,LocalDate.now().minusDays(4),LocalTime.parse("10:11",TimeFormatter),Duration.ofMinutes(10),null,null,AndreasId,TaskId3,repository.getTaskClean(TaskId3)));
            repository.AddFinishedTaskClean(new FinishedTaskObject(null,LocalDate.now().minusDays(4),LocalTime.parse("16:01",TimeFormatter),Duration.ofMinutes(35),null,null,AndreasId,TaskId4,repository.getTaskClean(TaskId4)));
            repository.AddFinishedTaskClean(new FinishedTaskObject(null,LocalDate.now().minusDays(3),LocalTime.parse("18:27",TimeFormatter),Duration.ofMinutes(25),null,null,SofieId,TaskId5,repository.getTaskClean(TaskId5)));
            repository.AddFinishedTaskClean(new FinishedTaskObject(null,LocalDate.now().minusDays(3),LocalTime.parse("17:55",TimeFormatter),Duration.ofMinutes(20),null,null,SofieId,TaskId6,repository.getTaskClean(TaskId6)));
            repository.AddFinishedTaskClean(new FinishedTaskObject(null,LocalDate.now().minusDays(2),LocalTime.parse("07:49",TimeFormatter),Duration.ofMinutes(15),null,null,AnnaId,TaskId7,repository.getTaskClean(TaskId7)));
            repository.AddFinishedTaskClean(new FinishedTaskObject(null,LocalDate.now().minusDays(2),LocalTime.parse("14:33",TimeFormatter),Duration.ofMinutes(20),null,null,AnnaId,TaskId8,repository.getTaskClean(TaskId8)));
            repository.AddFinishedTaskClean(new FinishedTaskObject(null,LocalDate.now(),LocalTime.now().minusMinutes(5),Duration.ofMinutes(20),null,null,MartinId,TaskId9,repository.getTaskClean(TaskId9)));

            //PRODUCTS

        });
    }
}
