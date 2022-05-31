package edu.zjut.zzy.ticklist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.zjut.zzy.ticklist.CInterface.SwitchFragment;
import edu.zjut.zzy.ticklist.dao.DBOpenHelper;
import edu.zjut.zzy.ticklist.fragment.ClockFragment;
import edu.zjut.zzy.ticklist.fragment.ListFragment;
import edu.zjut.zzy.ticklist.fragment.PersonFragment;
import edu.zjut.zzy.ticklist.fragment.RankFragment;
import edu.zjut.zzy.ticklist.fragment.StatisticsFragment;
import edu.zjut.zzy.ticklist.service.ClockService;

public class MainActivity extends AppCompatActivity implements SwitchFragment {
    private static final String TAG = "MainActivity";
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private Fragment fragment;
    private int fragmentFlag;

    @BindView(R.id.bottom_navigation_bar)
    public BottomNavigationView bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if(savedInstanceState == null){
            fragment = new ListFragment(this);
            fragmentFlag = 1;
            getSupportFragmentManager().beginTransaction().replace(R.id.frameContain, fragment).commit();
        }
        BindListener();
        Context context;
        alertDialogBuilder = new AlertDialog.Builder(this);


    }
    @Override
    protected void onStop(){
        System.out.println("退出Activity");
        super.onStop();
    }

    @Override
    public void onBackPressed(){
        if(fragmentFlag == 3){
            alertDialog = alertDialogBuilder.setTitle("提示:")
                    .setMessage("确认放弃本次计时吗？")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            System.out.println("点击了取消");
                        }
                    }).setPositiveButton("放弃", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            System.out.println("点击了确定");
                            MainActivity.super.onBackPressed();
                        }
                    }).create();
            alertDialog.show();
        }
        else {
            MainActivity.super.onBackPressed();
        }
    }

    private void BindListener(){
        bottomNavigationBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.list:
                        getWindow().setStatusBarColor(Color.rgb(54, 132, 132));
                        fragment = new ListFragment(MainActivity.this);
                        fragmentFlag = 1;
                        break;

                    case R.id.rank:
                        fragment = new RankFragment();
                        fragmentFlag = 2;
                        break;

                    case R.id.clock:
                        getWindow().setStatusBarColor(Color.BLACK);
//                        bottomNavigationBar.setBackgroundColor(Color.BLACK);
                        fragment = new ClockFragment();
                        fragmentFlag = 3;
                        break;

                    case R.id.statistics:
                        fragment = new StatisticsFragment();
                        fragmentFlag = 4;
                        break;

                    case R.id.person:
                        fragment = new PersonFragment();
                        fragmentFlag = 5;
                        break;
                }
                assert fragment != null;
                getSupportFragmentManager().beginTransaction().replace(R.id.frameContain, fragment).commit();
                return true;
            }
        });
    }

    @Override
    public void switchFragment(int fragmentId) {
        Log.d(TAG, String.valueOf(fragmentId));
        fragmentFlag = fragmentId;
        fragment = new ClockFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameContain, fragment).commit();
        bottomNavigationBar.setSelectedItemId(bottomNavigationBar.getMenu().getItem(fragmentId - 1).getItemId());
    }
}