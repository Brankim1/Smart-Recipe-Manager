package com.example.smartrecipemanager.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.smartrecipemanager.fragement.AiFragment;
import com.example.smartrecipemanager.fragement.IngredientsFragment;
import com.example.smartrecipemanager.fragement.StyleFragment;

import java.util.List;

public class SearchViewPagerAdapter extends FragmentPagerAdapter {
    private int list;
    public SearchViewPagerAdapter(FragmentManager fm, int list) {
        super(fm);
        this.list = list;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                StyleFragment styleFragment = new StyleFragment();
                return styleFragment;
            case 1:
                IngredientsFragment ingredientsFragment = new IngredientsFragment();
                return ingredientsFragment;
            case 2:
                AiFragment aiFragment = new AiFragment();
                return aiFragment;
            default:
                StyleFragment styleFragment2 = new StyleFragment();
                return styleFragment2;
        }
    }
    @Override
    public int getCount() {
        return list;
    }

}
