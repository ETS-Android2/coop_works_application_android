package com.example.coopapp20.zOtherFiles;

import java.time.Duration;
import java.util.Locale;

public class DurationFormat {

    public static String DurationToTimeString(Duration duration){
        int s = (int) duration.getSeconds();
        return String.format(Locale.getDefault(),"%d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60));
    }
}
