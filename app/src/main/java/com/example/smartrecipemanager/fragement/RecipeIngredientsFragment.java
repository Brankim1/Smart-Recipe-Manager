package com.example.smartrecipemanager.fragement;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartrecipemanager.Adapter.RecipeIngredientAdapter;
import com.example.smartrecipemanager.Ingredient;
import com.example.smartrecipemanager.R;
import com.example.smartrecipemanager.RecipeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeIngredientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeIngredientsFragment extends Fragment {
    private JSONArray ingredientsArr;
    private List<Ingredient> ingredientsList = new ArrayList<Ingredient>();
    private RecyclerView myrv;
    private View view;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;

    public RecipeIngredientsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment IngredientsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeIngredientsFragment newInstance(String param1) {
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            Log.d("fragment 1 ","参数是"+mParam1);

            try {
                JSONObject result = new JSONObject(mParam1);
                Log.d("fragment 1 ","参数是"+result);
                ingredientsArr = (JSONArray) result.get("extendedIngredients");
                for (int i = 0; i < ingredientsArr.length(); i++) {
                    JSONObject jsonObject1;
                    jsonObject1 = ingredientsArr.getJSONObject(i);
                    Ingredient ingredient1=new Ingredient();
                    ingredient1.setName(jsonObject1.optString("originalString"));
                    ingredient1.setPic("https://spoonacular.com/cdn/ingredients_100x100/"+jsonObject1.optString("image"));
                    ingredientsList.add(ingredient1);
                    Log.d("ingredientsList","is: "+ingredientsList);
                    Log.d("ingredientsList","is: "+jsonObject1.optString("originalString"));
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
        RecipeIngredientAdapter myAdapter = new RecipeIngredientAdapter(getContext(), ingredientsList);
        myrv.setAdapter(myAdapter);
        myrv.setItemAnimator(new DefaultItemAnimator());
        return view;
    }
}