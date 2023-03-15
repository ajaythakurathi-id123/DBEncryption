package com.skycore.db_encryption.repository.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skycore.db_encryption.repository.cache.entity.Comment

@Dao
interface CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(comment:Comment)

    @Query("SELECT * FROM tbl_comment WHERE id = :id")
    fun getById(id:Int):LiveData<Comment>
}