package com.calamity.rtuitlabclicker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.calamity.rtuitlabclicker.data.local.dao.UserDao
import com.calamity.rtuitlabclicker.di.ApplicationScope
import com.calamity.rtuitlabclicker.domain.model.User
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [User::class], version = 1)
abstract class UsersDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    class Callback @Inject constructor(
        private val database: Provider<UsersDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback()
}