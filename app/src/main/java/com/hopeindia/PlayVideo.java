package com.hopeindia;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.ImageButton;

public class PlayVideo extends Fragment {
    TextView textView;
    VideoView videoView;
    private RelativeLayout.LayoutParams defaultVideoViewParams;
    private int defaultScreenOrientationMode;
    LinearLayout heading_layout;
    private int position = 0;
    MediaController mediaController;
    Button zoomButton;
    ImageButton back;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.play_video_fragment, null);
        textView = view.findViewById(R.id.play_topic_name);
        String topic = new Show_Topic().checkStringLength(getArguments().getString("topic"));
        textView.setText(topic);
        back=view.findViewById(R.id.backTo);
        zoomButton=view.findViewById(R.id.zoomButton);
        heading_layout=view.findViewById(R.id.heading_layout);
        videoView = view.findViewById(R.id.topic_video);
        Log.e("Video", getArguments().getString("topic_video"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("subject", getArguments().getString("subject"));
                args.putString("subject_id", getArguments().getString("subject_id"));
                Fragment myFragment = new Show_Topic();
                myFragment.setArguments(args);
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
        try {
            String videoUrl = "https://hopeindia.biz/admin/" + getArguments().getString("topic_video");
            Uri video = Uri.parse(videoUrl);
            mediaController= new MediaController(getContext());
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(video);
            videoView.getRotation();
            videoView.start();

            /*videoView.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    if( event.getAction() == MotionEvent.ACTION_DOWN )
                    {
                        zoomButton.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                zoomButton.setVisibility(View.INVISIBLE);
                            }
                        },3000);
                    }
                    return true;
                }
            });*/
        } catch (Exception e) {
            // TODO: handle exception
            Toast.makeText(getContext(), "Error connecting", Toast.LENGTH_SHORT).show();
        }
        zoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Message","Zoom Button");
                if(getActivity().getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    zoomButton.setBackgroundResource(R.drawable.zoom_out);
                }
                else {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    zoomButton.setBackgroundResource(R.drawable.zoom_in);
                }
            }
        });
        return view;
    }
    private void makeVideoFullScreen() {

        defaultScreenOrientationMode = getResources().getConfiguration().orientation;
        defaultVideoViewParams = (RelativeLayout.LayoutParams) videoView.getLayoutParams();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        videoView.postDelayed(new Runnable() {

            @Override
            public void run() {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);

                videoView.setLayoutParams(params);
                videoView.layout(10, 10, 10, 10);
            }
        }, 700);
    }
    private void exitVideoFullScreen() {
        getActivity().setRequestedOrientation(defaultScreenOrientationMode);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        videoView.postDelayed(new Runnable() {

            @Override
            public void run() {
                videoView.setLayoutParams(defaultVideoViewParams);
                videoView.layout(10, 10, 10, 10);
            }
        }, 700);
    }
    @Override
    public void onPause() {
        super.onPause();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            getActivity().getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getActivity().getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            getActivity().getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getActivity().getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setPlayerViewLayoutParamsForLandScape();
            heading_layout.setVisibility(View.GONE);
        } else {
            setPlayerViewLayoutParamsForPortrait();
            heading_layout.setVisibility(View.VISIBLE);
        }
    }

    private void setPlayerViewLayoutParamsForLandScape() {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        zoomButton.setBackgroundResource(R.drawable.zoom_out);
        videoView.setLayoutParams(lp);
    }

    private void setPlayerViewLayoutParamsForPortrait() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Double doubleHeight = width / 1.5;
        Integer height = doubleHeight.intValue();

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, height);
        zoomButton.setBackgroundResource(R.drawable.zoom_in);
        videoView.setLayoutParams(lp);
    }

    private void setPlayerViewLayoutParams() {
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setPlayerViewLayoutParamsForLandScape();
        } else {
            setPlayerViewLayoutParamsForPortrait();
        }
    }
}

