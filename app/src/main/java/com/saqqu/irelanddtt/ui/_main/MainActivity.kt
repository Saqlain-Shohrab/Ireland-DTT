package com.saqqu.irelanddtt.ui._main

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.saqqu.irelanddtt.R
import com.saqqu.irelanddtt.data.models.QuizDataModel
import com.saqqu.irelanddtt.data.results.OfflineResults
import com.saqqu.irelanddtt.databinding.ActivityMainBinding
import com.saqqu.irelanddtt.ui.results.ResultsFragment
import com.saqqu.irelanddtt.ui.utils.FragmentFactory
import com.saqqu.irelanddtt.ui.utils.Screens
import com.saqqu.irelanddtt.ui.utils.ViewModelFactory

class MainActivity : AppCompatActivity(), MainActivityInteractionListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var viewManager: ViewManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelFactory().setupMainViewModel(this)
        viewManager = ViewManager(this)
        //setupNav()
        binding.navigation.setOnNavigationItemSelectedListener { item ->
            viewManager.onOptionsMeuSelected(item);
            true
        }
        binding.navigation.selectedItemId = R.id.bottomNavBarHome;
        //TODO: Remove before live
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token: String? ->
            Log.e(
                "newToken",
                token ?: ""
            )
        }
        askNotificationPermission()
    }

    private fun setupNav() {
        //binding.mainActivityFrameLayout.findNavController().setGraph(R.navigation.navigation_controller)
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        //viewManager.maybeShowHome();
    }

    override fun onBackPressed() {
        viewManager.onBackPressed()?.let { exit ->
            if (exit) {
                super.onBackPressed();
            }
            super.onBackPressed();
        }
    }
    override fun activity(): MainActivity {
        return this
    }

    override fun showToast(message: String) {
        viewManager.showToast(message)
    }

    override fun showCustomPopup(dialog: Dialog) {}
    override fun navigateToFragment(fragment: Fragment) {
        viewManager.navigateTo(fragment)
    }

    override fun navigateTo(screen: Screens) {
        val fragment = FragmentFactory().createFragment(screen)!!
        viewManager.navigateTo(fragment)
    }

    override fun submitResult(questions: MutableList<QuizDataModel>) {
        viewModel.addResult(questions)?.observe(this) { results ->

            val fragment = ResultsFragment(viewModel.getResultsDB())
            supportFragmentManager.popBackStack()
            supportFragmentManager.executePendingTransactions()
            viewManager.navigateTo(fragment)

        }
    }

    override fun getOfflineResult(): OfflineResults {
        return viewModel.getResultsDB()
    }

    override fun showHideBottomNav(show: Boolean) {
        binding.navigation.visibility = if (show) View.VISIBLE else View.GONE
    }

    private val requestPermissionLauncher =
        registerForActivityResult<String, Boolean>(ActivityResultContracts.RequestPermission()) { isGranted: Boolean? ->
            if (!isGranted!!) {
                viewManager.showToast(R.string.noti_permission_not_granted)
            }
        }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}