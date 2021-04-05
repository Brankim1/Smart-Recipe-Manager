package com.example.smartrecipemanager;

import android.app.Instrumentation;

import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

/**
 * author:Pengcheng Jin
 */
public class LogoutActivityTest {
    private ActivityScenario<LoginActivity> scenario;
    private Instrumentation.ActivityMonitor am;
    @Before
    public void setUp() throws Exception {
        am = getInstrumentation().addMonitor("com.example.smartrecipemanager.LoginActivity", null, false);
        scenario = ActivityScenario.launch(LoginActivity.class);
    }
    @Test
    public void onCreate() {
        am.waitForActivityWithTimeout(5000);
        assertEquals(0, am.getHits());
    }

}