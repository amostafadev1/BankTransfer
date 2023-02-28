package com.example.banktransfer.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Customer(
    val id: Long,
    val name: String,
    val email: String,
    val currentBalance: Double,
    val age: Int
) : Parcelable