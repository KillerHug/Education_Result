package com.hopeindia;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends Fragment {
    EditText updateUsername, updateName, updateEmail, updateAddress;
    Button submitData, back;
    SessionManager sessionManager;
    ConstraintLayout successMSG;
    String txtUsername, txtname, txtEmail, txtAddress;
    NavigationView navigationView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_profile_fragment, null);
        updateUsername = view.findViewById(R.id.updateUser_Name);
        updateName = view.findViewById(R.id.updateName);
        updateAddress = view.findViewById(R.id.updateAddress);
        updateEmail = view.findViewById(R.id.updateEmailId);
        submitData = view.findViewById(R.id.updateData_btn);
        sessionManager = new SessionManager(getContext());
        updateUsername.setText(sessionManager.getUsername());
        updateName.setText(sessionManager.getName());
        updateEmail.setText(sessionManager.getEmail());
        updateAddress.setText(sessionManager.getAddress());
        successMSG = view.findViewById(R.id.successMSG);
        back = view.findViewById(R.id.backTo);
        navigationView=getActivity().findViewById(R.id.navigationView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationView.getMenu().getItem(0).setChecked(true);
                Fragment fragment = new Deskboard_Fragment();
                FragmentTransaction transaction = getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.enter_left_to_right,
                                R.anim.exit_left_to_right,
                                R.anim.enter_right_to_left,
                                R.anim.exit_right_to_left);
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        submitData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkEmail()) {
                    return;
                }
                updateData();
            }
        });
        showStudentData();
        return view;
    }

    private void showStudentData() {
        SessionManager sessionManager = new SessionManager(getContext());
        final String user_name = sessionManager.getUsername();
        String url = "https://hopeindia.biz/app/student.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            updateEmail.setText(jsonObject1.getString("email"));
                            updateAddress.setText(jsonObject1.getString("address"));
                        }
                    }
                } catch (JSONException error) {
                    Log.e("JSON Message", error.getMessage());
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
                param.put("action", "showStudentData");
                param.put("user_name", user_name);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public boolean checkEmail() {
        String id = updateEmail.getText().toString().trim();
        String pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!id.isEmpty()) {
            if (!id.matches(pattern)) {
                updateEmail.setError("Invalid Email!");
                updateEmail.setBackgroundResource(R.drawable.edit_error);
                return false;
            } else {
                updateEmail.setError(null);
            }
        }
        return true;
    }

    private boolean checkAddress() {
        String id = updateAddress.getText().toString().trim();
        if (id.isEmpty()) {
            updateAddress.setError("Field can not be Empty");
            updateAddress.setBackgroundResource(R.drawable.edit_error);
            return false;
        } else {
            updateAddress.setError(null);
        }
        return true;
    }

    public void updateData() {
        txtUsername = sessionManager.getUsername();
        txtname = updateName.getText().toString();
        txtEmail = updateEmail.getText().toString();
        txtAddress = updateAddress.getText().toString();
        String url = "https://hopeindia.biz/app/student.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                successMSG.setVisibility(View.VISIBLE);
                            }
                        }, 3000);
                        navigationView.getMenu().getItem(0).setChecked(true);
                        Fragment fragment = new Deskboard_Fragment();
                        FragmentTransaction transaction = getFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(
                                        R.anim.enter_right_to_left,
                                        R.anim.exit_right_to_left,
                                        R.anim.enter_left_to_right,
                                        R.anim.exit_left_to_right);
                        transaction.replace(R.id.fragment_container, fragment); // give your fragment container id in first parameter
                        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                        transaction.commit();
                        Toast.makeText(getContext(), "Your Data successfully updates", Toast.LENGTH_SHORT).show();
                    } else if (success.equals("0")) {
                        Toast.makeText(getContext(), "Data  not updates", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException error) {
                    Log.e("JSON Message", error.getMessage());
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
                param.put("action", "updateInfo");
                param.put("user_name", txtUsername);
                param.put("name", txtname);
                param.put("email", txtEmail);
                param.put("address", txtAddress);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}

