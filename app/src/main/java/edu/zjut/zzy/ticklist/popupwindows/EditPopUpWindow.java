package edu.zjut.zzy.ticklist.popupwindows;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import edu.zjut.zzy.ticklist.CInterface.HandToDo;
import edu.zjut.zzy.ticklist.CInterface.PickedDate;
import edu.zjut.zzy.ticklist.CInterface.PickedTime;
import edu.zjut.zzy.ticklist.R;
import edu.zjut.zzy.ticklist.SP.SettingManager;
import edu.zjut.zzy.ticklist.android.AndroidState;
import edu.zjut.zzy.ticklist.android.CalendarAccount;
import edu.zjut.zzy.ticklist.bean.ToDo;
import edu.zjut.zzy.ticklist.dao.DBOpenHelper;
import edu.zjut.zzy.ticklist.dao.SQLiteDao;
import razerdp.basepopup.BasePopupWindow;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

public class EditPopUpWindow extends BasePopupWindow implements PickedDate, PickedTime {
    private static final String TAG = EditPopUpWindow.class.getSimpleName();

    private Context context;
    private View root;
    private ToDo todo;
    private int position;
    private HandToDo handToDo;

    //ui控件
    //待办内容 保存按钮
    private TextView saveButton;
    private EditText todoContext;

    // 日期
    private int dateFlag = 1;
    private TextView todayButton;
    private TextView tomorrowButton;
    private TextView noDateButton;
    private TextView customerDateButton;

    //计时方式
    private Switch aSwitch;
    private LinearLayout timerLayout;
    private int timeFlag = 1;
    private TextView timeButton1;
    private TextView timeButton2;
    private EditText timeButton3;

    //重复方式
    private int repeatFlag = 1;
    private TextView noRepeatButton;
    private TextView dailyButton;
    private TextView weeklyButton;
    private EditText customerRepeatButton;

    //闹钟
    private ImageButton clockButton;
    private TextView clockText;
    private EditText desctiptionEditText;


    private void bindView(){
        aSwitch = root.findViewById(R.id.switchbutton_countimer);
        timerLayout = root.findViewById(R.id.timerlength_view);
        saveButton = root.findViewById(R.id.save_button);
        todoContext = root.findViewById(R.id.todocontext);

        // 日期
        todayButton = root.findViewById(R.id.today);
        tomorrowButton = root.findViewById(R.id.tomorrow);
        noDateButton = root.findViewById(R.id.nodate);
        customerDateButton = root.findViewById(R.id.customdate);

        timeButton1 = root.findViewById(R.id.time1);
        timeButton2 = root.findViewById(R.id.time2);
        timeButton3 = root.findViewById(R.id.time3);

        //重复方式
        noRepeatButton = root.findViewById(R.id.norepeat);
        dailyButton = root.findViewById(R.id.daily);
        weeklyButton = root.findViewById(R.id.weekly);
        customerRepeatButton = root.findViewById(R.id.customerday);

        //闹钟
        clockButton = root.findViewById(R.id.clockset);
        clockText = root.findViewById(R.id.clocktext);
        desctiptionEditText = root.findViewById(R.id.description);

    }

