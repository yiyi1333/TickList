package edu.zjut.zzy.ticklist.popupwindows;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;

import edu.zjut.zzy.ticklist.CInterface.ClassroomJoin;
import edu.zjut.zzy.ticklist.Http.HttpService;
import edu.zjut.zzy.ticklist.R;
import edu.zjut.zzy.ticklist.SP.UserManager;
import edu.zjut.zzy.ticklist.bean.Classroom;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import razerdp.basepopup.BasePopupWindow;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

public class ClassroomCreatePopWindow extends BasePopupWindow {
    private static final String TAG = ClassroomCreatePopWindow.class.getSimpleName();
    private Context context;
    private View root;
    private ClassroomJoin classroomJoin;

    private EditText classroomNameEdit;
    private Button classroomCreateButton;

    public ClassroomCreatePopWindow(Context context, ClassroomJoin classroomJoin){
        super(context);
        this.context = context;
        this.classroomJoin = classroomJoin;
        root = createPopupById(R.layout.popwindow_classroom_create);

        classroomNameEdit = root.findViewById(R.id.classroom_name_edit);
        classroomCreateButton = root.findViewById(R.id.classroom_create_btn);
        setContentView(root);


        classroomCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String classroomName = classroomNameEdit.getText().toString();
                if(classroomName != null && classroomName != ""){
                    //创建自习室
                    OkHttpClient okHttpClient = new OkHttpClient();
                    UserManager userManager = new UserManager(context);
                    String email = userManager.getEmail();
                    String url = HttpService.URL + "createclassroom?email=" + email + "&classroomName=" + classroomName;
                    Request request = new Request.Builder().url(url).build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Log.d(TAG, "Call failed");
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            if(response.isSuccessful()){
                                String jsonString = response.body().string();
                                Log.d(TAG, jsonString);
                                Message message = httphandle.obtainMessage();
                                message.obj = jsonString;
                                httphandle.sendMessage(message);
                            }
                        }
                    });

                }
            }
        });

    }

    private Handler httphandle = new Handler(){
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String jsonStr = msg.obj.toString();
            Gson gson = new Gson();
            Classroom classroom = gson.fromJson(jsonStr, Classroom.class);
            classroomJoin.switchToClassroom(classroom);
            ClassroomCreatePopWindow.this.dismiss();
        }
    };

    @Override
    public Animation onCreateShowAnimation(){
        //创建弹窗出现动画，从下方展开
        return AnimationHelper.asAnimation().withTranslation(TranslationConfig.FROM_TOP).toShow();
    }

    @Override
    public Animation onCreateDismissAnimation(){
        return AnimationHelper.asAnimation().withTranslation(TranslationConfig.TO_TOP).toDismiss();
    }
}
