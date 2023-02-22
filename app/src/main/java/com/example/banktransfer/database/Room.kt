package com.example.banktransfer.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.RoomDatabase.Callback
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.banktransfer.domain.Customer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.coroutines.coroutineContext

@Dao
interface CustomerDao {

    @Query("SELECT * FROM customer")
    fun getCustomers(): LiveData<List<DatabaseCustomer>>

    @Update
    suspend fun updateCustomer(customer: DatabaseCustomer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg customer: DatabaseCustomer)

    @Query("SELECT * FROM customer WHERE name = :name")
    suspend fun getCustomerByName(name:String):DatabaseCustomer

    @Query("SELECT * FROM customer WHERE id = :id")
    fun getCustomerById(id:Long):LiveData<DatabaseCustomer>
}

@Database(entities = [DatabaseCustomer::class], version = 1, exportSchema = false)
abstract class CustomerDatabase : RoomDatabase() {
    abstract val customerDao: CustomerDao

    companion object {
        @Volatile
        private var INSTANCE: CustomerDatabase? = null

        fun getDatabase(context: Context, coroutineScope: CoroutineScope): CustomerDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CustomerDatabase::class.java,
                    "customer_database"
                ).addCallback(
                    object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            INSTANCE?.let { database ->
                                coroutineScope.launch {
                                    database.customerDao.insertAll(*initialCustomers)
                                }
                            }
                        }
                    }
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

private val initialCustomers = arrayOf<DatabaseCustomer>(
    DatabaseCustomer(1, "Ahmed Mostafa", "ahmed@gmail.com", 231.0, 25),
    DatabaseCustomer(2, "Mohamed Ahmed", "mohamed@gmail.com", 841.0, 22),
    DatabaseCustomer(3, "Mostafa Abdelaziz", "mostafa@gmail.com", 292.0, 22),
    DatabaseCustomer(4, "Mahmoud Sherief", "mahmoud@gmail.com", 589.13, 27),
    DatabaseCustomer(5, "Kareem Mostafa", "kareem@gmail.com", 2367.0, 23),
    DatabaseCustomer(6, "Mokhtar Ahmed", "mokhtar@gmail.com", 301.55, 21),
    DatabaseCustomer(7, "Eslam Mohamed", "eslam@gmail.com", 671.0, 28),
    DatabaseCustomer(8, "Ibrahim Ahmed", "ibrahim@gmail.com", 2901.8, 29),
    DatabaseCustomer(9, "Abdelrahman Hamed", "hamed@gmail.com", 7431.0, 27),
    DatabaseCustomer(10, "Abdallah Ahmed", "abdallah@gmail.com", 950.6, 20)
)