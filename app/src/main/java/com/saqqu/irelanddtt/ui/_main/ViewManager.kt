package com.saqqu.irelanddtt.ui._main

import android.content.Context
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.saqqu.irelanddtt.R
import com.saqqu.irelanddtt.ui.dtt.DTTFragment
import com.saqqu.irelanddtt.ui.home.HomeScreen
import com.saqqu.irelanddtt.ui.profile.SettingsFragment
import com.saqqu.irelanddtt.ui.results.ResultsFragment
import com.saqqu.irelanddtt.ui.shared.alert.CustomAlertDialog
import com.saqqu.irelanddtt.ui.shared.alert.CustomAlertDialogVM
import com.saqqu.irelanddtt.ui.utils.ViewModelFactory
import com.saqqu.irelanddtt.utils.Helper

class ViewManager internal constructor(var context: Context) {
    private var fragmentManager: FragmentManager
    var listener: MainActivityInteractionListener = Helper().getNavigator(context)
    private lateinit var dialog: CustomAlertDialog

    init {
        fragmentManager = listener.activity().supportFragmentManager
    }

    fun navigateTo(fragment: Fragment, addToBackStack: Boolean = true) {
        val currentClassName = if (currentFragment != null) currentFragment!!.javaClass.name else ""
        val isAlreadyShowing = currentClassName == fragment.javaClass.name
        updateContentFrame(fragment, fragment.javaClass.name, !isAlreadyShowing && addToBackStack)
        listener.activity().runOnUiThread {
            listener.showHideBottomNav(show = false)
        }
    }

    fun navigateTo(id: Int) {
        //listener.activity().findNavController(R.id.mainActivityNavFragment).navigate(id)
    }

    private fun isInBackStack(fragmentClass: Class<out Fragment>): Fragment? {
        val backStackEntryCount = fragmentManager.backStackEntryCount
        for (i in 0 until backStackEntryCount) {
            val backStackEntry = fragmentManager.getBackStackEntryAt(i)
            val fragmentTag = backStackEntry.name
            val fragment = fragmentManager.findFragmentByTag(fragmentTag)
            if (fragment != null && fragmentClass.isInstance(fragment)) {
                return fragment
            }
        }
        return null
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
                navigateToHome()
                //listener.activity().findNavController(R.id.mainActivityNavFragment).navigate(R.id.homeToResult)
            }

            R.id.bottomNavBarResults -> {
                //listener.activity().findNavController(R.id.mainActivityNavFragment).navigate(R.id.homeToResult)
                fragmentManager.clearBackStack(HomeScreen::class.java.name)
                navigateToResults()
            }

            R.id.bottomNavBarProfile -> {
                navigateToProfile()
            }
        }
        return false
    }

    private fun navigateToHome() {
        fragmentManager.clearBackStack(HomeScreen::class.java.name)
        val fragment = HomeScreen.newInstance(ViewModelFactory().setupHomeScreenViewModel(listener))
        val backStackFragment = isInBackStack(HomeScreen::class.java)
        backStackFragment?.let {
            updateContentFrame(it, it.javaClass.name, false)
            return
        }
        updateContentFrame(fragment, fragment.javaClass.name, false)
    }

    private fun navigateToResults() {
        fragmentManager.clearBackStack(HomeScreen::class.java.name)
        val fragment = ResultsFragment(listener.getOfflineResult())
        val backStackFragment = isInBackStack(ResultsFragment::class.java)
        backStackFragment?.let {
            updateContentFrame(it, it.javaClass.name, false)
            return
        }
        updateContentFrame(fragment, fragment.javaClass.name, false)
    }

    private fun navigateToProfile() {
        fragmentManager.clearBackStack(HomeScreen::class.java.name)
        val fragment = SettingsFragment()
        val backStackFragment = isInBackStack(SettingsFragment::class.java)
        backStackFragment?.let {
            updateContentFrame(it, it.javaClass.name, false)
            return
        }
        updateContentFrame(fragment, fragment.javaClass.name, false)
    }

    fun onBackPressed(): Boolean? {

        if (shouldHoldBack()) {
            return null
        }

        return false
    }

    private fun isFragment(clazz: Class<out Fragment>): Boolean {
        currentFragment?.let { return clazz === it::class.java }
        return false
    }

    private fun shouldHoldBack(): Boolean {
        if (isFragment(DTTFragment::class.java)) {
            if (this::dialog.isInitialized) {
                if (dialog.isShowing) {
                    return false
                }
            }
            showDTTExitDialog()
            return true
        }
        return false
    }

    private fun showDTTExitDialog() {
        val dialogVM = CustomAlertDialogVM(listener,"Are you sure to exit the quiz?","Yes","No")
        dialog = CustomAlertDialog(context, dialogVM, completionLeft = { dialog, _ ->
            listener.activity().onBackPressed()
            dialog.dismiss()
        }, completionRight = { dialog, _ ->
            dialog.dismiss()
        })
        dialog.show()
    }

}