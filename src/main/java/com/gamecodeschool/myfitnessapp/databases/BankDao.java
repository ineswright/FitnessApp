package com.gamecodeschool.myfitnessapp.databases;
import java.util.List;

import androidx.room.*;

@Dao
public interface BankDao {
    @Query("SELECT * FROM Exercise_Bank ORDER BY Exercise_Name")
    ExerciseBank[] getAll();

    @Query("Select * FROM Exercise_Bank ORDER BY exerciseId asc")
    ExerciseBank[] getExercises();

    @Query("SELECT * FROM Exercise_Bank where Exercise_Name LIKE  :name")
    ExerciseBank findByName(String name);

    @Insert
    void insertAll(ExerciseBank... bank);

    @Delete
    void delete(ExerciseBank bank);

}
