package edu.zjut.zzy.ticklist.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import edu.zjut.zzy.ticklist.activity.ImmersionModelActivity;
import edu.zjut.zzy.ticklist.fragment.ClockFragment;

public class ImmersionModelService extends Service {
    private static final String  TAG = ImmersionModelService.class.getSimpleName();
    private int targetTime;
    private Timer timer;
    private int finishTime;
    private LocalTime beginTime;

    @Override
    public void onCreate() {
        Log.d(TAG, "--------onCreate()");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if(timer != null){
            timer.cancel();
        }
        Log.d(TAG, "---------onDestroy()");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "-------------onBind");
        return new ImmersionModelServiceControl();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addTimer(){
        timer = new Timer();
        TimerTask task = null;
        beginTime = LocalTime.now();
        if(targetTime == 0){
            //正向计时
            task = new TimerTask() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    LocalTime nowTime = LocalTime.now();
                    int diff = (nowTime.getHour() - beginTime.getHour()) * 3600 + (nowTime.getMinute() - beginTime.getMinute()) * 60 + (nowTime.getSecond() - beginTime.getSecond())
                            + finishTime;
                    LocalTime clockTime = LocalTime.of(diff / 3600, diff % 3600 / 60, diff % 60);
                    Bundle bundle = new Bundle();
                    bundle.putInt("hour", clockTime.getHour());
                    bundle.putInt("minute", clockTime.getMinute());
                    bundle.putInt("second", clockTime.getSecond());
                    Message msg = ImmersionModelActivity.handler.obtainMessage();
                    msg.setData(bundle);
                    ImmersionModelActivity.handler.sendMessage(msg);
                    if(diff >= 3 * 3600){
                        //完成计时
//                        System.out.println("完成计时");
                        timer.cancel();
                    }
                }
            };
        }
        else {
            //倒计时
            task = new TimerTask() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    LocalTime nowTime = LocalTime.now();
                    int diff = (nowTime.getHour() - beginTime.getHour()) * 3600 + (nowTime.getMinute() - beginTime.getMinute()) * 60 + (nowTime.getSecond() - beginTime.getSecond());
                    int remainingTime = finishTime - diff;
                    LocalTime clockTime = LocalTime.of(remainingTime / 3600, remainingTime % 3600 / 60, remainingTime % 60);
                    Bundle bundle = new Bundle();
                    bundle.putInt("hour", clockTime.getHour());
                    bundle.putInt("minute", clockTime.getMinute());
                    bundle.putInt("second", clockTime.getSecond());
                    Message msg = ImmersionModelActivity.handler.obtainMessage();
                    msg.setData(bundle);
                    ImmersionModelActivity.handler.sendMessage(msg);
                    if(remainingTime <= 0){
                        //完成计时
//                        System.out.println("完成计时");
                        timer.cancel();
                    }
                }
            };
        }
        timer.schedule(task, 0, 500);
    }


    public class ImmersionModelServiceControl extends Binder {
        //设置目标时间
        public void setTime(int target, int finish){
            targetTime = target;
            finishTime = finish;
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void start(){
            addTimer();
        }
    }
}
