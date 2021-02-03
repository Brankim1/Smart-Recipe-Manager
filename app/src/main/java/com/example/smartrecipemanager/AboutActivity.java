package com.example.smartrecipemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AboutActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    String gender;
    ImageView headerPortrait;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setDrawerLayout();

    }
    public void setDrawerLayout(){
        //component initialize
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //change toolbar name and add button
        if (toolbar != null) {
            toolbar.setTitle("About");
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.hamburger_icon);
        }

        //toolbar button set listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        //add drawerlayout listener to change activity
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        Intent HomeIntent = new Intent(AboutActivity.this, HomeActivity.class);
                        startActivity(HomeIntent);
                        finish();
                        break;
                    case R.id.menu_Search:
                        Intent SearchIntent = new Intent(AboutActivity.this, SearchActivity.class);
                        startActivity(SearchIntent);
                        finish();
                        break;
                    case R.id.menu_Favourite:
                        Intent FavouriteIntent = new Intent(AboutActivity.this, FavouriteActivity.class);
                        startActivity(FavouriteIntent);
                        finish();
                        break;
                    case R.id.menu_sharing:
                        Intent SharingIntent = new Intent(AboutActivity.this, SharingActivity.class);
                        startActivity(SharingIntent);
                        finish();
                        break;
                    case R.id.menu_calorie:
                        Intent CalorieIntent = new Intent(AboutActivity.this, CalorieActivity.class);
                        startActivity(CalorieIntent);
                        finish();
                        break;
                    case R.id.menu_Logout:
                        Intent LogoutIntent = new Intent(AboutActivity.this, LogoutActivity.class);
                        startActivity(LogoutIntent);
                        finish();
                        break;
                    case R.id.menu_about:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                drawerLayout.closeDrawer(navigationView);
                return false;
            }
        });

        //get user email,show it in drawerlayout header
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserEmail = mAuth.getCurrentUser().getEmail();
        View headerView = navigationView.inflateHeaderView(R.layout.drawer_header);
        TextView email=(TextView)headerView.findViewById(R.id.drawerText1);
        email.setText(currentUserEmail);

        //change drawerlayout header portrait image
        DatabaseReference mRootRef;
        headerPortrait=(ImageView)headerView.findViewById(R.id.headerPortrait);
        final String uid = mAuth.getCurrentUser().getUid();
        mRootRef = FirebaseDatabase.getInstance().getReference().child(uid).child("Information");
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    //get information
                    PersonalInfo Info=dataSnapshot.getValue(PersonalInfo.class);
                    gender=Info.getGender();
                    if(gender.equals("Male")) {
                        headerPortrait.setImageResource(R.drawable.ic_portrait);
                    }else{
                        headerPortrait.setImageResource(R.drawable.ic_portrait1);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}