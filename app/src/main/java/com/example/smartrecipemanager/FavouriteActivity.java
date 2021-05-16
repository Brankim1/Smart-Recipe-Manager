package com.example.smartrecipemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.smartrecipemanager.Adapter.SearchListRecyclerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
/**FavouriteActivity
* get user's favorite recipe and show
 * from Firebase server
* */
public class FavouriteActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private FirebaseAuth auth;
    private String gender;
    private ImageView headerPortrait;
    private DatabaseReference rootRef;
    private List<Recipe> RecipeList;
    private RecyclerView recyclerView;
    private SearchListRecyclerAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        //check network connect
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            RecipeList = new ArrayList<Recipe>();
            recyclerView = (RecyclerView) findViewById(R.id.favouriteRecyclerView);
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new SearchListRecyclerAdapter(getApplicationContext(), RecipeList);
            recyclerView.setAdapter(adapter);

            //register listener for swipeRefreshLayout, which can update favorite recipes
            swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.favouriteSwiperefreshlayout);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getFavouriteID();
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(FavouriteActivity.this, "Update", Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);
                }
            });
            setDrawerLayout();
        }else{
            dialog("Sorry","Network connect fail, Please press OK to finish");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getFavouriteID();
    }

    /**
     * get user favorite recipes' id  from server
     * */
    private void getFavouriteID() {
        RecipeList.clear();
        //get favorite recipes id from server
        auth = FirebaseAuth.getInstance();
        final String uid = auth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference().child(uid).child("favourite");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    //get favourite ids
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Recipe recipe=child.getValue(Recipe.class);
                        RecipeList.add(recipe);
                    }
                    adapter.notifyDataSetChanged();
                }else{
                    dialog("Sorry","Your Favourite Recipes is empty. You can choose OK to Home");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FavouriteActivity.this, "Favorite get fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * dialog to figure out problem
     * */
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
                        Intent HomeIntent = new Intent(FavouriteActivity.this, HomeActivity.class);
                        startActivity(HomeIntent);
                        finish();
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    /**
     * set Drawer layout, which are the main navigation
     * set toolbar, which show activity name and back button
     * get user information(such as email, gender, vegan)
     * */
    public void setDrawerLayout(){
        //component initialize
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //change toolbar name and add button
        if (toolbar != null) {
            toolbar.setTitle("Favourite");
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
                        Intent HomeIntent = new Intent(FavouriteActivity.this, HomeActivity.class);
                        startActivity(HomeIntent);
                        finish();
                        break;
                    case R.id.menu_Search:
                        Intent SearchIntent = new Intent(FavouriteActivity.this, SearchActivity.class);
                        startActivity(SearchIntent);
                        finish();
                        break;
                    case R.id.menu_Favourite:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_sharing:
                        Intent SharingIntent = new Intent(FavouriteActivity.this, SharingActivity.class);
                        startActivity(SharingIntent);
                        finish();
                        break;
                    case R.id.menu_calorie:
                        Intent CalorieIntent = new Intent(FavouriteActivity.this, CalorieActivity.class);
                        startActivity(CalorieIntent);
                        finish();
                        break;
                    case R.id.menu_Logout:
                        Intent LogoutIntent = new Intent(FavouriteActivity.this, LogoutActivity.class);
                        startActivity(LogoutIntent);
                        finish();
                        break;
                    case R.id.menu_about:
                        Intent AboutIntent = new Intent(FavouriteActivity.this, AboutActivity.class);
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
        DatabaseReference mRootRef3;
        headerPortrait=(ImageView)headerView.findViewById(R.id.headerPortrait);
        final String uid = mAuth.getCurrentUser().getUid();
        mRootRef3 = FirebaseDatabase.getInstance().getReference().child(uid).child("Information");
        mRootRef3.addListenerForSingleValueEvent(new ValueEventListener() {
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
