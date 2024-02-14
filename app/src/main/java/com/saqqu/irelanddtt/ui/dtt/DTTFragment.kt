package com.saqqu.irelanddtt.ui.dtt

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import com.saqqu.irelanddtt.R
import com.saqqu.irelanddtt.data.models.QuestionType
import com.saqqu.irelanddtt.data.shared_prefs.Settings
import com.saqqu.irelanddtt.databinding.FragmnetDttBinding
import com.saqqu.irelanddtt.ui._main.MainActivityInteractionListener
import com.saqqu.irelanddtt.ui.utils.ViewModelFactory
import com.saqqu.irelanddtt.utils.Helper


class DTTFragment(private val type: QuestionType) : Fragment() {

    //View Binding
    private lateinit var binding: FragmnetDttBinding
    //Data
    private lateinit var viewModel: DTTViewModel
    //Interactions
    private lateinit var listener: MainActivityInteractionListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        listener = Helper().getNavigator(context)
        viewModel = ViewModelFactory().setupDTTViewModel(listener, Settings.currentSettings(context).questionsLimit, type = type)
        binding = FragmnetDttBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onStop() {
        viewModel.stopTimer()
        removeObservers()
        viewModel.reset()
        super.onStop()
    }

    private fun initViews() {

        operations()
        setupObservers()
        viewModel.requestData()

    }

    private fun setupObservers() {

        viewModel.onQuestionsRetrieved.observe(viewLifecycleOwner) {}

        viewModel.onQuestionChanged.observe(viewLifecycleOwner) { question ->
            binding.question.text = question
            binding.explanation.visibility = View.GONE
            binding.dttProgressBar.progress = viewModel.getCurrentProgress()
        }

        viewModel.onOptionsChanged.observe(viewLifecycleOwner) { options ->

            try {
                binding.option1.text = options[0]
                binding.option2.text = options[1]
                binding.option3.text = options[2]
                binding.option4.text = options[3]
            } catch (e: IndexOutOfBoundsException) {
                viewModel.optionsIndexOutOfBound(-1)
            }
        }
        viewModel.onOptionsCheckedChanged.observe(viewLifecycleOwner) {checkId ->
            binding.options.check(checkId)
        }
        viewModel.onClearChecked.observe(viewLifecycleOwner) {
            binding.options.clearCheck()
        }
        viewModel.onImageChanged.observe(viewLifecycleOwner) {imageResID ->
            binding.qImg.setImageResource(imageResID)
        }
        viewModel.onImageVisibilityChanged.observe(viewLifecycleOwner) {visibility ->
            binding.qImg.visibility = visibility
        }
        viewModel.onNextButtonTextChanged.observe(viewLifecycleOwner) { buttonName ->
            binding.nextBtn.text = buttonName
        }
        viewModel.onExplanationChanged.observe(viewLifecycleOwner) { explanation ->
            binding.explanation.text = explanation
        }
        viewModel.onTimeChanged.observe(viewLifecycleOwner) { timeRemaining ->
            binding.timerText.text = timeRemaining

        }
    }

    private fun removeObservers() {
        viewModel.onQuestionsRetrieved.removeObservers(viewLifecycleOwner)
    }

    private fun operations() {

        binding.options.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {

                R.id.option1 -> {
                    viewModel.updateSelection(1)
                }

                R.id.option2 -> {
                    viewModel.updateSelection(2)
                }

                R.id.option3 -> {
                    viewModel.updateSelection(3)
                }

                R.id.option4 -> {
                    viewModel.updateSelection(4)
                }

            }
            activateNextButton()
        }

        binding.nextBtn.setOnClickListener {
            viewModel.goToNext()
        }
        binding.previousBtn.setOnClickListener {
            viewModel.goToPrevious()
        }

        binding.explanationBtn.setOnClickListener {it

            if (binding.explanation.visibility == View.VISIBLE) {
                val anim: Animation = AlphaAnimation(0.0f, 1.0f)
                anim.duration = 50 //You can manage the blinking time with this parameter

                anim.startOffset = 20
                anim.repeatMode = Animation.REVERSE
                anim.repeatCount = 10
                binding.explanation.startAnimation(anim)
            }
            binding.explanation.visibility = View.VISIBLE
        }

    }

    private fun activateNextButton() {
        binding.nextBtn.isActivated = true
    }
}