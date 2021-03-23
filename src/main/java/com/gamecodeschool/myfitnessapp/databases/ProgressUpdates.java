package com.gamecodeschool.myfitnessapp.databases;
import androidx.room.*;

@Entity(tableName = "Progress_Updates")
public class ProgressUpdates {

    @PrimaryKey(autoGenerate = true)
    private int updateId;

    @ColumnInfo(name = "Weight")
    private float weight;

    @ColumnInfo(name = "Bodyfat")
    private int bodyfat;

    @ColumnInfo(name = "Date")
    private String date;

    public int getUpdateId() {
        return updateId;
    }

    public void setUpdateId(int updateId) {
        this.updateId = updateId;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getBodyfat() {
        return bodyfat;
    }

    public void setBodyfat(int bodyfat) {
        this.bodyfat = bodyfat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    /*
    public static ProgressUpdates populateData() {
        ProgressUpdates update = new ProgressUpdates();
        update.setDate("-1");
        update.setWeight(-1);
        update.setBodyfat(-1);
        return update;
    }*/
}
