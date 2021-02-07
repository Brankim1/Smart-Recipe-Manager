package com.example.smartrecipemanager.fragement;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.smartrecipemanager.Adapter.IngredientListRecyclerAdapter;
import com.example.smartrecipemanager.Adapter.StyleListRecyclerAdapter;
import com.example.smartrecipemanager.R;
import com.example.smartrecipemanager.SearchResultActivity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IngredientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private Button searchButton;
    private List<String> ingredientsList;
    public IngredientsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IngredientsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IngredientsFragment newInstance(String param1, String param2) {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_ingredients, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.ingredientRecyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        //  StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        IngredientListRecyclerAdapter myAdapter = new IngredientListRecyclerAdapter(getContext());
        recyclerView.setAdapter(myAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        searchButton=(Button)root.findViewById(R.id.ingredientSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientsList = IngredientListRecyclerAdapter.ingredientsList;
                Log.d("ingredientFragment","ingredientsList is "+ingredientsList);
                if(ingredientsList.isEmpty()){
                    Toast.makeText(getActivity(), "Please select at least one ingredient", Toast.LENGTH_SHORT).show();
                }
                else{
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
                    Intent searchResultIntent = new Intent(getActivity(), SearchResultActivity.class);
                    searchResultIntent.putExtra("data",queryData);
                    searchResultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    startActivity(searchResultIntent);
                }
            }
        });
        return root;
    }
}