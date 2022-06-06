package edu.zjut.zzy.ticklist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.time.format.DateTimeFormatter;

import edu.zjut.zzy.ticklist.bean.ToDo;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "KiLinList.db";


    private static final String INIT_SQLITE1 = "create table todo\n" +
            "(\n" +
            "    kilin_id    integer,\n" +
            "    repeated_id integer,\n" +
            "    content     text,\n" +
            "    description text,\n" +
            "    date        text,\n" +
            "    repeatedWay integer,\n" +
            "    time        text,\n" +
            "    timermode   integer,\n" +
            "    targettime  integer,\n" +
            "    finishtime  integer,\n" +
            "    isfinish    integer,\n" +
            "    focustime   integer,\n" +
            "    event_id    integer\n" +
            ");";

    private static final  String INIT_SQLITE2 = "create table focus\n" +
            "(\n" +
            "    kilin_user_id int  not null,\n" +
            "    group_id      int  not null,\n" +
            "    content       text,\n" +
            "    description   text,\n" +
            "    date          text,\n" +
            "    focustime     int,\n" +
            "    finishtime    text,\n" +
            "    isupload      int\n" +
            ");";

    private static final String INIT_SQLITE3 = "create table rankinfo(\n" +
            "    rank_number int not null,\n" +
            "    user_name   text null,\n" +
            "    total_focustime int null,\n" +
            "    image_url   text null,\n" +
            "    email       text not null\n" +
            ");";
    public DBOpenHelper(Context context){
        super(context, DB_NAME, null, 1);
    }

    //数据库创建
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(INIT_SQLITE1);
        sqLiteDatabase.execSQL(INIT_SQLITE2);
        sqLiteDatabase.execSQL(INIT_SQLITE3);
        //插入初始数据
        ToDo todo = new ToDo(0, "未定义待办事项", null, null, 0, 0, null, -1, 25, 0, false, 25);
        ContentValues values = new ContentValues();
        //将todo属性注入到values中
        values.put("kilin_id", todo.getKiLinId());
        values.put("repeated_id", todo.getGroupId());
        values.put("content", todo.getContent());
        values.put("description", todo.getDescription());
        if(todo.getDate() != null){
            values.put("date", todo.getDate().toString());
        }
        values.put("repeatedWay", todo.getRepeatedWay());
        if(todo.getTime() != null){
            values.put("time", todo.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        }
        values.put("timermode", todo.getTimerMode());
        values.put("targettime", todo.getTargetTime());
        values.put("finishtime", todo.getFinishTime());
        values.put("isfinish", todo.isFinish());
        values.put("focustime", todo.getFocusTime());
        sqLiteDatabase.insert("todo", null, values);
    }

    //数据库更新
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
