package edu.zjut.zzy.ticklist.android;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;

import androidx.annotation.RequiresApi;

import edu.zjut.zzy.ticklist.bean.CalendarAccount;

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


}
