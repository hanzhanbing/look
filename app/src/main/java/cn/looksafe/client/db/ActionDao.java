package cn.looksafe.client.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Created by huyg on 2019-11-12.
 */
@Dao
public interface ActionDao {

    @Query("SELECT * FROM `action` where create_time < :endTime and create_time>:startTime")
    List<Action> getAll(long startTime, long endTime);


    @Query("SELECT * FROM `action` where create_time < :time and up_flg=0")
    List<Action> getAll(long time);


    @Query("SELECT * FROM `action`")
    List<Action> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Action... actions);

    @Update()
    void update(Action actions);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Action action);

    @Delete()
    void delete(Action... actions);

}
