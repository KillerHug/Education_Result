package com.hopeindia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.IntentCompat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogIn_Activity extends AppCompatActivity {
    private long backPressedTime;
    private Toast backToast;
    Button btnSignUp, btnMainActivity,forgetBtn,openForget,team_login;
    EditText txtId, txtPass,forgetNumber;
    TextView msg,forgetMessage;
    String strUser, strPass,forgetMobile,paidStatus;
    SessionManager sessionManager;
    User user;
    LinearLayout forgetLayout;
    ConstraintLayout msgLayout;
    ProgressDialog loading;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_);
        team_login=(Button)findViewById(R.id.teamLogin);
        forgetNumber=(EditText)findViewById(R.id.forgetNumber);
        btnSignUp = (Button) findViewById(R.id.goto_signup);
        btnMainActivity = (Button) findViewById(R.id.MainActivity_data);
        txtId = (EditText) findViewById(R.id.signId);
        txtPass = (EditText) findViewById(R.id.signPass);
        msg = (TextView) findViewById(R.id.errorMessage);
        forgetMessage = (TextView) findViewById(R.id.forgetPassMSG);
        msgLayout = (ConstraintLayout) findViewById(R.id.successMSG);
        forgetBtn=(Button)findViewById(R.id.forget_btn);
        openForget=(Button)findViewById(R.id.open_forget);
        forgetLayout=(LinearLayout)findViewById(R.id.forgetSection);
        loading=new ProgressDialog(LogIn_Activity.this);
        SharedPreferences sharedPreferences = getSharedPreferences("MSG", MODE_PRIVATE);
        String msg = sharedPreferences.getString("pass_success", null);
        team_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn_Activity.this, Team_Login.class);
                startActivity(intent);
            }
        });
        if (msg != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
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
                Intent intent = new Intent(LogIn_Activity.this, SignUP.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_right_to_left);
            }
        });
        btnMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkId() | !checkPass()) {
                    //msgLayout.setVisibility(View.VISIBLE);
                    return;
                }
                LoginData();
            }
        });
        sessionManager = new SessionManager(this);
        boolean id = sessionManager.getLogin();
        if (id) {
            moveToFragmentContainer();
        }
        openForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetLayout.setVisibility(forgetLayout.getVisibility()==View.GONE?View.VISIBLE:View.GONE);
                forgetMessage.setVisibility(View.GONE);
            }
        });
        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPassword();
            }
        });
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
    }

    private void LoginData() {
        strUser=txtId.getText().toString().trim();
        strPass=txtPass.getText().toString().trim();
        String url = "https://hopeindia.biz/app/student.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response",response);
                try {
                    String user_name,name,paidStatus;
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("success");
                    if(status.equals("verify"))
                    {
                        user_name = jsonObject.getString("user_name");
                        name = jsonObject.getString("name");
                        paidStatus=jsonObject.getString("paid");
                        sessionManager=new SessionManager(LogIn_Activity.this);
                        user = new User(user_name,name,paidStatus);
                        user.setUser_name(user_name);
                        sessionManager.saveSession(user);
                        sessionManager.setLogin(true);
                        moveToFragmentContainer();
                    }
                    else if (status.equals("noMatch")) {
                        loading.dismiss();
                        Log.e("Message", "Username or Password not matched");
                        msg.setVisibility(View.VISIBLE);
                        msg.setText("Username or Password not matched");
                    } else {
                        blankField();
                        Log.e("Message", "Not Login");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
                loading.dismiss();
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Toast.makeText(LogIn_Activity.this, "Message: " + message, Toast.LENGTH_SHORT).show();
                Log.e("Message", message);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();
                param.put("action","signin");
                param.put("user_name",strUser);
                param.put("password",strPass);
                return  param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 30000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 30000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }

    private void sendPassword() {
        forgetMobile = forgetNumber.getText().toString().trim();
        String url = "https://hopeindia.biz/app/student.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    String mobile;
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("success");
                    if (status.equals("verify")) {
                        forgetMessage.setVisibility(View.VISIBLE);
                        mobile= jsonObject.getString("mobile");
                        forgetMessage.setVisibility(View.VISIBLE);
                        forgetMessage.setText("Your password seccessfully send "+mobile);
                        forgetLayout.setVisibility(View.GONE);
                    } else if (status.equals("noMatch")) {
                        forgetMessage.setVisibility(View.VISIBLE);
                        forgetMessage.setText("Your User Id or Mobile no is not found");
                        forgetLayout.setVisibility(View.GONE);
                    } else {

                    }
                } catch (JSONException e) {
                    Log.e("JSON SMS Error", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
                loading.dismiss();
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Toast.makeText(LogIn_Activity.this, "Message: " + message, Toast.LENGTH_SHORT).show();
                Log.e("Message", message);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("action", "sendPassword");
                param.put("mobile", forgetMobile);
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
        else if (checkString.length() > 30) {
            Toast.makeText(this, "Your Username is greater than 30 digit", Toast.LENGTH_SHORT).show();
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
        else if (checkString.length() > 20) {
            Toast.makeText(this, "Your Password is greater than 20 digit", Toast.LENGTH_SHORT).show();
            txtPass.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        return true;
    }

    public void blankField() {
        txtId.setText("");
        txtPass.setText("");
    }
    private void moveToFragmentContainer() {
        Intent intent = new Intent(this, Fragment_container.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_right_to_left);
        finish();
    }

    public void backButtonHandler() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Leave application?");
        alertDialog.setMessage("Are you sure you want to leave the application?");
        alertDialog.setIcon(R.drawable.camera);
        alertDialog.setPositiveButton("YES",

                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                });
        alertDialog.setNegativeButton("NO",

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
        // Showing Alert Message

        alertDialog.show();
    }
}
