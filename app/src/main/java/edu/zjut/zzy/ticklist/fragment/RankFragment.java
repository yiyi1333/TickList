package edu.zjut.zzy.ticklist.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import edu.zjut.zzy.ticklist.CInterface.ClassroomJoin;
import edu.zjut.zzy.ticklist.Http.HttpService;
import edu.zjut.zzy.ticklist.R;
import edu.zjut.zzy.ticklist.SP.UserManager;
import edu.zjut.zzy.ticklist.bean.Classroom;
import edu.zjut.zzy.ticklist.bean.RankInfo;
import edu.zjut.zzy.ticklist.popupwindows.ClassroomCreatePopWindow;
import edu.zjut.zzy.ticklist.popupwindows.ClassroomJoinPopWindow;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

public class RankFragment extends Fragment implements ClassroomJoin {
    private static final String TAG = RankFragment.class.getSimpleName();

    private View root;

    private RelativeLayout classroomJoinLayout;
    private Button classroomButtonCreate;
    private Button classroomButtonJoin;

    private RelativeLayout rankLayout;
    private TextView classroomName;
    private TextView classroomRank;
    private TextView classroomNumber;
    private RecyclerView rankView;

    private ClassroomCreatePopWindow classroomCreatePopWindow;
    private ClassroomJoinPopWindow classroomJoinPopWindow;


    private String roomCode;
    private String roomName;

    private ArrayList<RankInfo> rankInfos;


/*
* 加载网络图片的方法
*  */
//    private Handler imageHandler = new Handler(){
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            byte[] bytes = (byte[]) msg.obj;
//            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
////            imageView.setImageBitmap(bitmap);
////            imageView1.setImageBitmap(bitmap);
//        }
//    };
//
//    private void httpImageLoad(String url){
////        String url = "https://yiyi-picture.oss-cn-hangzhou.aliyuncs.com/Typora/image-20220519101347343.png";
//        OkHttpClient okHttpClient = new OkHttpClient();
//        Request request = new Request.Builder().url(url).build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if(response.isSuccessful()){
//                    Message message = imageHandler.obtainMessage();
//                    message.obj = response.body().bytes();
//                    imageHandler.sendMessage(message);
//                }
//            }
//        });
//    }

    public RankFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(root == null){
            root = inflater.inflate(R.layout.fragment_rank, container, false);
        }
        bindView();
        bindEvents();
        //先从sp中读取自习室状态
        UserManager userManager = new UserManager(getContext());
        roomCode = userManager.getClassRoomCode();
        roomName = userManager.getClassroomName();
        if(roomCode == null){
            //未加入自习室
            rankLayout.setVisibility(View.GONE);
            classroomJoinLayout.setVisibility(View.VISIBLE);

        }else {
            //已经加入自习室
            rankLayout.setVisibility(View.VISIBLE);
            classroomJoinLayout.setVisibility(View.GONE);
            classroomName.setText(roomName);
            classroomRank.setText("No.1");
            classroomNumber.setText("自习室人数: 1/50  (" + roomCode + ")");
            loadListData();
        }
        //更新sp,异步
        updateClassroomStatus();

        return root;
    }

    private void bindView(){
        classroomJoinLayout = root.findViewById(R.id.classroom_join);
        classroomButtonCreate = root.findViewById(R.id.classroom_create_button);
        classroomButtonJoin = root.findViewById(R.id.classroom_join_button);

        rankLayout = root.findViewById(R.id.rank_layout);
        classroomName = root.findViewById(R.id.classroom_name);
        classroomRank = root.findViewById(R.id.classroom_rank);
        classroomNumber = root.findViewById(R.id.classroom_number);
        rankView = root.findViewById(R.id.rank_list);


    }

    private void bindEvents(){
        classroomButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳出弹窗，输入自习室名称
                Log.d(TAG, "click----create");
                classroomCreatePopWindow = new ClassroomCreatePopWindow(RankFragment.this.getContext(), RankFragment.this);
                classroomCreatePopWindow.setBlurBackgroundEnable(true);
                classroomCreatePopWindow.setKeyboardAdaptive(true);
                classroomCreatePopWindow.setPopupGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER);
                classroomCreatePopWindow.showPopupWindow();
                Log.d(TAG, "popupWindows");
            }
        });

        classroomButtonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳出弹窗, 输入自习室号
                Log.d(TAG, "click----create");
                classroomJoinPopWindow = new ClassroomJoinPopWindow(RankFragment.this.getContext(), RankFragment.this);
                classroomJoinPopWindow.setBlurBackgroundEnable(true);
                classroomJoinPopWindow.setKeyboardAdaptive(true);
                classroomJoinPopWindow.setPopupGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER);
                classroomJoinPopWindow.showPopupWindow();
                Log.d(TAG, "popupWindowsjoin");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadListData(){
        UserManager userManager = new UserManager(getContext());
        String email = userManager.getEmail();
        LocalDate nowDate = LocalDate.now();
        //向服务器请求专注信息数据
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = HttpService.URL + "getFocusRankInfo?email=" + email + "&date=" + nowDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        Log.d(TAG, url);
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String jsonStr = response.body().string();
                    Log.d(TAG, jsonStr);
                    Message message = httpHandler.obtainMessage(1);
                    message.obj = jsonStr;
                    httpHandler.sendMessage(message);
                }
            }
        });

    }

    private void updateClassroomStatus(){
        UserManager userManager = new UserManager(getContext());
        String email = userManager.getEmail();
        //向服务器请求自习室信息数据
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = HttpService.URL + "getClassroomNameAndCode?email=" + email;
        Log.d(TAG, url);
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String jsonStr = response.body().string();
                    Log.d(TAG, jsonStr);
                    if(!jsonStr.equals("null")){
                        Message message = httpHandler.obtainMessage(2);
                        message.obj = jsonStr;
                        httpHandler.sendMessage(message);
                    }else{
                        Message message = httpHandler.obtainMessage(3);
                        httpHandler.sendMessage(message);
                    }
                }
            }
        });
    }

    @Override
    public void switchToClassroom(Classroom classroom) {
        roomName = classroom.getClassroomName();
        roomCode = classroom.getRoomCode();
        classroomName.setText(roomName);
        classroomRank.setText("No.1");
        classroomNumber.setText("自习室人数: 1/50  (" + roomCode + ")");
        rankLayout.setVisibility(View.VISIBLE);
        classroomJoinLayout.setVisibility(View.GONE);
        //修改sp
        UserManager userManager = new UserManager(getContext());
        userManager.setClassroomCode(roomCode);
        userManager.setClassroomName(roomName);
    }

    private Handler httpHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String jsonStr = null;
            Gson gson = new Gson();
            UserManager userManager = new UserManager(getContext());
            switch (msg.what){
                case 1:
                    jsonStr = (String) msg.obj;
                    TypeToken<ArrayList<RankInfo>> typeToken = new TypeToken<ArrayList<RankInfo>>(){};
                    rankInfos = gson.fromJson(jsonStr, typeToken.getType());
                    Log.d(TAG, rankInfos.toString());
                    break;
                case 2:
                    //有自习室信息，更新一下sp
                    jsonStr = (String) msg.obj;
                    Classroom classroom = gson.fromJson(jsonStr, Classroom.class);
                    userManager.setClassroomName(classroom.getClassroomName());
                    userManager.setClassroomCode(classroom.getRoomCode());
                    break;
                case 3:
                    //也需要更新一下sp
                    userManager.setClassroomName(null);
                    userManager.setClassroomCode(null);
                    break;
            }
        }
    };
}