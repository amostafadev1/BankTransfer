package com.example.banktransfer.ui.main.detail

import android.app.Application
import androidx.lifecycle.*
import com.example.banktransfer.domain.asDatabaseModel
import com.example.banktransfer.database.CustomerDatabase.Companion.getDatabase
import com.example.banktransfer.database.DatabaseCustomer
import com.example.banktransfer.domain.Customer
import com.example.banktransfer.domain.asDomainModel
import kotlinx.coroutines.launch

class DetailViewModel(application: Application, customerId: Long) : AndroidViewModel(application) {

    private val database = getDatabase(application, viewModelScope)

    val customer = Transformations.map(database.customerDao.getCustomerById(customerId)) {
        it.asDomainModel()
    }

    var currentCustomer: Customer? = null

    private val customers: LiveData<List<DatabaseCustomer>> = database.customerDao.getCustomers()

    private val _customerNames = Transformations.map(customers) { list ->
        list.map { it.name }.toTypedArray()
    }
    val customerNames: LiveData<Array<String>>
        get() = _customerNames

    var currentCustomerNames = emptyArray<String>()

    private var selectedCustomerName: String = ""

    private val _eventInvalidAmount = MutableLiveData<Boolean>(false)
    val eventInvalidAmount: LiveData<Boolean>
        get() = _eventInvalidAmount

    private val _eventSuccessfulTransfer = MutableLiveData<String?>()
    val eventSuccessfulTransfer: LiveData<String?>
        get() = _eventSuccessfulTransfer

    fun onItemClick(selectedCustomerNamePosition: Int) {
        selectedCustomerName =
            currentCustomerNames.elementAtOrNull(selectedCustomerNamePosition) ?: ""
    }

    fun sendCredit(amount: Double) {
        currentCustomer?.let { sourceCustomer ->

            if (sourceCustomer.currentBalance < amount || amount < 0) {
                _eventInvalidAmount.value = true
                return
            }

            val newSourceCustomer = sourceCustomer
                .copy(currentBalance = sourceCustomer.currentBalance - amount).asDatabaseModel()

            viewModelScope.launch {
                val destinationCustomer = database.customerDao
                    .getCustomerByName(selectedCustomerName)
                val newDestinationCustomer = destinationCustomer
                    .copy(currentBalance = destinationCustomer.currentBalance + amount)

                database.customerDao.updateCustomer(newSourceCustomer)
                database.customerDao.updateCustomer(newDestinationCustomer)

                _eventSuccessfulTransfer.value = selectedCustomerName
            }
        }
    }

    fun endInvalidAmount() {
        _eventInvalidAmount.value = false
    }

    fun endSuccessfulTransfer() {
        _eventSuccessfulTransfer.value = null
    }

    @Suppress("UNCHECKED_CAST")
    class DetailViewModelFactory(val app: Application, val customerId: Long) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            (DetailViewModel(app, customerId) as T)
    }
}