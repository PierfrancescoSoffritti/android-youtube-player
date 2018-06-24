package com.pierfrancescosoffritti.cyplayersample

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.pierfrancescosoffritti.cyplayersample.examples.basicExample.BasicExampleActivity
import com.pierfrancescosoffritti.cyplayersample.examples.localPlayerExample.LocalPlayerInitExampleActivity
import com.pierfrancescosoffritti.cyplayersample.examples.notificationExample.NotificationExampleActivity
import com.pierfrancescosoffritti.cyplayersample.examples.playerControlsExample.PlayerControlsExample
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var selectedMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initWebView()

        adjustStatusBarTranslucency()
        initToolbar()
        initNavDrawer()

        showFeatureDiscovery()
    }

    public override fun onResume() {
        super.onResume()
        selectedMenuItem?.isChecked = false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
                return true
            }
            R.id.open_on_github -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/PierfrancescoSoffritti/chromecast-youtube-player")))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        when {
            drawer_layout.isDrawerOpen(GravityCompat.START) -> drawer_layout.closeDrawer(GravityCompat.START)
            main_activity_webview.canGoBack() -> main_activity_webview.goBack()
            else -> super.onBackPressed()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        main_activity_webview.settings.javaScriptEnabled = true
        main_activity_webview.loadUrl("https://pierfrancescosoffritti.github.io/Android-YouTube-Player/")

        main_activity_webview.webViewClient = object : WebViewClient() {
            override fun onPageCommitVisible(view: WebView, url: String) {
                super.onPageCommitVisible(view, url)
                progressbar.visibility = View.GONE
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                return if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    true
                } else
                    false
            }
        }
    }

    private fun adjustStatusBarTranslucency() {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            val windowParams = window.attributes
            windowParams.flags = windowParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            window.attributes = windowParams
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_nav_drawer_menu_24dp)
    }

    private fun initNavDrawer() {
        setNavigationViewWidth(navigation_view)

        navigation_view.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            selectedMenuItem = menuItem

            drawer_layout.closeDrawers()

            if (menuItem.itemId == R.id.open_base_example_menu_item) {
                val intent = Intent(this, BasicExampleActivity::class.java)
                startActivity(intent)
            } else if(menuItem.itemId == R.id.open_player_controls_example_menu_item) {
                val intent = Intent(this, PlayerControlsExample::class.java)
                startActivity(intent)
            } else if(menuItem.itemId == R.id.open_notification_example_menu_item) {
                val intent = Intent(this, NotificationExampleActivity::class.java)
                startActivity(intent)
            } else if(menuItem.itemId == R.id.open_local_player_example_menu_item) {
                val intent = Intent(this, LocalPlayerInitExampleActivity::class.java)
                startActivity(intent)
            } else if (menuItem.itemId == R.id.star_on_github)
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/PierfrancescoSoffritti/chromecast-youtube-player/stargazers")))
//                    else if (menuItem.getItemId() === R.id.rate_on_playstore) {
//                        val appPackageName = packageName
//                        try {
//                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
//                        } catch (exception: ActivityNotFoundException) {
//                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
//                        }
//
//                    }

            true
        }
    }

    private fun setNavigationViewWidth(navigationView: NavigationView) {
        val params = navigationView.layoutParams
        val width = getScreenWidth() - getToolbarHeight()
        val _320dp = resources.getDimensionPixelSize(R.dimen._320dp)
        params.width = if (width > _320dp) _320dp else width
        navigationView.layoutParams = params
    }

    private fun getToolbarHeight(): Int {
        return toolbar.layoutParams.height
    }

    private fun getScreenWidth(): Int {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }


    private fun showFeatureDiscovery() {
        val preferenceKey = "featureDiscoveryShown"
        val sharedPreferencesKey = "sampleApp_MainActivity_SharedPreferences"
        val prefs = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
        val featureDiscoveryShown = prefs.getBoolean(preferenceKey, false)

        if (featureDiscoveryShown)
            return
        else
            prefs.edit().putBoolean(preferenceKey, true).apply()

        val target = toolbar.getChildAt(1)

        TapTargetView.showFor(
                this,
                TapTarget.forView(target, getString(R.string.explore_examples), getString(R.string.explore_examples_description))
                        .outerCircleColor(R.color.github_black)
                        .outerCircleAlpha(1f)
                        .targetCircleColor(android.R.color.white)
                        .titleTextColor(android.R.color.white)
                        .drawShadow(true)
                        .transparentTarget(true), object : TapTargetView.Listener() {
            override fun onTargetClick(view: TapTargetView) {
                super.onTargetClick(view)
                target.performClick()
            }
        })
    }
}
