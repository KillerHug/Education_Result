package com.erpsonline.education_result;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Fragment_container extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CircularImageView imageView;
    TextView username;
    Button logout, changePassword, closeDrawer, imageChooser;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Bitmap bitmap;
    String encodeImage;

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
        imageChooser = (Button) headerView.findViewById(R.id.imageChooser);
        final SessionManager sessionManager = new SessionManager(this);
        closeDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        username.setText(sessionManager.getName() + "(" + sessionManager.getUsername() + ")");
        imageChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChoose();
                Log.e("Hello","Hi");
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new Dashboard_Fragment()).addToBackStack(null).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.changeProfile:
                drawerLayout.closeDrawer(GravityCompat.START);
                Log.e("Message", "Change Profile");
                break;
            case R.id.changePassword:
                drawerLayout.closeDrawer(GravityCompat.START);
                Log.e("Message", "Change Password");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Student_Change_Password()).addToBackStack(null).commit();
                break;
            case R.id.logout:
                drawerLayout.closeDrawer(GravityCompat.START);
                Log.e("Message", "logout");
                SessionManager sessionManager = new SessionManager(this);
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

    public void imageChoose() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), 10);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(data!=null)
        {
            Uri filePath=data.getData();
            try {
                InputStream inputStream=getApplicationContext().getContentResolver().openInputStream(filePath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
                imageStore(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void imageStore(Bitmap bitmap)
    {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] imageByte=stream.toByteArray();
        encodeImage=android.util.Base64.encodeToString(imageByte, Base64.DEFAULT);
        Log.e("Code",encodeImage);
    }
}