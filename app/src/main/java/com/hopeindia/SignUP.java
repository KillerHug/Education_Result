package com.hopeindia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUP extends AppCompatActivity {
    Button btnMainActivity, submitData;
    EditText txtName, txtMobile, txtReferral;
    RadioGroup genderRadio;
    RadioButton selectGender;
    String name, mobile, referral, gender, gen = "";
    TextView msg;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        msg = (TextView) findViewById(R.id.errorMessage);
        btnMainActivity = (Button) findViewById(R.id.goto_MainActivity);
        txtName = (EditText) findViewById(R.id.signUpName);
        txtMobile = (EditText) findViewById(R.id.signUp_Mobile);
        txtReferral = (EditText) findViewById(R.id.signUp_referral);
        genderRadio = (RadioGroup) findViewById(R.id.signUp_gender);
        submitData = (Button) findViewById(R.id.signup_data);
        btnMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUP.this, LogIn_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_left_to_right);
            }
        });
        submitData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkName() | !checkMobile() | !checkGender() | !checkReferral()) {
                    return;
                }
                SubmitData();
            }
        });
        loading = new ProgressDialog(SignUP.this);
    }

    public boolean checkGender() {
        int id = genderRadio.getCheckedRadioButtonId();
        if (id == -1) {
            return false;
        }
        return true;
    }

    public boolean checkReferral() {
        String checkString = txtReferral.getText().toString().trim();
        String pattern = "[A-Z0-9 ]+";
        if (checkString.isEmpty()) {
            txtReferral.setBackgroundResource(R.drawable.edit_error);
            return false;
        } else if (!checkString.matches(pattern)) {
            txtReferral.setBackgroundResource(R.drawable.edit_error);
            Toast.makeText(this, "No Lowercase Letter Allowed", Toast.LENGTH_SHORT).show();
            return false;
        } else if (checkString.length() > 6) {
            Toast.makeText(this, "Referral Code is greater than 6 digit", Toast.LENGTH_SHORT).show();
            txtReferral.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        return true;
    }

    public boolean checkName() {
        String checkString = txtName.getText().toString().trim();
        String pattern = "[a-zA-z ]+";
        if (checkString.length() > 50) {
            Toast.makeText(this, "Text limit is 50 character", Toast.LENGTH_SHORT).show();
            txtName.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        if (checkString.isEmpty()) {
            txtName.setBackgroundResource(R.drawable.edit_error);
            return false;
        } else if (!checkString.matches(pattern)) {
            Toast.makeText(this, "Please Enter Only Character", Toast.LENGTH_SHORT).show();
            txtName.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        return true;
    }

    public boolean checkMobile() {
        String checkString = txtMobile.getText().toString().trim();
        String pattern = "[0-9]+";
        if (checkString.isEmpty()) {
            Toast.makeText(this, "Field can not be Empty", Toast.LENGTH_SHORT).show();
            txtMobile.setBackgroundResource(R.drawable.edit_error);
            return false;
        } else if (!checkString.matches(pattern)) {
            Toast.makeText(this, "Please Enter Only Number", Toast.LENGTH_SHORT).show();
            txtMobile.setBackgroundResource(R.drawable.edit_error);
            return false;
        } else if (checkString.length() > 10) {
            Toast.makeText(this, "Mobile number is greater than 10 digit", Toast.LENGTH_SHORT).show();
            txtMobile.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        return true;
    }


    private void SubmitData() {
        loading.setTitle("Loading");
        loading.setCanceledOnTouchOutside(false);
        loading.show();
        name = txtName.getText().toString().trim();
        mobile = txtMobile.getText().toString().trim();
        referral = txtReferral.getText().toString().trim();
        int genderId = genderRadio.getCheckedRadioButtonId();
        selectGender = (RadioButton) findViewById(genderId);
        String url = "https://hopeindia.biz/app/student.php";
        if (genderId != -1) {
            gen = selectGender.getText().toString();
        }
        Log.e("Name", name);
        Log.e("Mobile", mobile);
        Log.e("Referral", referral);
        Log.e("Gender", gen);
        gender = gen;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Res", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("success");
                    if (status.equals("1")) {
                        loading.dismiss();
                        Log.e("Message", "Successfully Registered");
                        blankField();
                        Toast.makeText(SignUP.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                        txtName.setBackgroundResource(R.drawable.border_design);
                        txtReferral.setBackgroundResource(R.drawable.border_design);
                        txtMobile.setBackgroundResource(R.drawable.border_design);
                        Intent intent = new Intent(SignUP.this, LogIn_Activity.class);
                        SharedPreferences sharedPreferences = getSharedPreferences("MSG", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("pass_success", "pass_success");
                        editor.commit();
                        startActivity(intent);
                    } else if (status.equals("2")) {
                        loading.dismiss();
                        Log.e("Message", "Mobile No. already registered");
                        msg.setVisibility(View.VISIBLE);
                        msg.setText("Mobile No. already registered");
                        txtMobile.setBackgroundResource(R.drawable.edit_error);
                        txtMobile.setTextColor(Color.parseColor("#ff0000"));
                    } else if (status.equals("3")) {
                        loading.dismiss();
                        Log.e("Message", "Referral Code Not Matched");
                        msg.setVisibility(View.VISIBLE);
                        msg.setText("Referral Code Not Matched");
                        txtReferral.setBackgroundResource(R.drawable.edit_error);
                        txtReferral.setTextColor(Color.parseColor("#ff0000"));
                    } else {
                        blankField();
                        loading.dismiss();
                        Log.e("Message", "not Registered");
                    }
                } catch (JSONException e) {
                    Log.e("Sign Up JSON Error", e.getMessage());
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
                Toast.makeText(SignUP.this, "Message: " + message, Toast.LENGTH_SHORT).show();
                Log.e("Message", message);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("action", "register");
                param.put("name", name);
                param.put("mobile", mobile);
                param.put("referral", referral);
                param.put("gender", gender);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void blankField() {
        txtName.setText("");
        txtMobile.setText("");
        txtReferral.setText("");
    }
}