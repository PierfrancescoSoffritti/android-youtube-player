package com.pierfrancescosoffritti.aytplayersample;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.pierfrancescosoffritti.aytplayersample.examples.basicExample.BasicExampleActivity;
import com.pierfrancescosoffritti.aytplayersample.examples.chromecastExample.ChromecastExampleActivity;
import com.pierfrancescosoffritti.aytplayersample.examples.customUIExample.CustomUIActivity;
import com.pierfrancescosoffritti.aytplayersample.examples.liveVideoExample.LiveVideoActivity;
import com.pierfrancescosoffritti.aytplayersample.examples.pictureInPictureExample.PictureInPictureActivity;
import com.pierfrancescosoffritti.aytplayersample.examples.playerStatusExample.PlayerStatusActivity;
import com.pierfrancescosoffritti.aytplayersample.examples.recyclerViewExample.RecyclerViewActivity;
import com.pierfrancescosoffritti.aytplayersample.examples.viewPagerExample.ViewPagerActivity;

/**
 * This Activity is used as a starting point for all the sample Activities.
 * You won't find any code example for the library here. You can find those in the "examples" package.
 */
public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private WebView webview;
    private DrawerLayout drawerLayout;
    private MenuItem selectedMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWebView();

        adjustStatusBarTranslucency();
        initToolbar();
        initNavDrawer();

        showFeatureDiscovery();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(selectedMenuItem != null)
            selectedMenuItem.setChecked(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.open_on_github:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/PierfrancescoSoffritti/Android-YouTube-Player")));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else if (webview.canGoBack())
            webview.goBack();
        else
            super.onBackPressed();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        webview = findViewById(R.id.main_activity_webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("https://pierfrancescosoffritti.github.io/Android-YouTube-Player/");

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                findViewById(R.id.progressbar).setVisibility(View.GONE);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } else
                    return false;
            }
        });
    }

    private void adjustStatusBarTranslucency() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            window.setAttributes(windowParams);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_nav_drawer_menu_24dp);
    }

    private void initNavDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        setNavigationViewWidth(navigationView);

        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    menuItem.setChecked(true);
                    selectedMenuItem = menuItem;

                    drawerLayout.closeDrawers();

                    if(menuItem.getItemId() == R.id.open_base_example_menu_item) {
                        Intent intent = new Intent(this, BasicExampleActivity.class);
                        startActivity(intent);
                    } else if(menuItem.getItemId() == R.id.open_recycler_view_example_menu_item) {
                        Intent intent = new Intent(this, RecyclerViewActivity.class);
                        startActivity(intent);
                    } else if(menuItem.getItemId() == R.id.open_view_pager_example_menu_item) {
                        Intent intent = new Intent(this, ViewPagerActivity.class);
                        startActivity(intent);
                    } else if(menuItem.getItemId() == R.id.open_custom_ui_example_menu_item) {
                        Intent intent = new Intent(this, CustomUIActivity.class);
                        startActivity(intent);
                    } else if(menuItem.getItemId() == R.id.open_live_video_example_menu_item) {
                        Intent intent = new Intent(this, LiveVideoActivity.class);
                        startActivity(intent);
                    }  else if(menuItem.getItemId() == R.id.open_player_status_example_menu_item) {
                        Intent intent = new Intent(this, PlayerStatusActivity.class);
                        startActivity(intent);
                    }  else if(menuItem.getItemId() == R.id.open_picture_in_picture_example_menu_item) {
                        Intent intent = new Intent(this, PictureInPictureActivity.class);
                        startActivity(intent);
                    } else if(menuItem.getItemId() == R.id.open_chromecast_example_menu_item) {
                        Intent intent = new Intent(this, ChromecastExampleActivity.class);
                        startActivity(intent);
                    }

                    else if(menuItem.getItemId() == R.id.star_on_github)
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/PierfrancescoSoffritti/Android-YouTube-Player/stargazers")));
                    else if(menuItem.getItemId() == R.id.rate_on_playstore) {
                        final String appPackageName = getPackageName();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (ActivityNotFoundException exception) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }

                    return true;
                }
        );
    }

    private void setNavigationViewWidth(NavigationView navigationView) {
        ViewGroup.LayoutParams params = navigationView.getLayoutParams();
        int width = getScreenWidth() - getToolbarHeight();
        int _320dp = getResources().getDimensionPixelSize(R.dimen._320dp);
        params.width = width > _320dp ? _320dp : width;
        navigationView.setLayoutParams(params);
    }

    private int getToolbarHeight() {
        return toolbar.getLayoutParams().height;
    }

    private int getScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }


    private void showFeatureDiscovery() {
        String preferenceKey = "featureDiscoveryShown";
        String sharedPreferencesKey = "sampleApp_MainActivity_SharedPreferences";
        SharedPreferences prefs = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE);
        boolean featureDiscoveryShown = prefs.getBoolean(preferenceKey, false);

        if(featureDiscoveryShown)
            return;
        else
            prefs.edit().putBoolean(preferenceKey, true).apply();

        Toolbar toolbar = findViewById(R.id.toolbar);
        View target = toolbar.getChildAt(1);

        TapTargetView.showFor(
            this,
            TapTarget.forView(target, getString(R.string.explore_examples), getString(R.string.explore_examples_description))
                    .outerCircleColor(R.color.github_black)
                    .outerCircleAlpha(1)
                    .targetCircleColor(android.R.color.white)
                    .titleTextColor(android.R.color.white)
                    .drawShadow(true)
                    .transparentTarget(true)
            , new TapTargetView.Listener() {
                @Override
                public void onTargetClick(TapTargetView view) {
                    super.onTargetClick(view);
                    target.performClick();
                }
            });
    }
}
