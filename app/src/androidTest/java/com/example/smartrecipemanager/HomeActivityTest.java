package com.example.smartrecipemanager;

import android.app.Instrumentation;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.test.core.app.ActivityScenario;

import com.google.android.material.tabs.TabLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static androidx.lifecycle.Lifecycle.State.CREATED;
import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * author:Pengcheng Jin
 */
public class HomeActivityTest {
    private ActivityScenario<HomeActivity> scenario;
    private Instrumentation.ActivityMonitor am;
    @Before
    public void setUp() throws Exception {
        am = getInstrumentation().addMonitor("com.example.smartrecipemanager.MainActivity", null, false);
        scenario = ActivityScenario.launch(HomeActivity.class);
        scenario.moveToState(CREATED);
    }

    @After
    public void tearDown() throws Exception {
    }
    @Test
    public void onCreate() {
        scenario.onActivity(activity -> {
            assertTrue(activity.RecipeList.size()!=20);
           });
    }



    @Test
    public void setDrawerLayout() {
        scenario.onActivity(activity -> {
            DrawerLayout drawerLayout = activity.findViewById(R.id.drawer_layout);
            Toolbar toolbar = activity.findViewById(R.id.toolbar);
            assertThat(toolbar.getTitle().toString(), equalTo("Home"));
            assertFalse(drawerLayout.isDrawerOpen(GravityCompat.START));

            TabLayout mytab =  activity.findViewById(R.id.homeTab);
            for(int i=0;i<100;i++){
                mytab.performClick();
            }

        });
    }
}