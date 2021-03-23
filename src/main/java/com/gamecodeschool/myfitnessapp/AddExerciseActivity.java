package com.gamecodeschool.myfitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.health.SystemHealthManager;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gamecodeschool.myfitnessapp.databases.AppDatabase;
import com.gamecodeschool.myfitnessapp.databases.CompletedExercises;
import com.gamecodeschool.myfitnessapp.databases.ExerciseBank;
import com.gamecodeschool.myfitnessapp.databases.ProgressUpdates;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AddExerciseActivity extends AppCompatActivity {

    private AppDatabase db;
    private Button done;
    private Button add;
    private Button[] nameButtons;
    private LinearLayout layout;
    private EditText[] inputTextBoxes = new EditText[6];
    private TextView nameText;
    private ExerciseBank[] exercises;
    private ConstraintLayout pageLayout;
    boolean[] inputs = {false,false,false,false,false};
    String[] types = {"Sets","Reps","Weight","Time","Distance"};
    List<CompletedExercises> completedEx = new ArrayList<CompletedExercises>();
    private TextView errorMsg;

    private View.OnClickListener doneListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            {   done();
            }
        }
    };

    private View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            {   add();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        done = findViewById(R.id.doneButton);
        done.setOnClickListener(doneListener);
        add = findViewById(R.id.addExerciseButton);
        add.setOnClickListener(addListener);
        layout = findViewById(R.id.ExerciseButtonLayout);
        pageLayout = findViewById(R.id.pageLayout);
        nameText = findViewById(R.id.exName);
        errorMsg = findViewById(R.id.exerciseErrorMsg);

        db = AppDatabase.getAppDatabase(getApplicationContext());

        exercises = getExercises();
        String[] names = getNames(exercises);
        displayExercises(names);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        CompletedExercises[] data = (CompletedExercises[]) bundle.getSerializable("CompletedExercises");
        for (int i = 0; i < data.length; i++ ) {
            completedEx.add(data[i]);
        }
    }

    private ExerciseBank[] getExercises() {
        Future data = Executors.newSingleThreadExecutor().submit(new Callable() {
                                                                     @Override
                                                                     public ExerciseBank[] call(){
                                                                         return db.bankDao().getAll();
                                                                     }
                                                                 }
        );
        //REMEMBER: retrieves exercises in alphabetical order
        ExerciseBank[] exercises;
        try { exercises = (ExerciseBank[]) data.get();
        } catch (Exception ex){
            exercises = new ExerciseBank[1];
            exercises[0] = new ExerciseBank("-1",false,"-1",false,false,false,false,false);
        }
        return exercises;
    }

    private String[] getNames (ExerciseBank[] exercises) {
        int size = exercises.length;
        String[] names = new String[size];

        for (int i = 0; i < size; i++) {
            names[i] = exercises[i].getName();
        }
        return names;
    }

    private void displayExercises(String[] names) {
        nameButtons = new Button[names.length];
        for(int i = 0; i < names.length; i++){
            final Button btn = new Button(this);
            btn.setId(i);
            btn.setText(names[i]);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    final String name = (String) btn.getText();
                    exerciseSelected(name);
                }
            });
            //set layout parameters for buttons- so they are right height, width, colour, etc
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(50,-10,0,-10);
            btn.setLayoutParams(params);
            btn.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            btn.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);

            btn.setStateListAnimator(null);
            //can try addView(btn, params) if isn't working
            layout.addView(btn);
            nameButtons[i] = btn;
        }
    }

    private void done () {
        //converts list to array
        CompletedExercises[] ex = new CompletedExercises[completedEx.size()];
        for (int i = 0; i < completedEx.size();i++) {
            ex[i] = completedEx.get(i);
        }

        //sends data back to newWorkout activity
        Intent intent = new Intent(this, NewWorkoutActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("CompletedExercises",ex);
        intent.putExtras(extras);
        startActivity(intent);
        overridePendingTransition(0, 0);

    }

    private void add () {
        CompletedExercises ex = new CompletedExercises();

        //get exerciseID of which exercise has been chosen
        int exerciseID = -1;
        for (int j = 0; j < exercises.length; j++ ) {
            if (exercises[j].getName() == nameText.getText()) {
                exerciseID = exercises[j].getExerciseId();
                break;
            }       }
        ex.setExId(exerciseID);

        for(int i = 0; i< 5; i++) {
            //if an input box exists
            if (inputTextBoxes[i] != null) {
                //get input data from box
                float input = validate(inputTextBoxes[i], types[i]);

                //if invalid input
                if ((int)input == -1) {
                    return;
                }
                switch (i) {
                    case 0:
                        ex.setSets((int) input);
                        break;
                    case 1:
                        ex.setReps((int) input);
                        break;
                    case 2:
                        ex.setWeight(input);
                        break;
                    case 3:
                        ex.setTime((String.valueOf(input)));
                        break;
                    case 4:
                        ex.setDistance(input);
                        break;
                }
                inputTextBoxes[i].setText(null);
            }
        }
        nameText.setText("Choose another exercise");
        completedEx.add(ex);
    }

    private void exerciseSelected(String name) {
        //remove previous data
        for(int i = 0; i<5;i++) {
            if (inputTextBoxes != null) {
                pageLayout.removeView(inputTextBoxes[i]);
                inputTextBoxes[i] = null; } }

        //display name of chosen exercise
        nameText.setText(name);

        //supply obviously fake data in event of error
        ExerciseBank exercise = new ExerciseBank("-1",false,"-1",false,false,false,false,false);

        //gets exercise details of the selected exercise
        for(int i = 0; i < exercises.length; i++){
            if (exercises[i].getName().equals(name)) {
                exercise = exercises[i];
                break;
            } }

        //identify which input boxes have to be created
        inputs[0] = exercise.isSets();
        inputs[1] = exercise.isReps();
        inputs[2] = exercise.isWeight();
        inputs[3] = exercise.isTime();
        inputs[4] = exercise.isDistance();

        //create an input box for each type of data which needs to be entered
        for (int i = 0; i < 5; i++){
            if (inputs[i]){
                EditText inputBox = new EditText(this);
                inputBox.setId(i);
                if (types[i].equals("Weight")) {
                    inputBox.setHint(types[i] + " (kg)");
                }
                else if (types[i].equals("Time")) {
                    inputBox.setHint(types[i] + " (min)");
                }
                else if (types[i].equals("Distance")) {
                    inputBox.setHint(types[i] + " (km)");
                }
                else {inputBox.setHint(types[i]);}
                pageLayout.addView(inputBox);

                //gets the number of displayed input boxes
                int count = 0;
                for (int j = 0; j < i; j++) {
                    if (inputTextBoxes[j] != null) {  count++;  }
                }
                //define location of input boxes
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(pageLayout);
                constraintSet.connect(inputBox.getId(),ConstraintSet.TOP,R.id.exName,ConstraintSet.BOTTOM, 10);
                constraintSet.connect(inputBox.getId(),ConstraintSet.LEFT,pageLayout.getId(),ConstraintSet.LEFT,(200*(count)+50));
                constraintSet.applyTo(pageLayout);

                inputTextBoxes[i] = inputBox;
            }
        }
    }

    private float validate(EditText box, String type) {
        //conduct presence check
        if (box.getText().toString().isEmpty()) {
            errorMsg.setText("Please enter a value in every box.");
            return -1;
        }

        for (int i = 0; i < box.getText().toString().length(); i++) {
            if (!Character.isDigit(box.getText().toString().charAt(i)) && box.getText().toString().charAt(i) != '.') {
                errorMsg.setText("Please enter an integer in sets, reps or time.");
                errorMsg.setVisibility(View.VISIBLE);
                return -1;
            }
        }
        float data = Float.valueOf(box.getText().toString());

        //check input is integer and do range check
        if (type.equals("Sets") || type.equals("Reps")){
            if (data != (int) data) {
                errorMsg.setText("Please enter an integer in sets, reps or time.");
                errorMsg.setVisibility(View.VISIBLE);
                return -1;
            }
            else if (data < 0 || (data > 30 && type.equals("Sets")) || (data > 50 && type.equals("Reps"))) {
                errorMsg.setText("Please check the value in sets and reps.");
                errorMsg.setVisibility(View.VISIBLE);
                return -1;
            }
        }
        //do range check on everything else
        else if (data < 0 || (data > 250 && type.equals("Weight")) || (data > 50 && type.equals("Distance"))) {
                errorMsg.setText("Please check the value in weight or distance.");
                errorMsg.setVisibility(View.VISIBLE);
                return -1;
            }
        if (type.equals("Time")) {
            if (data > 350) {
                errorMsg.setText("Please enter time in minutes.");
                errorMsg.setVisibility(View.VISIBLE);
                return -1;
            }
        }
        errorMsg.setVisibility(View.INVISIBLE);
        return data;
    }
}
