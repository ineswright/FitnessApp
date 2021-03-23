package com.gamecodeschool.myfitnessapp.databases;

import java.util.List;

import androidx.room.*;

@Dao
public interface CompletedExercisesDao {
    @Query("SELECT * FROM Completed_Exercises")
    List<CompletedExercises> getAll();

    @Query("Select * FROM Completed_Exercises where workId like :id")
    CompletedExercises[] getByWorkId(int id);

    @Query("DELETE FROM completed_exercises WHERE workId like :id")
    void deleteById(int id);

    @Insert
    void insertAll(CompletedExercises... exercises);

    @Delete
    void delete(CompletedExercises exercises);

}
