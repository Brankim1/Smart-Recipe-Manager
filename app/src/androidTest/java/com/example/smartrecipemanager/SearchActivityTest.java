package com.example.smartrecipemanager;

import android.app.Instrumentation;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.test.core.app.ActivityScenario;

import com.google.android.material.textfield.TextInputLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static androidx.lifecycle.Lifecycle.State.CREATED;
import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * author:Pengcheng Jin
 */
public class SearchActivityTest {
    private ActivityScenario<SearchActivity> scenario;
    private Instrumentation.ActivityMonitor am;
    @Before
    public void setUp() throws Exception {
        am = getInstrumentation().addMonitor("com.example.smartrecipemanager.SearchResultActivity", null, false);
        scenario = ActivityScenario.launch(SearchActivity.class);
        scenario.moveToState(CREATED);
    }

    @After
    public void tearDown() throws Exception {
    }
    @Test
    public void onCreate() {
        scenario.onActivity(activity -> {
            TextInputLayout searchText= activity.findViewById(R.id.searchText);
            Button searchButton=activity.findViewById(R.id.searchButton);
            searchText.getEditText().setText("beef");
            searchButton.performClick();
            //test jump activity to Mainactivity
            am.waitForActivityWithTimeout(5000);
            assertEquals(1, am.getHits());
        });

    }

    @Test
    public void setDrawerLayout() {
        scenario.onActivity(activity -> {
            DrawerLayout drawerLayout = activity.findViewById(R.id.drawer_layout);
            Toolbar toolbar = activity.findViewById(R.id.toolbar);
            assertThat(toolbar.getTitle().toString(), equalTo("Search"));
            assertFalse(drawerLayout.isDrawerOpen(GravityCompat.START));
        });
    }
}