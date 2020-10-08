package com.hopeindia;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Subject_Adapter extends RecyclerView.Adapter<Subject_Adapter.ViewHolder> {
    private Subject_Model subject_model;
    private List<Subject_Model> subjectList;
    private Context context;

    public Subject_Adapter(Context context, List<Subject_Model> subjectList) {
        this.context = context;
        this.subjectList = subjectList;
    }
    @Override
    public Subject_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.subject_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Subject_Model model = subjectList.get(position);
        holder.subjectName.setText(model.getSubject());
        holder.subjectClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("subject", model.getSubject());
                args.putString("subject_id", model.getSubject_id());
                AppCompatActivity activity=(AppCompatActivity)v.getContext();
                Fragment myFragment = new Show_Topic();
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
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName;
        LinearLayout subjectClick;

        public ViewHolder(View view) {
            super(view);
            subjectName = view.findViewById(R.id.subject_name);
            subjectClick=view.findViewById(R.id.subjectCLickLayout);
        }
    }
}
