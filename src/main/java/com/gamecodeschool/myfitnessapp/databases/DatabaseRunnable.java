package com.gamecodeschool.myfitnessapp.databases;

public class DatabaseRunnable implements Runnable {

    private AppDatabase db;
    private Users user;
    private ProgressUpdates update;
    private ExerciseBank bank;
    private CompletedWorkouts workouts;
    private CompletedExercises exercises;

    public DatabaseRunnable(AppDatabase db, Users user) {
        this.db = db;
        this.user = user;
    }

    public void changeUser(Users newUser) {
        this.user = newUser;
    }

    @Override
    public void run() {
        db.userDao().insertAll(user);


    }
}
