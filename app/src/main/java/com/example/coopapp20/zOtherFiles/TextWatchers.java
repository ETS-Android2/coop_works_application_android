package com.example.coopapp20.zOtherFiles;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextWatchers {

    public void DurationTextWatcher(EditText CurrentEditText){
        CurrentEditText.addTextChangedListener(
        new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int PreviousCount = 0;
                String text = s.toString();
                Pattern pattern = Pattern.compile("\\d\\d:\\d\\d");
                Matcher matcher = pattern.matcher(text);

                //check if pattern matches
                if(!matcher.matches() && !matcher.hitEnd()){
                    CurrentEditText.setText(text.substring(0,text.length() -1));
                    CurrentEditText.setSelection(text.length() - 1);
                }

                //check if minute surpasses 60
                else if(text.matches("\\d\\d:\\d") && Integer.parseInt(text.substring(3,4)) > 5 || text.matches("\\d\\d:\\d\\d") && Integer.parseInt(text.substring(3,5)) > 59 ){
                    CurrentEditText.setText(text.substring(0,text.length() -1));
                    CurrentEditText.setSelection(text.length() - 1);
                }

                //set colon after 2 digits
                else if(text.matches("\\d\\d") && PreviousCount < count){
                    CurrentEditText.setText(text+":");
                    CurrentEditText.setSelection(CurrentEditText.getText().length());
                }

                PreviousCount = count;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void DurationToFromTextWatcher(EditText CurrentEditText){
        CurrentEditText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        int PreviousCount = 0;
                        String text = s.toString();
                        Pattern pattern = Pattern.compile("\\d\\d:\\d\\d - \\d\\d:\\d\\d");
                        Matcher matcher = pattern.matcher(text);

                        //check if pattern matches
                        if(!matcher.matches() && !matcher.hitEnd()){
                            CurrentEditText.setText(text.substring(0,text.length() -1));
                            CurrentEditText.setSelection(CurrentEditText.getText().length());
                        }

                        //check if minute surpasses 60
                        else if(text.matches("\\d\\d:\\d") && Integer.parseInt(text.substring(3,4)) > 5 || text.matches("\\d\\d:\\d\\d") && Integer.parseInt(text.substring(3,5)) > 59 ){
                            CurrentEditText.setText(text.substring(0,text.length() -1));
                            CurrentEditText.setSelection(CurrentEditText.getText().length());
                        }
                        else if(text.matches("\\d\\d:\\d\\d - \\d\\d:\\d") && Integer.parseInt(text.substring(11,12)) > 5 || text.matches("\\d\\d:\\d\\d - \\d\\d:\\d\\d") && Integer.parseInt(text.substring(11,13)) > 59 ){
                            CurrentEditText.setText(text.substring(0,text.length() -1));
                            CurrentEditText.setSelection(CurrentEditText.getText().length());
                        }

                        //set colon after 2 digits
                        else if((text.matches("\\d\\d") || text.matches("\\d\\d:\\d\\d - \\d\\d")) && PreviousCount < count){
                            CurrentEditText.setText(text+":");
                            CurrentEditText.setSelection(CurrentEditText.getText().length());
                        }
                        else if(text.matches("\\d\\d:\\d\\d")  && PreviousCount < count){
                            CurrentEditText.setText(text+" - ");
                            CurrentEditText.setSelection(CurrentEditText.getText().length());
                        }
                        else if(text.matches("\\d\\d:\\d\\d -") || text.matches("\\d\\d:\\d\\d ")){
                            CurrentEditText.setText(text.substring(0, 5));
                            CurrentEditText.setSelection(CurrentEditText.getText().length());
                        }

                        PreviousCount = count;
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });
    }

    public void TimeTextWatcher(EditText CurrentEditText){
        CurrentEditText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        int PreviousCount = 0;
                        String text = s.toString();
                        Pattern pattern = Pattern.compile("\\d\\d:\\d\\d");
                        Matcher matcher = pattern.matcher(text);

                        //check if pattern matches

                        //check if hour surpasses 23
                        if(text.matches("\\d") && Integer.parseInt(text.substring(0,1)) > 2 || text.matches("\\d\\d") && Integer.parseInt(text.substring(0,2)) > 23 ){
                            CurrentEditText.setText(text.substring(0,text.length() -1));
                            CurrentEditText.setSelection(CurrentEditText.getText().length());
                        }

                        //check if minute surpasses 60
                        else if(text.matches("\\d\\d:\\d") && Integer.parseInt(text.substring(3,4)) > 5 || text.matches("\\d\\d:\\d\\d") && Integer.parseInt(text.substring(3,5)) > 59 ){
                            CurrentEditText.setText(text.substring(0,text.length() -1));
                            CurrentEditText.setSelection(CurrentEditText.getText().length());
                        }

                        else if(text.matches("\\d\\d\\d")){
                            String NewText = text.substring(0,2)+":"+text.substring(2,3);
                            CurrentEditText.setText(NewText);
                            CurrentEditText.setSelection(CurrentEditText.getText().length());
                        }

                        //set colon after 2 digits
                        else if(text.matches("\\d\\d") && PreviousCount < count){
                            String NewText = (text+":");
                            CurrentEditText.setText(NewText);
                            CurrentEditText.setSelection(CurrentEditText.getText().length());
                        }

                        else if(!matcher.matches() && !matcher.hitEnd()){
                            CurrentEditText.setText(text.substring(0,text.length() -1));
                            CurrentEditText.setSelection(CurrentEditText.getText().length());
                        }

                        PreviousCount = count;
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });
    }

    public void DateTextWatchers(EditText CurrentEditText){
        CurrentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int PreviousCount = 0;
                String text = s.toString();
                Pattern pattern = Pattern.compile("\\d\\d/\\d\\d/\\d\\d");
                Matcher matcher = pattern.matcher(text);

                //check if pattern matches
                if(!matcher.matches() && !matcher.hitEnd()){
                    CurrentEditText.setText(text.substring(0,text.length() -1));
                    CurrentEditText.setSelection(text.length() - 1);
                }

                //check if numbers within date range
                else if(text.matches("\\d") && Integer.parseInt(text) > 3 || text.matches("\\d\\d") && Integer.parseInt(text) > 31 ){
                    CurrentEditText.setText(text.substring(0,text.length() -1));
                    CurrentEditText.setSelection(text.length() - 1);
                }else if(text.matches("\\d\\d/\\d") && Integer.parseInt(text.substring(3,4)) > 1 || text.matches("\\d\\d/\\d\\d") && Integer.parseInt(text.substring(3,5)) > 12  ){
                    CurrentEditText.setText(text.substring(0,text.length() -1));
                    CurrentEditText.setSelection(text.length() - 1);
                }

                //set colon after 2. and 4. digit
                else if(text.matches("\\d\\d")){
                    CurrentEditText.setText(text+"/");
                    CurrentEditText.setSelection(text.length()+1);
                } else if(text.matches("\\d\\d/\\d\\d")) {
                    CurrentEditText.setText(text + "/");
                    CurrentEditText.setSelection(text.length() + 1);
                }
                PreviousCount = count;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
