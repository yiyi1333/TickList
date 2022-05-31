package edu.zjut.zzy.ticklist.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import butterknife.ButterKnife;
import edu.zjut.zzy.ticklist.MainActivity;
import edu.zjut.zzy.ticklist.R;
import edu.zjut.zzy.ticklist.fragment.ListFragment;

public class ImmersionModelActivity extends AppCompatActivity {
    private static final String TAG = ImmersionModelActivity.class.getSimpleName();
    private int targetTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "----onCreate()");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_immersionmodel);
//        Bundle bundle = this.getIntent().getExtras();
//        targetTime = bundle.getInt("targetTime");
//        String nowTime = bundle.getString("clockTime");
    }

    @Override
    protected void onStop(){
        Log.d(TAG, "----onStop()");
        super.onStop();
    }

    @Override
    public void onBackPressed(){
        Log.d(TAG, "----onBackPressed()");
        super.onBackPressed();
    }
}
