package edu.zjut.zzy.ticklist.activity;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.account_edit)
    public EditText account;
    @BindView(R.id.password_edit)
    public EditText password;
    @BindView(R.id.checkBox)
    public CheckBox autoLogin;
    @BindView(R.id.login_button)
    public Button login;

    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!AndroidState.isNetworkConnected(getApplicationContext())){
                    Log.d(TAG, "网络未连接");
                }
                else{
                    //登录
                    OkHttpClient okHttpClient = new OkHttpClient();
                    String url = "https://f12f-115-200-32-15.ngrok.io/KiLin_war_exploded/login?email=" + account.getText().toString() + "&password=" + password.getText().toString();
                    Request request = new Request.Builder().url(url).get().build();
                    Call call = okHttpClient.newCall(request);
                    Log.d(TAG, "loginging........" + account.getText().toString() + "  " + password.getText().toString());
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            if(response.isSuccessful()){
                                userName = response.body().string();
                                Log.d(TAG, userName);
                                UserManager userManager = new UserManager(getApplicationContext());
                                userManager.setAutoLoginSetting(autoLogin.isChecked());
                                userManager.setUserName(userName);
                                userManager.setEmail(account.getText().toString());
                                userManager.setUserPassword(password.getText().toString());

                                //跳转页面
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                }

            }
        });



    }
}