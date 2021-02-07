package com.example.smartrecipemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartrecipemanager.Adapter.SearchListRecyclerAdapter;
import com.example.smartrecipemanager.Adapter.SharingPostListRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SharingActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;
    List<Post> PostList;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    String gender;
    ImageView headerPortrait;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);

        recyclerView = (RecyclerView) findViewById(R.id.sharingRecyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.sharingSwiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setPostList();
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(SharingActivity.this, "Update", Toast.LENGTH_SHORT).show();
                    }
                }, 2000);
            }
        });
        setDrawerLayout();
        setFloatingActionButton();

    }
    @Override
    protected void onResume() {
        super.onResume();
        setPostList();
    }
    private void setPostList() {
        PostList=new ArrayList<Post>();
        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference().child("sharing");
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("mRootRef", String.valueOf(dataSnapshot));
                if (dataSnapshot.getValue() != null) {
                    //get favourite ids
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Post post=child.getValue(Post.class);
                        PostList.add(post);
                    }
                    Log.d("sharingActivity","data get successful");
                    SharingPostListRecyclerAdapter myAdapter = new SharingPostListRecyclerAdapter(getApplicationContext(),PostList);
                    recyclerView.setAdapter(myAdapter);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("sharingActivity","data get failed");
            }
        });
    }

    private void setFloatingActionButton() {
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab_sharing);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AddIntent = new Intent(SharingActivity.this, SharingAddActivity.class);
                startActivity(AddIntent);
            }
        });
    }


    public void setDrawerLayout(){
        //component initialize
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //change toolbar name and add button
        if (toolbar != null) {
            toolbar.setTitle("Sharing");
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
                        Intent HomeIntent = new Intent(SharingActivity.this, HomeActivity.class);
                        startActivity(HomeIntent);
                        finish();
                        break;
                    case R.id.menu_Search:
                        Intent SearchIntent = new Intent(SharingActivity.this, SearchActivity.class);
                        startActivity(SearchIntent);
                        finish();
                        break;
                    case R.id.menu_Favourite:
                        Intent FavouriteIntent = new Intent(SharingActivity.this, FavouriteActivity.class);
                        startActivity(FavouriteIntent);
                        finish();
                        break;
                    case R.id.menu_sharing:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_calorie:
                        Intent CalorieIntent = new Intent(SharingActivity.this, CalorieActivity.class);
                        startActivity(CalorieIntent);
                        finish();
                        break;
                    case R.id.menu_Logout:
                        Intent LogoutIntent = new Intent(SharingActivity.this, LogoutActivity.class);
                        startActivity(LogoutIntent);
                        finish();
                        break;
                    case R.id.menu_about:
                        Intent AboutIntent = new Intent(SharingActivity.this, AboutActivity.class);
                        startActivity(AboutIntent);
                        finish();
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
        DatabaseReference mRootRef2;
        headerPortrait=(ImageView)headerView.findViewById(R.id.headerPortrait);
        final String uid = mAuth.getCurrentUser().getUid();
        mRootRef2 = FirebaseDatabase.getInstance().getReference().child(uid).child("Information");
        mRootRef2.addListenerForSingleValueEvent(new ValueEventListener() {
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