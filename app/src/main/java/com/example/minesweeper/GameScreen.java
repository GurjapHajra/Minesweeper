package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.*;

import java.util.concurrent.TimeUnit;


public class GameScreen extends AppCompatActivity {
    // size of the grid
    int row = 10;
    int col = 10;
    // number of mives
    int moves = 0;
    // number of mines
    int mines = 10;
    Boolean flag = false;
    // arrays holing the grid
    int field[][] = new int [row][col];
    int show[][] = new int [row][col];
    int flags[][] = new int [row][col];
    // array holing the images
    ImageView pics[] = new ImageView[row*col];
    // musics

    @Override
    //run when app is created
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        //hides the notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the navigation bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        //print the Game Grid
        GridLayout g = (GridLayout) findViewById(R.id.grid);
        int m = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                pics[m] = new ImageView(this);
                setpic(pics[m], m);
                pics[m].setId(m);
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(70,70);
                pics[m].setLayoutParams(parms);
                pics[m].setScaleType(ImageView.ScaleType.FIT_XY);
                pics[m].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        click(v.getId());
                    }
                });
                g.addView(pics[m]);
                m++;
            }
        }
        //adds mines
        addmines(mines);
        //add neighbours
        neighbour();
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
    //runs whenever a click is registered
    public void click (int pos){
        MediaPlayer mediaPlayer1;
        mediaPlayer1 = MediaPlayer.create(getApplicationContext(), R.raw.arrowshot);
        mediaPlayer1.start();

        // Makes an animation
        Animation animation;
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
        // gets the 2d i,j value of the button clicked
        int i = pos / col;
        int j = pos % col;
        // if flag is acctive
        if(flag){
            if(flags[i][j] == 0 && show[i][j]==0){
                flags[i][j] = 1;
            }else if(flags[i][j]==1){
                flags[i][j] = 0;
            }
        }
        // if cell is already showing
        else if(show[i][j]==1||flags[i][j]==1){
            Toast.makeText(getApplicationContext(),"Click Somewhere else",Toast.LENGTH_SHORT).show();
        }
        // if the cell contains a mine
        else if(field[i][j]==10){
            Toast.makeText(getApplicationContext(),"You Lose",Toast.LENGTH_LONG).show();
            show[i][j] = 1;
            pics[pos].setImageResource(R.drawable.mine);
            showbombs(1);
        }
        // if the cell contains a number
        else if(field[i][j]>0){
            show[i][j]=1;
            pics[convert(i,j)].startAnimation(animation);
            moves++;
        }
        // anything else
        else{
            int k = 0;
            show[i][j]=1;
            pics[convert(i,j)].startAnimation(animation);
            open(i,j);
            moves++;
        }
        //redraws the screen
        redraw();
    }
    //taken the image and the position and output the image at the correct position
    public void setpic(ImageView i, int pos){
        // calculate the 2d positions of the button clicked
        int x = pos/col;
        int y = pos%col;

        if (field[x][y]==0&&flags[x][y]==0){
            i.setImageResource(R.drawable.closed);
        }else if(flags[x][y]==1){
            i.setImageResource(R.drawable.n0);
        }else if(show[x][y]==1&&field[x][y]==0){
            i.setImageResource(R.drawable.flag);
        }else if(show[x][y]==1&&field[x][y]==1){
            i.setImageResource(R.drawable.n1);
        }else if(show[x][y]==1&&field[x][y]==2){
            i.setImageResource(R.drawable.n2);
        }else if(show[x][y]==1&&field[x][y]==3){
            i.setImageResource(R.drawable.n3);
        }else if(show[x][y]==1&&field[x][y]==4){
            i.setImageResource(R.drawable.n4);
        }else if(show[x][y]==1&&field[x][y]==5){
            i.setImageResource(R.drawable.n5);
        }else if(show[x][y]==1&&field[x][y]==6){
            i.setImageResource(R.drawable.n6);
        }else if(show[x][y]==1&&field[x][y]==7){
            i.setImageResource(R.drawable.n7);
        }else if(show[x][y]==1&&field[x][y]==8){
            i.setImageResource(R.drawable.n8);
        }else if(show[x][y]==1&&field[x][y]==10){
            i.setImageResource(R.drawable.mine);
        }
    }
    //redraws the grid
    public void redraw() {
        int m = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (show[i][j]==0&&flags[i][j]==0){
                    pics[m].setImageResource(R.drawable.closed);
                }else if(flags[i][j]==1){
                    pics[m].setImageResource(R.drawable.flag);
                }else if(show[i][j]==1&&field[i][j]==0){
                    pics[m].setImageResource(R.drawable.n0);
                }else if(show[i][j]==1&&field[i][j]==1){
                    pics[m].setImageResource(R.drawable.n1);
                }else if(show[i][j]==1&&field[i][j]==2){
                    pics[m].setImageResource(R.drawable.n2);
                }else if(show[i][j]==1&&field[i][j]==3){
                    pics[m].setImageResource(R.drawable.n3);
                }else if(show[i][j]==1&&field[i][j]==4){
                    pics[m].setImageResource(R.drawable.n4);
                }else if(show[i][j]==1&&field[i][j]==5){
                    pics[m].setImageResource(R.drawable.n5);
                }else if(show[i][j]==1&&field[i][j]==6){
                    pics[m].setImageResource(R.drawable.n6);
                }else if(show[i][j]==1&&field[i][j]==7){
                    pics[m].setImageResource(R.drawable.n7);
                }else if(show[i][j]==1&&field[i][j]==8){
                    pics[m].setImageResource(R.drawable.n8);
                }else if(show[i][j]==1&&field[i][j]==10){
                    pics[m].setImageResource(R.drawable.mine);
                }
                m++;
            }
        }
        TextView move = (TextView) findViewById(R.id.movesText);
        move.setText("Moves: "+moves);
        if(win()){
            Toast.makeText(getApplicationContext(), "Win!", Toast.LENGTH_SHORT).show();
        }
    }

    //adds mines in the staring of a game
    //amt is the number of mines
    public void addmines(int amt){
        // for loop to randomly add mines
        int x,y;
        for (int i=0;i<amt;i++){
            do {
                x = (int) (Math.random() * row);
                y = (int) (Math.random() * col);
            }while(field[x][y]!=0);
            field[x][y] = 10;
        }

    }
    public void showf (View view){
        //nested for loop to show the intire grid
        for (int i=0;i<show.length;i++){
            for(int j = 0; j<show[i].length;j++){
                show[i][j] = 1;
            }
        }
        //redraws the screen
        redraw();
    }
    // for debuging print the array in the logcat
    public void print_array(int a [][]){
        for(int i=0;i<a.length;i++){
            String s = "";
            for(int j=0;j<a.length;j++){
                s = s+a[i][j]+", ";
            }
            Log.d("myTag",s);
        }
    }
    // in the field array sets all the neighbours
    public void neighbour () {
        for(int i = 0;i<field.length;i++){
            for(int j = 0;j<field[i].length;j++){
                if(field[i][j]==10){
                    if(i-1>=0){
                        field[i-1][j]++;
                        if(j-1>=0){
                            field[i-1][j-1]++;
                        }
                        if(j+1<field[i].length){
                            field[i-1][j+1]++;
                        }
                    }

                    if(i+1<field[i].length){
                        field[i+1][j]++;
                        if(j-1>=0){
                            field[i+1][j-1]++;
                        }
                        if(j+1<field[i].length){
                            field[i+1][j+1]++;
                        }
                    }

                    if(j-1>=0){
                        field[i][j-1]++;
                    }
                    if(j+1<field[i].length){
                        field[i][j+1]++;
                    }
                    //skip this one because it's the block with mine
                    //field[i][j]++;
                }
                for(int k=0;k<field.length;k++){
                    for(int l = 0;l<field[k].length;l++){
                        if(field[k][l]>10){
                            field[k][l] = 10;
                        }
                    }
                }
            }
        }
    }
    // resets the grid
    public void reset(View view){
        for (int i=0;i<show.length;i++) {
            for (int j = 0; j < show[i].length; j++) {
                show[i][j] = 0;
            }
        }
        for (int i=0;i<field.length;i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = 0;
            }
        }
        for (int i=0;i<flags.length;i++) {
            for (int j = 0; j < flags[i].length; j++) {
                flags[i][j] = 0;
            }
        }
        addmines(mines);
        neighbour();
        moves=0;
        redraw();
    }
    // converts i,j into position in the 1d array
    public int convert(int i, int j){
        i = i*col;
        return i+j;
    }
    // acivates flags
    public void flagb(View view){
        Button flagb = findViewById(R.id.flagb);
        if(flag){
            flag = false;
            flagb.setBackgroundColor(Color.RED);
            flagb.setTextColor(Color.WHITE);
        }else{
            flag = true;
            flagb.setBackgroundColor(Color.GREEN);
            flagb.setTextColor(Color.BLACK);
        }
    }
    // win meathod
    public boolean win(){
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                if(field[i][j]!=10 && show[i][j]==0){
                    return false;
                }
                if(field[i][j]==10 && flags[i][j]==0){
                    return false;
                }
            }
        }
        return true;
    }
    // opens the grid when clicked a empty cell
    public void open(int i,int j){
        // initialize animation variable
        Animation animation;
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
        if(field[i][j]!=0){
            return;
        }if(i-1>=0 && j-1>=0 && show[i-1][j-1]==0){
            show[i-1][j-1]=1;
            pics[convert(i-1,j-1)].startAnimation(animation);
            if(field[i-1][j-1]==0){
                open(i-1,j-1);
            }
        }if(i-1>=0 && show[i-1][j]==0){
            show[i-1][j]=1;
            pics[convert(i-1,j)].startAnimation(animation);
            if(field[i-1][j]==0){
                open(i-1,j);
            }
        }if(i-1>=0 && j+1<col && show[i-1][j+1]==0){
            show[i-1][j+1]=1;
            pics[convert(i-1,j+1)].startAnimation(animation);
            if(field[i-1][j+1]==0){
                open(i-1,j+1);
            }
        }if(j-1>=0 && show[i][j-1]==0){
            show[i][j-1]=1;
            pics[convert(i,j-1)].startAnimation(animation);
            if(field[i][j-1]==0){
                open(i,j-1);
            }
        }if(show[i][j]==0){
            show[i][j]=1;
            pics[convert(i,j)].startAnimation(animation);
            if(field[i][j]==0){
                open(i,j);
            }
        }if(j+1<col && show[i][j+1]==0){
            show[i][j+1]=1;
            pics[convert(i,j+1)].startAnimation(animation);
            if(field[i][j+1]==0){
                open(i,j+1);
            }
        }if(i+1<row && j-1>=0 && show[i+1][j-1]==0){
            show[i+1][j-1]=1;
            pics[convert(i+1,j-1)].startAnimation(animation);
            if(field[i+1][j-1]==0){
                open(i+1,j-1);
            }
        }if(i+1<row && show[i+1][j]==0){
            show[i+1][j]=1;
            pics[convert(i+1,j)].startAnimation(animation);
            if(field[i+1][j]==0){
                open(i+1,j);
            }
        }if(i+1<row && j+1<row && show[i+1][j+1]==0){
            show[i+1][j+1]=1;
            pics[convert(i+1,j+1)].startAnimation(animation);
            if(field[i+1][j+1]==0){
                open(i+1,j+1);
            }
        }
    }
    //saves the current grid
    public void saveb(View view){
        try{
            FileOutputStream out1 = openFileOutput("data1.txt", Activity.MODE_PRIVATE);
            for(int i=0; i<row;i++){
                for(int j=0; j<col; j++){
                    out1.write(field[i][j]);
                }
            }
            out1.flush();
            out1.close();

            FileOutputStream out2 = openFileOutput("data2.txt", Activity.MODE_PRIVATE);
            for(int i=0; i<row;i++){
                for(int j=0; j<col; j++){
                    out2.write(show[i][j]);
                }
            }
            out2.flush();
            out2.close();

            FileOutputStream out3 = openFileOutput("data3.txt", Activity.MODE_PRIVATE);
            for(int i=0; i<row;i++){
                for(int j=0; j<col; j++){
                    out3.write(flags[i][j]);
                }
            }
            out3.write(moves);
            out3.flush();
            out3.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //open the saved file
    public void openb(View view){

        try {
            FileInputStream in1 = openFileInput("data1.txt");
            for(int i=0; i<row;i++) {
                for (int j = 0; j < col; j++) {
                    field[i][j] = in1.read();
                }
            }
            in1.close();

            FileInputStream in2 = openFileInput("data2.txt");
            for(int i=0; i<row;i++) {
                for (int j = 0; j < col; j++) {
                    show[i][j] = in2.read();
                }
            }
            in2.close();

            FileInputStream in3 = openFileInput("data3.txt");
            for(int i=0; i<row;i++) {
                for (int j = 0; j < col; j++) {
                    flags[i][j] = in3.read();
                }
            }
            moves = in3.read();
            in3.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        redraw();
    }
    //Shows all teh bombs at the end
    public void showbombs(int num) {
        int a = 1;
        try {
            FileInputStream in1 = openFileInput("dataset.txt");
            a = in1.read();
            in1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (a == 2) {
            MediaPlayer mediaPlayer2;
            mediaPlayer2 = MediaPlayer.create(getApplicationContext(), R.raw.shot);
            mediaPlayer2.start();
        }
        else{
            MediaPlayer mediaPlayer2;
            mediaPlayer2 = MediaPlayer.create(getApplicationContext(), R.raw.minexplosion);
            mediaPlayer2.start();
        }
        Animation animation;
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        if (num > mines) {
            return;
        } else {
            int bombnum = 1;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (field[i][j] == 10 && bombnum == num) {
                        show[i][j] = 1;
                        bombnum++;
                        pics[convert(i, j)].startAnimation(animation);
                    } else if (field[i][j] == 10) {
                        bombnum++;
                    }
                }
            }
            redraw();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    showbombs(num + 1);
                }
            }, 2000);
        }
    }

    //opens the insructions screen
    public void instruction(View view){
        Intent i = new Intent(this, Instructions.class);
        startActivity(i);
    }
}
