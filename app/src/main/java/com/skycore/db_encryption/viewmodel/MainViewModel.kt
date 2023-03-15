package com.skycore.db_encryption.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.skycore.db_encryption.repository.cache.CommentRepository
import com.skycore.db_encryption.repository.cache.UserRepository
import com.skycore.db_encryption.repository.cache.entity.Comment
import com.skycore.db_encryption.repository.cache.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {

    //User Part
    private val _username: MutableLiveData<String> = MutableLiveData()

    val fetchedUser: LiveData<User> = Transformations.switchMap(_username) {
        userRepository.getUser(it)
    }

    fun getUser(username: String) {
        _username.value = username
    }

    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insertUser(user)
        }
    }

    fun insertUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            var count = 10

            for (i in 0..1000) {
                insertUser(User(0, "user$count", "pass$count"))
                count++
            }
        }
    }

    //Comment Part
    private val _id: MutableLiveData<Int> = MutableLiveData()

    val fetchedComment: LiveData<Comment> = Transformations.switchMap(_id) {
        commentRepository.getComment(it)
    }

    fun getComment(id:Int){
        _id.value = id
    }

    fun insertComment(comment: Comment) {
        viewModelScope.launch(Dispatchers.IO) {
            commentRepository.insertComment(comment)
        }
    }

    fun insertComments() {
        viewModelScope.launch(Dispatchers.IO) {
            var count = 1

            for (i in 0..500) {
//                insertComment(User(0, "user$count", "pass$count"))
                count++;
            }
        }
    }
}

class MainViewModelFactory(
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(userRepository, commentRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}