package com.ysar.redditnews.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.ysar.redditnews.LifecycleLoggingActivity
import com.ysar.redditnews.MainApplication
import com.ysar.redditnews.R
import com.ysar.redditnews.databinding.ActivityMainBinding
import com.ysar.redditnews.di.component.MainComponent
import com.ysar.redditnews.util.setupWithNavController


class MainActivity : LifecycleLoggingActivity() {


    lateinit var mainComponent: MainComponent
    private lateinit var binding: ActivityMainBinding
    private var currentNavController: LiveData<NavController>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainToolbar)

        supportActionBar?.setDisplayShowTitleEnabled(true)

        // Creation of the main graph using the application graph
        mainComponent = (applicationContext as MainApplication).provideAppComponent().mainComponent().create()

        // Make Dagger instantiate @Inject fields in MainActivity
        mainComponent.inject(this)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }

    }


    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {

        val navGraphIds = listOf(
                R.navigation.news,
            R.navigation.favorites)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = binding.mainBottomNavigationView.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = supportFragmentManager,
                containerId = R.id.nav_host_fragment_container,
                intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })

        currentNavController = controller

    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}
