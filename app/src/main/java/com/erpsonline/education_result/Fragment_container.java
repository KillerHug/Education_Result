package com.erpsonline.education_result;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;

public class Fragment_container extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CircularImageView imageView;
    TextView username;
    Button logout, changePassword,closeDrawer;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        logout = (Button) findViewById(R.id.logout);
        changePassword = (Button) findViewById(R.id.changePassword);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        imageView = (CircularImageView) findViewById(R.id.user_logo);
        View headerView = navigationView.getHeaderView(0);
        username = (TextView) headerView.findViewById(R.id.user_name);
        closeDrawer = (Button) headerView.findViewById(R.id.drawerClose);
        final SessionManager sessionManager = new SessionManager(this);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.removeSession();
                Intent intent = new Intent(Fragment_container.this, SignIn.class);
                startActivity(intent);
                finish();

                Toast.makeText(Fragment_container.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        closeDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
                changePassword.setBackgroundResource(R.color.colorGray);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Student_Change_Password()).addToBackStack(null).commit();
            }
        });
        username.setText(sessionManager.getUsername());
        Log.e("username", sessionManager.getUsername());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new Dashboard_Fragment()).addToBackStack(null).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}