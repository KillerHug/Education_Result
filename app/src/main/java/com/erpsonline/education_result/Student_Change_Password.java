package com.erpsonline.education_result;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

public class Student_Change_Password extends Fragment {
    EditText oldPass,newPass,confirmPass;
    Button changePassBtn, drawerOpen;
    DrawerLayout drawerLayout;
    TextView msg;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.student_change_pass,container,false);
        oldPass=view.findViewById(R.id.oldPass);
        newPass=view.findViewById(R.id.newPass);
        confirmPass=view.findViewById(R.id.confirmPass);
        drawerOpen=view.findViewById(R.id.drawerOpen);
        drawerLayout=getActivity().findViewById(R.id.drawerLayout);
        msg = view. findViewById(R.id.errorMessage);
        drawerOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
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
            msg.setText("Confirm Password and New Password are not matche");
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
        String pattern="[0-9]+";
        if (checkString.isEmpty()) {
            Toast.makeText(getContext(), "Field can not be Empty", Toast.LENGTH_SHORT).show();
            newPass.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        else if(checkString.matches(pattern))
        {
            Toast.makeText(getContext(), "You entered only numeric number.", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    private boolean checkConfirmPass() {
        String checkString = confirmPass.getText().toString().trim();
        String pattern="[0-9]+";
        if (checkString.isEmpty()) {
            Toast.makeText(getContext(), "Field can not be Empty", Toast.LENGTH_SHORT).show();
            confirmPass.setBackgroundResource(R.drawable.edit_error);
            return false;
        }
        else if(checkString.matches(pattern))
        {
            Toast.makeText(getContext(), "You entered only numeric number.", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    private void changePassword() {
    }
}
