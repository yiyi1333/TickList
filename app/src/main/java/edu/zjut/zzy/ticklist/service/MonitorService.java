package edu.zjut.zzy.ticklist.service;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

/*
* 监控前台运行的activity
*  */
public class MonitorService extends Service {
    private static final  String TAG = MonitorService.class.getSimpleName();
    private Context context;
    private Timer timer;
    @Override
    public void onCreate() {
        Log.d(TAG, "------Create()");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "------Destory()");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "------onUnbind()");
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "------onBind()");
        return null;
    }

    public class MonitorServiceControl extends Binder{
        public void setContext(Context ctx){
            context = ctx;
            timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Log.d(TAG, "running");
                    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                    String packageName = context.getPackageName();
                    ArrayList<ActivityManager.RunningAppProcessInfo> appProcessInfoList = (ArrayList<ActivityManager.RunningAppProcessInfo>) activityManager.getRunningAppProcesses();
                    if(appProcessInfoList != null){
                        for(ActivityManager.RunningAppProcessInfo i : appProcessInfoList){
                            System.out.println(i.processName);
                        }
                    }else {
                        System.out.println("No appProcess");
                    }
                }
            };
            timer.schedule(task, 0, 3000);
        }
    }
}
