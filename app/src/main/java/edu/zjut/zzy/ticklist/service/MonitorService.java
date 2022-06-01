package edu.zjut.zzy.ticklist.service;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import edu.zjut.zzy.ticklist.MainActivity;
import edu.zjut.zzy.ticklist.activity.ImmersionModelActivity;
import edu.zjut.zzy.ticklist.activity.WarnActivity;

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
        return new MonitorServiceControl();
    }



    public class MonitorServiceControl extends Binder{
        public void setContext(Context ctx){
            context = ctx;
            timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                    String packageName = context.getPackageName();
                    List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(20);
                    Log.d(TAG, "begin----");
                    if(runningTasks != null){
                        for(ActivityManager.RunningTaskInfo i : runningTasks){
                            System.out.println(i.baseActivity.getPackageName());
                        }
                        if (!packageName.equals(runningTasks.get(0).baseActivity.getPackageName())){
                            //弹出警告
                            Intent intent = new Intent(context, WarnActivity.class);
                            intent.setFlags(Intent.FLAG_FROM_BACKGROUND|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                    Log.d(TAG, "end----");
                }
            };
            timer.schedule(task, 0, 1000);
        }
    }
}
