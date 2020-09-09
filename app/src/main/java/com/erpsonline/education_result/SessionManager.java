package com.erpsonline.education_result;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_NAME="session";
    public  SessionManager(Context context)
    {
        sharedPreferences=context.getSharedPreferences(SHARED_NAME,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }
    public void saveSession(User user)
    {
        editor.putString("USER_NAME",user.getUser_id());
        editor.commit();
    }
    public void setLogin(boolean login)
    {
        editor.putBoolean("KEY_VALUE",login);
        editor.commit();
    }
    public boolean getLogin()
    {
        return sharedPreferences.getBoolean("KEY_VALUE",false);
    }
    public String getUserID()
    {
        return sharedPreferences.getString("USER_NAME",null);
    }
    public void removeSession()
    {
        editor.putString("USER_NAME","");
        editor.commit();
    }
}
