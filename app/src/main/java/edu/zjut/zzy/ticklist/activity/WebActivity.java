package edu.zjut.zzy.ticklist.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.zjut.zzy.ticklist.CInterface.PickedDate;
import edu.zjut.zzy.ticklist.PDFReader.ZJUTCourseReader;
import edu.zjut.zzy.ticklist.R;
import edu.zjut.zzy.ticklist.android.AndroidState;
import edu.zjut.zzy.ticklist.android.CalendarAccount;
import edu.zjut.zzy.ticklist.bean.Course;
import edu.zjut.zzy.ticklist.bean.ToDo;
import edu.zjut.zzy.ticklist.dao.DBOpenHelper;
import edu.zjut.zzy.ticklist.dao.SQLiteDao;
import edu.zjut.zzy.ticklist.popupwindows.FirstWeekDatePopWindow;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WebActivity extends AppCompatActivity implements PickedDate {
    private static final String TAG = WebActivity.class.getSimpleName();
    @BindView(R.id.webView)
    public WebView webView;
    @BindView(R.id.download_button)
    public FloatingActionButton downloadButton;
    private WebSettings webSettings;
    private FirstWeekDatePopWindow firstWeekDatePopWindow;

    private LocalDate date;
    private ArrayList<Course> courseArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        //访问网页
        webView.loadUrl("http://www.gdjw.zjut.edu.cn/jwglxt/xtgl/login_slogin.html");
        webSettings = webView.getSettings();
        initWebSettings();
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.evaluateJavascript("document.getElementsByTagName('html')[0].innerHTML;", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.d(TAG, s);
                        //转义
                        s = s.replace("\\u003C", "<").replace("\\", "");
                        ZJUTCourseReader zjutCourseReader = new ZJUTCourseReader();
                        courseArrayList = zjutCourseReader.parseCourse(s);
                    }
                });
                //弹出弹窗
                firstWeekDatePopWindow = new FirstWeekDatePopWindow(WebActivity.this.getApplicationContext(), WebActivity.this);
                firstWeekDatePopWindow.setBlurBackgroundEnable(true);
                firstWeekDatePopWindow.setKeyboardAdaptive(true);
                firstWeekDatePopWindow.setPopupGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER);
                firstWeekDatePopWindow.showPopupWindow();
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    private void initWebSettings(){
        //启用javascript;
        webSettings.setJavaScriptEnabled(true);
        //缩放到屏幕大小
        webSettings.setLoadWithOverviewMode(true);
        //支持缩放
        webSettings.setSupportZoom(true);
        //设置内置缩放控件
        webSettings.setBuiltInZoomControls(true);
        //隐藏原生缩放控件
        webSettings.setDisplayZoomControls(false);
        //关闭缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //可以访问文件
        webSettings.setAllowFileAccess(true);
        //支持js打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void setPickedDate(LocalDate pickedDate) {
        date = pickedDate;
        Log.d(TAG, pickedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        Course demo = courseArrayList.get(1);
//        System.out.println(demo);
        //写入日历，创建ToDo
        CalendarAccount calendarAccount = AndroidState.CalendarManager.searchAccount(getApplicationContext());
        if(calendarAccount == null){
            AndroidState.CalendarManager.createCalendar(getApplicationContext());
            calendarAccount = AndroidState.CalendarManager.searchAccount(getApplicationContext());
        }
        for ( Course d: courseArrayList) {
            ArrayList<ToDo> toDoArrayList = AndroidState.CalendarManager.insertCourse(getApplicationContext(), calendarAccount.getCalID(), d, date);
            System.out.println(toDoArrayList);
            //将todolist写入sqlite
            DBOpenHelper dbOpenHelper = new DBOpenHelper(getApplicationContext());
            SQLiteDao sqLiteDao = new SQLiteDao(dbOpenHelper);
            for (ToDo item: toDoArrayList) {
                sqLiteDao.insertToDo(item);
            }
        }
    }
}