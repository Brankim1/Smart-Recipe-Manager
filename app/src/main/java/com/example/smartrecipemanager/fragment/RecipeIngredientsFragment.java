package com.example.smartrecipemanager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartrecipemanager.Adapter.RecipeIngredientsAdapter;
import com.example.smartrecipemanager.Ingredient;
import com.example.smartrecipemanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**RecipeIngredientsFragment
 * for RecipeIngredientsFragment in recipeActivity
 * it can handle data from recipeActivity and show recipe ingredients
 */
public class RecipeIngredientsFragment extends Fragment {
    private JSONArray ingredientsArr;
    private List<Ingredient> ingredientsList = new ArrayList<Ingredient>();
    private RecyclerView myrv;
    private View view;
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    public RecipeIngredientsFragment() {
        // Required empty public constructor
    }

    public static RecipeIngredientsFragment newInstance(String param1) {
        //get data from recipeViewPagerAdapter
        RecipeIngredientsFragment fragment = new RecipeIngredientsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //reshape data that from recipeViewPagerAdapter
            mParam1 = getArguments().getString(ARG_PARAM1);
            try {
                //handle the data to get ingredients
                JSONObject result = new JSONObject(mParam1);
                ingredientsArr = (JSONArray) result.get("extendedIngredients");
                for (int i = 0; i < ingredientsArr.length(); i++) {
                    JSONObject jsonObject1;
                    jsonObject1 = ingredientsArr.getJSONObject(i);
                    Ingredient ingredient1=new Ingredient();
                    ingredient1.setName(jsonObject1.optString("originalString"));
                    ingredient1.setPic("https://spoonacular.com/cdn/ingredients_100x100/"+jsonObject1.optString("image"));
                    ingredientsList.add(ingredient1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);
        myrv = (RecyclerView)view.findViewById(R.id.recipe_ingredients);
        myrv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        RecipeIngredientsAdapter myAdapter = new RecipeIngredientsAdapter(getContext(), ingredientsList);
        myrv.setAdapter(myAdapter);

        return view;
    }
}