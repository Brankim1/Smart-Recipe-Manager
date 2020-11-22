package com.example.smartrecipemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegisterActivity extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private TextInputLayout email;
    private TextInputLayout password;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        mAuth = FirebaseAuth.getInstance();
        email=(TextInputLayout)findViewById(R.id.email2);
        password=(TextInputLayout)findViewById(R.id.password2);
        register=(Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //register
                String emailText=email.getEditText().getText().toString();
                String passwordText=password.getEditText().getText().toString();
                if(emailText.length()==0){
                    Toast.makeText(RegisterActivity.this, "Please input email", Toast.LENGTH_SHORT).show();
                }else if(passwordText.length()==0){
                    Toast.makeText(RegisterActivity.this, "Please input password", Toast.LENGTH_SHORT).show();
                }else if (!(Patterns.EMAIL_ADDRESS.matcher(emailText).matches())){
                    Toast.makeText(RegisterActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }
                if(emailText.length()!=0&&passwordText.length()!=0&&Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                    mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Intent mainIntent= new Intent(RegisterActivity.this, MainActivity.class);
                                       // mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if(e instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(RegisterActivity.this, "Email already exists", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
          });

    }


}