    private void repeatChooser(){
        switch (repeatFlag){
            case 1:
                noRepeatButton.setBackgroundResource(R.drawable.button_normal_selected);
                noRepeatButton.setTextColor(Color.WHITE);
                dailyButton.setBackgroundResource(R.drawable.button_normal_unselect);
                dailyButton.setTextColor(Color.parseColor("#6C736F"));
                weeklyButton.setBackgroundResource(R.drawable.button_normal_unselect);
                weeklyButton.setTextColor(Color.parseColor("#6C736F"));
                customerRepeatButton.setBackgroundResource(R.drawable.button_normal_unselect);
                customerRepeatButton.setTextColor(Color.parseColor("#6C736F"));
                break;
            case 2:
                noRepeatButton.setBackgroundResource(R.drawable.button_normal_unselect);
                noRepeatButton.setTextColor(Color.parseColor("#6C736F"));
                dailyButton.setBackgroundResource(R.drawable.button_normal_selected);
                dailyButton.setTextColor(Color.WHITE);
                weeklyButton.setBackgroundResource(R.drawable.button_normal_unselect);
                weeklyButton.setTextColor(Color.parseColor("#6C736F"));
                customerRepeatButton.setBackgroundResource(R.drawable.button_normal_unselect);
                customerRepeatButton.setTextColor(Color.parseColor("#6C736F"));
                break;

            case 3:
                noRepeatButton.setBackgroundResource(R.drawable.button_normal_unselect);
                noRepeatButton.setTextColor(Color.parseColor("#6C736F"));
                dailyButton.setBackgroundResource(R.drawable.button_normal_unselect);
                dailyButton.setTextColor(Color.parseColor("#6C736F"));
                weeklyButton.setBackgroundResource(R.drawable.button_normal_selected);
                weeklyButton.setTextColor(Color.WHITE);
                customerRepeatButton.setBackgroundResource(R.drawable.button_normal_unselect);
                customerRepeatButton.setTextColor(Color.parseColor("#6C736F"));
                break;
            case 4:
                noRepeatButton.setBackgroundResource(R.drawable.button_normal_unselect);
                noRepeatButton.setTextColor(Color.parseColor("#6C736F"));
                dailyButton.setBackgroundResource(R.drawable.button_normal_unselect);
                dailyButton.setTextColor(Color.parseColor("#6C736F"));
                weeklyButton.setBackgroundResource(R.drawable.button_normal_unselect);
                weeklyButton.setTextColor(Color.parseColor("#6C736F"));
                customerRepeatButton.setBackgroundResource(R.drawable.button_normal_selected);
                customerRepeatButton.setTextColor(Color.WHITE);
                break;
        }
    }

    private void timeChooser(){
        switch (timeFlag){
            case 1:
                timeButton1.setBackgroundResource(R.drawable.button_normal_selected);
                timeButton1.setTextColor(Color.WHITE);
                timeButton2.setBackgroundResource(R.drawable.button_normal_unselect);
                timeButton2.setTextColor(Color.parseColor("#6C736F"));
                timeButton3.setBackgroundResource(R.drawable.button_normal_unselect);
                timeButton3.setTextColor(Color.parseColor("#6C736F"));
                break;
            case 2:
                timeButton1.setBackgroundResource(R.drawable.button_normal_unselect);
                timeButton1.setTextColor(Color.parseColor("#6C736F"));
                timeButton2.setBackgroundResource(R.drawable.button_normal_selected);
                timeButton2.setTextColor(Color.WHITE);
                timeButton3.setBackgroundResource(R.drawable.button_normal_unselect);
                timeButton3.setTextColor(Color.parseColor("#6C736F"));
                break;
            case 3:
                timeButton1.setBackgroundResource(R.drawable.button_normal_unselect);
                timeButton1.setTextColor(Color.parseColor("#6C736F"));
                timeButton2.setBackgroundResource(R.drawable.button_normal_unselect);
                timeButton2.setTextColor(Color.parseColor("#6C736F"));
                timeButton3.setBackgroundResource(R.drawable.button_normal_selected);
                timeButton3.setTextColor(Color.WHITE);
                break;
        }
    }

