package com.gamecodeschool.myfitnessapp.databases;
import androidx.room.*;

@Entity(tableName = "Exercise_Bank")
public class ExerciseBank {
    @PrimaryKey(autoGenerate = true)
    private int exerciseId;

    @ColumnInfo(name = "Exercise_Name")
    private String name;

    @ColumnInfo(name = "Strength")
    private boolean strength;

    @ColumnInfo(name = "Body area")
    private String body_area;

    @ColumnInfo(name = "Sets")
    private boolean sets;

    @ColumnInfo(name = "Reps")
    private boolean reps;

    @ColumnInfo(name = "Weight")
    private boolean weight;

    @ColumnInfo(name = "Distance")
    private boolean distance;

    @ColumnInfo(name = "Time")
    private boolean time;

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStrength() {
        return strength;
    }

    public void setStrength(boolean strength) {
        this.strength = strength;
    }

    public String getBody_area() {
        return body_area;
    }

    public void setBody_area(String body_area) {
        this.body_area = body_area;
    }

    public boolean isSets() {
        return sets;
    }

    public void setSets(boolean sets) {
        this.sets = sets;
    }

    public boolean isReps() {
        return reps;
    }

    public void setReps(boolean reps) {
        this.reps = reps;
    }

    public boolean isWeight() {
        return weight;
    }

    public void setWeight(boolean weight) {
        this.weight = weight;
    }

    public boolean isDistance() {
        return distance;
    }

    public void setDistance(boolean distance) {
        this.distance = distance;
    }

    public boolean isTime() {
        return time;
    }

    public void setTime(boolean time) {
        this.time = time;
    }

    public ExerciseBank(String name, boolean strength, String body_area, boolean sets, boolean reps,
                        boolean weight, boolean time, boolean distance) {
        this.name = name;
        this.strength = strength;
        this.body_area = body_area;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.time = time;
        this.distance = distance;
    }

    public static ExerciseBank[] populateData() {
        return new ExerciseBank[] {
                //Sample exercise data to pre-populate database with on create
                new ExerciseBank("Pull-up",true, "back, arms",true, true, true, false, false),
                new ExerciseBank("Bicep curl", true, "bicep, arms", true, true, true, false, false),
                new ExerciseBank("Sit-up", true, "core",true, true,false,false,false),
                new ExerciseBank("Squat",true,"legs, glutes",true,true,true,false,false),
                new ExerciseBank("Run", false, "legs",false,false,false,true,true),
                new ExerciseBank("Barbell Squat", true, "legs, glutes",true,true,true,false,false),
                new ExerciseBank("Lunges", true,"legs",true,true,false,false,false),
                new ExerciseBank("Mountain Climbers",true,"legs",true,true,false,false,false),
                new ExerciseBank("Plank",true,"core, arms, legs", false,false,false,true,false),
                new ExerciseBank("Crunches",true, "core",true,true,false,false,false),
                new ExerciseBank("Cycle",false,"legs", false, false, false, true, true),
                new ExerciseBank("Swim", false,"core, legs, arms", false, false, false, true, true),
                new ExerciseBank("Bench Press", true,"chest, arms",true, true, true, false, false),
                new ExerciseBank("Push-Up", true,"arms",true,true,false,false,false),
                new ExerciseBank("Barbell Deadlift", true,"arms, shoulders, core",true, true, true, false, false),
                new ExerciseBank("Dumbbell Row",true, "arms",true, true, true, false, false),
                new ExerciseBank("Machine Pull-Down",true,"arms",true,true,true,false,false),
                new ExerciseBank("Chin-Up", true,"arms, shoulders",true,true,false,false,false),
                new ExerciseBank("Leg Press",true, "legs", true, true, true, false, false),
                new ExerciseBank("Dumbbell Step-Ups", true,"legs, arms", true, true, true, false, false)
        };
    }

}
