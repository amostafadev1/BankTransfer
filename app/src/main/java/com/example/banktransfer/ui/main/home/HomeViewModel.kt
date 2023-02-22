package com.example.banktransfer.ui.main.home

import android.app.Application
import androidx.lifecycle.*
import com.example.banktransfer.database.CustomerDatabase.Companion.getDatabase
import com.example.banktransfer.database.DatabaseCustomer
import com.example.banktransfer.domain.Customer

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application, viewModelScope)

    val customers: LiveData<List<DatabaseCustomer>> = database.customerDao.getCustomers()

    fun onItemClick(selectedCustomerId: Long) {
        _navigateToDetail.value = selectedCustomerId
    }

    private val _navigateToDetail = MutableLiveData<Long>(-1)
    val navigateToDetail: LiveData<Long>
        get() = _navigateToDetail

    fun doneNavigating() {
        _navigateToDetail.value = -1
    }

    @Suppress("UNCHECKED_CAST")
    class HomeViewModelFactory(val app: Application)
        : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            (HomeViewModel(app) as T)
    }
}