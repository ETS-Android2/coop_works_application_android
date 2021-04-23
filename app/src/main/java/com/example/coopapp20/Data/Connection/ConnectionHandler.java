package com.example.coopapp20.Data.Connection;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.coopapp20.Data.Objects.DatabaseChangeObject;
import com.example.coopapp20.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConnectionHandler {

    private String StringReturn;
    private String ServerIp;
    private int ServerPortNr;
    private Gson gson = new Gson();

    public ConnectionHandler(Context context){
        SharedPreferences SharedPref = context.getSharedPreferences(context.getString(R.string.BasicPreferences), Context.MODE_PRIVATE);
        ServerIp = SharedPref.getString(context.getString(R.string.ServerName), null);
        ServerPortNr = SharedPref.getInt(context.getString(R.string.ServerPassword), 0);
        //Log.e("ConnectionHandler", "IP: "+ServerIp+" PORT: "+ServerPortNr);
    }

    public boolean CheckConnection(){
        try {
            SocketAddress address = new InetSocketAddress(ServerIp, ServerPortNr);
            Socket socket = new Socket();
            socket.connect(address, 2000);
            boolean isConnected = socket.isConnected();
            socket.close();
            return isConnected;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean Command(ConnectionCommandObject command){
        boolean Return = false;
        try {
            Socket socket = new Socket(ServerIp, ServerPortNr);
            socket.setSoTimeout(1000);
            if(socket.isConnected()) {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println(command.getCommandJsonPackage());
                Return = Boolean.parseBoolean(in.readLine());
                if (Return && command.getReturnExpected()) {
                    StringReturn = in.readLine();
                    Log.e("ConnectionHandler","Return:\n"+StringReturn);
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Return;
    }

    Boolean CommandChanges(List<DatabaseChangeObject> changes){
        boolean Return = false;

        ArrayList<ArrayList<String>> Changes = changes.stream().map(DatabaseChangeObject::getCommand).collect(Collectors.toCollection(ArrayList::new));

        try {
            Socket socket = new Socket(ServerIp, ServerPortNr);
            if(socket.isConnected()) {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println(gson.toJson(Changes));
                Return = Boolean.parseBoolean(in.readLine());

            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Return;
    }

    public String getStringReturn() {
        return StringReturn;
    }

}