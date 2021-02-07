package com.example.smartrecipemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

/*
* ResetActivity for user forget password
* */
public class ResetActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputLayout email;
    private Button reset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        mAuth = FirebaseAuth.getInstance();
        email=(TextInputLayout)findViewById(R.id.email3);
        reset=(Button)findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = email.getEditText().getText().toString();
                try {
                    if (!(Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches())){//check mail format
                        Toast.makeText(ResetActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        auth.sendPasswordResetEmail(emailAddress)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if (task.isSuccessful()) {
                                               Toast.makeText(ResetActivity.this, "Email sent", Toast.LENGTH_LONG).show();
                                           }
                                           else if(task.getException() instanceof FirebaseAuthInvalidUserException){
                                               Toast.makeText(ResetActivity.this, "Email not exist", Toast.LENGTH_LONG).show();
                                           }
                                       }
                                   }
                                );
                    }

                } catch (Exception e) {
                    Toast.makeText(ResetActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}