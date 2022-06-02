package edu.zjut.zzy.ticklist.activity;

import androidx.appcompat.app.AppCompatActivity;

import edu.zjut.zzy.ticklist.MainActivity;
import edu.zjut.zzy.ticklist.R;
import edu.zjut.zzy.ticklist.SP.UserManager;
import edu.zjut.zzy.ticklist.android.AndroidState;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {
    private static final String TAG = StartActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "------onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

        UserManager userManager = new UserManager(getApplicationContext());

        if(userManager.getAutoLoginSetting()){
            //自动进行登录
            if(!AndroidState.isNetworkConnected(getApplicationContext())){
                Log.d(TAG, "网络未连接");
            }
            else{
                //登录
                OkHttpClient okHttpClient = new OkHttpClient();
                String accountName = userManager.getEmail();
                String passwords = userManager.getUserPassword();
                String url = "https://f12f-115-200-32-15.ngrok.io/KiLin_war_exploded/login?email=" + accountName + "&password=" + passwords;
                Request request = new Request.Builder().url(url).get().build();
                Call call = okHttpClient.newCall(request);
                Log.d(TAG, "loginging........" + accountName + "  " + passwords);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if(response.isSuccessful()){
                            String userName;
                            userName = response.body().string();
                            Log.d(TAG, userName);

                            //跳转页面
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
            }

        }else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


    }
}