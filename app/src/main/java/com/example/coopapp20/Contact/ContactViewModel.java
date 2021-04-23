package com.example.coopapp20.Contact;

import android.app.Application;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.coopapp20.Contact.Contact.ContactObject;
import com.example.coopapp20.Contact.Contact.ContactUserObject;
import com.example.coopapp20.Data.Objects.DepartmentObject;
import com.example.coopapp20.Data.Objects.MessageObject;
import com.example.coopapp20.Data.Objects.UserObject;
import com.example.coopapp20.R;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ContactViewModel extends AndroidViewModel {

    private ContactRepository repository;

    private LiveData<List<DepartmentObject>> AllDepartments;
    private LiveData<List<ContactObject>> Contacts;
    private LiveData<List<MessageObject>> Messages;
    private LiveData<List<MessageObject>> Interactions;
    private MutableLiveData<ContactObject> SelectedContact = new MutableLiveData<>();
    private MutableLiveData<UserObject> CurrentUser = new MutableLiveData<>();

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy", Locale.ENGLISH);
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yy\nHH:mm");

    public ContactViewModel(@NonNull Application application) {
        super(application);
        repository = new ContactRepository(getApplication());

        AllDepartments = repository.getAllDepartments();

        MediatorLiveData<Void> contactMediator = new MediatorLiveData<>();
        contactMediator.addSource(CurrentUser, o -> contactMediator.setValue(null));
        contactMediator.addSource(repository.getAllUsers(), o -> contactMediator.setValue(null));
        contactMediator.addSource(repository.getAllDepartments(), o -> contactMediator.setValue(null));
        contactMediator.addSource(repository.getAllMessages(), o -> contactMediator.setValue(null));


        Contacts = Transformations.map(contactMediator, input -> CreateContactObjects());
        Messages = Transformations.switchMap(CurrentUser, input -> Transformations.switchMap(SelectedContact, input1 -> Transformations.map(repository.getContactMessages(input1, input.getId()), this::CreateContactMessageObjects)));
        Interactions = Transformations.map(Transformations.switchMap(CurrentUser, input -> repository.getUserInteractions(input)), this::CreateContactInteractionObjects);
    }

    public LiveData<List<DepartmentObject>> getAllDepartments() { return AllDepartments; }
    public LiveData<List<ContactObject>> getContacts(){ return Contacts; }
    public LiveData<List<MessageObject>> getMessages(){return Messages;}
    public LiveData<List<MessageObject>> getInteractions(){return Interactions;}
    public MutableLiveData<ContactObject> getSelectedContact(){return SelectedContact;}
    public LiveData<UserObject> getCurrentUser() { return CurrentUser; }
    public void setCurrentUser(LiveData<UserObject> currentUser) { CurrentUser.setValue(currentUser.getValue()); }

    public void ContactUserObjects(Integer UserId){
        new Thread(() -> {
            List<ContactUserObject> ContactUserObjects = repository.getContactUserObjects(UserId);
            Log.e("ContactViewModel",!ContactUserObjects.isEmpty() ? ContactUserObjects.get(0).getUserName() + " " + ContactUserObjects.get(0).getDepartmentName() : "FAIL");
        }).start();
    }

    public void SendText(String text){
        if(SelectedContact.getValue() != null && CurrentUser.getValue() != null && !text.equals("")){
            new Thread(() -> {
                int MessageType;
                List<Integer> MissingViews = Collections.emptyList();
                if(SelectedContact.getValue().getDataObject() instanceof DepartmentObject){
                    MessageType = MessageObject.TYPE_PROFESSIONAL_GROUP_MESSAGE;
                    MissingViews = repository.getDepartmentUsersClean(SelectedContact.getValue().getUserNr()).stream().map(UserObject::getId).collect(Collectors.toList());
                }
                else {
                    MessageType = MessageObject.TYPE_PROFESSIONAL_P2P_MESSAGE;
                    MissingViews = Collections.singletonList(SelectedContact.getValue().getUserNr());
                }

                repository.AddMessage(new MessageObject(null,CurrentUser.getValue().getId(),SelectedContact.getValue().getUserNr(), LocalDateTime.now(),MissingViews,MessageType,text));
            }).start();
        }
    }
    public void ContactInteractionClick(ContactObject contact){
        SelectedContact.setValue(contact);
    }

    public void setSelectedContactId(Integer Id){
        if(Contacts.getValue() != null){
            Contacts.getValue().stream().filter(c -> c.getUserNr().equals(Id)).findAny().ifPresent(c -> SelectedContact.setValue(c));
        }
    }

    private List<ContactObject> CreateContactObjects(){
        if(repository.getAllUsers().getValue() != null && repository.getAllDepartments().getValue() != null && repository.getAllMessages().getValue() != null && CurrentUser.getValue() != null) {


            ArrayList<ContactObject> ContactObjectsReturn = new ArrayList<>();

            ArrayList<UserObject> users = new ArrayList<>(repository.getAllUsers().getValue());
            ArrayList<DepartmentObject> departments = new ArrayList<>(repository.getAllDepartments().getValue());
            ArrayList<MessageObject> messages = new ArrayList<>(repository.getAllMessages().getValue());

            for (DepartmentObject department : departments) {

                //add message to DepartmentMessages if message is sent to department, and get oldest on top
                ArrayList<MessageObject> DepartmentMessages = messages.stream().filter(message -> message.getReceiver().equals(department.getId())).sorted((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime())).collect(Collectors.toCollection(ArrayList::new));

                //If there are no messages write "No messages", else, display latest message
                String Message = DepartmentMessages.stream().findFirst().map(MessageObject::getMessage).orElse("no messages");
                String DateOrTime = DepartmentMessages.stream().findFirst().map(message -> message.getDateTime().toLocalDate().isBefore(LocalDate.now()) ?  message.getDateTime().format(dateFormatter) : message.getDateTime().format(timeFormatter)).orElse("");

                String DepartmentColor = department.getColor();

                //Check if any of departments employees are active
                boolean Active = users.stream().anyMatch(user -> user.getDepartmentId().equals(department.getId()) && !user.getStatus().equals(UserObject.STATUS_OFF_DUTY));

                //Add Contact to ContactObjectArrayList
                ContactObjectsReturn.add(new ContactObject(department.getName(), department.getName(), DepartmentColor, Message, DateOrTime, false, Active, department.getId(),department));
            }

            //Create a ContactObject for all Contacts ( excluding yourself)
            for (UserObject user : users) {

                //Excluding yourself
                if (!user.getId().equals(CurrentUser.getValue().getId())) {

                    //get messages between contact and user
                    ArrayList<MessageObject> SentMessages = messages.stream().filter(message -> message.getSender().equals(CurrentUser.getValue().getId()) && message.getReceiver().equals(user.getId())).collect(Collectors.toCollection(ArrayList::new));
                    ArrayList<MessageObject> ReceivedMessages = messages.stream().filter(message -> message.getSender().equals(user.getId()) && message.getReceiver().equals(CurrentUser.getValue().getId())).collect(Collectors.toCollection(ArrayList::new));

                    //Compare messages to get the oldes in the top
                    Collections.sort(SentMessages, (o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()));

                    //Check if there are any unviewed messages
                    boolean UnViewed = ReceivedMessages.stream().anyMatch(message -> !message.getMissingViews().isEmpty());

                    //If there are no messages write "No messages", else, display latest message
                    String Message = SentMessages.stream().findFirst().map(MessageObject::getMessage).orElse("no messages");
                    String DateOrTime = SentMessages.stream().findFirst().map(message -> message.getDateTime().toLocalDate().isBefore(LocalDate.now()) ?  message.getDateTime().toLocalDate().format(dateFormatter) : message.getDateTime().toLocalDate().format(timeFormatter)).orElse("");

                    //Display color according to contacts department
                    String DepartmentName = departments.stream().filter(department -> user.getDepartmentId().equals(department.getId())).findAny().map(DepartmentObject::getName).orElse("N/A");
                    String Color = departments.stream().filter(department -> user.getDepartmentId().equals(department.getId())).findAny().map(DepartmentObject::getColor).orElse(null);

                    //check if user is active
                    boolean Active = !user.getStatus().equals(UserObject.STATUS_OFF_DUTY);

                    //Add Contact to ContactObjectArrayList
                    ContactObjectsReturn.add(new ContactObject(user.getName(), DepartmentName, Color, Message, DateOrTime, UnViewed, Active, user.getId(),user));
                }
            }

            //sort ContactObjects to get active contacts in the top
            Collections.sort(ContactObjectsReturn, (o1, o2) -> o2.getActive().compareTo(o1.getActive()));

            return ContactObjectsReturn;
        }else {return null;}
    }
    private List<MessageObject> CreateContactMessageObjects(List<MessageObject> messages){
        if(SelectedContact.getValue() != null && CurrentUser.getValue() != null && repository.getAllUsers().getValue() != null){

            messages.forEach(message -> {
                //if message was sent today, set time, else, set date
                message.setMessageDateTimeString(message.getDateTime().format(message.getDateTime().toLocalDate().isBefore(LocalDate.now()) ? dateFormatter : timeFormatter));

                //set name of message sender
                message.setMessageName(message.getSender().equals(CurrentUser.getValue().getId()) ? CurrentUser.getValue().getName() : repository.getAllUsers().getValue().stream().filter(user -> user.getId().equals(message.getSender())).findAny().map(UserObject::getName).orElse(""));

                //set background according to whether CurrentUser is sender or receiver
                message.setMessageBGColor(message.getSender().equals(CurrentUser.getValue().getId()) ? ContextCompat.getColor(getApplication(), R.color.SentMessage) : ContextCompat.getColor(getApplication(), R.color.ReceivedMessage));
            });

            Collections.reverse(messages);

            return messages;

        }else {return null;}
    }
    private List<MessageObject> CreateContactInteractionObjects(List<MessageObject> interactions){
        if(repository.getAllUsers().getValue() != null && repository.getAllDepartments().getValue() != null && repository.getAllMessages().getValue() != null && CurrentUser.getValue() != null && Contacts.getValue() != null) {

            interactions.forEach(i ->{
                boolean Receiving = !i.getSender().equals(CurrentUser.getValue().getId());
                Integer ContactNr;
                if (Receiving) {ContactNr = i.getSender();} else { ContactNr = i.getReceiver(); }

                i.setInteractionDateTimeString(i.getDateTime().format(dateTimeFormatter));
                i.setDivider(false);

                Contacts.getValue().stream().filter(c -> c.getUserNr().equals(ContactNr)).findAny().ifPresent(i::setContact);

                //set contact information
                if(i.getType().equals(MessageObject.TYPE_PROFESSIONAL_GROUP_MESSAGE)){
                    repository.getAllDepartments().getValue().stream().filter(d -> d.getId().equals(i.getReceiver())).findAny().ifPresent(d -> {
                        i.setInteractionName(d.getName());
                        i.setInteractionBGColor(Color.parseColor(d.getColor()));
                    });

                }else if(i.getType().equals(MessageObject.TYPE_PROFESSIONAL_P2P_MESSAGE)){
                    repository.getAllUsers().getValue().stream().filter(d -> d.getId().equals(ContactNr)).findAny().ifPresent(u -> {
                        i.setInteractionName(u.getName());
                        repository.getAllDepartments().getValue().stream().filter(d -> d.getId().equals(u.getDepartmentId())).findAny().ifPresent(d -> i.setInteractionBGColor(Color.parseColor(d.getColor())));
                    });
                }

                //set interaction type image
                if(i.getType().equals(MessageObject.TYPE_PROFESSIONAL_CALL) && Receiving){i.setTypeImage(ContextCompat.getDrawable(getApplication(),R.drawable.round_call_incomming_icon));}
                else if (i.getType().equals(MessageObject.TYPE_PROFESSIONAL_CALL) && !Receiving){i.setTypeImage(ContextCompat.getDrawable(getApplication(),R.drawable.round_message_outgoing_icon));}
                else if (i.getType().equals(MessageObject.TYPE_PROFESSIONAL_P2P_MESSAGE) || i.getType().equals(MessageObject.TYPE_PROFESSIONAL_GROUP_MESSAGE) && Receiving){i.setTypeImage(ContextCompat.getDrawable(getApplication(), R.drawable.round_message_incomming_icon));}
                else if (i.getType().equals(MessageObject.TYPE_PROFESSIONAL_P2P_MESSAGE) || i.getType().equals(MessageObject.TYPE_PROFESSIONAL_GROUP_MESSAGE) && !Receiving){i.setTypeImage(ContextCompat.getDrawable(getApplication(), R.drawable.round_message_outgoing_icon));}

            });


            //create list of all FinishedTask date
            ArrayList<LocalDate> dates = new ArrayList<>();
            interactions.forEach(o ->{
                if(dates.stream().noneMatch(d -> d.equals(o.getDateTime().toLocalDate()))){
                    dates.add(o.getDateTime().toLocalDate());
                }
            });

            //create divider for all dates
            dates.forEach(o ->{
                MessageObject message = new MessageObject(null,null,null,o.atTime(LocalTime.MAX),null,null,null);
                message.setInteractionDateTimeString(o.format(dateFormatter));
                message.setDivider(true);
                interactions.add(message);
            });

            //get the newest messages at the top
            Collections.sort(interactions, ((o1, o2) ->  o1.getDateTime().compareTo(o2.getDateTime())));

            return interactions;
        }else {return null;}
    }
}
