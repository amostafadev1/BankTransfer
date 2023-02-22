package com.example.banktransfer.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.NumberFormat
import java.util.Currency

@BindingAdapter("currencyText")
fun bindCurrency(textView:TextView, number:Double){
    val format = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 2
    format.currency = Currency.getInstance("USD")
    textView.text = format.format(number)
}