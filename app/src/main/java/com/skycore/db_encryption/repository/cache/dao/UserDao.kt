package com.skycore.db_encryption.repository.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skycore.db_encryption.repository.cache.entity.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM tbl_user WHERE first_name = :firstName")
    fun getByName(firstName:String) : LiveData<User>
}