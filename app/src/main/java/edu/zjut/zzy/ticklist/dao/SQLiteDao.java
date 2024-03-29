package edu.zjut.zzy.ticklist.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import edu.zjut.zzy.ticklist.bean.Focus;
import edu.zjut.zzy.ticklist.bean.RankInfo;
import edu.zjut.zzy.ticklist.bean.ToDo;

public class SQLiteDao {
    DBOpenHelper dbOpenHelper;

    public SQLiteDao(DBOpenHelper dbOpenHelper){
        this.dbOpenHelper = dbOpenHelper;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("Range")
    public ArrayList<ToDo> getToDoList(){
        ArrayList<ToDo> list = new ArrayList<>();
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String sql = "select *\n" +
                "from todo\n";
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            LocalDate date = null;
            LocalDateTime time = null;
            String dateStr = cursor.getString(cursor.getColumnIndex("date"));
            String timeStr = cursor.getString(cursor.getColumnIndex("time"));
            if(dateStr != null){
                date = LocalDate.parse(dateStr);
                if(timeStr != null){
                    time = LocalDateTime.parse(dateStr + "-" + timeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm"));
                }
            }
            list.add(new ToDo( cursor.getInt(cursor.getColumnIndex("kilin_id")), cursor.getString(cursor.getColumnIndex("content")),
                    cursor.getString(cursor.getColumnIndex("description")),  date,
                    cursor.getInt(cursor.getColumnIndex("repeatedWay")), cursor.getInt(cursor.getColumnIndex("repeated_id")), time,
                    cursor.getInt(cursor.getColumnIndex("timermode")), cursor.getInt(cursor.getColumnIndex("targettime")), cursor.getInt(cursor.getColumnIndex("finishtime")),
                    cursor.getInt(cursor.getColumnIndex("isfinish")) == 1, cursor.getInt(cursor.getColumnIndex("focustime"))));
        }
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("Range")
    public ArrayList<ToDo> getTODoListRange(LocalDate range){
        ArrayList<ToDo> list = new ArrayList<>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        String sql = "select *\n" +
                "from todo\n" +
                "where date = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{range.toString()});
        while(cursor.moveToNext()){
            LocalDate date = null;
            LocalDateTime time = null;
            String dateStr = cursor.getString(cursor.getColumnIndex("date"));
            String timeStr = cursor.getString(cursor.getColumnIndex("time"));
            if(dateStr != null){
                date = LocalDate.parse(dateStr);
                if(timeStr != null){
                    time = LocalDateTime.parse(dateStr + "-" + timeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm"));
                }
            }
            list.add(new ToDo( cursor.getInt(cursor.getColumnIndex("kilin_id")), cursor.getString(cursor.getColumnIndex("content")),
                    cursor.getString(cursor.getColumnIndex("description")),  date,
                    cursor.getInt(cursor.getColumnIndex("repeatedWay")),  cursor.getInt(cursor.getColumnIndex("repeated_id")), time,
                    cursor.getInt(cursor.getColumnIndex("timermode")), cursor.getInt(cursor.getColumnIndex("targettime")), cursor.getInt(cursor.getColumnIndex("finishtime")),
                    cursor.getInt(cursor.getColumnIndex("isfinish")) == 1, cursor.getInt(cursor.getColumnIndex("focustime"))));
        }
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("Range")
    public ArrayList<ToDo> getToDoMonth(int year, int month){
        ArrayList<ToDo> list = new ArrayList<>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        String sql = "select *\n" +
                "from todo\n" +
                "where date >= ? and date < ?\n" +
                "order by date asc;";
        LocalDate begin = LocalDate.of(year, month, 1);
        LocalDate end = LocalDate.of(year + month / 12, (month + 1) % 12, 1);
        Cursor cursor = db.rawQuery(sql, new String[]{begin.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))});
        while(cursor.moveToNext()){
            LocalDate date = null;
            LocalDateTime time = null;
            String dateStr = cursor.getString(cursor.getColumnIndex("date"));
            String timeStr = cursor.getString(cursor.getColumnIndex("time"));
            if(dateStr != null){
                date = LocalDate.parse(dateStr);
                if(timeStr != null){
                    time = LocalDateTime.parse(dateStr + "-" + timeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm"));
                }
            }
            list.add(new ToDo( cursor.getInt(cursor.getColumnIndex("kilin_id")), cursor.getString(cursor.getColumnIndex("content")),
                    cursor.getString(cursor.getColumnIndex("description")),  date,
                    cursor.getInt(cursor.getColumnIndex("repeatedWay")),  cursor.getInt(cursor.getColumnIndex("repeated_id")), time,
                    cursor.getInt(cursor.getColumnIndex("timermode")), cursor.getInt(cursor.getColumnIndex("targettime")), cursor.getInt(cursor.getColumnIndex("finishtime")),
                    cursor.getInt(cursor.getColumnIndex("isfinish")) == 1, cursor.getInt(cursor.getColumnIndex("focustime"))));
        }
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateToDo(ToDo todo){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
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
        db.update("todo", values, "kilin_id = ?", new String[]{String.valueOf(todo.getKiLinId())});
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("Range")
    public ToDo getTodo(int todoId){
        ToDo todo = null;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String sql = "select *\n" +
                "from todo\n" +
                "where kilin_id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(todoId)});
        while(cursor.moveToNext()){
            LocalDate date = null;
            LocalDateTime time = null;
            String dateStr = cursor.getString(cursor.getColumnIndex("date"));
            String timeStr = cursor.getString(cursor.getColumnIndex("time"));
            if(dateStr != null){
                date = LocalDate.parse(dateStr);
                if(timeStr != null){
                    time = LocalDateTime.parse(dateStr + "-" + timeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm"));
                }
            }
            todo = new ToDo( cursor.getInt(cursor.getColumnIndex("kilin_id")), cursor.getString(cursor.getColumnIndex("content")),
                    cursor.getString(cursor.getColumnIndex("description")),  date,
                    cursor.getInt(cursor.getColumnIndex("repeatedWay")), cursor.getInt(cursor.getColumnIndex("repeated_id")), time,
                    cursor.getInt(cursor.getColumnIndex("timermode")), cursor.getInt(cursor.getColumnIndex("targettime")), cursor.getInt(cursor.getColumnIndex("finishtime")),
                    cursor.getInt(cursor.getColumnIndex("isfinish")) == 1, cursor.getInt(cursor.getColumnIndex("focustime")));
        }
        return todo;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertToDo(ToDo todo){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
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
        values.put("event_id", todo.getEventID());
        db.insert("todo", null, values);
    }

    public int getMaxKinLinId(){
        int maxKinLinId = -1;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String sql = "select max(kilin_id) \"max_kilin_id\"\n" +
                "from todo;";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToNext()){
            maxKinLinId = cursor.getInt(0);
        }
        return maxKinLinId;
    }

    public int getMaxKinLinGroupId(){
        int maxKinLinId = -1;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String sql = "select max(repeated_id) \"max_kilin_group_id\"\n" +
                "from todo;";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToNext()){
            maxKinLinId = cursor.getInt(0);
        }
        return maxKinLinId;
    }

    public void insertFocus(Focus focus, String date, String time){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("kilin_user_id", focus.getKilinUserId());
        values.put("group_id", focus.getGroupId());
        values.put("content", focus.getContent());
        values.put("description", focus.getDescription());
        values.put("date", date);
        values.put("focustime", focus.getFocusTime());
        values.put("finishtime", time);
        values.put("isupload", 0);
        db.insert("focus", null, values);
    }

    public void deleteToDo(ToDo toDo){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("todo", "kilin_id = ?" , new String[]{String.valueOf(toDo.getKiLinId())});
    }

    @SuppressLint("Range")
    public ArrayList<RankInfo> getRankInfo(){
        ArrayList<RankInfo> list = new ArrayList<>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        String sql = "select *\n" +
                "from rankinfo\n" +
                "order by rank_number asc;";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()){
            list.add(new RankInfo(cursor.getInt(cursor.getColumnIndex("rank_number")), cursor.getString(cursor.getColumnIndex("user_name")), cursor.getInt(cursor.getColumnIndex("total_focustime")),
                    cursor.getString(cursor.getColumnIndex("image_url")), cursor.getString(cursor.getColumnIndex("email"))));
        }
        return list;
    }

    public void insertRankInfo(RankInfo rankInfo){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("rank_number", rankInfo.getRankNumber());
        values.put("user_name", rankInfo.getUserName());
        values.put("total_focustime", rankInfo.getTotalFocusTime());
        values.put("image_url", rankInfo.getImageUrl());
        values.put("email", rankInfo.getEmail());
        db.insert("rankinfo", null, values);
    }

    public void clearRankInfo(){
        String sql = "delete from rankinfo;";
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.execSQL(sql);
    }
}
