package com.gamecodeschool.myfitnessapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.CursorJoiner;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.gamecodeschool.myfitnessapp.databases.AppDatabase;
import com.gamecodeschool.myfitnessapp.databases.DatabaseRunnable;
import com.gamecodeschool.myfitnessapp.databases.ExerciseBank;
import com.gamecodeschool.myfitnessapp.databases.ProgressUpdates;
import com.gamecodeschool.myfitnessapp.databases.Users;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;


public class PersonalDetailsActivity extends BaseNavDrawer
        implements NavigationView.OnNavigationItemSelectedListener{

    private AppDatabase db;
    private Button saveButton;
    private Button cancelButton;
    private EditText bodyfat;
    private EditText bodyweight;
    private EditText day;
    private EditText month;
    private EditText year;
    private TextView errorMsg;

    //define what happens when save and cancel buttons are clicked
    private View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            {   addtoDb(db); }        }
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
        setContentView(R.layout.activity_personal_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //db = Room.databaseBuilder(getApplicationContext(),
        //        AppDatabase.class, "database-name").build();
        db = AppDatabase.getAppDatabase(getApplicationContext());

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(saveListener);
        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(cancelListener);

        bodyfat = findViewById(R.id.inputBodyFat);
        bodyweight = findViewById(R.id.inputBodyWeight);
        day = findViewById(R.id.inputDay);
        month = findViewById(R.id.inputMonth);
        year = findViewById(R.id.inputYear);
        errorMsg = findViewById(R.id.errorMsg);

        //get latest data and insert into input boxes
        ProgressUpdates recentUpdate = getDetails();
        bodyfat.setText(String.valueOf(recentUpdate.getBodyfat()));
        bodyweight.setText(String.valueOf(recentUpdate.getWeight()));

        //get date and insert into input boxes
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(c);

        String currentDay = Character.toString(date.charAt(0)) + Character.toString(date.charAt(1));
        String currentMonth = Character.toString(date.charAt(3)) + Character.toString(date.charAt(4));
        String currentYear = Character.toString(date.charAt(6)) + Character.toString(date.charAt(7)) + Character.toString(date.charAt(8)) + Character.toString(date.charAt(9));

        day.setText(currentDay);
        month.setText(currentMonth);
        year.setText(currentYear);

    }
    //TODO change to getting all data then sort by date manually
    private ProgressUpdates getDetails() {
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
            update.setWeight(-1);
            update.setBodyfat(-1);
            update.setDate("-1");
        }
        return update;
   }

    private void addtoDb(AppDatabase db) {
        ProgressUpdates update = new ProgressUpdates();
        if (!presenceCheck(bodyweight) || !presenceCheck(bodyfat) || !presenceCheck(day) || !presenceCheck(month) || !presenceCheck(year)) {
            errorMsg.setText("Make sure you enter a value in every box.");
            errorMsg.setVisibility(View.VISIBLE);
            return;
        }

        int newBodyfat = Integer.valueOf(bodyfat.getText().toString());
        float newWeight = Float.valueOf(bodyweight.getText().toString());
        String newDay = day.getText().toString();
        String newMonth = month.getText().toString();
        String newYear = year.getText().toString();

        String newDate =  newDay + "/" + newMonth + "/" + newYear;

        if (validate(newBodyfat, newWeight, newDay, newMonth, newYear)) {
            update.setBodyfat(newBodyfat);
            update.setWeight(newWeight);
            update.setDate(newDate);
            addUpdate(db, update);
            errorMsg.setVisibility(View.INVISIBLE);
        }
        else {
            errorMsg.setText("Make sure all your values are correct.");
            errorMsg.setVisibility(View.VISIBLE);
        }
    }

    private void addUpdate(final AppDatabase db, final ProgressUpdates update) {
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                                                 @Override
                                                                 public void run() {
                                                                     db.progressDao().insertAll(update);
                                                                 }
                                                             });
        /*
        year.setText(null);
        month.setText(null);
        day.setText(null);
        bodyfat.setText(null);
        bodyweight.setText(null); */

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void cancel() {
        year.setText(null);
        month.setText(null);
        day.setText(null);
        bodyfat.setText(null);
        bodyweight.setText(null);
    }
    private boolean presenceCheck(EditText eText) {
        if(eText.getText().toString().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    private boolean validate(int inputBodyfat, float inputBodyweight, String inputDay, String inputMonth, String inputYear){
        int day = Integer.valueOf(inputDay);
        int month = Integer.valueOf(inputMonth);
        int year = Integer.valueOf(inputYear);

        boolean valid = true;

        if (inputBodyfat < 3 || inputBodyfat > 45){
            valid = false;
        }
        else if (inputBodyweight < 40 || inputBodyweight > 140) {
            valid = false;
        }
        else if (day < 0 || day > 31 || month < 0 || month > 12) {
            valid = false;
        }
        else if ( day == 31 && (month == 2 || month == 4 || month == 6 || month == 9 || month == 11)) {
            valid = false;
        }
        else if ( day > 29 && month == 2) {
            valid = false;
        }
        else if (year < 2019 ) {
            valid = false;
        }
        return valid;
    }
}
