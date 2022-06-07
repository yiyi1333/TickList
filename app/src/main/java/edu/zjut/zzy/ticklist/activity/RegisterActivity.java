package edu.zjut.zzy.ticklist.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.zjut.zzy.ticklist.Http.HttpService;
import edu.zjut.zzy.ticklist.R;
import edu.zjut.zzy.ticklist.SP.UserManager;
import edu.zjut.zzy.ticklist.android.AndroidState;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.logging.Logger;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    @BindView(R.id.account_edit)
    public EditText emailText;

    @BindView(R.id.password_edit)
    public EditText passwordText;

    @BindView(R.id.again_password_edit)
    public EditText againPasswordText;

    @BindView(R.id.verification_code_edit)
    public EditText verificationCodeText;

    @BindView(R.id.verification_code_button)
    public Button verificationCodeButton;

    @BindView(R.id.login_button)
    public Button registerButton;

    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        verificationCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //请求获取验证码
                if(!AndroidState.isNetworkConnected(getApplicationContext())){
                    Log.d(TAG, "网络未连接");

                }
                else {
                    //发送获取验证码请求
                    String email = String.valueOf(emailText.getText());
                    if(email == null || email.equals("")){
                        //未输入emial
                        Log.d(TAG, "未输入邮箱");
                        emailText.setError("请输入邮箱");
                    }
                    else {
                        Log.d(TAG, "getVerifiation ing......");
                        OkHttpClient okHttpClient = new OkHttpClient();
                        String url = HttpService.URL + "verification?email=" + email;
                        Request request = new Request.Builder().url(url).build();
                        Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                if(response.isSuccessful()){
                                    verificationCode = response.body().string().split("\"")[1];
                                    Log.d(TAG, "verifiationCoed: " + verificationCode);
                                }
                            }
                        });
                    }

                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //先校验两次密码是否相同
                String email = String.valueOf(emailText.getText());
                String password1 = String.valueOf(passwordText.getText());
                String password2 = String.valueOf(againPasswordText.getText());
                if(password1 == null || password1.equals("")){
                    passwordText.setError("请输入密码");
                }
                else{
                    if(password2 == null || password2.equals("")){
                        againPasswordText.setError("请再次输入密码");
                    }
                    else{
                        String code = String.valueOf(verificationCodeText.getText());
                        if(code == null || code.equals("")){
                            verificationCodeText.setError("请输入验证码");
                        }
                        else{
                            //校验密码
                            if(!password1.equals(password2)){
                                againPasswordText.setError("两次输入密码不同!");
                            }
                            else{
                                if(code.equals(verificationCode)){
                                    OkHttpClient okHttpClient = new OkHttpClient();
                                    String url = HttpService.URL + "register?email=" + email + "&password=" + password1;
                                    Request request = new Request.Builder().url(url).build();
                                    Call call = okHttpClient.newCall(request);
                                    call.enqueue(new Callback() {
                                        @Override
                                        public void onFailure(@NonNull Call call, @NonNull IOException e) {

                                        }

                                        @Override
                                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                            if(response.isSuccessful()){
                                                String message = response.body().string().split("\"")[1];
                                                Log.d(TAG, message);
                                                if(message.equals("注册成功")){
                                                    //弹窗提示


                                                    finish();
                                                }
                                            }
                                        }
                                    });
                                }
                                else{
                                    Log.d(TAG + "  Code:", code);
                                    Log.d(TAG + "  RealCode:", verificationCode);
                                    verificationCodeText.setError("验证码错误!");
                                }
                            }
                        }
                    }
                }
            }
        });

    }
}