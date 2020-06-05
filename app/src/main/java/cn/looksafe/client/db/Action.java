package cn.looksafe.client.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by huyg on 2019-11-12.
 */
@Entity(tableName = "action")
public class Action {

    @ColumnInfo(name = "video_id")
    public int videoId;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "video_name")
    public String videoName;

    @ColumnInfo(name = "play_time")
    public long playTime;

    @ColumnInfo(name = "create_time")
    public long createTime;

    public Action(int videoId, @NonNull String videoName, long playTime, long createTime) {
        this.videoId = videoId;
        this.videoName = videoName;
        this.playTime = playTime;
        this.createTime = createTime;
    }
}