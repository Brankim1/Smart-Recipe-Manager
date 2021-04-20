package com.example.smartrecipemanager;

import android.widget.ImageView;

import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;

import static androidx.lifecycle.Lifecycle.State.CREATED;
import static org.junit.Assert.assertNotNull;

/**
 * author:Pengcheng Jin
 */
public class SharingActivityTest {
    private ActivityScenario<SharingActivity> scenario;
    @Before
    public void setUp() throws Exception {
        scenario = ActivityScenario.launch(SharingActivity.class);
        scenario.moveToState(CREATED);
    }
    @Test
    public void onCreate() {
        scenario.onActivity(activity -> {
            ImageView recyclerView = activity.findViewById(R.id.sharingRecyclerView);
            assertNotNull(recyclerView.getDrawable());
        });
    }

}