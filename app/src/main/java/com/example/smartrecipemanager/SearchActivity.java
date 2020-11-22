package com.example.smartrecipemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.smartrecipemanager.Adapter.SearchViewPagerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class SearchActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TabLayout mytab;
    private TextInputLayout searchText;
    private Button searchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setDrawerLayout();
        setTabLayout();
        searchText= (TextInputLayout)findViewById(R.id.searchText);
        searchButton=(Button)findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent ResultIntent = new Intent(SearchActivity.this, SearchResultActivity.class);
                ResultIntent.putExtra("data",searchText.getEditText().getText().toString());
                ResultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(ResultIntent);
            }
        });

    }

    public void setDrawerLayout(){
        //component initialize
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //change toolbar name and add button
        if (toolbar != null) {
            toolbar.setTitle("Search");
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //toolbar button set listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        //add drawerlayout listener to change activity
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        Intent HomeIntent = new Intent(SearchActivity.this, HomeActivity.class);
                        startActivity(HomeIntent);
                        finish();
                        break;
                    case R.id.menu_Search:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_Favourite:
                        Intent FavouriteIntent = new Intent(SearchActivity.this, FavouriteActivity.class);
                        startActivity(FavouriteIntent);
                        finish();
                        break;
                    case R.id.menu_Logout:
                        Intent LogoutIntent = new Intent(SearchActivity.this, LogoutActivity.class);
                        startActivity(LogoutIntent);
                        finish();
                        break;
                    case R.id.menu_about:
                        Intent AboutIntent = new Intent(SearchActivity.this, AboutActivity.class);
                        startActivity(AboutIntent);
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(navigationView);
                return false;
            }
        });

        //get user email,show it in drawerlayout header
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserEmail = mAuth.getCurrentUser().getEmail();
        View headerView = navigationView.inflateHeaderView(R.layout.drawer_header);
        TextView email=(TextView)headerView.findViewById(R.id.drawerText1);
        email.setText(currentUserEmail);
    }
    private void setTabLayout() {
        mytab = (TabLayout) findViewById(R.id.searchTab);
        mytab.addTab(mytab.newTab().setText("style"));
        mytab.addTab(mytab.newTab().setText("ingredients"));
        mytab.addTab(mytab.newTab().setText("AI"));
        mytab.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final SearchViewPagerAdapter adapter = new SearchViewPagerAdapter(getSupportFragmentManager(),mytab.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mytab));

        mytab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}

