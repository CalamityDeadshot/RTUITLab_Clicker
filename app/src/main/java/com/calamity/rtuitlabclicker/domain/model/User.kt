package com.calamity.rtuitlabclicker.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

@Entity(tableName = "users")
data class User(
    val authToken: String,
    val name: String,
    val profileImageUri: String,
    val counter: Long,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
) {
    override fun toString(): String =
        Gson().toJson(this)
}
