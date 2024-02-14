package com.saqqu.irelanddtt.ui._main

import android.content.Context
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.saqqu.irelanddtt.R
import com.saqqu.irelanddtt.ui.home.HomeScreen
import com.saqqu.irelanddtt.ui.profile.SettingsFragment
import com.saqqu.irelanddtt.ui.results.ResultsFragment
import com.saqqu.irelanddtt.ui.utils.ViewModelFactory
import com.saqqu.irelanddtt.utils.Helper
import kotlin.system.exitProcess

class ViewManager internal constructor(var context: Context) {
    private var fragmentManager: FragmentManager
    var listener: MainActivityInteractionListener = Helper().getNavigator(context)

    init {
        fragmentManager = listener.activity().supportFragmentManager
    }

    fun navigateTo(fragment: Fragment) {
        val currentClassName = if (currentFragment != null) currentFragment!!.javaClass.name else ""
        val isAlreadyShowing = currentClassName == fragment.javaClass.name
        updateContentFrame(fragment, fragment.javaClass.name, !isAlreadyShowing)
    }

    private val currentFragment: Fragment?
        get() = fragmentManager.findFragmentById(R.id.mainActivityFrameLayout)

    private fun updateContentFrame(
        fragment: Fragment,
        backStackName: String,
        addToBackStack: Boolean
    ) {
        try {
            val transaction = fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.mainActivityFrameLayout, fragment)
            if (addToBackStack) {
                transaction.addToBackStack(backStackName)
            }
            transaction.commit()
        } catch (ignored: Exception) {
        }
    }

    fun showToast(message: String?) {
        Toast.makeText(listener.activity(), message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(stringId: Int) {
        val message = context.resources.getString(stringId)
        Toast.makeText(listener.activity(), message, Toast.LENGTH_SHORT).show()
    }

    fun onOptionsMeuSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bottomNavBarHome -> {
                val fragment = HomeScreen(ViewModelFactory().setupHomeScreenViewModel(listener))
                navigateTo(fragment)
            }

            R.id.bottomNavBarResults -> {
                val resultsFragment = ResultsFragment(listener.activity().getOfflineResult())
                navigateTo(resultsFragment)
            }

            R.id.bottomNavBarProfile -> {
                val profileSettings = SettingsFragment()
                navigateTo(profileSettings)
            }
        }
        return false
    }

    fun onBackPressed() {

        if (isInHomeFragment()) {
            exitProcess(0)
        }

    }

    private fun isInHomeFragment(): Boolean {
        currentFragment?.let { return HomeScreen::class.java === it::class.java }
        return false
    }

}