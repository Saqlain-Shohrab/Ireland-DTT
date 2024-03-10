package com.saqqu.irelanddtt.ui.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.saqqu.irelanddtt.data.models.ResultModel
import com.saqqu.irelanddtt.data.models.ResultQuizDataModel
import com.saqqu.irelanddtt.databinding.FragmentResultBinding
import com.saqqu.irelanddtt.ui.results.rev.ResultRevAdapter
import com.saqqu.irelanddtt.ui.utils.ViewModelFactory
import com.saqqu.irelanddtt.utils.Helper

class ResultFragment(private val result: ResultModel): Fragment() {

    private lateinit var binding: FragmentResultBinding
    private lateinit var adapter: ResultRevAdapter
    private lateinit var viewModel: ResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(layoutInflater, container, false)
        viewModel = ResultViewModel(result = result)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservers()
        viewModel.requestResultData()
    }

    private fun setUpObservers() {
        viewModel.resultDataRetrieved.observe(viewLifecycleOwner) { result ->
            val scoreDivision = result.getScoreDivision()
            binding.resultDescription.text = "${scoreDivision}Total: ${result.scored}/${result.outOf}"
            setupRecyclerView(result.questions.map { ResultQuizDataModel(it) })
        }
    }

    private fun setupRecyclerView(results: List<ResultQuizDataModel>) {
        binding.resultRev.layoutManager = LinearLayoutManager(context)
        adapter = ResultRevAdapter(results, requireContext())
        binding.resultRev.adapter = adapter

        adapter.notifyDataSetChanged()
    }
}