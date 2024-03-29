package com.example.smartrecipemanager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

/**Login Activity
 * use firebase to verify email and password
* */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth auth;
    private TextInputLayout email;
    private TextInputLayout password;
    private Button login;
    private TextView newUserText;
    private TextView forgotText;
    private  ConnectivityManager manager;
    private NetworkInfo networkInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        //check network connect
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        email= (TextInputLayout)findViewById(R.id.email);
        password=(TextInputLayout)findViewById(R.id.password);
        login=(Button)findViewById(R.id.login);
        newUserText=(TextView)findViewById(R.id.registerText);
        forgotText=(TextView)findViewById(R.id.forgetText);
        login.setOnClickListener(this);
        newUserText.setOnClickListener(this);
        forgotText.setOnClickListener(this);

        if (networkInfo != null) {
            auth = FirebaseAuth.getInstance();
        }else{
            Toast.makeText(LoginActivity.this, "Network Connect Fail", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                if (network() == true) {
                    //handle input
                    String emailText=email.getEditText().getText().toString();
                    String passwordText=password.getEditText().getText().toString();
                    //check input
                    if(emailText.length()==0){
                        Toast.makeText(this, "Please input email", Toast.LENGTH_SHORT).show();
                    }else if(passwordText.length()==0){
                        Toast.makeText(this, "Please input password", Toast.LENGTH_SHORT).show();
                    }else if (!(Patterns.EMAIL_ADDRESS.matcher(emailText).matches())){
                        Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    }
                    if(emailText.length()!=0&&passwordText.length()!=0&&Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                        login(emailText,passwordText);
                    }
                }
                break;
            case R.id.forgetText:
                if (network() == true) {
                    Intent startIntent = new Intent(LoginActivity.this, ResetActivity.class);
                    startActivity(startIntent);
                }
                break;
            case R.id.registerText:
                if (network() == true) {
                    Intent startIntent2 = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(startIntent2);
                }
                break;
            default:
                break;
        }
    }

    /**
     * use firebase to login
     * */
    private void login(String emailText, String passwordText) {
        //use firebaseAuth to authentic email and password
        auth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent mainIntent= new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        }else{
                            if(task.getException() instanceof FirebaseAuthInvalidUserException){
                                Toast.makeText(LoginActivity.this, "Email not exist", Toast.LENGTH_LONG).show();
                            }else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    
                });

    }

    /**
     * judge network
     * */
    private Boolean network(){
        //check device whether connected to network
        boolean network=true;
        //check network connection
        manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null) {
            Toast.makeText(LoginActivity.this, "Internet Connect Fail", Toast.LENGTH_SHORT).show();
            network=false;
        }
        return network;
    }
}