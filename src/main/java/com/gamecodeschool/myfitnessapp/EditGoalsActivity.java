package com.gamecodeschool.myfitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gamecodeschool.myfitnessapp.databases.AppDatabase;
import com.gamecodeschool.myfitnessapp.databases.ProgressUpdates;
import com.gamecodeschool.myfitnessapp.databases.Users;
import com.google.android.material.navigation.NavigationView;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;


public class EditGoalsActivity extends BaseNavDrawer
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button saveGoals;
    private Button cancelGoals;
    private EditText workouts;
    private EditText bodyweight;
    private EditText bodyfat;
    private TextView goalsError;
    private AppDatabase db;

    //define what happens when save and cancel buttons are clicked
    private View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            {   updateGoals();
                 }        }
    };

    private View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancel();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_goals);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        saveGoals = findViewById(R.id.saveGoalsButton);
        saveGoals.setOnClickListener(saveListener);
        cancelGoals = findViewById(R.id.cancelGoalsButton);
        cancelGoals.setOnClickListener(cancelListener);
        workouts = findViewById(R.id.inputNewWorkouts);
        bodyweight = findViewById(R.id.inputNewBodyweight);
        bodyfat = findViewById(R.id.inputNewBodyfat);
        goalsError = findViewById(R.id.errorGoals);

        //db = Room.databaseBuilder(getApplicationContext(),
        //        AppDatabase.class, "database-name").build();
        db = AppDatabase.getAppDatabase(getApplicationContext());

        //get latest data and insert into input boxes
        Users user = getDetails();
        bodyfat.setText(String.valueOf(user.getGoalBodyfat()));
        bodyweight.setText(String.valueOf(user.getGoalWeight()));
        workouts.setText(String.valueOf(user.getGoalNumWorkouts()));

    }

    private void cancel() {
        workouts.setText(null);
        bodyweight.setText(null);
        bodyfat.setText(null);
    }

    private void updateGoals() {
       if (!presenceCheck(workouts) || !presenceCheck(bodyweight) || !presenceCheck(bodyfat)) {
            goalsError.setText("Please enter a value in every box");
            goalsError.setVisibility(View.VISIBLE);
            return;
        }
        final int newWorkouts = Integer.valueOf(workouts.getText().toString());
        final float newBodyweight = Float.valueOf(bodyweight.getText().toString());
        final float newBodyfat = Float.valueOf(bodyfat.getText().toString());

        if (!validate(newWorkouts, newBodyweight, newBodyfat)) {
            goalsError.setText("Make sure all your values are correct.");
            goalsError.setVisibility(View.VISIBLE);
            return;
        }
        else {
            goalsError.setVisibility(View.INVISIBLE);
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                db.userDao().update(newBodyweight, newBodyfat, newWorkouts);
            }
        });
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }

    private Users getDetails() {
        //get most recent entry in table
        Future data = Executors.newSingleThreadExecutor().submit(new Callable() {
                                                                     @Override
                                                                     public Users call(){
                                                                         return db.userDao().getTop();
                                                                     }
                                                                 }        );

        Users user;

        try { user = (Users) data.get();
        } catch (Exception ex){
            //if no value yet in database, return -1
            user = new Users();
            user.setGoalWeight(-1);
            user.setGoalBodyfat(-1);
            user.setGoalNumWorkouts(-1);
        }
        return user;
    }

    private boolean presenceCheck(EditText eText) {
        if(eText.getText().toString().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean validate(int workouts, float bodyweight, float bodyfat) {
        if (workouts < 1 || workouts > 7 || bodyweight < 45 || bodyweight > 250 || bodyfat < 2 || bodyfat > 50) {
            return false;
        }
        else {return true;}
    }

}
