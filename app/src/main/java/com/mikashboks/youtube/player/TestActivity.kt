package com.mikashboks.youtube.player

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.mikashboks.youtube.player.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(
            window,
            false
        )
        super.onCreate(savedInstanceState)

        binding =
            ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController =
            findNavController(R.id.nav_host_fragment_content_test)
        appBarConfiguration =
            AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(
            navController,
            appBarConfiguration
        )

        binding.fab.setOnClickListener { view ->
            Snackbar.make(
                view,
                "Replace with your own action",
                Snackbar.LENGTH_LONG
            )
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController =
            findNavController(R.id.nav_host_fragment_content_test)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}