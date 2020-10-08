package com.hopeindia;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class Fragment_container extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    boolean doubleBackToExitPressedOnce = false;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CircleImageView imageView;
    //CircularImageView imageView;
    TextView username,team_login;
    Button closeDrawer, imageChooser;
    Bitmap bitmap;
    private long backPressedTime;
    private Toast backToast;
    String encodeImage;
    private static final int PERMISSION_FILE = 23;
    private static final int ACCESS_FILE = 43;
    String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        team_login=(TextView)findViewById(R.id.teamLogin);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        imageView = (CircleImageView) headerView.findViewById(R.id.user_logo);
        imageView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
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
                Log.e("Hello", "Hi");
            }
        });
        team_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fragment_container.this, Team_Login.class);
                startActivity(intent);
            }
        });
        displayImage();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        String tag = "my_fragment";
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new Deskboard_Fragment(),tag).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.deshboard:
                drawerLayout.closeDrawer(GravityCompat.START);
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.enter_right_to_left,
                                R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right,
                                R.anim.exit_left_to_right)
                        .replace(R.id.fragment_container, new Deskboard_Fragment()).addToBackStack(null).commit();
                Log.e("Message", "Deskboard");
                break;
            case R.id.changeProfile:
                drawerLayout.closeDrawer(GravityCompat.START);
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.enter_right_to_left,
                                R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right,
                                R.anim.exit_left_to_right)
                        .replace(R.id.fragment_container, new UpdateProfile()).addToBackStack(null).commit();
                Log.e("Message", "Change Profile");
                break;
            case R.id.changePassword:
                drawerLayout.closeDrawer(GravityCompat.START);
                Log.e("Message", "Change Password");
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.anim.enter_right_to_left,
                                R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right,
                                R.anim.exit_left_to_right)
                        .replace(R.id.fragment_container, new Student_Change_Password()).addToBackStack(null).commit();
                break;
            case R.id.logout:
                drawerLayout.closeDrawer(GravityCompat.START);
                Log.e("Message", "logout");
                SessionManager sessionManager = new SessionManager(this);
                sessionManager.setLogin(false);
                sessionManager.removeSession();
                Intent intent = new Intent(Fragment_container.this, LogIn_Activity.class);
                startActivity(intent);
                finish();
                Toast.makeText(Fragment_container.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    public void imageChoose() {
        if (ContextCompat.checkSelfPermission(Fragment_container.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_FILE);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Pilih gamber"), ACCESS_FILE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACCESS_FILE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri FILE_URI = data.getData();
            CropImage.activity(FILE_URI)
                    .setAspectRatio(1,1)
                    .setActivityTitle("Crop Image")
                    .setCropMenuCropButtonTitle("Done")
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(resultUri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);
                    imageStore(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imageView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);

        byte[] imageByte = stream.toByteArray();
        encodeImage = android.util.Base64.encodeToString(imageByte, Base64.DEFAULT);
        final String imageAddress = encodeImage;
        Log.e("Image Address", encodeImage);
        SessionManager sessionManager = new SessionManager(this);
        user_name = sessionManager.getUsername();
        Log.e("Response", user_name);
        String url = "https://hopeindia.biz/app/student.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Image Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("success");
                    if (status.equals("verify")) {
                        Toast.makeText(Fragment_container.this, "Success", Toast.LENGTH_SHORT).show();
                    } else if (status.equals("noVerify")) {
                        Toast.makeText(Fragment_container.this, "Not Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Fragment_container.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(Fragment_container.this, "Fragment Message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Fragment_container.this, "Message: " + message, Toast.LENGTH_SHORT).show();
                Log.e("Message", message);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("action", "storeImage");
                param.put("imageAddress", imageAddress);
                param.put("user_name", user_name);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void displayImage() {
        String url = "https://hopeindia.biz/app/student.php";
        SessionManager sessionManager = new SessionManager(this);
        final String user_name = sessionManager.getUsername();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String photoAddress = jsonObject.getString("photo");
                    if (photoAddress.isEmpty()) {
                        imageView.setBackgroundResource(R.drawable.logo_boy);
                    }
                    String photoURL = "https://hopeindia.biz/admin/" + photoAddress;
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(photoURL)
                            .into(imageView);
                } catch (JSONException error) {
                    Toast.makeText(Fragment_container.this, "Message: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("JSON Error", error.getMessage());
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
                Toast.makeText(Fragment_container.this, "Message: " + message, Toast.LENGTH_SHORT).show();
                Log.e("Message", message);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_name", user_name);
                params.put("action", "displayPhoto");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            //super.onBackPressed();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Leave application?");
            alertDialog.setMessage("Are you sure you want to leave the application?");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            finish();

                        }
                    });
            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });
            alertDialog.show();
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }
}