package com.saqqu.irelanddtt.ui.dtt

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.saqqu.irelanddtt.R
import com.saqqu.irelanddtt.data.models.QuestionType
import com.saqqu.irelanddtt.data.shared_prefs.Settings
import com.saqqu.irelanddtt.databinding.FragmnetDttBinding
import com.saqqu.irelanddtt.ui._main.MainActivityInteractionListener
import com.saqqu.irelanddtt.ui.dtt.rev.DTTRevAdapter
import com.saqqu.irelanddtt.ui.dtt.rev.DTTRevListener
import com.saqqu.irelanddtt.ui.utils.ViewModelFactory
import com.saqqu.irelanddtt.utils.Helper


class DTTFragment() : Fragment(), DTTRevListener {

    //View Binding
    private lateinit var binding: FragmnetDttBinding

    //Data
    private lateinit var viewModel: DTTViewModel

    //Interactions
    private lateinit var listener: MainActivityInteractionListener

    //Sub view handlers
    private lateinit var adapter: DTTRevAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        listener = Helper().getNavigator(context)
        viewModel = ViewModelFactory().setupDTTViewModel(
            listener
        )
        binding = FragmnetDttBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        viewModel.maybeStartTimer()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopTimeRunner()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        removeObservers()
        viewModel.reset()
        super.onDestroyView()
    }

    private fun initViews() {

        operations()
        setupObservers()
        setupRev()
        viewModel.requestData()

    }

    private fun setupRev() {
        context?.let { adapter = DTTRevAdapter(it, ArrayList(), this, -1) } ?: return
        binding.optionRev.layoutManager = LinearLayoutManager(context)
        binding.optionRev.adapter = adapter
    }

    private fun addDataToAdapter(list: List<String>) {
        adapter.reFeedData(list)
        adapter.notifyDataSetChanged()
    }

    private fun setupObservers() {

        viewModel.onError.observe(viewLifecycleOwner) { errorMessage ->
            for (i in 0..<binding.root.childCount) {
                binding.root.getChildAt(i).visibility = View.GONE
            }
            val emptyView = TextView(context)
            emptyView.text = errorMessage
            binding.root.addView(emptyView)
        }

        viewModel.onQuestionsRetrieved.observe(viewLifecycleOwner) {}

        viewModel.onQuestionChanged.observe(viewLifecycleOwner) { question ->
            binding.question.text = question
            binding.explanation.visibility = View.GONE
            binding.dttProgressBar.progress = viewModel.getCurrentProgress()
        }

        viewModel.onOptionsChanged.observe(viewLifecycleOwner) { options ->

            addDataToAdapter(options)
        }
        viewModel.onOptionsCheckedChanged.observe(viewLifecycleOwner) { checkedPosition ->
            adapter.setSelectedItem(checkedPosition)
        }
        viewModel.onClearChecked.observe(viewLifecycleOwner) {
            //Clearing selected position for next/previous question
            adapter.setSelectedItem(-1)
        }
        viewModel.onImageChanged.observe(viewLifecycleOwner) { imageResID ->
            binding.qImg.setImageResource(imageResID)
        }
        viewModel.onImageVisibilityChanged.observe(viewLifecycleOwner) { visibility ->
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
        viewModel.onTimeFinished.observe(viewLifecycleOwner) { timeFinished ->
            if (timeFinished) {
                viewModel.submitForResult()
            }
        }
    }

    private fun removeObservers() {
        viewModel.onQuestionsRetrieved.removeObservers(viewLifecycleOwner)
    }

    private fun operations() {

        /*binding.options.setOnCheckedChangeListener { _, checkedId ->
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
        }*/

        binding.nextBtn.setOnClickListener {
            viewModel.goToNext()
        }
        binding.previousBtn.setOnClickListener {
            viewModel.goToPrevious()
        }

        binding.explanationBtn.setOnClickListener {
            it

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

    override fun onOptionSelected(position: Int, value: String) {
        viewModel.updateSelection(position)
    }
}