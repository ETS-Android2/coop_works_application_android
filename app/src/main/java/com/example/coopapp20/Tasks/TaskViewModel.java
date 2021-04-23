package com.example.coopapp20.Tasks;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.coopapp20.Data.Objects.DepartmentObject;
import com.example.coopapp20.Data.Objects.FinishedTaskObject;
import com.example.coopapp20.Data.Objects.TaskObject;
import com.example.coopapp20.Data.Objects.UserObject;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.DialogMultilineTextfieldBinding;
import com.example.coopapp20.zOtherFiles.DurationFormat;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository repository;

    private LiveData<List<DepartmentObject>> AllDepartments;
    private LiveData<List<TaskObject>> Tasks;
    private LiveData<List<FinishedTaskObject>> FinishedTasks;
    private MutableLiveData<TaskObject> SelectedTask = new MutableLiveData<>();
    private MutableLiveData<TaskObject> EditableTask = new MutableLiveData<>();
    private MutableLiveData<String> AddTaskDescriptionText = new MutableLiveData<>();
    private LiveData<UserObject> CurrentUser;

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy", Locale.ENGLISH);

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
    }

    public void InitiateViewModel(LiveData<UserObject> currentUser){
        CurrentUser = currentUser;

        AllDepartments = repository.getAllDepartments();

        MediatorLiveData<List<TaskObject>> taskMediator = new MediatorLiveData<>();
        taskMediator.addSource(AllDepartments, departments -> taskMediator.setValue(taskMediator.getValue()));
        taskMediator.addSource(Transformations.switchMap(CurrentUser, input -> repository.getDepartmentTasks(input != null ? input.getDepartmentId() : null)), taskMediator::setValue);

        Tasks = Transformations.map(taskMediator, this::CreateTaskObjects);

        MediatorLiveData<List<FinishedTaskObject>> finishedTasksMediator = new MediatorLiveData<>();
        finishedTasksMediator.addSource(Tasks, tasks -> finishedTasksMediator.setValue(finishedTasksMediator.getValue()));
        finishedTasksMediator.addSource(Transformations.switchMap(CurrentUser, input -> input != null ? repository.getUserFinishedTasks(input.getId()) : null), finishedTasksMediator::setValue);

        FinishedTasks = Transformations.map(finishedTasksMediator, this::CreateFinishedTaskObjects);
    }

    public LiveData<List<DepartmentObject>> getAllDepartments() { return AllDepartments; }
    public LiveData<List<TaskObject>> getTasks() {return Tasks;}
    public LiveData<List<FinishedTaskObject>> getFinishedTasks() {return FinishedTasks;}

    public MutableLiveData<TaskObject> getSelectedTask(){return SelectedTask;}
    public MutableLiveData<TaskObject> getEditableTask(){return EditableTask;}
    public MutableLiveData<String> getAddTaskDescriptionText() { return AddTaskDescriptionText; }

    public void CheckTask(TaskObject task){
        if(task != null && CurrentUser != null){
            repository.AddFinishedTask(new FinishedTaskObject(null, LocalDate.now(),LocalTime.now(), task.getDuration(), null, null, CurrentUser.getValue().getId(), task.getId(), task));

            if (task.getRepeat()) {
                task.setLastCompletionDate(LocalDate.now());
                task.setLastCompletionTime(LocalTime.now());
                repository.UpdateTask(task);
            } else { repository.DeleteTask(task);}
        } else if(SelectedTask.getValue() != null){
            repository.AddFinishedTask(new FinishedTaskObject(null, LocalDate.now(),LocalTime.now(), SelectedTask.getValue().getDuration(), null, null, CurrentUser.getValue().getId(), SelectedTask.getValue().getId(), SelectedTask.getValue()));
            if (SelectedTask.getValue().getRepeat()) {
                SelectedTask.getValue().setLastCompletionDate(LocalDate.now());
                SelectedTask.getValue().setLastCompletionTime(LocalTime.now());
                repository.UpdateTask(SelectedTask.getValue());
            } else { repository.DeleteTask(SelectedTask.getValue());}
        }
    }

    public void onFinishedTaskClick(View v, FinishedTaskObject finishedTask, Activity activity){
        if(FinishedTasks.getValue() != null && finishedTask != null) {
            //Prepare popup menu
            PopupMenu popupMenu = new PopupMenu(getApplication(), v);
            popupMenu.getMenu().add(getApplication().getString(R.string.add_feedback));
            popupMenu.getMenu().add(getApplication().getString(R.string.delete));

            List<FinishedTaskObject> PriorFinishedTasks = FinishedTasks.getValue().stream().filter(f -> !f.getDivider() && f.getTaskId().equals(finishedTask.getTaskId()) && !f.getId().equals(finishedTask.getId())).collect(Collectors.toCollection(ArrayList::new));

            //if clicked FinishedTask isn't the newest for the task, disable delete menu item
            PriorFinishedTasks.stream().filter(o -> o.getCompletionDate().isAfter(finishedTask.getCompletionDate()) && o.getCompletionTime().isAfter(finishedTask.getCompletionTime())).findFirst().ifPresent(o -> popupMenu.getMenu().getItem(1).setEnabled(false));

            //set popup menu ClickListener
            popupMenu.setOnMenuItemClickListener(item -> {
                if(item.getTitle().equals(getApplication().getString(R.string.add_feedback))){
                    //Dialog for commenting on task
                    Dialog dialog = new Dialog(activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_task_comment);
                    dialog.getWindow().setLayout(getApplication().getResources().getDisplayMetrics().widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.show();

                }else if(item.getTitle().equals(getApplication().getString(R.string.delete))){

                    if (finishedTask.getTask().getRepeat()) {
                        if (PriorFinishedTasks.size() > 0) {
                            //Delete finishedtask and set Task LastCompletionDate to latest PriorFinishedTask
                            Collections.sort(PriorFinishedTasks, (o1, o2) -> o1.getCompletionDate().compareTo(o2.getCompletionDate()));
                            Collections.sort(PriorFinishedTasks, (o1, o2) -> o1.getCompletionTime().compareTo(o2.getCompletionTime()));
                            finishedTask.getTask().setLastCompletionDate(PriorFinishedTasks.get(0).getTask().getLastCompletionDate());
                        } else {
                            //if no corresponding FinishedTask exist, set Task for 1 day left
                            Log.e("TaskViewModel","finishedTask.getTimeConsumption(): "+finishedTask.getDuration());
                            Log.e("TaskViewModel","finishedTask.getTask().getTime(): "+finishedTask.getTask().getDuration());
                            finishedTask.getTask().setLastCompletionDate(LocalDate.now().minusDays(finishedTask.getTask().getDays()));
                            finishedTask.getTask().setLastCompletionTime(LocalTime.MIN);
                            Log.e("TaskViewModel","finishedTask.getTask().getTime(): "+finishedTask.getTask().getDuration());
                        }
                        repository.UpdateTask(finishedTask.getTask());
                        repository.DeleteFinishedTask(finishedTask);
                    } else {
                        //if corresponding task is single, recreate Task and delete FinishedTask
                        repository.AddTask(finishedTask.getTask());
                        repository.DeleteFinishedTask(finishedTask);
                    }
                }

                return true;
            });
            popupMenu.show();
        }
    }
    public void onAddTaskBtnClick(String Title, String Time, String IdealTime, Integer Days, String Description, Integer DepartmentId, Integer Prioritization, boolean repeat){

        Duration time;
        LocalTime idealTime;
        if(Time.matches("^(\\d\\d:\\d\\d)")){time = Duration.between(LocalTime.MIN, LocalTime.parse(Time, timeFormatter));}else {time = Duration.ofMinutes(10);}
        if(repeat && IdealTime.matches("^(\\d\\d:\\d\\d)")){idealTime = LocalTime.parse(IdealTime, timeFormatter);}else {idealTime = null;}

        if(EditableTask.getValue() != null){
            //UpdateTask
            TaskObject task = EditableTask.getValue();
            task.setTitle(Title);
            task.setDuration(time);
            task.setDays(Days);
            task.setDescription(Description);
            task.setDepartmentId(DepartmentId);
            task.setPrioritization(Prioritization);
            repository.UpdateTask(task);
        }else {
            //AddTask
            TaskObject task = new TaskObject(null,Title,time,idealTime,Days,Description,DepartmentId,Prioritization,repeat,LocalDate.now(), LocalTime.now());
            repository.AddTask(task);
        }
    }
    public void onDescriptionTextViewClick( String description, Context context){
        //create dialog with custom layout
        Dialog dialog = new Dialog(Objects.requireNonNull(context));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogMultilineTextfieldBinding DialogBinding = DialogMultilineTextfieldBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(DialogBinding.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        //reference dialog layout
        DialogBinding.MultilineTextDialogTitle.setText("opgave beskrivelse");
        DialogBinding.MultilineTextDialogTextView.setText(description);
        dialog.setOnCancelListener(dialog1 -> AddTaskDescriptionText.setValue(DialogBinding.MultilineTextDialogTextView.getText().toString()));

        dialog.show();
    }
    public void onDeleteTaskBtnClick(Activity activity){
        if(EditableTask.getValue() != null) {
            new AlertDialog.Builder(activity)
                    .setTitle("VIL DU SLETTE OPGAVEN?")
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                        repository.DeleteTask(EditableTask.getValue());
                        activity.onBackPressed();
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }
    public void onTaskAccelerationClick(TaskObject task){
        repository.UpdateTask(task);
    }

    private List<TaskObject> CreateTaskObjects(List<TaskObject> tasks) {
       if (tasks != null && AllDepartments.getValue() != null) {
            tasks.forEach(t -> {
                t.setTimeString(DurationFormat.DurationToTimeString(t.getDuration()));
                t.setDaysLeft((int) (t.getDays() + ChronoUnit.DAYS.between(LocalDate.now(), t.getLastCompletionDate())));
                AllDepartments.getValue().stream().filter(d -> d.getId().equals(t.getDepartmentId())).findAny().ifPresent(d -> t.setDepartmentName(d.getName()));

                //If task is "non-repeat" and prioritized "immediately" or task has run past deadline, set color as red
                if(!t.getRepeat() && t.getPrioritization().equals(TaskObject.COMPLETE_IMMEDIATELY) || t.getDaysLeft() < 0){t.setColor(ContextCompat.getColor(getApplication(), R.color.TaskPrioritization0));}

                //if task deadline is today, set color as orange
                else if(t.getDaysLeft() < 1){t.setColor(ContextCompat.getColor(getApplication(), R.color.TaskPrioritization1));}

                //if task is "repeat" and prioritized "ad deadline" or task was finished same day, set color as green
                else if(t.getRepeat() && t.getPrioritization().equals(2) || t.getRepeat() && t.getDaysLeft().equals(t.getDays())){ t.setColor(ContextCompat.getColor(getApplication(), R.color.TaskPrioritization3));}

                //else set color as yellow
                else { t.setColor(ContextCompat.getColor(getApplication(), R.color.TaskPrioritization2));}
            });
            
            //Sort tasks
            Collections.sort(tasks, (o1, o2) -> {

                //Create integers for score system
                Integer FirstTaskScore = 0;
                Integer SecondTaskScore = 0;

                //Calculate % of days gone from countdown and add it to the score
                //if countdown has run down (DaysLeft < 0) give big bonus
                FirstTaskScore += o1.getDaysLeft() > 0 ? (100 - (o1.getDaysLeft() * 100 / o1.getDays())) : 300;
                SecondTaskScore += o2.getDaysLeft() > 0 ? (100 - (o2.getDaysLeft() * 100 / o2.getDays())) : 300;

                //get a minus for each day left of countdown
                FirstTaskScore -= o1.getDaysLeft();
                SecondTaskScore -= o2.getDaysLeft();
                
                //if task is single, set big bonus for high prioritization
                //else set lesser bonus for prioritization

                if (!o1.getRepeat()) {
                    if (o1.getPrioritization() == 0) { FirstTaskScore += 300;
                    } else if (o1.getPrioritization() == 1) { FirstTaskScore += 200; }
                } else {
                    if (o1.getPrioritization() == 0) { FirstTaskScore += 100;
                    } else if (o1.getPrioritization() == 1) { FirstTaskScore += 70; }
                }

                if (!o2.getRepeat()) {
                    if (o2.getPrioritization() == 0) { SecondTaskScore += 300;
                    } else if (o2.getPrioritization() == 1) { SecondTaskScore += 200; }
                } else {
                    if (o2.getPrioritization() == 0) { SecondTaskScore += 100;
                    } else if (o2.getPrioritization() == 1) { SecondTaskScore += 70; }
                }

                //Divide score with 10 if the task was finished today
                if (o1.getDaysLeft().equals(o1.getDays())) { FirstTaskScore /= 10; }
                if (o2.getDaysLeft().equals(o2.getDays())) { SecondTaskScore /= 10; }

                //organize after the highest score
                return SecondTaskScore.compareTo(FirstTaskScore);
            });

            return tasks;

        }else {return null;}
    }
    private List<FinishedTaskObject> CreateFinishedTaskObjects(List<FinishedTaskObject> finishedTasks){
        if(finishedTasks != null && Tasks.getValue() != null) {
            Log.e("TaskViewModel","CreateFinishedTaskObjects");

            //create list of all FinishedTask dates
            ArrayList<LocalDate> dates = new ArrayList<>();
            finishedTasks.forEach(f -> {
                if(f != null && f.getTask() != null) {
                    //set view values for each FinishedTask
                    f.setExtraValues(f.getTask().getTitle(), DurationFormat.DurationToTimeString(f.getTask().getDuration()), f.getCompletionDate().format(dateFormatter), false);

                    //Add date if it doesn't already exists
                    if (dates.stream().noneMatch(o -> o.equals(f.getCompletionDate()))) {
                        dates.add(f.getCompletionDate());
                    }
                }
            });

            //Make Divider for each date
            for(LocalDate date : dates){
                FinishedTaskObject finishedTaskTemp = new FinishedTaskObject(null, date,null, null, null, null, null, null,null);
                finishedTaskTemp.setExtraValues(null,null,date.format(dateFormatter),true);
                finishedTasks.add(finishedTaskTemp);
            }

            //Sort objects by date and time
            Collections.sort(finishedTasks, ((o1, o2) -> {
                if(o1.getCompletionDate().compareTo(o2.getCompletionDate()) != 0){
                    return o1.getCompletionDate().compareTo(o2.getCompletionDate());
                }else {
                    return o1.getDivider().compareTo(o2.getDivider());
                }
            }));

            //revers order of array
            Collections.reverse(finishedTasks);

            return finishedTasks;
        }else {return null;}
    }
}
