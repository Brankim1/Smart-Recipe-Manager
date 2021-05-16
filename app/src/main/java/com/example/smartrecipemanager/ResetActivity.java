package com.example.smartrecipemanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

/**ResetActivity
* ResetActivity for user forget password,use firebase
 * user will receive an email to reset password
* */
public class ResetActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private TextInputLayout email;
    private Button reset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            auth = FirebaseAuth.getInstance();
            email=(TextInputLayout)findViewById(R.id.email3);
            reset=(Button)findViewById(R.id.reset);
            reset.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    try {
                        //input check
                        String emailadd = email.getEditText().getText().toString();
                        if (Patterns.EMAIL_ADDRESS.matcher(emailadd).matches()){//check mail format
                            auth.sendPasswordResetEmail(emailadd)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                                Toast.makeText(ResetActivity.this, "Email not exist", Toast.LENGTH_LONG).show();
                                            }else if (task.isSuccessful()) {
                                                Toast.makeText(ResetActivity.this, "Email sent", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(ResetActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                    }
                }
            });
        }else{
            Toast.makeText(ResetActivity.this, "Network Connect Fail", Toast.LENGTH_LONG).show();
        }
    }
}