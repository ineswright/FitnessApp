package com.gamecodeschool.myfitnessapp.databases;

import java.util.List;

import androidx.room.*;

@Dao
public interface CompletedWorkoutsDao {
    @Query("SELECT * FROM Completed_Workouts")
    CompletedWorkouts[] getAll();

    @Query("SELECT * FROM Completed_Workouts where workoutId LIKE :id")
    CompletedWorkouts findByID(int id);

    @Query("SELECT workoutId FROM Completed_Workouts ORDER BY workoutId desc limit 1")
    Integer getTop();

    @Query("DELETE from Completed_Workouts where workoutId like :id")
    void deleteById(int id);

    @Insert
    void insertAll(CompletedWorkouts... workouts);

    @Delete
    void delete(CompletedWorkouts workouts);


}
