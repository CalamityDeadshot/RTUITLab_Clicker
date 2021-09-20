package com.calamity.rtuitlabclicker.data.local.dao

import androidx.room.*
import com.calamity.rtuitlabclicker.domain.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): List<User>

    @Query("SELECT * FROM users WHERE name = :name")
    fun getUserByName(name: String): User

    @Query("SELECT * FROM users WHERE name = :id")
    fun getUserById(id: Int): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)
}