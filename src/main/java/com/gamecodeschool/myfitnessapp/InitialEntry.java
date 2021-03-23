package com.gamecodeschool.myfitnessapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gamecodeschool.myfitnessapp.databases.AppDatabase;
import com.gamecodeschool.myfitnessapp.databases.ExerciseBank;
import com.gamecodeschool.myfitnessapp.databases.ProgressUpdates;
import com.gamecodeschool.myfitnessapp.databases.Users;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

public class InitialEntry extends AppCompatActivity {

    private Button saveButton;
    private AppDatabase db;
    private EditText inputName;
    private EditText inputGender;
    private EditText inputHeight;
    private EditText inputDay;
    private EditText inputMonth;
    private EditText inputYear;
    private EditText goalWeight;
    private EditText goalBodyfat;
    private EditText goalWorkouts;
    private EditText currentWeight;
    private EditText currentBodyfat;
    private TextView entryError;

    private View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            { addtoDb(db);
                }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_entry);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        saveButton = findViewById(R.id.initialSaveButton);
        saveButton.setOnClickListener(saveListener);

        //db = Room.databaseBuilder(getApplicationContext(),
          //      AppDatabase.class, "database-name").build();
        db = AppDatabase.getAppDatabase(getApplicationContext());


        inputName = findViewById(R.id.inputName);
        inputGender = findViewById(R.id.inputGender);
        inputHeight = findViewById(R.id.inputHeight);
        inputDay = findViewById(R.id.inputDay);
        inputMonth = findViewById(R.id.inputMonth);
        inputYear = findViewById(R.id.inputYear);
        goalWeight = findViewById(R.id.inputGoalWeight);
        goalBodyfat = findViewById(R.id.inputGoalBodyfat);
        goalWorkouts = findViewById(R.id.inputGoalWorkouts);
        currentWeight = findViewById(R.id.inputCurrentWeight);
        currentBodyfat = findViewById(R.id.inputCurrentBodyfat);
        entryError = findViewById(R.id.entryError);

    }

    private void goHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void addtoDb(AppDatabase db) {
        ProgressUpdates update = new ProgressUpdates();
        Users user = new Users();

        //check user has entered a value in every field
        if (!presenceCheck(inputName) || !presenceCheck(inputGender) || !presenceCheck(inputHeight) || !presenceCheck(inputDay) || !presenceCheck(inputMonth)|| !presenceCheck(inputYear) || !presenceCheck(goalWeight) || !presenceCheck(goalBodyfat)|| !presenceCheck(goalWorkouts) || !presenceCheck(currentWeight) || !presenceCheck(currentBodyfat)) {
            //if a field has been left blank
            entryError.setText("Please enter a value for every attribute");
            entryError.setVisibility(View.VISIBLE);
            return;
        }

        //collects all the data
        String name = inputName.getText().toString();
        String initialgender = inputGender.getText().toString();
        char gender = initialgender.charAt(0);
        int height = Integer.valueOf(inputHeight.getText().toString());
        int day = Integer.valueOf(inputDay.getText().toString());
        int month = Integer.valueOf(inputMonth.getText().toString());
        int year = Integer.valueOf(inputYear.getText().toString());
        float weight = Float.valueOf(goalWeight.getText().toString());
        int bodyfat = Integer.valueOf(goalBodyfat.getText().toString());
        int workouts = Integer.valueOf(goalWorkouts.getText().toString());
        String DOB = day + "/" + month + "/" + year;
        float currentWeightValue = Float.valueOf(currentWeight.getText().toString());
        int currentBodyfatValue = Integer.valueOf(currentBodyfat.getText().toString());

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = df.format(c);

        //check numerical values are reasonable
        if (!rangeCheck(height, weight, bodyfat, workouts, currentWeightValue, currentBodyfatValue, name)) {
            entryError.setText("One of your values seems wrong, please change it");
            entryError.setVisibility(View.VISIBLE);
            return;
        }
        if (!otherChecks(gender, day, month, year)) {
            entryError.setText("Please make sure your DOB is correct and gender is entered as m/f");
            entryError.setVisibility(View.VISIBLE);
            return;
        }

        //inserts data into object to be entered into db
        user.setName(name);
        user.setGender(gender);
        user.setHeight(height);
        user.setDOB(DOB);
        user.setGoalWeight(weight);
        user.setGoalBodyfat(bodyfat);
        user.setGoalNumWorkouts(workouts);

        update.setDate(currentDate);
        update.setWeight(currentWeightValue);
        update.setBodyfat(currentBodyfatValue);

        addUpdate(db, update, user);
        goHome();
    }

    private void addUpdate(final AppDatabase db, final ProgressUpdates update, final Users user) {
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                db.progressDao().insertAll(update);
                db.userDao().insertAll(user);
                db.bankDao().insertAll(ExerciseBank.populateData());
            }        });
        System.out.println("Prepopulated");

        //declare that it's no longer "firstrun" so this activity will never be called again
        SharedPreferences myPref = this.getSharedPreferences(
                "prefs", 0);
        myPref.edit().putBoolean("firstRun", false).commit();
    }

    private boolean presenceCheck(EditText eText) {
        if(eText.getText().toString().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean rangeCheck(int height, float weight, int bodyfat, int workouts, float currentWeight, int currentFat, String name) {
        if (height < 70 || height > 250) {
            return false;
        }
        else if (weight < 45 || weight > 250 || currentWeight < 45 || currentWeight > 250) {
            return false;
        }
        else if (bodyfat > 50 || bodyfat < 2 || currentFat > 50 || currentFat < 2) {
            return false;
        }
        else if (workouts > 7 || workouts < 1) {
            return false;
        }

        for (int i = 0; i < name.length(); i++) {
            if (!Character.isLetter(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean otherChecks(Character gender, int day, int month, int year) {
        if (!gender.equals('F') && !gender.equals('M') && !gender.equals('f') && !gender.equals('m')) {
            return false;
        }
        if ( day < 1 || day > 31 || month < 1 || month > 12 || year > 2010 || year < 1920) {
            return false;
        }
        if (day > 30 && (month == 2 || month == 4 || month == 6 || month == 9 || month == 11)) {
            return false;
        }
        if (day > 29 && month == 2) {
            return false;
        }
        else {return true;}
    }

}
