package com.example.smartrecipemanager.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.smartrecipemanager.fragement.AiFragment;
import com.example.smartrecipemanager.fragement.IngredientsFragment;
import com.example.smartrecipemanager.fragement.RecipeIngredientsFragment;
import com.example.smartrecipemanager.fragement.RecipeInstructionsFragment;
import com.example.smartrecipemanager.fragement.StyleFragment;

import org.json.JSONObject;

public class RecipeViewPagerAdapter extends FragmentPagerAdapter {
    private int list;
    private JSONObject response;
    public RecipeViewPagerAdapter(FragmentManager fm, int list, JSONObject response) {
        super(fm);
        this.list = list;
        this.response=response;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                RecipeIngredientsFragment recipeIngredientsFragment = new RecipeIngredientsFragment();
                return recipeIngredientsFragment.newInstance(response.toString());
            case 1:
                RecipeInstructionsFragment recipeInstructionsFragment = new RecipeInstructionsFragment();
                return recipeInstructionsFragment.newInstance(response.toString());
            default:
                RecipeIngredientsFragment recipeIngredientsFragment2 = new RecipeIngredientsFragment();
                return recipeIngredientsFragment2.newInstance(response.toString());
        }
    }
    @Override
    public int getCount() {
        return list;
    }

}
