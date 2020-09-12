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
    Button logout, changePassword, closeDrawer;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        imageView = (CircularImageView) findViewById(R.id.user_logo);
        View headerView = navigationView.getHeaderView(0);
        username = (TextView) headerView.findViewById(R.id.user_name);
        closeDrawer = (Button) headerView.findViewById(R.id.drawerClose);
        final SessionManager sessionManager = new SessionManager(this);
        closeDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        username.setText(sessionManager.getName() + "(" + sessionManager.getUsername() + ")");
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new Dashboard_Fragment()).addToBackStack(null).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.changeProfile:
                drawerLayout.closeDrawer(GravityCompat.START);
                Log.e("Message","Change Profile");
                break;
            case R.id.changePassword:
                drawerLayout.closeDrawer(GravityCompat.START);
                Log.e("Message","Change Password");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Student_Change_Password()).addToBackStack(null).commit();
                break;
            case R.id.logout:
                drawerLayout.closeDrawer(GravityCompat.START);
                Log.e("Message","logout");
                SessionManager sessionManager=new SessionManager(this);
                sessionManager.setLogin(false);
                sessionManager.removeSession();
                Intent intent = new Intent(Fragment_container.this, SignIn.class);
                startActivity(intent);
                finish();

                Toast.makeText(Fragment_container.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}