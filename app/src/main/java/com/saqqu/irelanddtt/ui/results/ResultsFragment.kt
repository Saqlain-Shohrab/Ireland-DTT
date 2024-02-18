package com.saqqu.irelanddtt.ui.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.saqqu.irelanddtt.data.models.ResultModel
import com.saqqu.irelanddtt.data.results.OfflineResults
import com.saqqu.irelanddtt.databinding.FragmentResultsBinding
import com.saqqu.irelanddtt.ui._main.MainActivityInteractionListener
import com.saqqu.irelanddtt.ui.results.rev.ResultsRevAdapter
import com.saqqu.irelanddtt.ui.results.rev.ResultsToResultListener
import com.saqqu.irelanddtt.utils.Helper

class ResultsFragment(private val offlineResults: OfflineResults): Fragment(),
    ResultsToResultListener {

    private lateinit var binding: FragmentResultsBinding
    private lateinit var adapter: ResultsRevAdapter
    private lateinit var listener: MainActivityInteractionListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentResultsBinding.inflate(layoutInflater, container, false)
        listener = Helper().getNavigator(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRev()

        Helper().getNavigator(context).showHideBottomNav(true)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setupRev() {
        adapter = ResultsRevAdapter(listener.activity().baseContext, emptyList(), this)
        binding.resultsRev.layoutManager = LinearLayoutManager(context)
        binding.resultsRev.adapter = adapter
        setupObserver()
    }

    private fun setupObserver() {
        offlineResults.getAllResults().observe(viewLifecycleOwner) { results ->
            results?.let { adapter.setData(it) }

            adapter.notifyDataSetChanged()
        }
    }

    override fun onResultClicked(result: ResultModel) {
        val resultFragment = ResultFragment(result)
        listener.navigateToFragment(resultFragment)
    }
}