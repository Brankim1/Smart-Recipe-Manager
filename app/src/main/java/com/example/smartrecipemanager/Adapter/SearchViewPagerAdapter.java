package com.example.smartrecipemanager.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.smartrecipemanager.fragment.AiFragment;
import com.example.smartrecipemanager.fragment.IngredientsFragment;
import com.example.smartrecipemanager.fragment.StyleFragment;


/**SearchViewPagerAdapter
 * for swipe three fragment (StyleFragment and IngredientsFragment and AiFragment) in searchActivity.
 * */
public class SearchViewPagerAdapter extends FragmentPagerAdapter {
    private int list;
    private StyleFragment styleFragment;
    private IngredientsFragment ingredientsFragment;
    private AiFragment aiFragment;
    public SearchViewPagerAdapter(FragmentManager fm, int list) {
        super(fm);
        this.list = list;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                styleFragment = new StyleFragment();
                return styleFragment;
            case 1:
                ingredientsFragment = new IngredientsFragment();
                return ingredientsFragment;
            case 2:
                aiFragment = new AiFragment();
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
