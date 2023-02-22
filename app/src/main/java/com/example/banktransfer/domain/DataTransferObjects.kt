package com.example.banktransfer.domain

import com.example.banktransfer.database.DatabaseCustomer

fun List<Customer>.asDatabaseModel(): Array<DatabaseCustomer> {
    return map { it.asDatabaseModel() }.toTypedArray()
}

fun Customer.asDatabaseModel(): DatabaseCustomer {
    return DatabaseCustomer(
        id = this.id,
        name = this.name,
        email = this.email,
        currentBalance = this.currentBalance,
        age = this.age
    )
}

fun List<DatabaseCustomer>.asDomainModel(): List<Customer> {
    return map { it.asDomainModel() }
}

fun DatabaseCustomer.asDomainModel(): Customer {
    return Customer(
        id = this.id,
        name = this.name,
        email = this.email,
        currentBalance = this.currentBalance,
        age = this.age
    )
}