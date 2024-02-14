package com.saqqu.irelanddtt.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.saqqu.irelanddtt.data.shared_prefs.Settings
import com.saqqu.irelanddtt.databinding.FragmentSettingsBinding
import com.saqqu.irelanddtt.ui._main.MainActivityInteractionListener
import com.saqqu.irelanddtt.ui.utils.ViewModelFactory
import com.saqqu.irelanddtt.utils.Helper

class SettingsFragment: Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsVM
    private lateinit var listener: MainActivityInteractionListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingsBinding.inflate(inflater)
        listener = Helper().getNavigator(context)
        viewModel = ViewModelFactory().setUpSettingsModel(listener);

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        operations()

    }

    private fun operations() {

        binding.questionSetLimitNP.maxValue = 40
        binding.questionSetLimitNP.minValue = 3
        binding.questionSetLimitNP.value = Settings.currentSettings(listener.activity().baseContext).questionsLimit

        binding.questionSetLimitSave.setOnClickListener {
            viewModel.saveQuestionLimit(binding.questionSetLimitNP.value)
        }
    }
}