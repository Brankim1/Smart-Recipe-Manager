package com.example.smartrecipemanager;

import android.widget.Button;

import androidx.test.core.app.ActivityScenario;

import com.google.android.material.textfield.TextInputLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static androidx.lifecycle.Lifecycle.State.CREATED;

/**
 * author:Pengcheng Jin
 */
public class ResetActivityTest {
    private ActivityScenario<ResetActivity> scenario;
    @Before
    public void setUp() throws Exception {
        scenario = ActivityScenario.launch(ResetActivity.class);
        scenario.moveToState(CREATED);
    }

    @After
    public void tearDown() throws Exception {
    }
    @Test
    public void onCreate() {
        scenario.onActivity(activity -> {
            TextInputLayout textView = activity.findViewById(R.id.email3);
            textView.getEditText().setText("pengchengjin123@gmail.com");
            Button button =activity.findViewById(R.id.reset);
            button.performClick();
            //detect email whether receive reset password email
        });
    }
}