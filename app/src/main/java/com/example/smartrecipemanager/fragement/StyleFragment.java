package com.example.smartrecipemanager.fragement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartrecipemanager.Adapter.StyleListRecyclerAdapter;
import com.example.smartrecipemanager.R;

/**StyleFragment
 * StyleFragment for searchActivity
 * it can search style recipes, such as chinese, English food
 */
public class StyleFragment extends Fragment {
    // the fragment initialization parameters
   private RecyclerView recyclerView;

    public StyleFragment() {
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
        View root =inflater.inflate(R.layout.fragment_style, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.styleRecyclerView);
        //recyclerView have 2 columns
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        StyleListRecyclerAdapter myAdapter = new StyleListRecyclerAdapter(getContext());
        recyclerView.setAdapter(myAdapter);

        return root;
    }
}