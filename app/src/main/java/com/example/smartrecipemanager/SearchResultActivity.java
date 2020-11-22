package com.example.smartrecipemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartrecipemanager.Adapter.SearchListRecyclerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    String data;
    JSONArray recipeArray;
    List<Recipe> RecipeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Intent intent = getIntent();
        /*get data form intent*/
        data = intent.getStringExtra("data");
        setToolBar();
        getRecipes();
    }
    public void setToolBar() {
        //component initialize
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //change toolbar name and add button
        if (toolbar != null) {
            toolbar.setTitle(data+" Result");
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //toolbar button set listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    private void getRecipes() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.searchResultRecyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        //  StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        RecipeList = new ArrayList<Recipe>();
        String url;
        if (data.isEmpty()) {
            url = "https://api.spoonacular.com/recipes/random?number=30&instructionsRequired=true&apiKey="+getString(R.string.spoonacular_key);
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
                                    Recipe recipe = new Recipe();
                                    recipe.setId(jsonObject1.optString("id"));
                                    recipe.setTitle(jsonObject1.optString("title"));
                                    recipe.setPic(jsonObject1.optString("image"));
                                    RecipeList.add(recipe);
                                }
                                SearchListRecyclerAdapter myAdapter = new SearchListRecyclerAdapter(getApplicationContext(), RecipeList);
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
        } else {
            url = "https://api.spoonacular.com/recipes/search?number=30&instructionsRequired=true&apiKey="+getString(R.string.spoonacular_key)+"&query=" + data;

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                recipeArray = (JSONArray) response.get("results");
                                Log.i("the res is:", String.valueOf(recipeArray));
                                for (int i = 0; i < recipeArray.length(); i++) {
                                    JSONObject jsonObject1;
                                    jsonObject1 = recipeArray.getJSONObject(i);
                                    Recipe recipe = new Recipe();
                                    recipe.setId(jsonObject1.optString("id"));
                                    recipe.setTitle(jsonObject1.optString("title"));
                                    recipe.setPic("https://spoonacular.com/recipeImages/" + jsonObject1.optString("image"));
                                    RecipeList.add(recipe);
                                }
                                SearchListRecyclerAdapter myAdapter = new SearchListRecyclerAdapter(getApplicationContext(), RecipeList);
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
}