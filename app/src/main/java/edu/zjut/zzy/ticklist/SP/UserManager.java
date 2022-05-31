package edu.zjut.zzy.ticklist.SP;
/*
*
* UserManager 对用户的个人数据（本地）进行管理包括用户的登录信息等
*
*
*  */

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {
    private SharedPreferences sp;

    public UserManager(Context context) {
        sp = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
    }

    /* 若不存在UserID则返回-1 */
    public int getUserId(){
        return sp.getInt("userId", -1);
    }

    /* 用户密码 */
    public String getUserPassword(){
        return sp.getString("userPassword", "12345678");
    }

    /* 用户名 */
    public String getUserName(){
        return sp.getString("userName", "admin");
    }

    /* 电子邮箱 */
    public String getEmail(){
        return sp.getString("UserEmail", null);
    }

    public int getToDoId(){
        return sp.getInt("UserToDoId", 0);
    }

    public void setToDoId(int id){
        sp.edit().putInt("UserToDoId", id).apply();
    }


}
