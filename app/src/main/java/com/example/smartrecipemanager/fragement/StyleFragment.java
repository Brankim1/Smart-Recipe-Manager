package com.example.smartrecipemanager.fragement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartrecipemanager.Adapter.SearchListRecyclerAdapter;
import com.example.smartrecipemanager.Adapter.StyleListRecyclerAdapter;
import com.example.smartrecipemanager.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StyleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StyleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    public StyleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StyleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StyleFragment newInstance(String param1, String param2) {
        StyleFragment fragment = new StyleFragment();
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
        Log.d("abc","style is ok");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_style, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.styleRecyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        //  StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        StyleListRecyclerAdapter myAdapter = new StyleListRecyclerAdapter(getContext());
        recyclerView.setAdapter(myAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return root;
    }
}