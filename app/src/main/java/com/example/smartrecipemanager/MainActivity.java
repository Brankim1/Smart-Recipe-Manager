package com.example.smartrecipemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
* main Activity
* it can judge whether user were already login, if yes go to home Activity.
 * if not, go to Login Activity
* */
public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ConnectivityManager manager;
    private NetworkInfo networkInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart() {
        super.onStart();
        //check network connection
        manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            //login authentication(Firebase API)
            auth = FirebaseAuth.getInstance();
            //get current user, choose to login or home activity
            FirebaseUser user = auth.getCurrentUser();
            if (user == null) {
                //go to login activity
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }else{
                //go to home activity
                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }else {
            Toast.makeText(MainActivity.this, "Network Connect Fail", Toast.LENGTH_LONG).show();
            //waiting 5s to finish
            dialog("Sorry","Network connect fail, Please press OK to finish");
        }

    }
    public void dialog(String title,String message){
        //set Alert Dialog to hint
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create();
        dialog.show();
    }
}