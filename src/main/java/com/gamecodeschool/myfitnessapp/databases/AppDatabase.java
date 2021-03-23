package com.gamecodeschool.myfitnessapp.databases;

import android.content.Context;

import com.gamecodeschool.myfitnessapp.HomeActivity;

import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.*;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {Users.class, ProgressUpdates.class, ExerciseBank.class,CompletedWorkouts.class,
        CompletedExercises.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    //when you need to do database migration? uninstall from emulator and reinstall instead.

    //create variables to manage DAO's
    private static AppDatabase INSTANCE;
    public abstract UserDao userDao();
    public abstract ProgressDao progressDao();
    public abstract BankDao bankDao();
    public abstract CompletedWorkoutsDao workoutsDao();
    public abstract CompletedExercisesDao exercisesDao();


    //constructor called when AppDatabase is created

    public static AppDatabase getAppDatabase (Context context) {
        //if database doesn't already exist, create it
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    //create database
                    //callback inserts the data needed into the database on creation
                    //this is run through an executor so that it doesn't execute on the main thread
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "user-database")
                                    .addCallback(new Callback() {
                                        @Override
                                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                            super.onCreate(db);
                                            Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //INSTANCE.bankDao().insertAll(ExerciseBank.populateData());
                                                    //System.out.println("Db prepopulated");
                                                }
                                            }); }}).build();
                }}}
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
