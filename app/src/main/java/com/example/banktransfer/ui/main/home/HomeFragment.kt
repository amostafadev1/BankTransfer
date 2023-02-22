package com.example.banktransfer.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banktransfer.databinding.FragmentHomeBinding
import com.example.banktransfer.domain.Customer
import com.example.banktransfer.domain.asDomainModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by lazy {
        val viewModelFactory = HomeViewModel.HomeViewModelFactory(requireActivity().application)
        ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    private val homeAdapter by lazy { HomeAdapter(viewModel::onItemClick) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initView()
        initObserver()

        return binding.root
    }

    private fun initView() {
        binding.lifecycleOwner = viewLifecycleOwner
        val recyclerView = binding.homeCustomerRv
        recyclerView.adapter = homeAdapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)
        )
    }

    private fun initObserver() {
        viewModel.customers.observe(viewLifecycleOwner) {
            val customers = it.asDomainModel()

            homeAdapter.submitList(customers)
        }

        viewModel.navigateToDetail.observe(viewLifecycleOwner) {
            if (it != -1L) {
                navigateToDetail(it)
                viewModel.doneNavigating()
            }
        }
    }

    private fun navigateToDetail(selectedCustomerId: Long) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDetailFragment(selectedCustomerId)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}