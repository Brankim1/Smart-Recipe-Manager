package com.example.smartrecipemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.smartrecipemanager.Adapter.SharingPostListRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
* SharingActivity, get post from server and show
* */
public class SharingActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;
    List<Post> PostList;
    RecyclerView recyclerView;
    SharingPostListRecyclerAdapter myAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    String gender;
    ImageView headerPortrait;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);

        PostList = new ArrayList<Post>();
        recyclerView = (RecyclerView) findViewById(R.id.sharingRecyclerView);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);
        myAdapter = new SharingPostListRecyclerAdapter(getApplicationContext(), PostList);
        recyclerView.setAdapter(myAdapter);

        //register listener for swipeRefreshLayout, which can update post
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.sharingSwiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //get post from server
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
        PostList.clear();
        //get post from server
        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference().child("sharing");
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("mRootRef", String.valueOf(dataSnapshot));
                if (dataSnapshot.getValue() != null) {
                    //get post ids
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Post post=child.getValue(Post.class);
                        PostList.add(post);
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("sharingActivity","data get failed");
            }
        });
    }

    private void setFloatingActionButton() {
        //for add post
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sharing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete) {
            showListDialog();
        }
        return super.onOptionsItemSelected(item);
    }
    private void showListDialog(){
        List PostName=new ArrayList<String>();
        List PostId=new ArrayList<String>();
        for(int i=0;i<PostList.size();i++){
            String uid= PostList.get(i).getUserid();
            if(uid.equals(mAuth.getUid())){
                PostName.add(PostList.get(i).getTitle());
                PostId.add(PostList.get(i).getPostid());
            }
        }
        String[] item = new String[PostName.size()];
        String[] id = new String[PostId.size()];
        for(int i = 0; i < PostName.size();i++){
            item[i] = String.valueOf(PostName.get(i));
            id[i] = String.valueOf(PostId.get(i));
        }

        AlertDialog.Builder listDialog = new AlertDialog.Builder(SharingActivity.this);
        listDialog.setIcon(R.drawable.delete_icon);
        listDialog.setTitle("Please click data to delete");
        listDialog.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DatabaseReference  mRootRef2 = FirebaseDatabase.getInstance().getReference().child("sharing").child(id[which]);
                mRootRef2.removeValue();
                Toast.makeText(SharingActivity.this,"Remove successful "+item[which],Toast.LENGTH_LONG).show();
                setPostList();
            }
        });
        listDialog.show();
    }
}