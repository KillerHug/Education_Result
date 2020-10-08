package com.hopeindia;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Student_Change_Password extends Fragment {
    EditText oldPass,newPass,confirmPass;
    Button changePassBtn;
    ImageButton back;
    TextView msg,changePassMSG,passError;
    String txtOld,txtNew,txtUserName;
    NavigationView navigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.student_change_pass,container,false);
        oldPass=view.findViewById(R.id.oldPass);
        newPass=view.findViewById(R.id.newPass);
        confirmPass=view.findViewById(R.id.confirmPass);
        msg = view. findViewById(R.id.errorMessage);
        changePassMSG=view.findViewById(R.id.changePassMSG);
        back=view.findViewById(R.id.backTo);
        navigationView=getActivity().findViewById(R.id.navigationView);
        passError=view.findViewById(R.id.passError);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Deskboard_Fragment();
                navigationView.getMenu().getItem(0).setChecked(true);
                FragmentTransaction transaction = getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.enter_left_to_right,
                                R.anim.exit_left_to_right,
                                R.anim.enter_right_to_left,
                                R.anim.exit_right_to_left);
                transaction.replace(R.id.fragment_container, fragment ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
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
        String pattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$";
        if (checkString.isEmpty()) {
            Toast.makeText(getContext(), "Field can not be Empty", Toast.LENGTH_SHORT).show();
            newPass.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        else if(checkString.length()<4) {
            passError.setText("Password minimum limit is 4 Character");
            passError.setVisibility(View.VISIBLE);
            newPass.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        return true;
    }
    private boolean checkConfirmPass() {
        String checkString = confirmPass.getText().toString().trim();
        String pattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$";
        if (checkString.isEmpty()) {
            Toast.makeText(getContext(), "Field can not be Empty", Toast.LENGTH_SHORT).show();
            confirmPass.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        else if(checkString.length()<4) {
            passError.setText("Password minimum limit is 4 Character");
            passError.setVisibility(View.VISIBLE);
            confirmPass.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        return true;
    }
    private void changePassword() {
        txtOld = oldPass.getText().toString().trim();
        txtNew = newPass.getText().toString().trim();
        String url = "https://hopeindia.biz/app/student.php";
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
                        navigationView.getMenu().getItem(0).setChecked(true);
                        msg.setVisibility(View.GONE);
                        passError.setVisibility(View.GONE);
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
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
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
                Toast.makeText(getContext(), "Message: " + message, Toast.LENGTH_SHORT).show();
                Log.e("Message", message);
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
    public void blankField() {
        oldPass.setText("");
        newPass.setText("");
        confirmPass.setText("");
    }
}
