package com.example.smartrecipemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
/**SearchResultActivity
 * show search results by recyclerView
 * */
public class SearchResultActivity extends AppCompatActivity {
    private ConnectivityManager manager;
    private NetworkInfo networkInfo;
    private String data;
    private JSONArray recipeArray;
    private  List<Recipe> RecipeList;
    private RecyclerView recyclerView;
    private SearchListRecyclerAdapter myAdapter;
    private String vegan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Intent intent = getIntent();
        /*get data form search activity*/
        data = intent.getStringExtra("data");
        RecipeList = new ArrayList<Recipe>();

        recyclerView = (RecyclerView) findViewById(R.id.searchResultRecyclerView);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);
        myAdapter = new SearchListRecyclerAdapter(getApplicationContext(), RecipeList);
        recyclerView.setAdapter(myAdapter);

        setToolBar();
        manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            getVegan();
        }else{
            dialog("Sorry","Network connect fail, Please press OK to finish");
        }

    }

    /**
     * get user information whether is vegan(from server)
     * */
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
                Toast.makeText(SearchResultActivity.this,"vegan get fail",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * search recipe in spoonacular api,and handle backed data
     * */
    private void getRecipes() {
        String url;
        if (data.isEmpty()) {
            //for empty search, get random recipes
            url = "https://api.spoonacular.com/recipes/random?number=30&instructionsRequired=true&apiKey="+getString(R.string.spoonacular_key);
            if(vegan.equals("Vegan")) {
                //for vegan
                url = "https://api.spoonacular.com/recipes/random?number=30&instructionsRequired=true&apiKey="+getString(R.string.spoonacular_key)+"&tags=vegan";
            }
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
                                    Recipe recipe = new Recipe();
                                    recipe.setId(jsonObject1.optString("id"));
                                    recipe.setTitle(jsonObject1.optString("title"));
                                    recipe.setPic(jsonObject1.optString("image"));
                                    RecipeList.add(recipe);
                                }
                                myAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                Toast.makeText(SearchResultActivity.this,"recipes get fail",Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(SearchResultActivity.this,"recipes get fail",Toast.LENGTH_SHORT).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(String.valueOf(response.get("totalResults")).equals("0")){
                                    dialog("Sorry","there is no result for your search, Please choose OK to back");
                                }else{
                                    recipeArray = (JSONArray) response.get("results");
                                    for (int i = 0; i < recipeArray.length(); i++) {
                                        JSONObject jsonObject1;
                                        jsonObject1 = recipeArray.getJSONObject(i);
                                        Recipe recipe = new Recipe();
                                        recipe.setId(jsonObject1.optString("id"));
                                        recipe.setTitle(jsonObject1.optString("title"));
                                        recipe.setPic("https://spoonacular.com/recipeImages/" + jsonObject1.optString("image"));
                                        RecipeList.add(recipe);
                                    }
                                    myAdapter.notifyDataSetChanged();
                                }
                            }catch (JSONException e) {
                                Toast.makeText(SearchResultActivity.this,"recipes get fail",Toast.LENGTH_SHORT).show();
                                dialog("Sorry","Query recipes failed, Please choose OK to back");
                            }
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(SearchResultActivity.this,"recipes get fail",Toast.LENGTH_SHORT).show();
                                    dialog("Sorry","Query recipes failed, Please choose OK to back");
                                }

                            }
                    );
            requestQueue.add(jsonObjectRequest);
        }
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
                        finish();
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
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