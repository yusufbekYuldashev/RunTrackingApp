package com.yusufbek.runtrackingapp.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.yusufbek.runtrackingapp.R
import com.yusufbek.runtrackingapp.databinding.ActivityMainBinding
import com.yusufbek.runtrackingapp.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.yusufbek.runtrackingapp.other.Constants.KEY_NAME
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigateToTrackingFragmentIfNeeded(intent)

        setSupportActionBar(binding.toolbar)
        "Let's go ${sharedPreferences.getString(KEY_NAME, "")}".also {
            binding.tvToolbarTitle.text = it
        }

        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.navHostFragment))
        binding.bottomNavigationView.setOnNavigationItemReselectedListener { /* NO-OP */ }

        findNavController(R.id.navHostFragment).addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.settingsFragment, R.id.runFragment, R.id.statisticsFragment ->
                    binding.bottomNavigationView.visibility = View.VISIBLE
//                else -> binding.bottomNavigationView.visibility = View.GONE
                R.id.setupFragment, R.id.trackingFragment ->
                    binding.bottomNavigationView.visibility = View.GONE
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragmentIfNeeded(intent)
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?) {

        if (intent?.action == ACTION_SHOW_TRACKING_FRAGMENT) {
            findNavController(R.id.navHostFragment).navigate(R.id.action_global_tracking_fragment)
        }
    }
}