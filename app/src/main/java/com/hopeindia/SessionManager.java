package com.hopeindia;

import android.content.Context;
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
        editor.putString("PAID",user.getPaid());
        editor.putString("EMAIL",user.getEmail());
        editor.putString("ADDRESS",user.getAddress());
        editor.commit();
    }
    public void saveEmail_Address(User user)
    {
        editor.putString("EMAIL",user.getEmail());
        editor.putString("ADDRESS",user.getAddress());
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
    public String getPaid()
    {
        return sharedPreferences.getString("PAID",null);
    }
    public String getEmail()
    {
        return sharedPreferences.getString("EMAIL",null);
    }
    public String getAddress()
    {
        return sharedPreferences.getString("ADDRESS",null);
    }
    public void removeSession()
    {
        editor.putString("USER_NAME","");
        editor.clear();
        editor.commit();
    }
}
