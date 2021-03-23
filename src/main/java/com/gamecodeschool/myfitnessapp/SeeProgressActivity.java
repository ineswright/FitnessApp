package com.gamecodeschool.myfitnessapp;

import android.os.Bundle;
import android.widget.TextView;

import com.gamecodeschool.myfitnessapp.databases.AppDatabase;
import com.gamecodeschool.myfitnessapp.databases.ProgressUpdates;
import com.gamecodeschool.myfitnessapp.databases.Users;
import com.google.android.material.navigation.NavigationView;

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


public class SeeProgressActivity extends BaseNavDrawer
        implements NavigationView.OnNavigationItemSelectedListener {

    private AppDatabase db;
    private Users user;
    private ProgressUpdates[] update = new ProgressUpdates[2];
    private TextView changeWeightText;
    private TextView percentageWeightText;
    private TextView changeFatText;
    private TextView percentageFatText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_progress);
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

        changeWeightText = findViewById(R.id.totalChangeWeight);
        percentageWeightText = findViewById(R.id.percentageChangeWeight);
        changeFatText = findViewById(R.id.totalChangeFat);
        percentageFatText = findViewById(R.id.percentageChangeFat);

        user = getGoals();
        update = getUpdates();
        displayProgress();
    }

    private Users getGoals() {
        //get the users goals
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

    private ProgressUpdates[] getUpdates() {
        //get most recent data on user
        Future data = Executors.newSingleThreadExecutor().submit(new Callable() {
                                                                     @Override
                                                                     public ProgressUpdates[] call(){
                                                                         ProgressUpdates[] updates = new ProgressUpdates[2];
                                                                         updates[0] = db.progressDao().getBottom();
                                                                         updates[1] = db.progressDao().getTop();
                                                                         return updates;
                                                                     }
                                                                 }
        );

        ProgressUpdates[] update = new ProgressUpdates[2];

        try { update = (ProgressUpdates[]) data.get();
        } catch (Exception ex){
            //if no value yet in database, return -1
            update[0] = new ProgressUpdates();
            update[0].setWeight(-1);
            update[0].setBodyfat(-1);
            update[0].setDate("-1");
        }
        return update;

    }

    private void displayProgress() {
        //do some calculations of progress
        float totalChangeWeight = update[0].getWeight() - update[1].getWeight();
        float totalWeightToLose = update[0].getWeight() - user.getGoalWeight();
        float percentageChangeWeight = totalChangeWeight / totalWeightToLose;
        float totalChangeFat = update[0].getBodyfat() - update[1].getBodyfat();
        float totalFatToLose = update[0].getBodyfat() - user.getGoalBodyfat();
        float percentageChangeFat = totalChangeFat / totalFatToLose;

        //display progress calculations on screen
        changeWeightText.setText(changeWeightText.getText() + " " + totalChangeWeight);
        changeFatText.setText(changeFatText.getText() + " " + totalChangeFat);
        percentageWeightText.setText(percentageWeightText.getText() + " " + percentageChangeWeight);
        percentageFatText.setText(percentageFatText.getText() + " " + percentageChangeFat);

    }




}