    private void dateChooser(){
        switch (dateFlag){
            case 1:
                todayButton.setBackgroundResource(R.drawable.button_normal_selected);
                todayButton.setTextColor(Color.WHITE);
                tomorrowButton.setBackgroundResource(R.drawable.button_normal_unselect);
                tomorrowButton.setTextColor(Color.parseColor("#6C736F"));
                noDateButton.setBackgroundResource(R.drawable.button_normal_unselect);
                noDateButton.setTextColor(Color.parseColor("#6C736F"));
                customerDateButton.setBackgroundResource(R.drawable.button_normal_unselect);
                customerDateButton.setTextColor(Color.parseColor("#6C736F"));
                break;
            case 2:
                todayButton.setBackgroundResource(R.drawable.button_normal_unselect);
                todayButton.setTextColor(Color.parseColor("#6C736F"));
                tomorrowButton.setBackgroundResource(R.drawable.button_normal_selected);
                tomorrowButton.setTextColor(Color.WHITE);
                noDateButton.setBackgroundResource(R.drawable.button_normal_unselect);
                noDateButton.setTextColor(Color.parseColor("#6C736F"));
                customerDateButton.setBackgroundResource(R.drawable.button_normal_unselect);
                customerDateButton.setTextColor(Color.parseColor("#6C736F"));
                break;
            case 3:
                todayButton.setBackgroundResource(R.drawable.button_normal_unselect);
                todayButton.setTextColor(Color.parseColor("#6C736F"));
                tomorrowButton.setBackgroundResource(R.drawable.button_normal_unselect);
                tomorrowButton.setTextColor(Color.parseColor("#6C736F"));
                noDateButton.setBackgroundResource(R.drawable.button_normal_selected);
                noDateButton.setTextColor(Color.WHITE);
                customerDateButton.setBackgroundResource(R.drawable.button_normal_unselect);
                customerDateButton.setTextColor(Color.parseColor("#6C736F"));
                break;
            case 4:
                todayButton.setBackgroundResource(R.drawable.button_normal_unselect);
                todayButton.setTextColor(Color.parseColor("#6C736F"));
                tomorrowButton.setBackgroundResource(R.drawable.button_normal_unselect);
                tomorrowButton.setTextColor(Color.parseColor("#6C736F"));
                noDateButton.setBackgroundResource(R.drawable.button_normal_unselect);
                noDateButton.setTextColor(Color.parseColor("#6C736F"));
                customerDateButton.setBackgroundResource(R.drawable.button_normal_selected);
                customerDateButton.setTextColor(Color.WHITE);
                break;
        }
    }

