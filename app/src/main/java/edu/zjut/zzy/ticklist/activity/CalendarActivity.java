package edu.zjut.zzy.ticklist.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import edu.zjut.zzy.ticklist.R;
import edu.zjut.zzy.ticklist.android.CalendarManager;
import edu.zjut.zzy.ticklist.bean.CalendarAccount;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;


public class CalendarActivity extends AppCompatActivity {
    private static final String TAG = CalendarActivity.class.getSimpleName();
//    public static final String[] EVENT_PROJECTION = new String[] {
//            CalendarContract.Calendars._ID,                           // 0
//            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
//            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
//            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
//    };
//
//    // The indices for the projection array above.
//    private static final int PROJECTION_ID_INDEX = 0;
//    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
//    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
//    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "-----onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        cur = cr.query(uri, new String[]{CalendarContract.Calendars.ACCOUNT_NAME}, null, null, null);
        Log.d(TAG, String.valueOf(cur.getCount()));
        CalendarAccount calendarAccount = CalendarManager.searchAccount(getApplicationContext());
        if (calendarAccount != null){
            Log.d(TAG, calendarAccount.toString());
        }


        //插入事件
//        CalendarManager.insertEvent(getApplicationContext(), 1);
        CalendarManager.updateEvent(getApplicationContext(), 2);



    }
}