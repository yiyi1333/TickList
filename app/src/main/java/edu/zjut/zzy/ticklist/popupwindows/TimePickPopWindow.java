package edu.zjut.zzy.ticklist.popupwindows;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.LocalTime;

import edu.zjut.zzy.ticklist.CInterface.PickedTime;
import edu.zjut.zzy.ticklist.R;
import razerdp.basepopup.BasePopupWindow;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

public class TimePickPopWindow extends BasePopupWindow {
    private static final String TAG = TimePickPopWindow.class.getSimpleName();
    private Context context;
    private View root;
    private TimePicker timePicker;
    private PickedTime pickedTime;
    private TextView timeSave;
    private LocalTime time;

    public TimePickPopWindow(Context context, PickedTime picker){
        super(context);
        this.context = context;
        this.pickedTime = picker;
        root = createPopupById(R.layout.timepickpopupwindow);
        bindView();
        bindListener();
        setContentView(root);
    }

    private void bindListener() {
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                LocalTime localTime = LocalTime.of(i, i1);
                time = LocalTime.of(i, i1, 0);
            }
        });

        timeSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(time != null){
                    pickedTime.setPickedTime(time);
                    TimePickPopWindow.this.dismiss();
                }
            }
        });
    }

    private void bindView() {
        timePicker = root.findViewById(R.id.time_picker);
        timeSave = root.findViewById(R.id.time_save);
    }

    @Override
    public Animation onCreateShowAnimation(){
        //创建弹窗出现动画，从下方展开
        return AnimationHelper.asAnimation().withTranslation(TranslationConfig.FROM_TOP).toShow();
    }

    public Animation onCreateDismissAnimation(){
        return AnimationHelper.asAnimation().withTranslation(TranslationConfig.TO_TOP).toDismiss();
    }
}
