package com.example.smartrecipemanager;

import android.app.Instrumentation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.test.core.app.ActivityScenario;

import com.google.android.material.textfield.TextInputLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static androidx.lifecycle.Lifecycle.State.CREATED;
import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * author:Pengcheng Jin
 */
public class LoginActivityTest {
    private ActivityScenario<LoginActivity> scenario;
    private Instrumentation.ActivityMonitor am;
    @Before
    public void setUp() throws Exception {
        am = getInstrumentation().addMonitor("com.example.smartrecipemanager.MainActivity", null, false);
        scenario = ActivityScenario.launch(LoginActivity.class);
        scenario.moveToState(CREATED);
    }

    @After
    public void tearDown() throws Exception {
    }
    @Test
    public void onCreate() {
        scenario.onActivity(activity -> {
            ImageView imageView = activity.findViewById(R.id.imageLogin);
            assertNotNull(imageView.getDrawable());
        });

    }

    @Test
    public void onClick() {
        scenario.onActivity(activity -> {
            TextInputLayout textView = activity.findViewById(R.id.email);
            TextInputLayout textView2 = activity.findViewById(R.id.password);
            textView.getEditText().setText("pengchengjin123@gmail.com");
            textView2.getEditText().setText("Jin15229161606");
            Button button =activity.findViewById(R.id.login);
            button.performClick();
            //test jump activity to Mainactivity
            am.waitForActivityWithTimeout(5000);
            assertEquals(0, am.getHits());
        });
    }
}