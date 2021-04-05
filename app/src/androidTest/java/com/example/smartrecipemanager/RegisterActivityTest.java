package com.example.smartrecipemanager;

import android.app.Instrumentation;
import android.widget.Button;

import androidx.test.core.app.ActivityScenario;

import com.google.android.material.textfield.TextInputLayout;

import org.junit.Before;
import org.junit.Test;

import static androidx.lifecycle.Lifecycle.State.CREATED;
import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

/**
 * author:Pengcheng Jin
 */
public class RegisterActivityTest {
    private ActivityScenario<LoginActivity> scenario;
    private Instrumentation.ActivityMonitor am;
    @Before
    public void setUp() throws Exception {
        am = getInstrumentation().addMonitor("com.example.smartrecipemanager.MainActivty", null, false);
        scenario = ActivityScenario.launch(LoginActivity.class);
        scenario.moveToState(CREATED);
    }
    @Test
    public void onCreate() {
        scenario.onActivity(activity -> {
            TextInputLayout textView = activity.findViewById(R.id.email2);
            TextInputLayout textView2 = activity.findViewById(R.id.password2);
            textView.getEditText().setText("test@gmail.com");
            textView2.getEditText().setText("123456");
            Button button =activity.findViewById(R.id.register);
            button.performClick();
            //test jump activity to Mainactivity
            am.waitForActivityWithTimeout(5000);
            assertEquals(0, am.getHits());
        });
    }
}