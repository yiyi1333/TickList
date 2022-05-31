package edu.zjut.zzy.ticklist.Http;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import edu.zjut.zzy.ticklist.bean.Focus;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpService {
    private static OkHttpClient okHttpClient;
    private static final String URL = "https://85b9-115-200-2-145.ngrok.io";

    public static OkHttpClient getOkHttpClient() {
        if(okHttpClient == null){
            okHttpClient = new OkHttpClient();
        }
        return okHttpClient;
    }

    /*
    //同步请求
    public static void synchronousRequest(){
        new Thread(){
            public void run(){
                if (okHttpClient == null){
                    okHttpClient = HttpService.getOkHttpClient();
                }
                Request request = new Request.Builder().url("https://www.httpbin.org/").build();
                //准备好请求的Call对象
                Call call = okHttpClient.newCall(request);
                try {
                    Response response = call.execute();
                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    //异步请求
    public static void asynchronousRequest(){
        if (okHttpClient == null){
            okHttpClient = HttpService.getOkHttpClient();
        }
        Request request = new Request.Builder().url("https://www.httpbin.org/").build();
        //准备好请求的Call对象
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    System.out.println(response.body().string());
                }
            }
        });
    }
    */

    public static void uploadFocusTime(Focus focus){
        Gson gson = new Gson();
        String jsonStr = gson.toJson(focus);
        if (okHttpClient == null){
            okHttpClient = HttpService.getOkHttpClient();
        }
        RequestBody requestBody = RequestBody.create(jsonStr, MediaType.parse("application/json"));
        Request request = new Request.Builder().url(URL + "/KiLin_war_exploded/uploadFocus").post(requestBody).build();
        //准备好请求的Call对象
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    System.out.println(response.body().string());
                }
            }
        });
    }


}
