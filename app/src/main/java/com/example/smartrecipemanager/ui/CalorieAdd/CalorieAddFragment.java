package com.example.smartrecipemanager.ui.CalorieAdd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.example.smartrecipemanager.Calorie;
import com.example.smartrecipemanager.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;

public class CalorieAddFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private TextInputLayout editText;
    private Button query;
    private String text;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calorieadd, container, false);
        //register textView,button
        editText=(TextInputLayout)root.findViewById(R.id.CalorieData);
        query=(Button)root.findViewById(R.id.add);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text=editText.getEditText().getText().toString();
                if(text.equals("")) {
                    Toast.makeText(getContext(), "Input Empty", Toast.LENGTH_SHORT).show();
                }else{
                    addToDatabase();
                }
            }
        });
        return root;
    }

    private void addToDatabase() {
        //get current time
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        final String time=year+","+month+","+day+"_"+hour+":"+minute+":"+second;
        //upload information to server
        mAuth = FirebaseAuth.getInstance();
        final String uid = mAuth.getCurrentUser().getUid();
        mRootRef = FirebaseDatabase.getInstance().getReference().child(uid).child("calorie").child(time);
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Calorie calorie = new Calorie(time,text);
                mRootRef.setValue(calorie);
                    Toast.makeText(getContext(),"upload successful",Toast.LENGTH_SHORT).show();
                    editText.getEditText().setText("");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}