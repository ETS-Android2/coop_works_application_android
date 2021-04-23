package com.example.coopapp20.Contact.ContactVoiceChat;

import android.app.Application;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.coopapp20.Contact.ContactRepository;
import com.example.coopapp20.Data.Objects.UserObject;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class VoiceChatViewModel extends AndroidViewModel {

    //Network
    private ServerSocket server;
    private Socket socket;
    private MediaPlayer Player;

    private ContactRepository repository;
    private MainViewModel mainViewModel;

    private LiveData<List<UserObject>> AllUsers;
    private LiveData<UserObject> CurrentUser;
    private MutableLiveData<UserObject> CallUser = new MutableLiveData<>();
    private MutableLiveData<Integer> CallStatus = new MutableLiveData<>();
    private MutableLiveData<String> CallDuration = new MutableLiveData<>();
    private MutableLiveData<Boolean> CallOutgoing = new MutableLiveData<>();

    static final int STATUS_PENDING = 1;
    static final int STATUS_ACCEPTED = 2;
    static final int STATUS_ENDED = 3;

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public VoiceChatViewModel(@NonNull Application application) {
        super(application);
        repository = new ContactRepository(getApplication());

        AllUsers = Transformations.map(repository.getAllUsers(),input -> input);

        try { server = new ServerSocket(4321);} catch (IOException e) { e.printStackTrace();}
    }

    MutableLiveData<UserObject> getCallUser(){return CallUser;}
    MutableLiveData<Integer> getCallStatus(){return CallStatus;}
    MutableLiveData<Boolean> getOutgoing() { return CallOutgoing; }
    MutableLiveData<String> getCallDuration() { return CallDuration; }
    public void setCaller(UserObject user){if(mainViewModel != null){CallUser.setValue(user);MakeCall();CallOutgoing.setValue(true);}}
    public void setCurrentUser(LiveData<UserObject> currentUser) { CurrentUser = currentUser; }
    public void setMainViewModel(MainViewModel MainViewModel){mainViewModel = MainViewModel;}

    private void ListenForCalls(){
        Socket socketTemp = null;
        try { socketTemp = server.accept(); } catch (IOException e) { e.printStackTrace(); }

        if(mainViewModel != null && CallStatus == null && socketTemp != null && socketTemp.isConnected() && AllUsers.getValue() != null){
            try {
                Integer Return = Integer.parseInt(new BufferedReader(new InputStreamReader(socketTemp.getInputStream())).readLine());
                UserObject user = AllUsers.getValue().stream().filter(o -> o.getId().equals(Return)).findAny().orElse(null);

                if(user != null){
                    socket = socketTemp;
                    CallUser.setValue(user);
                    CallOutgoing.setValue(false);
                    CallStatus.setValue(STATUS_PENDING);
                    mainViewModel.getMainNavController().navigate(R.id.contactCallFrag);
                }
            } catch (IOException e) { e.printStackTrace();}
        }

        if(CallStatus == null){ClearCall();}
    }

    private void MakeCall(){
        new Thread(()->{
            try {
                Log.e("VoiceChat", "ATTEMPTING CALL");
                CallStatus.setValue(STATUS_PENDING);
                CallCountDown();
                setMediaPlayer(1);

                mainViewModel.getMainNavController().navigate(R.id.contactCallFrag);

                socket = new Socket(CallUser.getValue().getIpAddress(), CallUser.getValue().getPortNr());

                //begin reading messages from caller
                if (socket.isConnected()) {
                    new PrintWriter(socket.getOutputStream()).println(CurrentUser.getValue().getId());
                    String Return = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
                    if(Return != null && !Return.equals("-1")){CallStatus.setValue(STATUS_ACCEPTED); new VoiceChatAudio(socket,this);CallDurationCounter();}
                } else { EndCall(); }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    void AcceptCall(){
        CallStatus.setValue(STATUS_ACCEPTED);
        new VoiceChatAudio(socket, this);
    }

    void EndCall(){
        if(CallStatus.getValue() != STATUS_ENDED){
            CallStatus.setValue(STATUS_ENDED);

            setMediaPlayer(2);

            if(socket != null && !socket.isClosed()){
                try { socket.close();} catch (IOException e) { e.printStackTrace();}
            }
        }
    }

    void ClearCall(){
        setMediaPlayer(0);
        CallUser.setValue(null);
        CallStatus.setValue(null);
        CallDuration.setValue(null);
        CallOutgoing.setValue(null);
        socket = null;
        ListenForCalls();
    }

    private void setMediaPlayer(int status){
        if(Player != null){
            Player.stop();
            Player.release();
            Player = null;
        }

        switch (status){
            case 1:
                Player = MediaPlayer.create(getApplication(), R.raw.call_outgoing);
                Player.setLooping(true);
                Player.start();
                break;
            case 2:
                Player = MediaPlayer.create(getApplication(),R.raw.call_ended);
                Player.setLooping(true);
                Player.start();
                break;
        }
    }

    private void CallCountDown(){
        int CountDownMillis = 15000;

        new Timer().schedule(
                new TimerTask() {@Override public void run() { if(CallStatus.getValue() == null || CallStatus.getValue() == STATUS_PENDING){EndCall();} }},
                new Date(System.currentTimeMillis() + CountDownMillis)
                );
    }

    private void CallDurationCounter(){
        LocalTime CallStart = LocalTime.now();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(CallStatus.getValue() == STATUS_ACCEPTED) {
                    CallDuration.setValue(LocalTime.now().minusSeconds(CallStart.toSecondOfDay()).format(timeFormatter));
                }else {cancel();}
            }
        }, 1000);

    }
}
