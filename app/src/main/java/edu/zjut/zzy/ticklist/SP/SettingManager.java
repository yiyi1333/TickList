package edu.zjut.zzy.ticklist.SP;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingManager {
    private SharedPreferences sp;
    public SettingManager(Context context){
        sp = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
    }

    //获取自动登录配置
    public boolean getLoginAutoSetting(){
        return sp.getBoolean("loginAuto", false);
    }
}
