package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class HomeScreen extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        // plays music
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.gamemusic);
        mediaPlayer.start();
        mediaPlayer.isLooping();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        //hides the notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the navigation bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
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

    public void play(View view){
        mediaPlayer.stop();
        Intent i = new Intent(this, GameScreen.class);
        //
        EditText names= (EditText) findViewById(R.id.name);
        String s = names.getText().toString();
        //stores the ascii value of the String
        try {
            FileOutputStream out1 = openFileOutput("dataname.txt", Activity.MODE_PRIVATE);
            for (int j = 0; j < s.length(); j++) {
                out1.write(s.charAt(j));
            }
            out1.flush();
            out1.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        startActivity(i);
    }
    public void instruction(View view){
        Intent i = new Intent(this, Instructions.class);
        startActivity(i);
    }
    public void quit(View view){
        finish();
        moveTaskToBack(true);
    }
    public void setting(View view){
        Intent i = new Intent(this, setting.class);
        startActivity(i);
    }
}
