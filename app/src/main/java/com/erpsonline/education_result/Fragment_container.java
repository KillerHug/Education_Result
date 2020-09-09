package com.erpsonline.education_result;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.w3c.dom.Text;

public class Fragment_container extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CircularImageView imageView;
    TextView userid;
    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        userid=(TextView)findViewById(R.id.user_name);
        logout=(Button)findViewById(R.id.logout);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);
        navigationView=(NavigationView)findViewById(R.id.navigationView);
        imageView=(CircularImageView)findViewById(R.id.user_logo);
        final SessionManager sessionManager =new SessionManager(this);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.removeSession();
                Intent intent = new Intent(Fragment_container.this, SignIn.class);
                startActivity(intent);
                Toast.makeText(Fragment_container.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
            }
        });
//        userid.setText(sessionManager.getUserID().toString());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new Deskboard()).commit();
    }
}