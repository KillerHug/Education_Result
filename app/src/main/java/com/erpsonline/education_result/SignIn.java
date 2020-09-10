package com.erpsonline.education_result;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {
    Button btnSignUp, btnSignIn;
    EditText txtId, txtPass;
    TextView msg;
    String strUser, strPass;
    SessionManager sessionManager;
    User user;
ConstraintLayout msgLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnSignUp = (Button) findViewById(R.id.goto_signup);
        btnSignIn = (Button) findViewById(R.id.signin_data);
        txtId = (EditText) findViewById(R.id.signId);
        txtPass = (EditText) findViewById(R.id.signPass);
        msg = (TextView) findViewById(R.id.errorMessage);
        msgLayout=(ConstraintLayout)findViewById(R.id.successMSG);
        SharedPreferences sharedPreferences = getSharedPreferences("MSG", MODE_PRIVATE);
        String msg = sharedPreferences.getString("pass_success", null);
        if (msg != null) {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.clear();
            editor.commit();
            msgLayout.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    msgLayout.setVisibility(View.GONE);
                }
            }, 3000);

        }
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this, SignUP.class);
                startActivity(intent);
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkId() | !checkPass()) {
                    //msgLayout.setVisibility(View.VISIBLE);
                    return;
                }
                LoginData();
            }
        });
        sessionManager=new SessionManager(this);
        boolean id=sessionManager.getLogin();
        if(id)
        {
            Intent intent = new Intent(SignIn.this, Fragment_container.class);
            startActivity(intent);
        }
    }

    private void LoginData() {
        strUser = txtId.getText().toString().trim();
        strPass = txtPass.getText().toString().trim();
        String url = "http://hopeindia.biz/app/student.php";
        final String[] status = new String[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("success");
                    if (status.equals("verify")) {
                        sessionManager = new SessionManager(SignIn.this);
                        user=new User(strUser);
                        user.setUser_name(strUser);
                        sessionManager.saveSession(user);
                        sessionManager.setLogin(true);
                        Intent intent = new Intent(SignIn.this, Fragment_container.class);
                        startActivity(intent);
                    } else if (status.equals("noMatch")) {
                        Log.e("Message", "Username or Password not matched");
                        msg.setVisibility(View.VISIBLE);
                        msg.setText("Username or Password not matched");
                    }
                    else {
                        blankField();
                        Log.e("Message", "Not Login");
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
                param.put("action", "signin");
                param.put("user_name", strUser);
                param.put("password", strPass);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public boolean checkId() {
        String checkString = txtId.getText().toString().trim();
        if (checkString.isEmpty()) {
            Toast.makeText(this, "Field can not be Empty", Toast.LENGTH_SHORT).show();
            txtId.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        return true;
    }

    public boolean checkPass() {
        String checkString = txtPass.getText().toString().trim();

        if (checkString.isEmpty()) {
            Toast.makeText(this, "Field can not be Empty", Toast.LENGTH_SHORT).show();
            txtPass.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        return true;
    }

    public void blankField() {
        txtId.setText("");
        txtPass.setText("");
    }
}