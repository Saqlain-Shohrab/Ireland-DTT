package com.saqqu.irelanddtt.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.saqqu.irelanddtt.data.models.QuestionType
import com.saqqu.irelanddtt.data.shared_prefs.Settings
import com.saqqu.irelanddtt.databinding.FragmentHomeBinding
import com.saqqu.irelanddtt.ui.dtt.DTTFragment
import com.saqqu.irelanddtt.utils.Helper

class HomeScreen: Fragment {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeScreenVM

    constructor()
    private constructor(viewModel: HomeScreenVM) {
        this.viewModel = viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        operations()
        Helper().getNavigator(context).showHideBottomNav(true)
    }

    private fun operations() {
        binding.quizStartBtn.setOnClickListener {
            viewModel.showDialog(QuestionType.ALL)
        }
        binding.quizStartBtnCOV.setOnClickListener {
            viewModel.showDialog(QuestionType.CONTROL_OF_VEHICLE)
        }
        binding.quizStartBtnLMROTR.setOnClickListener {
            viewModel.showDialog(QuestionType.LEGAL_MATTER__RULES_OF_THE_ROAD)
        }
        binding.quizStartBtnMR.setOnClickListener {
            viewModel.showDialog(QuestionType.MANAGING_RISK)
        }
        binding.quizStartBtnSARD.setOnClickListener {
            viewModel.showDialog(QuestionType.SAFE_AND_RESPONSIBLE_DRIVING)
        }
        binding.quizStartBtnTM.setOnClickListener {
            viewModel.showDialog(QuestionType.TECHNICAL_MATTERS)
        }

    }

    companion object {
        fun  newInstance(viewModel: HomeScreenVM): HomeScreen {
            return instance ?: HomeScreen(viewModel)
        }

        private var instance: HomeScreen? = null
    }

}