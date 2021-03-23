package com.gamecodeschool.myfitnessapp.databases;

import java.util.Date;

import androidx.room.*;

@Entity(tableName = "Completed_Workouts")
public class CompletedWorkouts {
    @PrimaryKey(autoGenerate = true)
    private int workoutId;

    @ColumnInfo(name = "Date")
    private String date;

    @ColumnInfo(name = "Total_Time")
    private String time;

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
