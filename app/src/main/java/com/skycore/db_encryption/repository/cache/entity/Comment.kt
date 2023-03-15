package com.skycore.db_encryption.repository.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_comment")
data class Comment(
    @PrimaryKey
    val id:Int,
    val postId:Int,
    val name:String,
    val email:String,
    val body:String
)
