package edu.zjut.zzy.ticklist.popupwindows;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.widget.DatePicker;
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
    private Context context;
    private View root;
    private TimePicker timePicker;
    private PickedTime pickedTime;

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
                pickedTime.setPickedTime(localTime);
//                TimePickPopWindow.this.dismiss();
            }
        });
    }

    private void bindView() {
        timePicker = root.findViewById(R.id.time_picker);
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
