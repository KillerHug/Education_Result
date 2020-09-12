package com.erpsonline.education_result;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_NAME="session";
    Context context;
    public  SessionManager(Context context)
    {
        sharedPreferences=context.getSharedPreferences(SHARED_NAME,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }
    public void saveSession(User user)
    {
        editor.putString("USER_NAME",user.getUser_name());
        editor.putString("NAME",user.getName());
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
    public String getUsername()
    {
        return sharedPreferences.getString("USER_NAME",null);
    }
    public String getName()
    {
        return sharedPreferences.getString("NAME",null);
    }
    public void removeSession()
    {
        editor.putString("USER_NAME","");
        editor.clear();
        editor.commit();
    }
}
