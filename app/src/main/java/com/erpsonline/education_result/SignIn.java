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
    ConstraintLayout msgLayout;
    TextView msg;
    String strUser, strPass;
    SessionManager sessionManager;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        msgLayout = (ConstraintLayout) findViewById(R.id.successMSG);
        btnSignUp = (Button) findViewById(R.id.goto_signup);
        btnSignIn = (Button) findViewById(R.id.signin_data);
        txtId = (EditText) findViewById(R.id.signId);
        txtPass = (EditText) findViewById(R.id.signPass);
        msg = (TextView) findViewById(R.id.errorMessage);
        SharedPreferences sharedPreferences = getSharedPreferences("MY Success", MODE_PRIVATE);
        String msg = sharedPreferences.getString("pass_success", null);
        if (msg != null) {
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
        final String[] temp = {null};
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("info");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String status = jsonObject1.getString("success");
                        //String status=temp[0].toString();
                        if (status.equals("verify")) {
                            String id = jsonObject1.getString("user_name");
                            Log.e("User Id", id);
                            sessionManager = new SessionManager(SignIn.this);
                            user=new User(id);
                            sessionManager.saveSession(user);
                            sessionManager.setLogin(true);
                            Intent intent = new Intent(SignIn.this, Fragment_container.class);
                            startActivity(intent);
                        } else if (status.equals("noMatchPass")) {
                            Log.e("Message", "Password not matched");
                            msg.setVisibility(View.VISIBLE);
                            msg.setText("Password not matched");
                        } else if (status.equals("noMatchId")) {
                            Log.e("Message", "ID or Mobile no. not matched");
                            msg.setVisibility(View.VISIBLE);
                            msg.setText("ID or Mobile no. not matched");
                        } else {
                            blankField();
                            Log.e("Message", "Not Login");
                        }
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
                param.put("id", strUser);
                param.put("pass", strPass);
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