package com.example.smartrecipemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;
/**PostDetailActivity
* get information from SharingActivity, and show post details.
* */
public class PostDetailActivity extends AppCompatActivity {
    private String postid;
    private String userid;
    private String name;
    private String title;
    private String pic;
    private String content;
    private ImageView img;
    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        Intent intent = getIntent();
        /*get data form intent*/
        postid = intent.getStringExtra("postid");
        userid = intent.getStringExtra("userid");
        name = intent.getStringExtra("name");
        title = intent.getStringExtra("title");
        pic = intent.getStringExtra("pic");
        content = intent.getStringExtra("content");

        setToolBar();
        img = findViewById(R.id.post_img);
        Picasso.get().load(pic).into(img);
        text=findViewById(R.id.post_text);
        text.setText(content);
    }

    /**
     * set toolbar to show post name in the top
     * */
    public void setToolBar() {
        //component initialize
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //change toolbar name and add button
        if (toolbar != null) {
            toolbar.setTitle(title);
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        }

        //toolbar button set listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}