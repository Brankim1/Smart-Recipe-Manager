package com.example.smartrecipemanager;

import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.test.core.app.ActivityScenario;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static androidx.lifecycle.Lifecycle.State.CREATED;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * author:Pengcheng Jin
 */
public class AboutActivityTest {
    private ActivityScenario<AboutActivity> scenario;

    @Before
    public void setUp() throws Exception {
        scenario = ActivityScenario.launch(AboutActivity.class);
        scenario.moveToState(CREATED);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void onCreate() {
        scenario.onActivity(activity -> {
            TextView textView = activity.findViewById(R.id.textViewAbout);
            assertThat(textView.getText().toString(), equalTo("Thank you very much for participation in testing SmartRecipeManager.If you have any questions when testing, Just Email me or Call me! I am very glad to receive your feedback. "));
        });
    }

    @Test
    public void setDrawerLayout() {
        scenario.onActivity(activity -> {
           DrawerLayout drawerLayout = activity.findViewById(R.id.drawer_layout);
            Toolbar toolbar = activity.findViewById(R.id.toolbar);
            assertThat(toolbar.getTitle().toString(), equalTo("About"));
            assertFalse(drawerLayout.isDrawerOpen(GravityCompat.START));

        });
    }
}