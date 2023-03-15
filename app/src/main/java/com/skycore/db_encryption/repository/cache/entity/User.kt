package com.skycore.db_encryption.repository.cache.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_user")
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "first_name") val firstName: String,
//    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "password") val password: String
)
