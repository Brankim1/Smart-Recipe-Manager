package com.example.smartrecipemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**RegisterActivity
* RegisterActivity, user can register through email(use firebase)
* */
public class RegisterActivity extends AppCompatActivity{
    private FirebaseAuth auth;
    private DatabaseReference rootRef;
    private TextInputLayout email;
    private TextInputLayout password;
    private RadioGroup genderGroup;
    private RadioButton  genderSelect;
    private RadioGroup veganGroup;
    private RadioButton  veganSelect;
    private Button register;
    private PersonalInfo personalInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        auth = FirebaseAuth.getInstance();
        email=(TextInputLayout)findViewById(R.id.email2);
        password=(TextInputLayout)findViewById(R.id.password2);

        //create a multiple choice button to choose gender
        genderGroup = (RadioGroup) findViewById(R.id.genderSelect);
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                genderSelect = (RadioButton) findViewById(checkedId);
            }
        });
        //create a multiple choice button to choose vegan
        veganGroup = (RadioGroup) findViewById(R.id.veganSelect);
        veganGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                veganSelect = (RadioButton) findViewById(checkedId);
            }
        });

        register=(Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //register
                String emailText=email.getEditText().getText().toString();
                String passwordText=password.getEditText().getText().toString();
                //check input
                if(emailText.length()==0){
                    Toast.makeText(RegisterActivity.this, "Please input email", Toast.LENGTH_SHORT).show();
                }else if(passwordText.length()==0){
                    Toast.makeText(RegisterActivity.this, "Please input password", Toast.LENGTH_SHORT).show();
                }else if (!(Patterns.EMAIL_ADDRESS.matcher(emailText).matches())){
                    Toast.makeText(RegisterActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                } else if(genderGroup.getCheckedRadioButtonId()==-1){
                    Toast.makeText(RegisterActivity.this, "Please choose gender", Toast.LENGTH_SHORT).show();
                }else if(veganGroup.getCheckedRadioButtonId()==-1){
                    Toast.makeText(RegisterActivity.this, "Please choose whether vegan", Toast.LENGTH_SHORT).show();
                }
                if(emailText.length()!=0&&passwordText.length()!=0&&Patterns.EMAIL_ADDRESS.matcher(emailText).matches()&&genderGroup.getCheckedRadioButtonId()!=-1&&veganGroup.getCheckedRadioButtonId()!=-1) {
                    auth.createUserWithEmailAndPassword(emailText, passwordText)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        //if register successful, then add gender and vegan information to server
                                        addPersonalInfo();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, "Email already exists", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
          });
    }

    /**
     * if register successful, then add gender and vegan information to server
     * */
    private void addPersonalInfo() {
        //add gender and vegan information to server
        auth = FirebaseAuth.getInstance();
        final String uid = auth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference().child(uid).child("Information");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                personalInfo = new PersonalInfo(genderSelect.getText().toString(),veganSelect.getText().toString());
                rootRef.setValue(personalInfo);
                Intent mainIntent= new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RegisterActivity.this, "user information add fail", Toast.LENGTH_LONG).show();
            }
        });

    }


}

/**PersonalInfo
 * constructor for store users' register information to server
 * */
class PersonalInfo{
    private String gender;
    private String vegan;
    public PersonalInfo(){}
    public String getGender() {
        return gender;
    }
    public void setGender(String gender){
        this.gender=gender;
    }
    public String getVegan() { return vegan; }
    public void setVegan(String vegan){
        this.vegan=vegan;
    }
    public PersonalInfo(String gender,String vegan) {
        this.gender = gender;
        this.vegan = vegan;
    }
}