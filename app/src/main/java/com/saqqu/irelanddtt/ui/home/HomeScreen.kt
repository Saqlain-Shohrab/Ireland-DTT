package com.saqqu.irelanddtt.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.saqqu.irelanddtt.data.models.QuestionType
import com.saqqu.irelanddtt.databinding.FragmentHomeBinding
import com.saqqu.irelanddtt.ui.dtt.DTTFragment

class HomeScreen(private var viewModel: HomeScreenVM) : Fragment() {

    private lateinit var binding: FragmentHomeBinding

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
    }

    private fun operations() {
        binding.quizStartBtn.setOnClickListener {
            val fragment = DTTFragment(QuestionType.ALL)
            viewModel.showDialog(fragment)
        }
        binding.quizStartBtnCOV.setOnClickListener {
            val fragment = DTTFragment(QuestionType.CONTROL_OF_VEHICLE)
            viewModel.showDialog(fragment)
        }
        binding.quizStartBtnLMROTR.setOnClickListener {
            val fragment = DTTFragment(QuestionType.LEGAL_MATTER__RULES_OF_THE_ROAD)
            viewModel.showDialog(fragment)
        }
        binding.quizStartBtnMR.setOnClickListener {
            val fragment = DTTFragment(QuestionType.MANAGING_RISK)
            viewModel.showDialog(fragment)
        }
        binding.quizStartBtnSARD.setOnClickListener {
            val fragment = DTTFragment(QuestionType.SAFE_AND_RESPONSIBLE_DRIVING)
            viewModel.showDialog(fragment)
        }
        binding.quizStartBtnTM.setOnClickListener {
            val fragment = DTTFragment(QuestionType.TECHNICAL_MATTERS)
            viewModel.showDialog(fragment)
        }

    }

}