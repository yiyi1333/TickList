package edu.zjut.zzy.ticklist.fragment;

import static android.content.Context.BIND_AUTO_CREATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import edu.zjut.zzy.ticklist.Http.HttpService;
import edu.zjut.zzy.ticklist.MainActivity;
import edu.zjut.zzy.ticklist.R;
import edu.zjut.zzy.ticklist.SP.StateManager;
import edu.zjut.zzy.ticklist.SP.UserManager;
import edu.zjut.zzy.ticklist.activity.ImmersionModelActivity;
import edu.zjut.zzy.ticklist.android.AndroidState;
import edu.zjut.zzy.ticklist.bean.Focus;
import edu.zjut.zzy.ticklist.bean.ToDo;
import edu.zjut.zzy.ticklist.dao.DBOpenHelper;
import edu.zjut.zzy.ticklist.dao.SQLiteDao;
import edu.zjut.zzy.ticklist.service.ClockService;
import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ClockFragment extends Fragment {
    private static final String TAG = ClockFragment.class.getSimpleName();
    private View root;
    private static ClockService.ClockControl clockControl;
    private ClockServiceConnection clockConnection;
    private ToDo todoRecent;
    private static int targetTime;
    private static Context context;
    private static TextView statusBar;
    private static CircularFillableLoaders circularFillableLoaders;
    private static TextView clockTime;
    private static FancyButton startButton;
    private ImageView musicButton;
    private ImageView musicSelectButton;
    private ImageView finishButton;
    private ImageView fullScreenButton;
    private static TextView tomatoNumber;


    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;


    public ClockFragment() {

    }

    @Override
    public void onPause(){
        super.onPause();
    }

    public void onDestroy(){
        getActivity().unbindService(clockConnection);
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        alertDialogBuilder = new AlertDialog.Builder(context);
        /* 获取目标计时时间 */
        UserManager userManager = new UserManager(context);
        int todoId = userManager.getToDoId();
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        SQLiteDao sqLiteDao = new SQLiteDao(dbOpenHelper);
        todoRecent = sqLiteDao.getTodo(todoId);
        targetTime = todoRecent.getFocusTime();
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(root == null){
            System.out.println("CreateFragement");
            root = inflater.inflate(R.layout.fragment_clock, container, false);
        }
        bindView();

        tomatoNumber.setText(String.valueOf(targetTime / 60));
        if(targetTime == 0){
            clockTime.setText("00:00");
        }
        else{
            clockTime.setText(String.valueOf(targetTime % 60) + ":00");
        }

        bindListener();
        clockConnection = new ClockServiceConnection();
        Intent intent = new Intent(getContext(), ClockService.class);
        getActivity().bindService(intent, clockConnection, BIND_AUTO_CREATE);
        return root;
    }

    private void bindListener(){
        startButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(startButton.getText().toString().equals("开始专注")){
                    System.out.println("开始计时");
                    clockControl.play();
                    startButton.setText("暂停一会");
                    startButton.setIconResource("| |");
                }else {
                    System.out.println("暂停专注");
                    clockControl.pause();
                    startButton.setText("开始专注");
                    startButton.setIconResource("▶");
                }

            }
        });
        
        fullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ImmersionModelActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("clockTime", String.valueOf(clockTime.getText()));
                bundle.putInt("targetTime", targetTime);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startButton.getText().toString().equals("暂停一会")){
                    //弹窗提示是否提前结束计时
                    AlertDialog alertDialog = alertDialogBuilder.setTitle("提示:")
                            .setMessage("现在退出会提前结束计时")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    System.out.println("点击了取消");
                                }
                            }).setPositiveButton("结束", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    clockControl.pause();
                                    clockControl.init();
                                    System.out.println("点击了确定");
                                    LocalDateTime justNow = LocalDateTime.now();
                                    Date today = Date.valueOf(justNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                                    Time now = Time.valueOf(justNow.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                                    int focusTime = 0;
                                    focusTime += Integer.parseInt(tomatoNumber.getText().toString()) * 60;
                                    String[] minuteSec = clockTime.getText().toString().split(":");
                                    focusTime += Integer.parseInt(minuteSec[0]);
                                    if(targetTime != 0){
                                        focusTime = targetTime - focusTime;
                                    }
                                    Focus focus = new Focus(todoRecent.getGroupId(), todoRecent.getGroupId(), todoRecent.getContent(), todoRecent.getDescription(), today, focusTime, now);
                                    //判断网络环境
                                    if(AndroidState.isNetworkConnected(context)){
                                        //上传数据
                                        System.out.println("上传\n" + focus.toString());
                                        HttpService.uploadFocusTime(focus);
                                    }else{
                                        //将数据保存到本地
                                        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
                                        SQLiteDao sqLiteDao = new SQLiteDao(dbOpenHelper);
                                        sqLiteDao.insertFocus(focus, justNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), justNow.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                                        //将缓存状态改为true
                                        StateManager stateManager = new StateManager(context);
                                        stateManager.setBufferedState(true);
                                    }
                                    //时间重置
                                    tomatoNumber.setText(String.valueOf(targetTime / 60));
                                    clockTime.setText(String.valueOf(targetTime % 60) + ":00");


                                }
                            }).create();
                    alertDialog.show();
                }
            }
        });
    }


    private void bindView(){
        statusBar = root.findViewById(R.id.status_bar);
        circularFillableLoaders = root.findViewById(R.id.circularFillableLoaders);
        clockTime = root.findViewById(R.id.clock_time);
        startButton = root.findViewById(R.id.btn_startpause);
        musicButton = root.findViewById(R.id.music_start);
        musicSelectButton = root.findViewById(R.id.music_select);
        finishButton = root.findViewById(R.id.todo_finish);
        fullScreenButton = root.findViewById(R.id.full_screen);
        tomatoNumber = root.findViewById(R.id.tomato_num);
    }


    public static Handler handler = new Handler(){
      @RequiresApi(api = Build.VERSION_CODES.O)
      public void handleMessage(Message msg){
          Bundle bundle = msg.getData();
          int hour = bundle.getInt("hour");
          int minute = bundle.getInt("minute");
          int second = bundle.getInt("second");
          LocalTime time = LocalTime.of(hour, minute, second);
          int finishTime =  time.getHour() * 3600 + time.getMinute() * 60 + time.getSecond();
          clockTime.setText(time.format(DateTimeFormatter.ofPattern("mm:ss")));
          if(time.getHour() != 0){
              tomatoNumber.setText(time.getHour());
          }
          if(targetTime != 0){
              //倒计时模式
              circularFillableLoaders.setProgress((time.getMinute() * 60 + time.getSecond()) * 100 / ((targetTime % 60) * 60));
          }else {
              circularFillableLoaders.setProgress(100 - (time.getMinute() * 60 + time.getSecond()) * 100 / 3600);
          }
          if(targetTime != 0 && finishTime <= 0){
              //倒计时
              System.out.println("完成计时");
              clockControl.init();
              startButton.setText("开始专注");
              startButton.setIconResource("▶");
              tomatoNumber.setText(String.valueOf(targetTime / 60));
              clockTime.setText(LocalTime.of(0, targetTime % 60, 0).format(DateTimeFormatter.ofPattern("mm:ss")));
              circularFillableLoaders.setProgress(100);

              //构造上传数据格式
              UserManager userManager = new UserManager(context);
              int userId = userManager.getUserId();
              int todoId = userManager.getToDoId();
              DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
              SQLiteDao sqLiteDao = new SQLiteDao(dbOpenHelper);
              ToDo todo = sqLiteDao.getTodo(todoId);
              LocalDateTime justNow = LocalDateTime.now();
              Date today = Date.valueOf(justNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
              Time now = Time.valueOf(justNow.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
              Focus focus = new Focus(userId, todo.getGroupId(), todo.getContent(), todo.getDescription(), today, targetTime, now);
              System.out.println(focus.toString());
              //判断网络环境
              if(AndroidState.isNetworkConnected(context)){
                  //上传数据
                  HttpService.uploadFocusTime(focus);
              }else{
                  //将数据保存到本地
                  sqLiteDao.insertFocus(focus, justNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), justNow.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                  //将缓存状态改为true
                  StateManager stateManager = new StateManager(context);
                  stateManager.setBufferedState(true);
              }


          }else if(targetTime == 0 && finishTime >= 3 * 3600){
              //完成正向计时
              System.out.println("完成计时");
              clockControl.init();
              startButton.setText("开始专注");
              startButton.setIconResource("▶");
              clockTime.setText("00:00");
              tomatoNumber.setText("0");
              circularFillableLoaders.setProgress(100);


              //构造上传数据格式
              UserManager userManager = new UserManager(context);
              int userId = userManager.getUserId();
              int todoId = userManager.getToDoId();
              DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
              SQLiteDao sqLiteDao = new SQLiteDao(dbOpenHelper);
              ToDo todo = sqLiteDao.getTodo(todoId);
              LocalDateTime justNow = LocalDateTime.now();
              Date today = Date.valueOf(justNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
              Time now = Time.valueOf(justNow.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
              Focus focus = new Focus(userId, todo.getGroupId(), todo.getContent(), todo.getDescription(), today, 180, now);
              System.out.println(focus.toString());
              //判断网络环境
              if(AndroidState.isNetworkConnected(context)){
                  //上传数据
                  HttpService.uploadFocusTime(focus);
              }else{
                  //将数据保存到本地
                  sqLiteDao.insertFocus(focus, justNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), justNow.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                  //将缓存状态改为true
                  StateManager stateManager = new StateManager(context);
                  stateManager.setBufferedState(true);
              }

          }
      }
    };

    //连接服务
    private class ClockServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            clockControl = (ClockService.ClockControl) iBinder;
            clockControl.setTarget(targetTime);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

}