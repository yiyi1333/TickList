package edu.zjut.zzy.ticklist.android;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.icu.util.TimeZone;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventListener;

import edu.zjut.zzy.ticklist.bean.Course;
import edu.zjut.zzy.ticklist.bean.ToDo;
import edu.zjut.zzy.ticklist.dao.DBOpenHelper;
import edu.zjut.zzy.ticklist.dao.SQLiteDao;

public class AndroidState {

    public static boolean isNetworkConnected(Context context){
        if(context != null){
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null){
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    public static class CalendarManager {
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

        @RequiresApi(api = Build.VERSION_CODES.O)
        public static LocalTime courseStartTime(int courseNumber){
            LocalTime time = null;
            switch (courseNumber){
                case 1:
                    time = LocalTime.of(8, 0, 0);
                    break;
                case 2:
                    time = LocalTime.of(8, 55, 0);
                    break;
                case 3:
                    time = LocalTime.of(9, 55, 0);
                    break;
                case 4:
                    time = LocalTime.of(10, 50, 0);
                    break;
                case 5:
                    time = LocalTime.of(11, 50, 0);
                    break;
                case 6:
                    time = LocalTime.of(13, 30, 0);
                    break;
                case 7:
                    time = LocalTime.of(14, 25, 0);
                    break;
                case 8:
                    time = LocalTime.of(15, 25, 0);
                    break;
                case 9:
                    time = LocalTime.of(16, 20, 0);
                    break;
                case 10:
                    time = LocalTime.of(18, 30, 0);
                    break;
                case 11:
                    time = LocalTime.of(19, 25, 0);
                    break;
                case 12:
                    time = LocalTime.of(20, 25, 0);
                    break;

            }
            return time;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public static LocalTime courseEndTime(int courseNumber){
            LocalTime time = null;
            switch (courseNumber){
                case 1:
                    time = LocalTime.of(8, 45, 0);
                    break;
                case 2:
                    time = LocalTime.of(9, 40, 0);
                    break;
                case 3:
                    time = LocalTime.of(10, 40, 0);
                    break;
                case 4:
                    time = LocalTime.of(11, 35, 0);
                    break;
                case 5:
                    time = LocalTime.of(12, 35, 0);
                    break;
                case 6:
                    time = LocalTime.of(14, 15, 0);
                    break;
                case 7:
                    time = LocalTime.of(15, 10, 0);
                    break;
                case 8:
                    time = LocalTime.of(16, 10, 0);
                    break;
                case 9:
                    time = LocalTime.of(17, 5, 0);
                    break;
                case 10:
                    time = LocalTime.of(19, 15, 0);
                    break;
                case 11:
                    time = LocalTime.of(20, 10, 0);
                    break;
                case 12:
                    time = LocalTime.of(21, 10, 0);
                    break;

            }
            return time;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public static ArrayList<ToDo> insertCourse(Context context, long calID, Course course, LocalDate firstDate){
            ArrayList<ToDo> arrayList = new ArrayList<>();
            LocalTime startTime = courseStartTime(course.getBeginTime());
            LocalTime endTime = courseEndTime(course.getEndTime());
            DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
            SQLiteDao sqLiteDao = new SQLiteDao(dbOpenHelper);
            //创建重复组号
            int groupid = sqLiteDao.getMaxKinLinGroupId() + 1;
            int id = sqLiteDao.getMaxKinLinId() + 1;
            for(int i = 1; i <= 16; i++){
                if(i < course.getBeginWeek() || i > course.getEndWeek()) continue;
                LocalDate date = firstDate.plusDays(7 * (i - 1) + course.getCourseWeekday() - 1);
                Calendar courseBeginTime = Calendar.getInstance();
                courseBeginTime.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), startTime.getHour(), startTime.getMinute());
                Calendar courseEndTime = Calendar.getInstance();
                courseEndTime.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), endTime.getHour(), endTime.getMinute());
                long startMillis = courseBeginTime.getTimeInMillis();
                long endMillis = courseEndTime.getTimeInMillis();

                ContentResolver cr = context.getContentResolver();
                ContentValues values = new ContentValues();
                values.put(CalendarContract.Events.DTSTART, startMillis);
                values.put(CalendarContract.Events.DTEND, endMillis);
                values.put(CalendarContract.Events.TITLE, course.getCourseName());
                values.put(CalendarContract.Events.DESCRIPTION, course.getCourseClassroom() + " " + course.getCourseTeacher());
                values.put(CalendarContract.Events.CALENDAR_ID, calID);
                values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
                Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
                long eventId = Long.parseLong(uri.getLastPathSegment());
                ToDo todo = new ToDo(id++, course.getCourseName(), course.getCourseClassroom() + " " + course.getCourseTeacher(),
                        date, ToDo.WEEKLY, groupid, LocalDateTime.of(date, startTime), ToDo.PASSIVE, 45 * (course.getEndTime() - course.getBeginTime()),
                        0, false, 45);
                todo.setEventID(eventId);
                arrayList.add(todo);
            }
            return arrayList;
        }


    }
}
