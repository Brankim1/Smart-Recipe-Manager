package com.example.smartrecipemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartrecipemanager.Adapter.RecipeViewPagerAdapter;
import com.example.smartrecipemanager.Adapter.SearchViewPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RecipeActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    String id;
    String title;
    String pic;
    TabLayout mytab;
    private ImageView img;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;
    private FloatingActionButton fab;
    private boolean like = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Intent intent = getIntent();
        /*get data form intent*/
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        pic = intent.getStringExtra("pic");
        img = findViewById(R.id.recipe_img);
        fab = findViewById(R.id.floatingActionButton);
        setToolBar();
        checkFavourite();
        getImage();
        getDetailInfo();


    }

    private void getDetailInfo() {
        String URL = "https://api.spoonacular.com/recipes/" + id + "/information?apiKey="+getString(R.string.spoonacular_key);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
            public void onResponse(JSONObject response) {
                Log.i("the response is: ", ""+response);
                setTabLayout(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("the res is error:", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void checkFavourite() {
        mAuth = FirebaseAuth.getInstance();
        final String uid = mAuth.getCurrentUser().getUid();
        mRootRef = FirebaseDatabase.getInstance().getReference().child(uid).child("favourite").child(id);
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("mRootRef", String.valueOf(dataSnapshot));
                if (dataSnapshot.getValue() != null) {
                    fab.setImageResource(R.drawable.like);
                    like = true;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like = !like;
                mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (like) {
                            fab.setImageResource(R.drawable.like);
                            Recipe recipe = new Recipe(id,title,pic);
                            mRootRef.setValue(recipe);
                        } else {
                            try {
                                fab.setImageResource(R.drawable.dislike);
                                mRootRef.setValue(null);
                            } catch (Exception e) {
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
    }

    private void getImage() {
        try {
            Picasso.get().load((String) pic).into(img);
        }
        catch (Exception e){

        }
    }

    public void setToolBar() {
        //component initialize
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //change toolbar name and add button
        if (toolbar != null) {
            toolbar.setTitle(title);
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        }

        //toolbar button set listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
            }
        });
    }
    private void setTabLayout(JSONObject response) {
        mytab = (TabLayout) findViewById(R.id.recipeTab);
        mytab.addTab(mytab.newTab().setText("ingredients"));
        mytab.addTab(mytab.newTab().setText("instructions"));
        mytab.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.recipe_pager);
        final RecipeViewPagerAdapter adapter = new RecipeViewPagerAdapter(getSupportFragmentManager(),mytab.getTabCount(),response);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mytab));

        mytab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}