package edu.zjut.zzy.ticklist.SP;

import android.content.Context;
import android.content.SharedPreferences;

public class StateManager {
    private SharedPreferences sp;
    public StateManager(Context context){
        sp = context.getSharedPreferences("state", Context.MODE_PRIVATE);
    }

    public boolean getBufferedState(){
        return sp.getBoolean("bufferedUnload", false);
    }

    public void setBufferedState(Boolean flag){
        sp.edit().putBoolean("bufferedUnload", flag).apply();
    }
}
