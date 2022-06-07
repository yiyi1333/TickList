package edu.zjut.zzy.ticklist.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import butterknife.ButterKnife;
import edu.zjut.zzy.ticklist.MainActivity;
import edu.zjut.zzy.ticklist.R;
import edu.zjut.zzy.ticklist.SP.UserManager;
import edu.zjut.zzy.ticklist.bean.ToDo;
import edu.zjut.zzy.ticklist.dao.DBOpenHelper;
import edu.zjut.zzy.ticklist.dao.SQLiteDao;
import edu.zjut.zzy.ticklist.fragment.ListFragment;
import edu.zjut.zzy.ticklist.service.ImmersionModelService;
import edu.zjut.zzy.ticklist.service.MonitorService;

public class ImmersionModelActivity extends AppCompatActivity {
    private static final String TAG = ImmersionModelActivity.class.getSimpleName();
    private static TextView clockTime;
    private static Context context;
    private static ToDo todoRecent;
    private static int targetTime;
    private static int finishTime;
    private ImmersionModelService.ImmersionModelServiceControl immersionModelServiceControl;
    private ImmersionModelConnection conn;

    private MonitorService.MonitorServiceControl monitorServiceControl;
    private MonitrorServieConnection mconn;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, String.valueOf(keyCode));
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "----onCreate()");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_immersionmodel);

        context = getApplicationContext();
        clockTime = findViewById(R.id.clock_time);

        Bundle bundle = this.getIntent().getExtras();
        targetTime = bundle.getInt("targetTime");
        String nowTime = bundle.getString("clockTime");
        int hour = Integer.parseInt(bundle.getString("hour"));
        String[] minsec = nowTime.split(":");
        int minute = Integer.parseInt(minsec[0]);
        int second = Integer.parseInt(minsec[1]);
        finishTime = hour * 3600 + minute * 60 + second;
        new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                super.run();
                UserManager userManager = new UserManager(getApplicationContext());
                int todoId = userManager.getToDoId();
                DBOpenHelper dbOpenHelper = new DBOpenHelper(getApplicationContext());
                SQLiteDao sqLiteDao = new SQLiteDao(dbOpenHelper);
                todoRecent = sqLiteDao.getTodo(todoId);
            }
        }.start();

        //绑定服务
        conn = new ImmersionModelConnection();
        Intent intent = new Intent(getApplicationContext(), ImmersionModelService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);

        mconn = new MonitrorServieConnection();
        Intent intent1 = new Intent(getApplicationContext(), MonitorService.class);
        bindService(intent1, mconn, BIND_AUTO_CREATE);


    }

    @Override
    protected void onStop(){
        Log.d(TAG, "----onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        Log.d(TAG, "----onDestory()");
        unbindService(conn);
        super.onDestroy();
    }

    @Override
    public void onBackPressed(){
        Log.d(TAG, "----onBackPressed()");
        if(targetTime == 0){
            super.onBackPressed();
        }

    }

    public static Handler handler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            int hour = data.getInt("hour");
            int minute = data.getInt("minute");
            int second = data.getInt("second");
            LocalTime time = LocalTime.of(hour, minute, second);
            if(hour == 0){
                clockTime.setText(time.format(DateTimeFormatter.ofPattern("mm:ss")));

            }else {
                clockTime.setText(time.format(DateTimeFormatter.ofPattern("H:mm:ss")));
            }

            if(targetTime == 0 && hour == 3){
                //正向计时
                Log.d(TAG, "结束正向计时");
                //上传数据

            }
            else if(targetTime != 0 && hour * 3600 + minute * 60 + second <= 0){
                //
                Log.d(TAG, "结束倒计时");
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    };


    private class ImmersionModelConnection implements ServiceConnection{

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            immersionModelServiceControl = (ImmersionModelService.ImmersionModelServiceControl) iBinder;
            Log.d(TAG, "bindTimerService");
            immersionModelServiceControl.setTime(targetTime, finishTime);
            immersionModelServiceControl.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

    private class MonitrorServieConnection implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            monitorServiceControl = (MonitorService.MonitorServiceControl) iBinder;
            System.out.println("setContext");
            monitorServiceControl.setContext(ImmersionModelActivity.this.getApplicationContext());
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }
}
