package com.skycore.db_encryption.repository.cache

import androidx.lifecycle.LiveData
import com.skycore.db_encryption.repository.cache.dao.CommentDao
import com.skycore.db_encryption.repository.cache.entity.Comment
import kotlinx.coroutines.*

class CommentRepository(private val commentDao: CommentDao) {

    //Rest API
    var job: CompletableJob? = null

    fun getAllComments(): LiveData<List<Comment>> {
        job = Job()
        return object : LiveData<List<Comment>>() {
            override fun onActive() {
                super.onActive()
                job?.let {
                    CoroutineScope(Dispatchers.IO + it).launch {

                    }
                }
            }
        }
    }

    //Database Part
    suspend fun insertComment(comment: Comment) {
        commentDao.insert(comment)
    }

    fun getComment(id: Int): LiveData<Comment> {
        return commentDao.getById(id)
    }
}