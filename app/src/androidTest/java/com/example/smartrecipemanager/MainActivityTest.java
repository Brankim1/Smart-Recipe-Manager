package com.example.smartrecipemanager;

import android.app.Instrumentation;

import androidx.test.core.app.ActivityScenario;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static androidx.lifecycle.Lifecycle.State.DESTROYED;
import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * author:Pengcheng Jin
 */
public class MainActivityTest {
    private ActivityScenario<MainActivity> scenario;
    private Instrumentation.ActivityMonitor am;
    @Before
    public void setUp() throws Exception {
        am = getInstrumentation().addMonitor("com.example.smartrecipemanager.HomeActivity", null, false);
        scenario = ActivityScenario.launch(MainActivity.class);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void onCreate() {
        assertTrue(DESTROYED.isAtLeast(scenario.getState()));
    }

    @Test
    public void onStart() {
        //test jump activity to homeactivity
        am.waitForActivityWithTimeout(5000);
        assertEquals(1, am.getHits());
    }

}