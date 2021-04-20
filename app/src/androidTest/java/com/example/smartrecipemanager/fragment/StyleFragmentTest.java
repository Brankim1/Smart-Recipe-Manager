package com.example.smartrecipemanager.fragment;

import android.app.Instrumentation;

import androidx.test.core.app.ActivityScenario;

import com.example.smartrecipemanager.SearchActivity;

import org.junit.Before;
import org.junit.Test;

import static androidx.lifecycle.Lifecycle.State.CREATED;

/**
 * author:Pengcheng Jin
 */
public class StyleFragmentTest {
    private ActivityScenario<SearchActivity> scenario;
    private Instrumentation.ActivityMonitor am;
    @Before
    public void setUp() throws Exception {
        scenario = ActivityScenario.launch(SearchActivity.class);
        scenario.moveToState(CREATED);
    }
    @Test
    public void onCreateView() {

    }
}