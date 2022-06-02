package edu.zjut.zzy.ticklist.bean;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;

import androidx.annotation.RequiresApi;

import java.util.Calendar;

import edu.zjut.zzy.ticklist.bean.CalendarAccount;
import edu.zjut.zzy.ticklist.bean.ToDo;

public class CalendarManager {
    private static final  String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.ACCOUNT_NAME,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.OWNER_ACCOUNT
    };

    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void createCalendar(Context context){
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Calendars.NAME, "yiyi");
        values.put(CalendarContract.Calendars.ACCOUNT_NAME, "1138154255@qq.com");
        values.put(CalendarContract.Calendars.ACCOUNT_TYPE, "com.android.exchange");
        values.put(CalendarContract.Calendars.VISIBLE, 1);
        values.put(CalendarContract.Calendars.CALENDAR_COLOR, -9206951);
        values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        values.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        values.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        values.put(CalendarContract.Calendars.OWNER_ACCOUNT, "1138154255@qq.com");
        values.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);
        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "KiLin");

        Uri calendarUri = CalendarContract.Calendars.CONTENT_URI;
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "1138154255@qq.com")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, "com.android.exchange")
                .build();
        context.getContentResolver().insert(calendarUri, values);
    }

    public static CalendarAccount searchAccount(Context context){
        CalendarAccount calendarAccount = null;
        Cursor cursor = null;
        ContentResolver cr = context.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";

        String[] selectionArgs = new String[]{
                "1138154255@qq.com", "com.android.exchange", "1138154255@qq.com"
        };

        cursor = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

        while (cursor.moveToNext()){
            calendarAccount = new CalendarAccount(cursor.getLong(PROJECTION_ID_INDEX), cursor.getString(PROJECTION_DISPLAY_NAME_INDEX),
                    cursor.getString(PROJECTION_ACCOUNT_NAME_INDEX), cursor.getString(PROJECTION_OWNER_ACCOUNT_INDEX));
        }
        return calendarAccount;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static long insertEvents(Context context, long calID){
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2022, 5, 1, 22, 0);
        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2022, 5, 1, 23, 30);
        long endMillis = endTime.getTimeInMillis();

        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, "日历开发");
        values.put(CalendarContract.Events.DESCRIPTION, "增加日历的增删改功能");
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        long eventID = Long.parseLong(uri.getLastPathSegment());
        return eventID;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long insertEvents(Context context, long calID, ToDo toDo){
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(toDo.getDate().getYear(), toDo.getDate().getMonthValue() - 1, toDo.getDate().getDayOfMonth(), toDo.getTime().getHour(), toDo.getTime().getMinute());
        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(toDo.getDate().getYear(), toDo.getDate().getMonthValue() - 1, toDo.getDate().getDayOfMonth(), toDo.getTime().getHour(), toDo.getTime().getMinute() + 5);
        long endMillis = endTime.getTimeInMillis();

        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, toDo.getContent());
        values.put(CalendarContract.Events.DESCRIPTION, toDo.getDescription());
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        long eventID = Long.parseLong(uri.getLastPathSegment());
        return eventID;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void updateEvents(Context context, ToDo toDo){
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(toDo.getDate().getYear(), toDo.getDate().getMonthValue() - 1, toDo.getDate().getDayOfMonth(), toDo.getTime().getHour(), toDo.getTime().getMinute());
        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(toDo.getDate().getYear(), toDo.getDate().getMonthValue() - 1, toDo.getDate().getDayOfMonth(), toDo.getTime().getHour(), toDo.getTime().getMinute() + 5);
        long endMillis = endTime.getTimeInMillis();
        Uri updateUri = null;
        values.put(CalendarContract.Events.TITLE, toDo.getContent());
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.DESCRIPTION, toDo.getDescription());
        updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, toDo.getEventID());
        int rows = cr.update(updateUri, values, null, null);
        System.out.println("EventsRows: " + rows);
    }

    public static void updateEvent(Context context, long eventID){
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        Uri updateUri = null;

        values.put(CalendarContract.Events.TITLE, "日历修改测试");
        updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        int rows = cr.update(updateUri, values, null, null);
        System.out.println("EventsRows: " + rows);
    }

    public static void deleteEvent(Context context, long eventID){
        ContentResolver cr = context.getContentResolver();
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        int rows = cr.delete(deleteUri, null, null);
        System.out.println("EventsRows: " + rows);
    }


}
