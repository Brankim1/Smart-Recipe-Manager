package com.example.smartrecipemanager;

import android.app.Instrumentation;
import android.widget.ImageView;

import androidx.test.core.app.ActivityScenario;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.junit.Before;
import org.junit.Test;

import static androidx.lifecycle.Lifecycle.State.CREATED;
import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

/**
 * author:Pengcheng Jin
 */
public class RecipeActivityTest {
    private ActivityScenario<RecipeActivity> scenario;
    private Instrumentation.ActivityMonitor am;
    @Before
    public void setUp() throws Exception {
        am = getInstrumentation().addMonitor("com.example.smartrecipemanager.RecipeActivity", null, false);
        scenario = ActivityScenario.launch(RecipeActivity.class);
        scenario.moveToState(CREATED);
    }
    @Test
    public void onCreate() {
        scenario.onActivity(activity -> {
            ImageView imageView = activity.findViewById(R.id.recipe_img);
            assertNotNull(imageView.getDrawable());


            FloatingActionButton fab = activity.findViewById(R.id.floatingActionButton);
            am.waitForActivityWithTimeout(5000);
            for(int i=0;i<100;i++){
                fab.performClick();
            }
        });

    }

}