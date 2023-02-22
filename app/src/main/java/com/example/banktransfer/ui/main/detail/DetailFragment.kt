package com.example.banktransfer.ui.main.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.banktransfer.R
import com.example.banktransfer.databinding.FragmentDetailBinding
import com.example.banktransfer.utils.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val customerId by lazy(LazyThreadSafetyMode.NONE) {
        DetailFragmentArgs.fromBundle(requireArguments()).selectedCustomerId
    }

    private val viewModel: DetailViewModel by lazy {
        val viewModelFactory =
            DetailViewModel.DetailViewModelFactory(requireActivity().application, customerId)
        ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        initView()
        initObserver()

        return binding.root
    }

    private fun initView() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.executePendingBindings()
    }

    private fun initObserver() {
        // I did this to get the value of data at any moment.
        // Perhaps it's possible with coroutines directly from database livedata.
        viewModel.customerNames.observe(viewLifecycleOwner) {
            viewModel.currentCustomerNames = it
        }

        viewModel.customer.observe(viewLifecycleOwner) {
            viewModel.currentCustomer = it
        }

        viewModel.eventInvalidAmount.observe(viewLifecycleOwner) {
            if (it) {
                requireContext().toast(R.string.invalid_amount)
                binding.transferAmountInputText.text?.clear()
                viewModel.endInvalidAmount()
            }
        }

        viewModel.eventSuccessfulTransfer.observe(viewLifecycleOwner) {
            if (it != null) {
                requireContext().toast(getString(R.string.successful_transfer_message, it))
                binding.transferAmountInputText.text?.clear()
                viewModel.endSuccessfulTransfer()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.transferButton.setOnClickListener {
            makeDialog()
        }
    }

    private fun makeDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.transfer_credit))
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.send)) { _, _ ->
                viewModel.sendCredit(
                    binding.transferAmountInputText.text?.toString()?.toDoubleOrNull()
                        ?: -1.0
                )
            }
            .setSingleChoiceItems(viewModel.currentCustomerNames, -1)
            { _, which -> viewModel.onItemClick(which) }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}