    private void bindListener(){
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    timerLayout.setVisibility(View.VISIBLE);
                    todo.setTimerMode(ToDo.PASSIVE);
                }
                else {
                    timerLayout.setVisibility(View.GONE);
                    todo.setTimerMode(ToDo.POSITIVE);
                }
            }
        });

        todayButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                dateFlag = 1;
                dateChooser();
                todo.setDate(LocalDate.now());
            }
        });

        tomorrowButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                dateFlag = 2;
                dateChooser();
                todo.setDate(LocalDate.now().plusDays(1));
            }
        });

        noDateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                dateFlag = 3;
                dateChooser();
                todo.setDate(null);
            }
        });

        customerDateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                dateFlag = 4;
                dateChooser();
                DatePickPopWindow datePicker = new DatePickPopWindow(context, EditPopUpWindow.this);
                datePicker.setBlurBackgroundEnable(true);
                datePicker.setKeyboardAdaptive(true);
                //设置在底部弹出
                datePicker.setPopupGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
                datePicker.showPopupWindow();
            }
        });


        timeButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeFlag = 1;
                timeChooser();
                todo.setFocusTime(25);
            }
        });

        timeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeFlag = 2;
                timeChooser();
                todo.setFocusTime(45);
            }
        });


        timeButton3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    timeFlag = 3;
                    timeChooser();
                }
            }
        });

        noRepeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repeatFlag = 1;
                repeatChooser();
                todo.setRepeatedWay(ToDo.NOREPEATED);
            }
        });

        dailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repeatFlag = 2;
                repeatChooser();
                todo.setRepeatedWay(ToDo.DAYLY);
            }
        });

        weeklyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repeatFlag = 3;
                repeatChooser();
                todo.setRepeatedWay(ToDo.WEEKLY);
            }
        });

        customerRepeatButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                repeatFlag = 4;
                repeatChooser();
            }
        });

        clockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickPopWindow timePickPopWindow = new TimePickPopWindow(context, EditPopUpWindow.this);
                timePickPopWindow.setBlurBackgroundEnable(true);
                timePickPopWindow.setPopupGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
                timePickPopWindow.showPopupWindow();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(todoContext.getText() != null){
                    todo.setContent(todoContext.getText().toString());
                }
                if(desctiptionEditText.getText() != null){
                    todo.setDescription(desctiptionEditText.getText().toString());
                }
                if(timeFlag == 3 && timeButton3.getText() != null){
                    todo.setFocusTime(Integer.parseInt(timeButton3.getText().toString()));
                }
                if(repeatFlag == 4 && customerRepeatButton.getText() != null){
                    todo.setRepeatedWay(Integer.parseInt(customerRepeatButton.getText().toString()));
                }

                if(position != -1){
                    //已有的修改
                    CalendarAccount calendarAccount = AndroidState.CalendarManager.searchAccount(context);
                    if(calendarAccount == null){
                        AndroidState.CalendarManager.createCalendar(context);
                        calendarAccount = AndroidState.CalendarManager.searchAccount(context);
                    }
                    if(todo.getEventID() != 0){
                        AndroidState.CalendarManager.updateEvents(context, todo);
                    }else {
                        AndroidState.CalendarManager.insertEvents(context, calendarAccount.getCalID(), todo);
                    }
                    handToDo.setToDo(todo, position);
                    DBOpenHelper dbOpenHelper = new DBOpenHelper(getContext());
                    SQLiteDao sqLiteDao = new SQLiteDao(dbOpenHelper);
                    sqLiteDao.updateToDo(todo);
                    /*
                    *
                    *
                    * 需要修改的bug
                    *
                    *
                    *
                    *   */
                }
                else{
                    //新建
                    if(todo.getRepeatedWay() != 0){
                        /* 重复事件添加日历
                        * 默认方式为只添加第一天，需要在设置里修改设置
                        *  */
                        SettingManager settingManager = new SettingManager(context);
                        if(settingManager.getRepeatedEventsCalendarSetting()){
                            CalendarAccount calendarAccount = AndroidState.CalendarManager.searchAccount(context);
                            if(calendarAccount == null){
                                AndroidState.CalendarManager.createCalendar(context);
                                calendarAccount = AndroidState.CalendarManager.searchAccount(context);
                            }
                            todo.setEventID(AndroidState.CalendarManager.insertEvents(context, calendarAccount.getCalID(), todo));
                        }

                        LocalDate temp = todo.getDate();
                        DBOpenHelper dbOpenHelper = new DBOpenHelper(getContext());
                        SQLiteDao sqLiteDao = new SQLiteDao(dbOpenHelper);
                        //创建一个重复组号
                        int groupid = sqLiteDao.getMaxKinLinGroupId() + 1;
                        int id = sqLiteDao.getMaxKinLinId() + 1;
                        todo.setGroupId(groupid);
                        while (temp.isBefore(LocalDate.now().plusMonths(3))){
                            todo.setKiLinId(id);
                            sqLiteDao.insertToDo(todo);
                            temp = temp.plusDays(todo.getRepeatedWay());
                            todo.setDate(temp);
                            id++;
                        }
                    }else {
                        // 插入日历
                        CalendarAccount calendarAccount = AndroidState.CalendarManager.searchAccount(context);
                        if(calendarAccount == null){
                            AndroidState.CalendarManager.createCalendar(context);
                            calendarAccount = AndroidState.CalendarManager.searchAccount(context);
                        }
                        todo.setEventID(AndroidState.CalendarManager.insertEvents(context, calendarAccount.getCalID(), todo));
                        DBOpenHelper dbOpenHelper = new DBOpenHelper(getContext());
                        SQLiteDao sqLiteDao = new SQLiteDao(dbOpenHelper);
                        /* 创建一个重复组号和唯一id号 */
                        int groupid = sqLiteDao.getMaxKinLinGroupId() + 1;
                        int id = sqLiteDao.getMaxKinLinId() + 1;
                        todo.setKiLinId(id);
                        todo.setGroupId(groupid);
                        sqLiteDao.insertToDo(todo);
                        handToDo.setToDo(todo, position);
                    }
                }
                EditPopUpWindow.this.dismiss();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public EditPopUpWindow(Context context, HandToDo handToDo){
        super(context);
        this.context = context;
        this.handToDo = handToDo;
        this.position = -1;
        todo = new ToDo();
        todo.setDate(LocalDate.now());
        todo.setTimerMode(ToDo.PASSIVE);
        todo.setFocusTime(25);
        todo.setRepeatedWay(ToDo.NOREPEATED);
        root = createPopupById(R.layout.editpopwindows);
        bindView();
        bindListener();
        setContentView(root);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public EditPopUpWindow(Context context, ToDo data, int position, HandToDo handToDo){
        super(context);
        this.context = context;
        this.todo = data.clone();
        this.position = position;
        this.handToDo = handToDo;
        root = createPopupById(R.layout.editpopwindows);
        bindView();
        bindListener();
        loadData(data);
        setContentView(root);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadData(ToDo data) {
        todoContext.setText(data.getContent());
        LocalDate now = LocalDate.now();
        if(todo.getDate() == null){
            dateFlag = 3;
        }
        else if(now.isEqual(todo.getDate())){
            dateFlag = 1;
        }
        else if (now.isEqual(todo.getDate().plusDays(-1))){
            dateFlag = 2;
        }else {
            dateFlag = 4;
            customerDateButton.setText(todo.getDate().getYear() + "." + todo.getDate().getMonthValue() + "." + todo.getDate().getDayOfMonth());
        }
        dateChooser();

        if (todo.getTimerMode() == ToDo.POSITIVE){
            timerLayout.setVisibility(View.GONE);
            aSwitch.setChecked(false);
        }
        if(todo.getFocusTime() == 25){
            timeFlag = 1;
        }else if(todo.getFocusTime() == 45){
            timeFlag = 2;
        }else{
            timeFlag = 3;
            timeButton3.setText(String.valueOf(todo.getFocusTime()));
        }
        timeChooser();

        if(todo.getDescription() != null){
            desctiptionEditText.setText(todo.getDescription());
        }

        if(todo.getTime() != null){
            clockText.setVisibility(View.VISIBLE);
            clockText.setText(todo.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        }

        if(todo.getRepeatedWay() != ToDo.NOREPEATED){
            switch (todo.getRepeatedWay()){
                case ToDo.DAYLY:
                    repeatFlag = 2;
                    break;
                case ToDo.WEEKLY:
                    repeatFlag = 3;
                    break;
                default:
                    repeatFlag = 4;
                    customerRepeatButton.setText(String.valueOf(todo.getRepeatedWay()));
                    break;
            }
            repeatChooser();
        }

    }

    @Override
    public Animation onCreateShowAnimation(){
        //创建弹窗出现动画，从下方展开
        return AnimationHelper.asAnimation().withTranslation(TranslationConfig.FROM_BOTTOM).toShow();
    }

    public Animation onCreateDismissAnimation(){
        return AnimationHelper.asAnimation().withTranslation(TranslationConfig.TO_BOTTOM).toDismiss();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void setPickedDate(LocalDate pickedDate) {
        todo.setDate(pickedDate);
        customerDateButton.setText(todo.getDate().getYear() + "." + todo.getDate().getMonthValue() + "." + todo.getDate().getDayOfMonth());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void setPickedTime(LocalTime time) {
        todo.setTime(LocalDateTime.of(todo.getDate().getYear(), todo.getDate().getMonthValue(), todo.getDate().getDayOfMonth(), time.getHour(), time.getMinute(), 0));
        clockText.setText(todo.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        clockText.setVisibility(View.VISIBLE);
        Log.d(TAG, todo.getTime().toString());
    }
}
