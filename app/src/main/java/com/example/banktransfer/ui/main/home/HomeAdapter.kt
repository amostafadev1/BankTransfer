package com.example.banktransfer.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.banktransfer.databinding.CustomerItemBinding
import com.example.banktransfer.domain.Customer

class HomeAdapter(private val onClick: (Long) -> Unit) :
    ListAdapter<Customer, HomeAdapter.CustomerViewHolder>(CustomerDiffCallBack()) {

    class CustomerViewHolder private constructor(private val binding: CustomerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(customer: Customer) {
            binding.customer = customer
            binding.executePendingBindings()
        }

        companion object {
            fun create(parent: ViewGroup): CustomerViewHolder {
                val binding = CustomerItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return CustomerViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        return CustomerViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val customer = getItem(position)
        holder.itemView.setOnClickListener { onClick(customer.id) }
        holder.bind(customer)
    }

}

class CustomerDiffCallBack : DiffUtil.ItemCallback<Customer>() {

    override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
        return oldItem == newItem
    }
}