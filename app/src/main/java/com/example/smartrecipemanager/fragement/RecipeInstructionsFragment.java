package com.example.smartrecipemanager.fragement;

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
import com.example.smartrecipemanager.Adapter.RecipeInstructionAdapter;
import com.example.smartrecipemanager.Ingredient;
import com.example.smartrecipemanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeInstructionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeInstructionsFragment extends Fragment {
    private JSONArray instructionArr;
    private List instructionList = new ArrayList<>();
    private RecyclerView myrv;
    private View view;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;


    public RecipeInstructionsFragment() {
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
    public static RecipeInstructionsFragment newInstance(String param1) {
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            try {
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
        RecipeInstructionAdapter myAdapter = new RecipeInstructionAdapter(getContext(), instructionList);
        myrv.setAdapter(myAdapter);
        myrv.setItemAnimator(new DefaultItemAnimator());
        return view;
    }
}