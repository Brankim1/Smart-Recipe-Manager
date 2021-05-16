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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**HomeActivity
* show random recipes(breakfast,lunch,dinner)
 * use waterfall showing
* */
public class HomeActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private TabLayout tab;
    private JSONArray recipeArray;
    public List<Recipe> RecipeList;
    private String style;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private SearchListRecyclerAdapter adapter;
    private int lastVisibleItem;
    private String gender;
    private String vegan;
    private ImageView headerPortrait;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        style="breakfast";
        //check network connect
        ConnectivityManager  manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            RecipeList = new ArrayList<Recipe>();
            //waterfall showing image
            recyclerView = (RecyclerView) findViewById(R.id.homeRecyclerView);
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new SearchListRecyclerAdapter(getApplicationContext(),RecipeList);
            recyclerView.setAdapter(adapter);

            //load more recipes
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    //if visible recyclerview id is last one and no scroll, update
                    if (newState ==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 ==adapter.getItemCount()) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Loading...", Toast.LENGTH_SHORT).show();
                                getRandomRecipes(false);
                                adapter.notifyDataSetChanged();
                            }
                        },1000);
                    }
                }
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView,dx, dy);
                    //get visible recyclerview id
                    int[] visibleList =layoutManager.findLastCompletelyVisibleItemPositions(null);
                    Arrays.sort(visibleList);
                    lastVisibleItem= (int) visibleList[visibleList.length-1];
                }
            });
            //register listener for swipeRefreshLayout, which can update random recipes
            swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swiperefreshlayout);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getRandomRecipes(true);
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(HomeActivity.this, "Update", Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);
                }
            });
            setDrawerLayout();
            setTabLayout();
            getRandomRecipes(true);
        }else{
            dialog("Sorry","Network connect fail, Please press OK to finish");
        }

    }

    /**
     * for breakfast, lunch, dinner change
     * */
    private void setTabLayout() {
        tab = (TabLayout) findViewById(R.id.homeTab);
        tab.addTab(tab.newTab().setText("Breakfast"));
        tab.addTab(tab.newTab().setText("Lunch"));
        tab.addTab(tab.newTab().setText("Dinner"));

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals("Breakfast")){
                    style="breakfast";
                    getRandomRecipes(true);
                }
                if(tab.getText().equals("Lunch")){
                    style="lunch";
                    getRandomRecipes(true);
                }
                if(tab.getText().equals("Dinner")){
                    style="dinner";
                    getRandomRecipes(true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //update random recipes
                if(tab.getText().equals("Breakfast")){
                    style="breakfast";
                    getRandomRecipes(true);
                }
                if(tab.getText().equals("Lunch")){
                    style="lunch";
                    getRandomRecipes(true);
                }
                if(tab.getText().equals("Dinner")){
                    style="dinner";
                    getRandomRecipes(true);
                }
            }
        });
    }

    /**
     * get random recipes from spoonacular api,
     * variable clear is judge load more or update all
     * */
    private void getRandomRecipes(boolean clear) {
        //get random recipes from spoonacular API
        if(clear==true){
            RecipeList.clear();
        }
        String url = "https://api.spoonacular.com/recipes/random?number=20&instructionsRequired=true&apiKey="+getString(R.string.spoonacular_key)+"&tags="+style;
        if(vegan.equals("Vegan")) {
           //for vegan
            url = "https://api.spoonacular.com/recipes/random?number=20&instructionsRequired=true&apiKey="+getString(R.string.spoonacular_key)+"&tags="+style+",vegan";
        }
        //get random recipes from spoonacular api
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            recipeArray = (JSONArray) response.get("recipes");
                            for (int i = 0; i < recipeArray.length(); i++) {
                                JSONObject jsonObject1;
                                jsonObject1 = recipeArray.getJSONObject(i);
                                Recipe recipe=new Recipe();
                                recipe.setId(jsonObject1.optString("id"));
                                recipe.setTitle(jsonObject1.optString("title"));
                                recipe.setPic(jsonObject1.optString("image"));
                                RecipeList.add(recipe);
                            }
                            //send recipe data to adapter for show
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            dialog("Sorry","Query recipes failed, Please choose OK to restart");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog("Sorry","Query recipes error, Please choose OK to restart");
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }


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
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
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
            toolbar.setTitle("Home");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
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
        vegan="Not Vegan";
        //get user email,show it in drawerlayout header
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mRootRef;
        String currentUserEmail = mAuth.getCurrentUser().getEmail();
        View headerView = navigationView.inflateHeaderView(R.layout.drawer_header);
        TextView email=(TextView)headerView.findViewById(R.id.drawerText1);
        email.setText(currentUserEmail);

        //change drawerlayout header portrait image
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
                    vegan=Info.getVegan();
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
