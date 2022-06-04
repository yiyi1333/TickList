package edu.zjut.zzy.ticklist.popupwindows;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Logger;

import edu.zjut.zzy.ticklist.CInterface.PickedDate;
import edu.zjut.zzy.ticklist.R;
import razerdp.basepopup.BasePopupWindow;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

public class FirstWeekDatePopWindow extends BasePopupWindow implements PickedDate {
    private static final String TAG = FirstWeekDatePopWindow.class.getSimpleName();

    private Context context;
    private View root;
    private PickedDate pickedDate;
    private LocalDate date;

    private EditText dateEdit;
    private Button confirmButton;

    public FirstWeekDatePopWindow(Context context, PickedDate pickedDate){
        super(context);
        this.context = context;
        this.pickedDate = pickedDate;
        root = createPopupById(R.layout.popwindow_firstday_select);

        dateEdit = root.findViewById(R.id.classroom_name_edit);
        confirmButton = root.findViewById(R.id.classroom_create_btn);
        setContentView(root);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(date != null){
                    Log.d(TAG, date.toString());
                    pickedDate.setPickedDate(date);
                }
                FirstWeekDatePopWindow.this.dismiss();
            }
        });

        dateEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    //获得焦点
                    DatePickPopWindow datePicker = new DatePickPopWindow(context, FirstWeekDatePopWindow.this);
                    datePicker.setBlurBackgroundEnable(true);
                    datePicker.setKeyboardAdaptive(true);
                    //设置在底部弹出
                    datePicker.setPopupGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
                    datePicker.showPopupWindow();
                }
            }
        });



    }


    @Override
    public Animation onCreateShowAnimation(){
        //创建弹窗出现动画，从下方展开
        return AnimationHelper.asAnimation().withTranslation(TranslationConfig.FROM_TOP).toShow();
    }

    @Override
    public Animation onCreateDismissAnimation(){
        return AnimationHelper.asAnimation().withTranslation(TranslationConfig.TO_TOP).toDismiss();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void setPickedDate(LocalDate pickedDate) {
        date = pickedDate;
        dateEdit.setText(pickedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}
