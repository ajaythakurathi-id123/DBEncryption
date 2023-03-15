package com.skycore.db_encryption.repository.cache

import androidx.lifecycle.LiveData
import com.skycore.db_encryption.repository.cache.dao.UserDao
import com.skycore.db_encryption.repository.cache.entity.User

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: User) {
        userDao.insert(user)
    }

    fun getUser(username: String): LiveData<User> {
        return userDao.getByName(username)
    }
}