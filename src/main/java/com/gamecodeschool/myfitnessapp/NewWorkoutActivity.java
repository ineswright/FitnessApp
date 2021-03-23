package com.gamecodeschool.myfitnessapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gamecodeschool.myfitnessapp.databases.AppDatabase;
import com.gamecodeschool.myfitnessapp.databases.CompletedExercises;
import com.gamecodeschool.myfitnessapp.databases.CompletedWorkouts;
import com.gamecodeschool.myfitnessapp.databases.ExerciseBank;
import com.gamecodeschool.myfitnessapp.databases.ProgressUpdates;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class NewWorkoutActivity extends BaseNavDrawer
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView text;
    private ImageButton add;
    private Button cancel;
    private Button save;
    private AppDatabase db;
    private EditText inputDay;
    private EditText inputMonth;
    private EditText inputYear;
    private EditText inputTime;
    private CompletedExercises[] data;
    private TextView errorMsg;
    private LinearLayout layout;
    private TextView[] inputEx;
    private Button[] delButtons;
    private String[] names;


    private View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            {   goToAdd();
            }
        }
    };

    private View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            {   cancel();
            }
        }
    };

    private View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            {   save();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);
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


        inputDay = findViewById(R.id.inputWorkoutDay);
        inputMonth = findViewById(R.id.inputWorkoutMonth);
        inputYear = findViewById(R.id.inputWorkoutYear);
        inputTime = findViewById(R.id.inputWorkoutTime);
        layout = findViewById(R.id.displayExercises);

        //get date and insert into input boxes
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(c);

        String currentDay = Character.toString(date.charAt(0)) + Character.toString(date.charAt(1));
        String currentMonth = Character.toString(date.charAt(3)) + Character.toString(date.charAt(4));
        String currentYear = Character.toString(date.charAt(6)) + Character.toString(date.charAt(7)) + Character.toString(date.charAt(8)) + Character.toString(date.charAt(9));

        inputDay.setText(currentDay);
        inputMonth.setText(currentMonth);
        inputYear.setText(currentYear);

        text = findViewById(R.id.addEx);
        errorMsg = findViewById(R.id.workoutErrorMsg);

        add = findViewById(R.id.addButton);
        add.setOnClickListener(addListener);
        cancel = findViewById(R.id.cancelButton);
        cancel.setOnClickListener(cancelListener);
        save = findViewById(R.id.initialSaveButton);
        save.setOnClickListener(saveListener);

        //gets data from addExerciseActivity
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        data = (CompletedExercises[]) bundle.getSerializable("CompletedExercises");
        if (data.length != 0) {
            displayData();
            text.setVisibility(View.INVISIBLE);
        }
        else {
            text.setVisibility(View.VISIBLE);
            text.setText("Add exercises by pressing the add button below");
        }

    }

    private ExerciseBank[] getExercises() {
        Future retrievedEx = Executors.newSingleThreadExecutor().submit(new Callable() {
                                                                     @Override
                                                                     public ExerciseBank[] call(){
                                                                         return db.bankDao().getExercises();
                                                                     }
                                                                 }
        );
        //REMEMBER: retrieves exercises in order of ID
        ExerciseBank[] exercises;
        try { exercises = (ExerciseBank[]) retrievedEx.get();
        } catch (Exception ex){
            exercises = new ExerciseBank[0];
        }
        return exercises;
    }

    private void displayData(){
        layout.removeAllViews();
        inputEx = new TextView[data.length];
        delButtons = new Button[data.length];

        ExerciseBank[] ex = getExercises();
        names = new String[ex.length];
        for (int i = 0; i < ex.length; i++) {
            names[i] = ex[i].getName();
        }

        for (int i = 0; i < data.length; i++) {
            final Button btn = new Button(this);
            btn.setId(i);
            btn.setText("Delete");
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    final int id = (int) btn.getId();
                    del(id);
                }
            });
            //format text to be displayed about each exercises
            String textData = names[data[i].getExId()-1];
            if (data[i].getSets() != 0) {
                textData = textData + "   Sets: " + String.valueOf(data[i].getSets());
            }
            if (data[i].getReps() != 0) {
                textData = textData + "   Reps: " + String.valueOf(data[i].getReps());
            }
            if (data[i].getWeight() != 0.0) {
                textData = textData + "   Weight: " + String.valueOf(data[i].getWeight());
            }
            if (data[i].getTime() != null) {
                textData = textData + "   Time: " + String.valueOf(data[i].getTime());
            }
            if (data[i].getDistance() != 0.0) {
                textData = textData + "   Distance: " + String.valueOf(data[i].getDistance());
            }

            final TextView text = new TextView(this);
            text.setText(textData);
            text.setTextColor(ContextCompat.getColor(this, R.color.black));
            text.setTextSize(15);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(50,15,0,-5);
            text.setLayoutParams(params);

            layout.addView(text);
            inputEx[i] = text;

            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            btn.setLayoutParams(btnParams);
            btn.setBackgroundColor(Color.parseColor("#00FFFFFF"));

            btn.setStateListAnimator(null);
            //can try addView(btn, params) if isn't working
            layout.addView(btn);
            delButtons[i] = btn;
        }


    }

    private void goToAdd() {
        Intent intent = new Intent(this, AddExerciseActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("CompletedExercises",data);
        intent.putExtras(extras);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void cancel() {
        Intent intent = new Intent(this, NewWorkoutActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("CompletedExercises",new CompletedExercises[0]);
        intent.putExtras(extras);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    //buttons ID corresponds to the data's index in data array
    private void del(int id) {
        int previousLength = data.length;
        int newLength = previousLength-1;
        CompletedExercises[] newData = new CompletedExercises[newLength];
        for (int j = 0; j < previousLength;j++) {
            if (j < id) {
                newData[j] = data[j];
            }
            else if (j > id) {
                newData[j-1] = data[j];
            }
        }

        //reset completed exercises data
        data = new CompletedExercises[newLength];
        data = newData;
        if (newLength > 0) {
        displayData();
        }
        else { layout.removeAllViews();
        text.setVisibility(View.VISIBLE);}
    }

    private void save () {
        if (data.length == 0) {
            //if no exercises have been sent, return
            text.setVisibility(View.VISIBLE);
            return;
        }
        String day = inputDay.getText().toString();
        String month = inputMonth.getText().toString();
        String year = inputYear.getText().toString();
        String newDate =  day + "/" + month + "/" + year;
        String time = inputTime.getText().toString();

        if (!presenceCheck(inputDay, inputMonth, inputYear, inputTime)) {
            errorMsg.setText("Please enter a value in every box.");
            errorMsg.setVisibility(View.VISIBLE);
            return;
        }
        if (!validate(Integer.valueOf(day), Integer.valueOf(month), Integer.valueOf(year), Integer.valueOf(time))) {
            errorMsg.setText("Please enter a valid date.");
            errorMsg.setVisibility(View.VISIBLE);
            return;
        }

        //todo logic error- if last workout input was deleted, exercises are stored w wrong workoutID- WRITEUP
        final CompletedWorkouts workout = new CompletedWorkouts();
        workout.setDate(newDate);
        workout.setTime(time);
        //gets last workout id
        Future retrievedID = Executors.newSingleThreadExecutor().submit(new Callable() {
                                                                     @Override
                                                                     public Integer call(){
                                                                         db.workoutsDao().insertAll(workout);
                                                                         return db.workoutsDao().getTop();
                                                                     }
                                                                 } );

        int id;
        try { id = (int) retrievedID.get();
        } catch (Exception ex){
            //if no value yet in database, return -1
            id = 1;
        }

        for (int i = 0; i < data.length; i++) {
            data[i].setWorkId(id);
        }
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                db.exercisesDao().insertAll(data);
            }
        });
        cancel();

    }

    private boolean validate(int day, int month, int year, int time) {
        if ( day < 1 || day > 31 || month < 1 || month > 12 || year > 2030 || year < 1920) {
            return false;
        }
        if (day > 30 && (month == 2 || month == 4 || month == 6 || month == 9 || month == 11)) {
            return false;
        }
        if (day > 29 && month == 2) {
            return false;
        }
        if (time > 300) {
            return false;
        }
        else {return true;}
    }

    private boolean presenceCheck(EditText a, EditText b, EditText c, EditText d) {
        if (a.getText().toString().isEmpty() || b.getText().toString().isEmpty() || c.getText().toString().isEmpty() || d.getText().toString().isEmpty() ) {
            return false;
        }
        return true;
    }



}
