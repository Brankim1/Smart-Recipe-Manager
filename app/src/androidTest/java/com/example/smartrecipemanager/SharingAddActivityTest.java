package com.example.smartrecipemanager;

import androidx.test.core.app.ActivityScenario;

import com.google.android.material.textfield.TextInputLayout;

import org.junit.Before;
import org.junit.Test;

import static androidx.lifecycle.Lifecycle.State.CREATED;
import static org.junit.Assert.assertTrue;

/**
 * author:Pengcheng Jin
 */
public class SharingAddActivityTest {
    private ActivityScenario<SharingAddActivity> scenario;
    @Before
    public void setUp() throws Exception {
        scenario = ActivityScenario.launch(SharingAddActivity.class);
        scenario.moveToState(CREATED);
    }

    @Test
    public void onCreate() {
        scenario.onActivity(activity -> {
            TextInputLayout textView = activity.findViewById(R.id.title);
            assertTrue(textView.getEditText()!=null);

        });
    }

}