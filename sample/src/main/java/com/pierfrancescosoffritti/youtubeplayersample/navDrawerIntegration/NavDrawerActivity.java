package com.pierfrancescosoffritti.youtubeplayersample.navDrawerIntegration;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.pierfrancescosoffritti.youtubeplayersample.MainActivity;
import com.pierfrancescosoffritti.youtubeplayersample.R;
import com.pierfrancescosoffritti.youtubeplayersample.recyclerViewSample.RecyclerViewActivity;

public class NavDrawerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_nav_drawer_menu_24dp);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();

                    if(menuItem.getItemId() == R.id.open_base_example_menu_item) {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    } else if(menuItem.getItemId() == R.id.open_recycler_view_example_menu_item) {
                        Intent intent = new Intent(this, RecyclerViewActivity.class);
                        startActivity(intent);
                    }

                    return true;
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
