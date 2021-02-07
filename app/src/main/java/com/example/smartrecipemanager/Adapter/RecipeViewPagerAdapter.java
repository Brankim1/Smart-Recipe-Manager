package com.example.smartrecipemanager.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.smartrecipemanager.fragement.RecipeIngredientsFragment;
import com.example.smartrecipemanager.fragement.RecipeInstructionsFragment;
import org.json.JSONObject;

/*
* for swipe two fragment (RecipeIngredientsFragment and RecipeInstructionsFragment) in recipeActivity.
* */
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
                //go to RecipeIngredientsFragment
                RecipeIngredientsFragment recipeIngredientsFragment = new RecipeIngredientsFragment();
                return recipeIngredientsFragment.newInstance(response.toString());
            case 1:
                //go to RecipeInstructionsFragment
                RecipeInstructionsFragment recipeInstructionsFragment = new RecipeInstructionsFragment();
                return recipeInstructionsFragment.newInstance(response.toString());
            default:
                //default go to RecipeIngredientsFragment
                RecipeIngredientsFragment recipeIngredientsFragment2 = new RecipeIngredientsFragment();
                return recipeIngredientsFragment2.newInstance(response.toString());
        }
    }

    @Override
    public int getCount() {
        return list;
    }
}
