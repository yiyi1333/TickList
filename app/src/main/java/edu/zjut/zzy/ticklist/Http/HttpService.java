package edu.zjut.zzy.ticklist.Http;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

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
    public static final String URL = "https://f98a-115-200-38-114.ngrok.io/KiLin_war_exploded/";

    public static OkHttpClient getOkHttpClient() {
        if(okHttpClient == null){
            okHttpClient = new OkHttpClient();
        }
        return okHttpClient;
    }


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
