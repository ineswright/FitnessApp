package com.gamecodeschool.myfitnessapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.*;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.gamecodeschool.myfitnessapp.databases.*;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class HomeActivity extends BaseNavDrawer
        implements NavigationView.OnNavigationItemSelectedListener {

    //TODO delete testButton and listener once confident initialEntry triggers correctly
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private AppDatabase db;
    private Button testButton;
    private TextView text;

    private View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            {   goToEntry();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //create variables to hold UI elements required, create activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set up the navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        db = AppDatabase.getAppDatabase(getApplicationContext());

        testButton = findViewById(R.id.button);
        testButton.setOnClickListener(buttonOnClickListener);
        text = findViewById(R.id.text);

        //If app has just launched, launch initial entry activity
        SharedPreferences settings = getSharedPreferences("prefs", 0);
        boolean firstRun = settings.getBoolean("firstRun", true);
        if ( firstRun )
        {            // to be ran on install
            goToEntry();
        }
    }

    private void goToEntry() {
        Intent intent = new Intent(this, InitialEntry.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }















    /*
    private boolean testDb() {
        //get most recent entry in table
        Future data = Executors.newSingleThreadExecutor().submit(new Callable() {
                                                                     @Override
                                                                     public Users call(){
                                                                         return db.userDao().getTop();
                                                                     }});

        Users user;
        try { user = (Users) data.get();
        } catch (Exception ex){
            user = new Users();
            user.setGender('M');
        }

        boolean exists;
        try { String name = user.getName();
            exists = true;}
        catch (java.lang.NullPointerException ex) {
            System.out.println("Database does not exist");
            exists = false;
        }
        return exists;
    }
//Above method was previous attempt to see if db existed
*/

/*
    private ProgressUpdates testDb() {
        //get most recent entry in table
        Future data = Executors.newSingleThreadExecutor().submit(new Callable() {
                                                                     @Override
                                                                     public ProgressUpdates call(){
                                                                         return db.progressDao().getTop();
                                                                     }
                                                                 }
        );

        ProgressUpdates update;

        try { update = (ProgressUpdates) data.get();
        } catch (Exception ex){
            //if no value yet in database, return -1
            update = new ProgressUpdates();
            update.setBodyfat(1);
            update.setWeight(1);
            update.setDate("1");
        }
        System.out.println("testDb ran");
        return update;
    }
*/
/*    private void populateWithTestData(AppDatabase db) {
        Users user = new Users();
        user.setName("Ines Wright");
        user.setGender('F');
        user.setHeight(174);
        user.setDOB("2001-09-01");
        user.setGoalWeight(60);
        user.setGoalBodyfat(10);
        user.setGoalNumWorkouts(3);
        addUser(db, user);
    }

// will be able to delete kevins database runnable class once I can comfortably delete these 2 methods
    private void addUser(final AppDatabase db, Users user) {
        DatabaseRunnable dbRunnable = new DatabaseRunnable(db, user);
        executorService.execute(dbRunnable);
    }
*/
}
