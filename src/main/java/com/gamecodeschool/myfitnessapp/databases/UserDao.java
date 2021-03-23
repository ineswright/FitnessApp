package com.gamecodeschool.myfitnessapp.databases;

import java.util.List;

import androidx.room.*;

@Dao
public interface UserDao {
    @Query("SELECT * FROM Users")
    List<Users> getAll();

    @Query("SELECT * FROM Users limit 1")
    Users getTop();

    @Insert
    void insertAll(Users... users);

    @Delete
    void delete(Users user);

    @Query ("Update Users set Goal_weight = :goalWeight, Goal_bodyfat = :goalBodyfat, Goal_Workouts_Per_Week = :workouts")
    void update(float goalWeight, float goalBodyfat, int workouts);

}
