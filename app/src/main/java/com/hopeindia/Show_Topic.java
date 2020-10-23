package com.hopeindia;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Show_Topic extends Fragment {
    TextView subject_name;
    Topic_Adapter topic_adapter;
    Topic_Model topic_model;
    List<Topic_Model> topicList;
    RecyclerView recyclerView;
    ProgressDialog loading;
    String subject_id, subjectName, txtSubjectID;
    LinearLayout loader;
    ImageButton back;
    String sub_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_topic_fragment, null);
        subject_name = view.findViewById(R.id.topic_sub_name);
        loading = new ProgressDialog(getContext());
        recyclerView = view.findViewById(R.id.recyclerViewTopic);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(topic_adapter);
        topicList = new ArrayList<>();
        back = view.findViewById(R.id.backTo);
        sub_name = getArguments().getString("subject");
        subject_name.setText(sub_name);
        loader = view.findViewById(R.id.loading);
        if (savedInstanceState == null) {
            subject_id = getArguments().getString("subject_id");
            showTopic(subject_id);
            Log.e("Hello", "Null SaveInstance");
        } else {
            subject_id = savedInstanceState.getString("subject_id");
            showTopic(subject_id);
            Log.e("Hello", "Full SaveInstance");
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new Deskboard_Fragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.enter_left_to_right,
                                R.anim.exit_left_to_right,
                                R.anim.enter_right_to_left,
                                R.anim.exit_right_to_left)
                        .replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
            }
        });
        return view;
    }

    public void showTopic(final String subjectId) {
        final String subject_id = subjectId;
        /*loading.setTitle("Loading");
        loading.setCanceledOnTouchOutside(false);
        loading.show();*/
        loader.setVisibility(View.VISIBLE);
        String url = "https://hopeindia.biz/app/student.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    //loading.dismiss();
                    loader.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("topic");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String topic_id = jsonObject1.getString("topic_id");
                            String topic_name = checkStringLength(jsonObject1.getString("topic_name"));
                            String course_level = jsonObject1.getString("course_level");
                            String topic_video = jsonObject1.getString("topic_video");
                            String sr_no = jsonObject1.getString("sr_no");
                            topic_model = new Topic_Model(sub_name,subject_id,topic_id, sr_no, topic_name, course_level, topic_video);
                            topicList.add(topic_model);
                            Topic_Adapter topic_adapter = new Topic_Adapter(getContext(), topicList);
                            topic_adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(topic_adapter);
                        }
                    }
                } catch (JSONException error) {
                    Log.e("Error Topic json", error.getMessage());
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
                Toast.makeText(getContext(), "Message: " + message, Toast.LENGTH_SHORT).show();
                Log.e("Message", message);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("action", "showTopic");
                param.put("subject_id", subject_id);
                param.put("user_name",new SessionManager(getContext()).getUsername());
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("subject_id", getArguments().getString("subject_id"));
    }
    public String checkStringLength(String subject) {
        String subString="";
        if(subject.length()>20)
        {
            subString=subject.substring(0,19)+"...";
        }
        else
        {
            subString=subject;
        }
        return subString;
    }
}
