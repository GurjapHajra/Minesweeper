package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.minesweeper.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class setting extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String spinner = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //hides the notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the navigation bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //Calls the method below to set up the spinner
        addItemsOnSpinner();
    }
    //hides the navigation bar
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    public void addItemsOnSpinner() {
        //Find your spinner
        Spinner theSpinner = (Spinner) findViewById(R.id.bombspinner);
        List<String> list = new ArrayList<String>();
        //Put in the values that you want
        list.add("Default");
        list.add("Gun Shot");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Call these two methods on your spinner
        theSpinner.setAdapter(dataAdapter);
        theSpinner.setOnItemSelectedListener(this);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //This is where you put th ecode that you want the spinner to run
        // when the user selects it.
        Spinner theSpinner = (Spinner) findViewById(R.id.bombspinner);
        //Pulls out the selected item. You probably want to store this in a global variable
        spinner = theSpinner.getSelectedItem().toString();

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // Just put this in. You need it.
    }
    public void doneb(){
        // when the user selects it.
        Spinner theSpinner = (Spinner) findViewById(R.id.bombspinner);
        //Pulls out the selected item. You probably want to store this in a global variable
        spinner = theSpinner.getSelectedItem().toString();
        if(spinner.equals("Default")){
            try{
                FileOutputStream out1 = openFileOutput("dataset.txt", Activity.MODE_PRIVATE);
                out1.write(1);
                out1.flush();
                out1.close();
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            try{
                FileOutputStream out1 = openFileOutput("dataset.txt", Activity.MODE_PRIVATE);
                out1.write(2);
                out1.flush();
                out1.close();
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        Intent i = new Intent(this, HomeScreen.class);
        startActivity(i);
    }

}
