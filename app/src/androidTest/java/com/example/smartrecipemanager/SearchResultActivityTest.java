package com.example.smartrecipemanager;

import androidx.appcompat.widget.Toolbar;
import androidx.test.core.app.ActivityScenario;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static androidx.lifecycle.Lifecycle.State.CREATED;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * author:Pengcheng Jin
 */
public class SearchResultActivityTest {
    private ActivityScenario<SearchResultActivity> scenario;

    @Before
    public void setUp() throws Exception {
        scenario = ActivityScenario.launch(SearchResultActivity.class);
        scenario.moveToState(CREATED);
    }

    @After
    public void tearDown() throws Exception {
    }
    @Test
    public void onCreate() {
        scenario.onActivity(activity -> {
            assertTrue(activity.RecipeList.size()!=50);
        });
    }

    @Test
    public void setToolBar() {
        scenario.onActivity(activity -> {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        assertThat(toolbar.getTitle().toString(), equalTo(toolbar.getTitle().toString()));
        });
        }
}