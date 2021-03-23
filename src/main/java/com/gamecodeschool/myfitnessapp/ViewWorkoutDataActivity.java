package com.gamecodeschool.myfitnessapp;

import android.content.Intent;
import android.os.Bundle;

import com.gamecodeschool.myfitnessapp.databases.AppDatabase;
import com.gamecodeschool.myfitnessapp.databases.CompletedExercises;
import com.gamecodeschool.myfitnessapp.databases.CompletedWorkouts;
import com.gamecodeschool.myfitnessapp.databases.ExerciseBank;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ViewWorkoutDataActivity extends AppCompatActivity {

    private AppDatabase db;
    private Button backButton;
    private TextView workoutDateText;
    private LinearLayout layout;
    private CompletedWorkouts workout;
    private CompletedExercises[] completedExercises;
    private ExerciseBank[] exercises;
    private TextView[] exerciseText;

    private View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            {   back(); }        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_workout_data);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = AppDatabase.getAppDatabase(getApplicationContext());
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(backListener);
        workoutDateText = findViewById(R.id.workoutDateText);
        layout = findViewById(R.id.workoutDataLayout);

        Intent intent = this.getIntent();
        int id = intent.getExtras().getInt("workoutID");
        workout = getWorkout(id);
        workoutDateText.setText("View workout on day: " + workout.getDate());
        completedExercises = getCompletedExercises(id);
        exercises = getExercises();

        displayCompletedExercises();
    }

    private CompletedWorkouts getWorkout(final int id){
        Future data = Executors.newSingleThreadExecutor().submit(new Callable() {
                                                                     @Override
                                                                     public CompletedWorkouts call(){
                                                                         return db.workoutsDao().findByID(id);
                                                                     }
                                                                 }
        );

        CompletedWorkouts workout;
        //returns workouts by ID, which are 0 indexed
        try { workout = (CompletedWorkouts) data.get();
        } catch (Exception ex){
            System.out.println("Workout not retrieved");
            workout = new CompletedWorkouts();
        }
        return workout;
    }

    private CompletedExercises[] getCompletedExercises(final int id) {
        Future data = Executors.newSingleThreadExecutor().submit(new Callable() {
            @Override
            public CompletedExercises[] call() {
                return db.exercisesDao().getByWorkId(id);
            }
        });

        CompletedExercises[] exercises;
        try {
            exercises = (CompletedExercises[]) data.get();
        } catch (Exception ex) {
            System.out.println("No exercises retrieved");
            exercises = new CompletedExercises[0];
        }
        return exercises;
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

    private void displayCompletedExercises() {
        exerciseText = new TextView[completedExercises.length];

        String[] names = new String[exercises.length];
        for (int i = 0; i < exercises.length; i++) {
            names[i] = exercises[i].getName();
        }

        for (int i = 0; i < completedExercises.length; i++) {
            final TextView text = new TextView(this);

            String textData = names[completedExercises[i].getExId()-1];
            if (completedExercises[i].getSets() != 0) {
                textData = textData + "   Sets: " + String.valueOf(completedExercises[i].getSets());
            }
            if (completedExercises[i].getReps() != 0) {
                textData = textData + "   Reps: " + String.valueOf(completedExercises[i].getReps());
            }
            if (completedExercises[i].getWeight() != 0.0) {
                textData = textData + "   Weight: " + String.valueOf(completedExercises[i].getWeight());
            }
            if (completedExercises[i].getTime() != null) {
                textData = textData + "   Time: " + String.valueOf(completedExercises[i].getTime());
            }
            if (completedExercises[i].getDistance() != 0.0) {
                textData = textData + "   Distance: " + String.valueOf(completedExercises[i].getDistance());
            }

            text.setText(textData);
            text.setTextColor(ContextCompat.getColor(this, R.color.black));
            text.setTextSize(18);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(50,15,0,15);
            text.setLayoutParams(params);

            layout.addView(text);
            exerciseText[i] = text;
        }
    }


    private void back() {
        Intent intent = new Intent(this, ViewWorkoutsActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

}
