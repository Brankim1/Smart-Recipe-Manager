package com.example.smartrecipemanager.fragement;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartrecipemanager.Adapter.RecipeInstructionsAdapter;
import com.example.smartrecipemanager.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * for RecipeInstructionsFragment in recipeActivity
 * it can handle data from recipeActivity and show recipe Instructions
 */
public class RecipeInstructionsFragment extends Fragment {
    private JSONArray instructionArr;
    private List instructionList = new ArrayList<>();
    private RecyclerView myrv;
    private View view;
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    public RecipeInstructionsFragment() {
        // Required empty public constructor
    }

    public static RecipeInstructionsFragment newInstance(String param1) {
        //get data from recipeViewPagerAdapter
        RecipeInstructionsFragment fragment = new RecipeInstructionsFragment();
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
                //handle the data to get instructions
                JSONObject result = new JSONObject(mParam1);
                instructionArr = (JSONArray) result.get("analyzedInstructions");
                JSONObject jsonObject1 = instructionArr.getJSONObject(0);
                JSONArray instructionArr2 = (JSONArray) jsonObject1.get("steps");
                for (int i = 0; i < instructionArr2.length(); i++) {
                    JSONObject jsonObject3;
                    jsonObject3 = instructionArr2.getJSONObject(i);
                    instructionList.add(jsonObject3.optString("step"));
                }
                Log.d("instruction list","is : "+instructionList);
            }
             catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recipe_instructions, container, false);
        myrv = (RecyclerView)view.findViewById(R.id.recipe_instructions);
        myrv.setLayoutManager(new GridLayoutManager(getContext(), 1));
        RecipeInstructionsAdapter myAdapter = new RecipeInstructionsAdapter(getContext(), instructionList);
        myrv.setAdapter(myAdapter);

        return view;
    }
}