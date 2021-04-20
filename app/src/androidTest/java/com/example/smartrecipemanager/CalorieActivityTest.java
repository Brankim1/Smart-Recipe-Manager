package com.example.smartrecipemanager;

import android.app.Instrumentation;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;

import static androidx.lifecycle.Lifecycle.State.CREATED;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * author:Pengcheng Jin
 */
public class CalorieActivityTest {
    private ActivityScenario<CalorieActivity> scenario;
    private Instrumentation.ActivityMonitor am;
    @Before
    public void setUp() throws Exception {
        scenario = ActivityScenario.launch(CalorieActivity.class);
        scenario.moveToState(CREATED);
    }

    @Test
    public void setDrawerLayout() {
        scenario.onActivity(activity -> {
            DrawerLayout drawerLayout = activity.findViewById(R.id.drawer_layout);
            Toolbar toolbar = activity.findViewById(R.id.toolbar);
            assertThat(toolbar.getTitle().toString(), equalTo("Calorie"));
            assertFalse(drawerLayout.isDrawerOpen(GravityCompat.START));

        });
    }
}