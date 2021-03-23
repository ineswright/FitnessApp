package com.gamecodeschool.myfitnessapp.databases;
import androidx.room.*;

@Entity(tableName = "Users")
public class Users {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "Name")
    private String name;

    @ColumnInfo(name = "Gender")
    private char gender;

    @ColumnInfo(name = "Height")
    private float height;

    @ColumnInfo(name = "Date_of_birth")
    private String DOB;

    @ColumnInfo(name = "Goal_weight")
    private float goalWeight;

    @ColumnInfo(name = "Goal_bodyfat")
    private float goalBodyfat;

    @ColumnInfo(name = "Goal_Workouts_Per_Week")
    private int goalNumWorkouts;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public float getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(float goalWeight) {
        this.goalWeight = goalWeight;
    }

    public float getGoalBodyfat() {
        return goalBodyfat;
    }

    public void setGoalBodyfat(float goalBodyfat) {
        this.goalBodyfat = goalBodyfat;
    }

    public int getGoalNumWorkouts() {
        return goalNumWorkouts;
    }

    public void setGoalNumWorkouts(int goalNumWorkouts) {
        this.goalNumWorkouts = goalNumWorkouts;
    }


}
