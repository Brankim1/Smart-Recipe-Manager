package com.example.smartrecipemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartrecipemanager.Adapter.SearchListRecyclerAdapter;
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
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    String data;
    JSONArray recipeArray;
    List<Recipe> RecipeList;
    String vegan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Intent intent = getIntent();
        /*get data form intent*/
        data = intent.getStringExtra("data");
        setToolBar();
        getVegan();
    }

    private void getVegan() {
        vegan="";
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mRootRef;
        final String uid = mAuth.getCurrentUser().getUid();
        mRootRef = FirebaseDatabase.getInstance().getReference().child(uid).child("Information");
        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    //get vegan information from server
                    PersonalInfo Info = dataSnapshot.getValue(PersonalInfo.class);
                    vegan = Info.getVegan();
                    getRecipes();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private void getRecipes() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.searchResultRecyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        RecipeList = new ArrayList<Recipe>();
        String url;
        if (data.isEmpty()) {
            //for empty search, get random recipes
            url = "https://api.spoonacular.com/recipes/random?number=50&instructionsRequired=true&apiKey="+getString(R.string.spoonacular_key);
            if(vegan.equals("Vegan")) {
                //for vegan
                url = "https://api.spoonacular.com/recipes/random?number=50&instructionsRequired=true&apiKey="+getString(R.string.spoonacular_key)+"&tags=vegan";
            }
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
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("the res is error:", error.toString());
                                }

                            }
                    );
            requestQueue.add(jsonObjectRequest);
        } else {
            //for non empty search, get recipes
            url = "https://api.spoonacular.com/recipes/search?query=" + data+"&number=30&instructionsRequired=true&apiKey="+getString(R.string.spoonacular_key);
            if(vegan.equals("Vegan")) {
                //for vegan
                url = "https://api.spoonacular.com/recipes/search?query=" + data+"&number=30&instructionsRequired=true&apiKey="+getString(R.string.spoonacular_key)+"&diet="+vegan;
            }
            Log.d("searchResult","url is"+url);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                recipeArray = (JSONArray) response.get("results");
                                for (int i = 0; i < recipeArray.length(); i++) {
                                    JSONObject jsonObject1;
                                    jsonObject1 = recipeArray.getJSONObject(i);
                                    Log.d("the res is:","recipe array is "+ String.valueOf(jsonObject1));
                                    Recipe recipe = new Recipe();
                                    recipe.setId(jsonObject1.optString("id"));
                                    recipe.setTitle(jsonObject1.optString("title"));
                                    recipe.setPic("https://spoonacular.com/recipeImages/" + jsonObject1.optString("image"));
                                    RecipeList.add(recipe);
                                }
                                SearchListRecyclerAdapter myAdapter = new SearchListRecyclerAdapter(getApplicationContext(), RecipeList);
                                recyclerView.setAdapter(myAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("the res is error:", error.toString());
                                }

                            }
                    );
            requestQueue.add(jsonObjectRequest);
        }
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
}