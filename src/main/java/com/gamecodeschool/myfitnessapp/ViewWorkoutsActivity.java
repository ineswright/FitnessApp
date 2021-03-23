package com.gamecodeschool.myfitnessapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gamecodeschool.myfitnessapp.databases.AppDatabase;
import com.gamecodeschool.myfitnessapp.databases.CompletedWorkouts;
import com.gamecodeschool.myfitnessapp.databases.ProgressUpdates;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

/*
import android.support.design.widget.NavigationView;
import androidx.core.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
*/


public class ViewWorkoutsActivity extends BaseNavDrawer
        implements NavigationView.OnNavigationItemSelectedListener {

    private AppDatabase db;
    private CompletedWorkouts[] workouts;
    private LinearLayout layout;
    private TextView topText;
    private Button[] buttons;
    private Button[] delButtons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_workouts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = AppDatabase.getAppDatabase(getApplicationContext());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        layout = findViewById(R.id.displayWorkoutsLayout);
        topText = findViewById(R.id.topText);

        workouts = getWorkouts();
        //if no workout data has been retrieved, display error message
        if (workouts.length == 0) {
            topText.setText("No workout data stored. Please add workouts on the New Workout Page.");
        } else {
            displayWorkouts();
        }
    }


    //todo display by date by searching by date on sql and storing at same index using
    //todo btn.setId(workouts[i].getWorkId()) and same to set index in arrays
    //todo would then have to fix sorting by date programmatically?

    public void displayWorkouts() {
        buttons = new Button[workouts.length];
        delButtons = new Button[workouts.length];
        for (int i = 0; i < workouts.length; i++) {
            final Button btn = new Button(this);
            //id of the button corresponds to the ID of the workout
            btn.setId(workouts[i].getWorkoutId());
            btn.setText("Date: " + workouts[i].getDate() + "   Time: " + workouts[i].getTime() + " mins");
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    final int id = (int) btn.getId();
                    workoutSelected(id);
                }
            });
            //set layout parameters for buttons- so they are right height, width, colour, etc
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(50,0,0,-30);
            btn.setLayoutParams(params);
            btn.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            btn.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            btn.setStateListAnimator(null);

            layout.addView(btn);
            buttons[i] = btn;

            final Button delBtn = new Button(this);
            //id of the button corresponds to the ID of the workout it refers to
            delBtn.setId(workouts[i].getWorkoutId());
            delBtn.setText("Delete");
            delBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view){
                    final int id = (int) delBtn.getId();
                    deleteWorkout(id);
                }
            });
            LinearLayout.LayoutParams delParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            delParams.setMargins(50,-30,0,0);
            delBtn.setLayoutParams(delParams);
            delBtn.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            delBtn.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            delBtn.setStateListAnimator(null);

            layout.addView(delBtn);
            delButtons[i] = delBtn;
        }
    }

    public CompletedWorkouts[] getWorkouts() {
        Future data = Executors.newSingleThreadExecutor().submit(new Callable() {
                                                                     @Override
                                                                     public CompletedWorkouts[] call(){
                                                                         return db.workoutsDao().getAll();
                                                                     }
                                                                 }
        );

        CompletedWorkouts[] workouts;
        //returns workouts by ID, which are 0 indexed
        try { workouts = (CompletedWorkouts[]) data.get();
        } catch (Exception ex){
            //if no value yet in database, return -1
            workouts = new CompletedWorkouts[0];
        }
        return workouts;
    }
    public void workoutSelected(int id) {
        Intent intent = new Intent(this, ViewWorkoutDataActivity.class);
        intent.putExtra("workoutID",id);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void deleteWorkout(final int id) {
        //remove workout from completed workouts table
        Executors.newSingleThreadScheduledExecutor().submit(new Runnable() {
            @Override
            public void run() {
                db.exercisesDao().deleteById(id);
                db.workoutsDao().deleteById(id);
            }
        });
        //relaunches activity with updated workouts
        Intent intent = new Intent(this, ViewWorkoutsActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
    }


}
