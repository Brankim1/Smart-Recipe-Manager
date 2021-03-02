package com.example.smartrecipemanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

/**
* Log out
* */
public class LogoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Firebase API user log out
        FirebaseAuth.getInstance().signOut();
        //send to login activity
        Intent LoginIntent = new Intent(LogoutActivity.this, LoginActivity.class);
        startActivity(LoginIntent);
        finish();
    }
}