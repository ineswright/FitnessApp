package com.gamecodeschool.myfitnessapp.databases;
import java.util.ArrayList;
import java.util.List;

import androidx.room.*;

@Dao
public interface ProgressDao {
    @Query("SELECT * FROM Progress_Updates")
    List<ProgressUpdates> getAll();

    @Query("SELECT * FROM Progress_Updates order by updateId desc limit 1")
    ProgressUpdates getTop();

    @Query("SELECT * FROM Progress_Updates order by updateId asc limit 1")
    ProgressUpdates getBottom();

    @Insert
    void insertAll(ProgressUpdates updates);

    @Query ("Delete from progress_updates")
    void deleteAll();

    @Delete
    void delete(ProgressUpdates update);

}
