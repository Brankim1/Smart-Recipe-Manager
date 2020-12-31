package com.example.smartrecipemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartrecipemanager.Adapter.SearchListRecyclerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TabLayout mytab;
    JSONArray recipeArray;
    List<Recipe> RecipeList;
    String style;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        style="breakfast";
        recyclerView = (RecyclerView) findViewById(R.id.homeRecyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        //  StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getRandomRecipes();
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(HomeActivity.this, "Update", Toast.LENGTH_SHORT).show();
                    }
                }, 2000);
            }
        });
        setDrawerLayout();
        setTabLayout();
        getRandomRecipes();
    }

    private void setTabLayout() {
        mytab = (TabLayout) findViewById(R.id.homeTab);

        mytab.addTab(mytab.newTab().setText("Breakfast"));
        mytab.addTab(mytab.newTab().setText("Lunch"));
        mytab.addTab(mytab.newTab().setText("Dinner"));

        mytab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals("Breakfast")){
                    style="breakfast";
                    getRandomRecipes();
                }
                if(tab.getText().equals("Lunch")){
                    style="lunch";
                    getRandomRecipes();
                }
                if(tab.getText().equals("Dinner")){
                    style="dinner";
                    getRandomRecipes();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(tab.getText().equals("Breakfast")){
                    style="breakfast";
                    getRandomRecipes();
                }
                if(tab.getText().equals("Lunch")){
                    style="lunch";
                    getRandomRecipes();
                }
                if(tab.getText().equals("Dinner")){
                    style="dinner";
                    getRandomRecipes();
                }
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
            toolbar.setTitle("Home");
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                        drawerLayout.closeDrawer(GravityCompat.START);//if choose the same activity, just close drawer
                        break;
                    case R.id.menu_Search:
                        Intent SearchIntent = new Intent(HomeActivity.this, SearchActivity.class);
                        startActivity(SearchIntent);
                        finish();
                        break;
                    case R.id.menu_Favourite:
                        Intent FavouriteIntent = new Intent(HomeActivity.this, FavouriteActivity.class);
                        startActivity(FavouriteIntent);
                        finish();
                        break;
                    case R.id.menu_sharing:
                        Intent SharingIntent = new Intent(HomeActivity.this, SharingActivity.class);
                        startActivity(SharingIntent);
                        finish();
                        break;
                    case R.id.menu_calorie:
                        Intent CalorieIntent = new Intent(HomeActivity.this, CalorieActivity.class);
                        startActivity(CalorieIntent);
                        finish();
                        break;
                    case R.id.menu_Logout:
                        Intent LogoutIntent = new Intent(HomeActivity.this, LogoutActivity.class);
                        startActivity(LogoutIntent);
                        finish();
                        break;
                    case R.id.menu_about:
                        Intent AboutIntent = new Intent(HomeActivity.this, AboutActivity.class);
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
    }

    private void getRandomRecipes() {

        RecipeList = new ArrayList<Recipe>();
        String url = "https://api.spoonacular.com/recipes/random?number=5&instructionsRequired=true&apiKey="+getString(R.string.spoonacular_key)+"&tags="+style;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            recipeArray = (JSONArray) response.get("recipes");
                            Log.i("the res is:", String.valueOf(recipeArray));
                            for (int i = 0; i < recipeArray.length(); i++) {
                                JSONObject jsonObject1;
                                jsonObject1 = recipeArray.getJSONObject(i);
                                Recipe recipe=new Recipe();
                                recipe.setId(jsonObject1.optString("id"));
                                recipe.setTitle(jsonObject1.optString("title"));
                                recipe.setPic(jsonObject1.optString("image"));
                                RecipeList.add(recipe);
                            }
                            SearchListRecyclerAdapter myAdapter = new SearchListRecyclerAdapter(getApplicationContext(),RecipeList);
                            recyclerView.setAdapter(myAdapter);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
//                            progressBar.setVisibility(View.GONE);
//                            RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(getContext(), lstRecipe);
//                            myrv.setAdapter(myAdapter);
//                            myrv.setItemAnimator(new DefaultItemAnimator());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("the res is error:", error.toString());
//                        progressBar.setVisibility(View.GONE);
//                        myrv.setAlpha(0);
//                        emptyView.setVisibility(View.VISIBLE);
                    }

                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}