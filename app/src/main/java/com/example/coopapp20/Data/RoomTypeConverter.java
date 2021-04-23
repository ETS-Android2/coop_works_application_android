package com.example.coopapp20.Data;

import android.util.Log;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class RoomTypeConverter {

    @TypeConverter
    public static LocalDateTime DateTimeFromTimeStamp(Long value){

        return value == null ? null : LocalDateTime.ofEpochSecond(value,0, ZoneOffset.UTC);
    }

    @TypeConverter
    public static Long DateTimeToTimeStamp(LocalDateTime dateTime){
        return dateTime == null ? null : dateTime.toEpochSecond( ZoneOffset.UTC);
    }

    @TypeConverter
    public static LocalDate DateFromTimeStamp(Long value){
        return value == null ? null : LocalDate.ofEpochDay(value);

    }

    @TypeConverter
    public static Long DateToTimeStamp(LocalDate date){
        return date == null ? null : date.toEpochDay();
    }

    @TypeConverter
    public static LocalTime TimeFromTimeStamp(Long value){
        return value == null ? null : LocalTime.ofSecondOfDay(value);
    }

    @TypeConverter
    public static Long TimeToTimeStamp(LocalTime time){
        return time == null ? null : (long) time.toSecondOfDay();
    }

    @TypeConverter
    public static Duration DurationFromTimeStamp(Long value){
        return value == null ? null : Duration.ofSeconds(value);
    }

    @TypeConverter
    public static Long DurationToTimeStamp(Duration time){
        return time == null ? null : time.getSeconds();
    }

    @TypeConverter
    public static String IntegerListToString(List<Integer> integerList){
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {}.getType();
        return gson.toJson(integerList, type);
    }

    @TypeConverter
    public static List<Integer> IntegerListFromString(String value){
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {}.getType();
        return gson.fromJson(value, type);
    }

    @TypeConverter
    public static String StringListToString(ArrayList<String> StringList){
        Gson gson = new Gson();
        return gson.toJson(StringList);
    }

    @TypeConverter
    public static ArrayList<String> StringListFromString(String value){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(value, type);
    }
}
