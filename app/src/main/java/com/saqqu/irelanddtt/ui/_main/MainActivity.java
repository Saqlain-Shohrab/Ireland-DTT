package com.saqqu.irelanddtt.ui._main;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.saqqu.irelanddtt.R;
import com.saqqu.irelanddtt.data.models.QuizDataModel;
import com.saqqu.irelanddtt.data.results.OfflineResults;
import com.saqqu.irelanddtt.databinding.ActivityMainBinding;
import com.saqqu.irelanddtt.ui.results.ResultsFragment;
import com.saqqu.irelanddtt.ui.utils.FragmentFactory;
import com.saqqu.irelanddtt.ui.utils.Screens;
import com.saqqu.irelanddtt.ui.utils.ViewModelFactory;

import java.util.List;


public class MainActivity extends AppCompatActivity implements MainActivityInteractionListener {
    ActivityMainBinding binding;
    MainViewModel viewModel;
    ViewManager viewManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelFactory().setupMainViewModel(this);
        viewManager = new ViewManager(this);


        binding.navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                viewManager.onOptionsMeuSelected(item);
                //binding.navigation.setSelectedItemId(item.getItemId());
                return true;
            }
        });
        binding.navigation.setSelectedItemId(R.id.bottomNavBarHome);
        //TODO: Remove before live
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            Log.e("newToken", token);
        });

        askNotificationPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewManager.maybeShowHome();
    }

    @Override
    public void onBackPressed() {
        if (viewManager.onBackPressed()  != null) {
            if (Boolean.FALSE.equals(viewManager.onBackPressed())) {
                super.onBackPressed();
            }
            super.onBackPressed();
        }
    }

    @NonNull
    @Override
    public MainActivity activity() {
        return this;
    }

    @Override
    public void showToast(@NonNull String message) {
        viewManager.showToast(message);
    }

    @Override
    public void showCustomPopup(@NonNull Dialog dialog) {

    }

    @Override
    public void navigateToFragment(@NonNull Fragment fragment) {
        viewManager.navigateTo(fragment);
    }

    @Override
    public void navigateTo(@NonNull Screens screen) {
        Fragment fragment = new FragmentFactory().createFragment(screen);
        assert fragment != null;
        viewManager.navigateTo(fragment);
    }

    @Override
    public void submitResult(@NonNull List<QuizDataModel> questions) {
        viewModel.addResult(questions).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                ResultsFragment fragment = new ResultsFragment(viewModel.getResultsDB());
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().executePendingTransactions();
                viewManager.navigateTo(fragment);
            }
        });
    }

    @NonNull
    @Override
    public OfflineResults getOfflineResult() {
        return viewModel.getResultsDB();
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    viewManager.showToast(R.string.noti_permission_not_granted);
                }
            });

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
}