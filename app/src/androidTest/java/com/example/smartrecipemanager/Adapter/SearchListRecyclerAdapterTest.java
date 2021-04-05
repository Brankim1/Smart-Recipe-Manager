package com.example.smartrecipemanager.Adapter;

import com.example.smartrecipemanager.Recipe;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertTrue;

/**
 * author:Pengcheng Jin
 */
public class SearchListRecyclerAdapterTest {
    SearchListRecyclerAdapter myAdapter;
    @Before
    public void setUp() throws Exception {
        List<Recipe> RecipeList=new ArrayList<Recipe>();
        Recipe recipe=new Recipe();
        recipe.setTitle("abc");
        recipe.setPic("www.abc.com");
        recipe.setId("123");
        RecipeList.add(recipe);
        myAdapter = new SearchListRecyclerAdapter(getApplicationContext(),RecipeList);

    }
    @Test
    public void onCreateViewHolder() {
        assertTrue(myAdapter.getItemCount()==1);
    }


}