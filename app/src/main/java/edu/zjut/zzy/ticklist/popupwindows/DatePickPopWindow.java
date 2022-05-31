package edu.zjut.zzy.ticklist.popupwindows;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.widget.DatePicker;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;

import edu.zjut.zzy.ticklist.CInterface.PickedDate;
import edu.zjut.zzy.ticklist.R;
import razerdp.basepopup.BasePopupWindow;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

public class DatePickPopWindow extends BasePopupWindow {
    private Context context;
    private View root;
    private DatePicker datePicker;

    private PickedDate pickedDate;

    private void bindView(){
        datePicker = root.findViewById(R.id.date_picker);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void bindListener(){

        //选中后关闭弹窗并传递时间
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                LocalDate date = LocalDate.of(i, i1 + 1, i2);
                pickedDate.setPickedDate(date);
                DatePickPopWindow.this.dismiss();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public DatePickPopWindow(Context context, PickedDate pickeddate){
        super(context);
        this.context = context;
        this.pickedDate = pickeddate;
        root = createPopupById(R.layout.datepickpopupwindow);
        bindView();
        bindListener();
        setContentView(root);
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
