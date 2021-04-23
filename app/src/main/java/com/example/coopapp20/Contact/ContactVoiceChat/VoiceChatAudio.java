package com.example.coopapp20.Contact.ContactVoiceChat;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

import androidx.lifecycle.Observer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class VoiceChatAudio {

    //Audio
    private boolean CallOngoing;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int sampleRate = 8000;
    private int RecorderChannelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int PlayerChannelConfig = AudioFormat.CHANNEL_OUT_MONO;
    private int RecorderBuffSize = AudioRecord.getMinBufferSize(sampleRate, RecorderChannelConfig, audioFormat);
    private int PlayerBuffSize = AudioTrack.getMinBufferSize(sampleRate, PlayerChannelConfig, audioFormat);

    private Socket socket;
    private VoiceChatViewModel viewModel;
    private Observer<Integer> observer;

    VoiceChatAudio(Socket Socket, VoiceChatViewModel ViewModel){
        socket =Socket;
        viewModel = ViewModel;
        observer = i -> CallOngoing = i == VoiceChatViewModel.STATUS_ACCEPTED;
        viewModel.getCallStatus().observeForever(observer);

        AudioSender();
        AudioReceiver();
    }

    private void AudioSender(){
        new Thread(()->{
            try {
                //set values for DataStream and recorder
                byte[] buf = new byte[24];
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,sampleRate, RecorderChannelConfig,audioFormat,RecorderBuffSize);
                recorder.startRecording();

                Log.e("VoiceChat","Sender - Started : )");

                //While call hasn't ended, check if sound is supposed to be sent
                while (CallOngoing) {
                        //read bytes from recorder and send over DataOutputStream
                        if(recorder.read(buf, 0, buf.length,AudioRecord.READ_BLOCKING) != -1) {
                            dos.write(buf, 0, buf.length);
                        }
                }
                Log.e("VoiceChat","Sender - Stopped");
            } catch (IOException e) { e.printStackTrace(); viewModel.getCallStatus().setValue(VoiceChatViewModel.STATUS_ENDED);}
        }).start();
    }

    private void AudioReceiver(){
        new Thread(()->{
            try {
                //set values for DataStream and recorder
                byte[] buf = new byte[24];
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                AudioTrack Player = new AudioTrack(AudioTrack.MODE_STREAM,sampleRate,PlayerChannelConfig,audioFormat,PlayerBuffSize,AudioTrack.MODE_STREAM);

                Log.e("VoiceChat","Receiver - Started");

                while (CallOngoing) {
                    //read audio
                    if(dis.read(buf,0,buf.length) == buf.length){
                        Player.write(buf,0,buf.length);
                    }else {viewModel.getCallStatus().setValue(VoiceChatViewModel.STATUS_ENDED);}
                }

                Player.stop();
                Log.e("VoiceChat","Receiver - Stopped");

            } catch (IOException e) { e.printStackTrace(); viewModel.getCallStatus().setValue(VoiceChatViewModel.STATUS_ENDED);}
        }).start();
    }

}
