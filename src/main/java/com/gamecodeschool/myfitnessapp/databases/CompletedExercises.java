package com.gamecodeschool.myfitnessapp.databases;
import java.io.Serializable;

import androidx.room.*;

@Entity(tableName = "Completed_Exercises")
public class CompletedExercises implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int exerciseId;

    @ColumnInfo(name = "exId")
    private int exId;

    @ColumnInfo(name = "workId")
    private int workId;

    @ColumnInfo(name = "Sets")
    private int sets;

    @ColumnInfo(name = "Reps")
    private int reps;

    @ColumnInfo(name = "Weight")
    private float weight;

    @ColumnInfo(name = "Distance")
    private float distance;

    @ColumnInfo(name = "Time")
    private String time;

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getExId() {
        return exId;
    }

    public void setExId(int exId) {
        this.exId = exId;
    }

    public int getWorkId() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }

    public Integer getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
