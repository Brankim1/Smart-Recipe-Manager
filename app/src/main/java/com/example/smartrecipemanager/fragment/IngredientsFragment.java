package com.example.smartrecipemanager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartrecipemanager.Adapter.IngredientListRecyclerAdapter;
import com.example.smartrecipemanager.R;
import com.example.smartrecipemanager.SearchResultActivity;

import java.util.List;

/**IngredientsFragment
 * IngredientsFragment in search Activity
 * it can choose different ingredients to show recipes
 */
public class IngredientsFragment extends Fragment {

    // the fragment initialization parameters
    private RecyclerView recyclerView;
    private Button searchButton;
    private List<String> ingredientsList;
    private IngredientListRecyclerAdapter myAdapter;
    private Intent searchResultIntent;
    public IngredientsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_ingredients, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.ingredientRecyclerView);
        //recyclerView have 3 columns
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(layoutManager);
        myAdapter = new IngredientListRecyclerAdapter(getContext());
        recyclerView.setAdapter(myAdapter);

        //register search button
        searchButton=(Button)root.findViewById(R.id.ingredientSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get data(which ingredient were selected) from recycler adapter
                ingredientsList = IngredientListRecyclerAdapter.ingredientsList;
                if(ingredientsList.isEmpty()){
                    Toast.makeText(getActivity(), "Please select at least one ingredient", Toast.LENGTH_SHORT).show();
                }
                else{
                    //reshape data that which ingredients were selected
                    String queryData="";
                    if(ingredientsList.size() == 1){
                        queryData= ingredientsList.get(0).toString();
                    }else {
                        for (int i = 0; i < ingredientsList.size() ; i++) {
                            if(i == 0){
                                queryData = ingredientsList.get(i).toString();
                            }else {
                                queryData = queryData +"," + ingredientsList.get(i).toString();
                            }
                        }}
                    //go to SearchResultActivity
                    searchResultIntent = new Intent(getActivity(), SearchResultActivity.class);
                    searchResultIntent.putExtra("data",queryData);
                    searchResultIntent.putExtra("ingredient","ingredientSearch");
                    searchResultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    startActivity(searchResultIntent);
                }
            }
        });
        return root;
    }
}