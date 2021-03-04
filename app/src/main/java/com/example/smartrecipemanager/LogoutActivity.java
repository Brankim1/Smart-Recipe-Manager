package com.example.smartrecipemanager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

/**
* Log out
* */
public class LogoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            //Firebase API user log out
            FirebaseAuth.getInstance().signOut();
            //send to login activity
            Intent LoginIntent = new Intent(LogoutActivity.this, LoginActivity.class);
            startActivity(LoginIntent);
        }else{
            Toast.makeText(LogoutActivity.this, "Network Connect Fail", Toast.LENGTH_LONG).show();
        }
        finish();
    }
}