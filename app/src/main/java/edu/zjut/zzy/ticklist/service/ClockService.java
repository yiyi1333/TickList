package edu.zjut.zzy.ticklist.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.time.Clock;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

import edu.zjut.zzy.ticklist.fragment.ClockFragment;

public class ClockService extends Service {
    private int targetTime;
    private Timer timer;
    private int finishTime;
    private LocalTime beginTime;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("ClockService binded");
        return new ClockControl();
    }

    @Override
    public void onCreate(){
        System.out.println("ClockService created");
        super.onCreate();
    }

    public void onDestroy(){
        if(timer != null){
            timer.cancel();
        }
        super.onDestroy();
    }
    //创建计时器
    public void addTimer(){
        timer = new Timer();
        TimerTask task = null;
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
                    Message msg = ClockFragment.handler.obtainMessage();
                    msg.setData(bundle);
                    ClockFragment.handler.sendMessage(msg);
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
                    int diff = (nowTime.getHour() - beginTime.getHour()) * 3600 + (nowTime.getMinute() - beginTime.getMinute()) * 60 + (nowTime.getSecond() - beginTime.getSecond())
                            + finishTime;
                    int remainingTime = targetTime - diff;
                    LocalTime clockTime = LocalTime.of(remainingTime / 3600, remainingTime % 3600 / 60, remainingTime % 60);
                    Bundle bundle = new Bundle();
                    bundle.putInt("hour", clockTime.getHour());
                    bundle.putInt("minute", clockTime.getMinute());
                    bundle.putInt("second", clockTime.getSecond());
                    Message msg = ClockFragment.handler.obtainMessage();
                    msg.setData(bundle);
                    ClockFragment.handler.sendMessage(msg);
                    if(diff >= targetTime){
                        //完成计时
//                        System.out.println("完成计时");
                        timer.cancel();
                    }
                }
            };
        }
        timer.schedule(task, 0, 500);
    }

    public class ClockControl extends Binder{
        //设置目标时间
        public void setTarget(int target){
            targetTime = target * 60;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void pause(){
            timer.cancel();
            LocalTime nowTime = LocalTime.now();
            finishTime = (nowTime.getHour() - beginTime.getHour()) * 3600 + (nowTime.getMinute() - beginTime.getMinute()) * 60 + (nowTime.getSecond() - beginTime.getSecond()) + finishTime;
        }
        //开始计时
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void play(){
            beginTime = LocalTime.now();
            addTimer();
        }

        public void init(){
            finishTime = 0;
        }
    }
}
