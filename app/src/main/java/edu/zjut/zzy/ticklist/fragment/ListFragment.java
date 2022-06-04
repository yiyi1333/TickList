package edu.zjut.zzy.ticklist.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.zjut.zzy.ticklist.CInterface.HandToDo;
import edu.zjut.zzy.ticklist.CInterface.SwitchFragment;
import edu.zjut.zzy.ticklist.MainActivity;
import edu.zjut.zzy.ticklist.R;
import edu.zjut.zzy.ticklist.activity.WebActivity;
import edu.zjut.zzy.ticklist.adapter.ToDoAdapter;
import edu.zjut.zzy.ticklist.bean.ToDo;
import edu.zjut.zzy.ticklist.custom.ItemTouchHelperCallback;
import edu.zjut.zzy.ticklist.custom.SpaceItemDecoration;
import edu.zjut.zzy.ticklist.dao.DBOpenHelper;
import edu.zjut.zzy.ticklist.dao.SQLiteDao;
import edu.zjut.zzy.ticklist.popupwindows.EditPopUpWindow;

public class ListFragment extends Fragment implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener,
        HandToDo {
    private static final String TAG = ListFragment.class.getSimpleName();
    private View root;

    private int year;
    private int month;
    private int day;
    private Map<String, Calendar> map;
    private ArrayList<ToDo> toDoArrayList;
    private ArrayList<ToDo> monthDate;
    private ToDoAdapter toDoAdapter;

    //ui控件
    private SwipeRefreshLayout swipeRefreshLayout;
    private CalendarLayout calendarLayout;
    private TextView dateView;
    private ImageView moreOptions;
    private FloatingActionButton add;
    private CalendarView calendarView;
    private RecyclerView recyclerView;

    private SwitchFragment switchFragment;

    public ListFragment(SwitchFragment switchFragment) {
        this.switchFragment = switchFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(root == null){
            Log.d(TAG, "---------------oncreatedView");
            root = inflater.inflate(R.layout.fragment_list, container, false);
        }
        loadData();
        bindView();
        bindListener();
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        toDoAdapter = new ToDoAdapter(root.getContext(), toDoArrayList);
        toDoAdapter.setSwitchFragment(switchFragment);
        recyclerView.setAdapter(toDoAdapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(30));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(toDoAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);


        return root;
    }

    @SuppressLint("SetTextI18n")
    private void init(){
        dateView.setText(month + "月" + day + "日");
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        calendarView.setSchemeDate(map);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadData(){
        LocalDate today = LocalDate.now();
        year = today.getYear();
        month = today.getMonthValue();
        day = today.getDayOfMonth();
        toDoArrayList = new ArrayList<>();
        DBOpenHelper dbOpenHelper = new DBOpenHelper(getContext());
        SQLiteDao sqLiteDao = new SQLiteDao(dbOpenHelper);
        toDoArrayList = sqLiteDao.getTODoListRange(today);
        monthDate = sqLiteDao.getToDoMonth(year, month);
        Log.d(TAG, monthDate.toString());
        map = new HashMap<>();
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = LocalDate.of(year + month / 12, (month + 1) % 12, 1);
        int i = 0;
        while (firstDay.isBefore(lastDay)){
            int finished = 0;
            int total = 0;
            while (i < monthDate.size() && monthDate.get(i).getDate().isEqual(firstDay)){
                total++;
                if(monthDate.get(i).isFinish()){
                    finished++;
                }
                i++;
            }
            if(total != 0){
                map.put(getSchemeCalendar(firstDay.getYear(), firstDay.getMonthValue(), firstDay.getDayOfMonth(), 0xFF40db25, String.valueOf(finished * 100/ total)).toString(),
                        getSchemeCalendar(firstDay.getYear(), firstDay.getMonthValue(), firstDay.getDayOfMonth(), 0xFF40db25, String.valueOf(finished * 100/ total)));
            }

            firstDay = firstDay.plusDays(1);
        }

    }

    private void bindView(){
        add = root.findViewById(R.id.addTicket);
        dateView = root.findViewById(R.id.dateText);
        moreOptions = root.findViewById(R.id.moreoptions);
        calendarView = root.findViewById(R.id.calendarView);
        calendarLayout = root.findViewById(R.id.calendarLayout);
        recyclerView = root.findViewById(R.id.recyclerView);
        swipeRefreshLayout = root.findViewById(R.id.refreshView);
    }

    private void bindListener(){
        moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WebActivity.class);
                startActivity(intent);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                EditPopUpWindow editPopUpWindow = new EditPopUpWindow(root.getContext(), ListFragment.this);
                editPopUpWindow.setBlurBackgroundEnable(true);
                editPopUpWindow.setKeyboardAdaptive(true);
                //设置在底部弹出
                editPopUpWindow.setPopupGravity(Gravity.CENTER|Gravity.BOTTOM);
                editPopUpWindow.showPopupWindow();
            }
        });

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!calendarLayout.isExpand()){
                    calendarLayout.expand();
                    return;
                }
                calendarView.showYearSelectLayout(year);
                dateView.setText(String.valueOf(year));
            }
        });


        calendarView.setOnCalendarSelectListener(this);
        calendarView.setOnYearChangeListener(this);
        //下拉刷新背景色
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.Huawei_White);
        //下拉进度主题色
        swipeRefreshLayout.setColorSchemeResources(R.color.Mask_Green3);
        //设置下拉刷新事件
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //开始刷新
                //创建线程进行数据同步
                new Thread(){
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run(){
                        super.run();
                        DBOpenHelper dbOpenHelper = new DBOpenHelper(getContext());
                        SQLiteDao sqLiteDao = new SQLiteDao(dbOpenHelper);

                        Message message1 = listHandler.obtainMessage(1);
                        message1.obj = sqLiteDao.getTODoListRange(LocalDate.of(year, month, day));;
                        listHandler.sendMessage(message1);
                        Message message2 = listHandler.obtainMessage(2);
                        message2.obj = sqLiteDao.getToDoMonth(year, month);
                        listHandler.sendMessage(message2);
                    }
                }.start();

            }
        });

    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();

        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }


    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        dateView.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        day = calendar.getDay();
        month = calendar.getMonth();
        year = calendar.getYear();
        DBOpenHelper dbOpenHelper = new DBOpenHelper(getContext());
        SQLiteDao sqLiteDao = new SQLiteDao(dbOpenHelper);
        toDoArrayList = sqLiteDao.getTODoListRange(LocalDate.of(year, month, day));
        System.out.println(LocalDate.of(year, month, day).toString());
        toDoAdapter.setData(toDoArrayList);
    }

    @Override
    public void onYearChange(int year) {
        dateView.setText(String.valueOf(year));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void setToDo(ToDo toDo, int i) {
        toDoArrayList.add(toDo);
        toDoAdapter.insertToDo(toDoArrayList.size());
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void refreshMonthView(ArrayList<ToDo> monthdata){
        if(!map.isEmpty()){
            map.clear();
        }
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = LocalDate.of(year + month / 12, (month + 1) % 12, 1);
        int i = 0;
        while (firstDay.isBefore(lastDay)){
            int finished = 0;
            int total = 0;
            while (i < monthDate.size() && monthDate.get(i).getDate().isEqual(firstDay)){
                total++;
                if(monthDate.get(i).isFinish()){
                    finished++;
                }
                i++;
            }
            if(total != 0){
                map.put(getSchemeCalendar(firstDay.getYear(), firstDay.getMonthValue(), firstDay.getDayOfMonth(), 0xFF40db25, String.valueOf(finished * 100/ total)).toString(),
                        getSchemeCalendar(firstDay.getYear(), firstDay.getMonthValue(), firstDay.getDayOfMonth(), 0xFF40db25, String.valueOf(finished * 100/ total)));
            }

            firstDay = firstDay.plusDays(1);
        }
    }


    private Handler listHandler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    toDoArrayList = (ArrayList<ToDo>) msg.obj;
                    toDoAdapter.setData(toDoArrayList);
                    Log.d(TAG, "refresh todaydata" + toDoArrayList.toString());
                    break;
                case 2:
                    monthDate = (ArrayList<ToDo>) msg.obj;
                    Log.d(TAG, "refresh monthdata" + monthDate.toString());
                    refreshMonthView(monthDate);
                    swipeRefreshLayout.setRefreshing(false);
                    break;

            }
        }
    };
}