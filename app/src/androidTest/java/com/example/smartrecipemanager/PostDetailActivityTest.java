package com.example.smartrecipemanager;

import android.widget.ImageView;

import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;

import static androidx.lifecycle.Lifecycle.State.CREATED;
import static org.junit.Assert.assertTrue;

/**
 * author:Pengcheng Jin
 */
public class PostDetailActivityTest {
    private ActivityScenario<PostDetailActivity> scenario;
    @Before
    public void setUp() throws Exception {
        scenario = ActivityScenario.launch(PostDetailActivity.class);
        scenario.moveToState(CREATED);
    }
    @Test
    public void onCreate() {
        scenario.onActivity(activity -> {
            ImageView imageView = activity.findViewById(R.id.post_img);
            assertTrue(imageView.getDrawable()!=null);

        });
    }

}