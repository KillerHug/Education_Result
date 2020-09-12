package com.erpsonline.education_result;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Student_Change_Password extends Fragment {
    EditText oldPass,newPass,confirmPass;
    Button changePassBtn, drawerOpen;
    DrawerLayout drawerLayout;
    TextView msg,changePassMSG;
    String txtOld,txtNew,txtUserName;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.student_change_pass,container,false);
        oldPass=view.findViewById(R.id.oldPass);
        newPass=view.findViewById(R.id.newPass);
        confirmPass=view.findViewById(R.id.confirmPass);
        drawerOpen=view.findViewById(R.id.drawerOpen);
        drawerLayout=getActivity().findViewById(R.id.drawerLayout);
        msg = view. findViewById(R.id.errorMessage);
        changePassMSG=view.findViewById(R.id.changePassMSG);
        drawerOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        changePassBtn=view.findViewById(R.id.changePass);
        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkConfirm()|!checkOldPass()|!checkNewPass()|!checkConfirmPass())
                {
                    return;
                }
                changePassword();
            }
        });
        return view;
    }

    private boolean checkConfirm() {
        String txtNewPass,txtConfirmPass;
        txtNewPass=newPass.getText().toString().trim();
        txtConfirmPass=confirmPass.getText().toString().trim();
        if(!txtConfirmPass.equals(txtNewPass))
        {
            msg.setVisibility(View.VISIBLE);
            msg.setText("Confirm Password and New Password are not matched");
            return false;
        }
        return true;
    }
    private boolean checkOldPass() {
        String checkString = oldPass.getText().toString().trim();

        if (checkString.isEmpty()) {
            Toast.makeText(getContext(), "Field can not be Empty", Toast.LENGTH_SHORT).show();
            oldPass.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        return true;
    }
    private boolean checkNewPass() {
        String checkString = newPass.getText().toString().trim();
        String pattern="[0-9]+";
        if (checkString.isEmpty()) {
            Toast.makeText(getContext(), "Field can not be Empty", Toast.LENGTH_SHORT).show();
            newPass.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        else if(!checkString.matches(pattern))
        {
            Toast.makeText(getContext(), "You entered only numeric number.", Toast.LENGTH_SHORT).show();
            newPass.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        return true;
    }
    private boolean checkConfirmPass() {
        String checkString = confirmPass.getText().toString().trim();
        String pattern="[0-9]+";
        if (checkString.isEmpty()) {
            Toast.makeText(getContext(), "Field can not be Empty", Toast.LENGTH_SHORT).show();
            confirmPass.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        else if(!checkString.matches(pattern)) {
            Toast.makeText(getContext(), "You entered only numeric number.", Toast.LENGTH_SHORT).show();
            confirmPass.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        return true;
    }
    private void changePassword() {
        txtOld = oldPass.getText().toString().trim();
        txtNew = newPass.getText().toString().trim();
        String url = "http://hopeindia.biz/app/student.php";
        SessionManager sessionManager=new SessionManager(getContext());
        txtUserName=sessionManager.getUsername();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    String user_name,name;
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("success");
                    if (status.equals("verify")) {
                        blankField();
                        changePassMSG.setVisibility(View.VISIBLE);
                        changePassMSG.setText("Your Password successfully changed");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                changePassMSG.setVisibility(View.GONE);
                            }
                        },5000);
                    } else if (status.equals("noMatch")) {
                        msg.setVisibility(View.VISIBLE);
                        msg.setText("Password not found");
                        changePassMSG.setVisibility(View.GONE);
                    } else {

                    }
                } catch (JSONException e) {
                    Log.e("Message", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Message", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("action", "changePassword");
                param.put("oldPassword", txtOld);
                param.put("newPassword", txtNew);
                param.put("user_name", txtUserName);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    public void blankField()
    {
        oldPass.setText("");
        newPass.setText("");
        confirmPass.setText("");
    }
}
