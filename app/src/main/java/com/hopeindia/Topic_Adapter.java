package com.hopeindia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.security.auth.Subject;

public class Topic_Adapter extends RecyclerView.Adapter<Topic_Adapter.ViewHolder> {
    private Topic_Model topic_model;
    private List<Topic_Model> topicList;
    private Context context;

    public Topic_Adapter(Context context, List<Topic_Model> topicList) {
        this.context = context;
        this.topicList = topicList;
    }
    @Override
    public Topic_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.topic_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Topic_Model model = topicList.get(position);
        holder.sr_no.setText(model.getSr_no());
        holder.topic_name.setText(model.getTopic_name());
        holder.course_level.setText(model.getCourse_level());
        holder.playVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("subject", model.getSubject());
                args.putString("subject_id", model.getSubject_id());
                args.putString("topic", model.getTopic_name());
                args.putString("topic_id", model.getTopic_id());
                args.putString("topic_video", model.getTopic_video());
                AppCompatActivity activity=(AppCompatActivity)v.getContext();
                Fragment myFragment = new PlayVideo();
                myFragment.setArguments(args);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.enter_right_to_left,
                                R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right,
                                R.anim.exit_left_to_right)
                        .replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sr_no,topic_name,course_level;
        Button playVideo;

        public ViewHolder(View view) {
            super(view);
            sr_no=view.findViewById(R.id.topic_no);
            topic_name=view.findViewById(R.id.topic_name);
            course_level=view.findViewById(R.id.course_level);
            playVideo=view.findViewById(R.id.seeVideo);
        }
    }
}
