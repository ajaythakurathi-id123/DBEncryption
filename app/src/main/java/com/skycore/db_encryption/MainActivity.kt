package com.skycore.db_encryption

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.skycore.db_encryption.databinding.ActivityMainBinding
import com.skycore.db_encryption.misc.SQLCipherUtils
import com.skycore.db_encryption.repository.cache.CommentRepository
import com.skycore.db_encryption.repository.cache.DatabaseHandler
import com.skycore.db_encryption.repository.cache.SQLDBHelper
import com.skycore.db_encryption.repository.cache.UserRepository
import com.skycore.db_encryption.repository.cache.entity.User
import com.skycore.db_encryption.viewmodel.MainViewModel
import com.skycore.db_encryption.viewmodel.MainViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //inserting on login click
        binding.btnLogin.setOnClickListener(this)
        binding.btnGetPass.setOnClickListener(this)
        binding.btnInsert.setOnClickListener(this)
        binding.btnEncryptDB.setOnClickListener(this)
    }

    private fun checkViewModelStatus() {
        //init objects/observers if not initialized
        if (!this::viewModel.isInitialized) {
            val userDao = DatabaseHandler.getDatabase(applicationContext).userDao()
            val commentDao = DatabaseHandler.getDatabase(applicationContext).commentDao()
            val userRepo = UserRepository(userDao)
            val commentRepo = CommentRepository(commentDao)

            viewModel = ViewModelProvider(
                this,
                MainViewModelFactory(userRepo, commentRepo)
            )[MainViewModel::class.java]


            viewModel.fetchedUser.observe(this) {
                Log.d(TAG, "onCreate: user: $it")
                it?.let { user ->
                    binding.tvPassword.text = user.password
                }
            }
        }
    }

    private fun validateFields(): Boolean {
        return binding.edtUsername.text.toString()
            .isNotBlank() && binding.edtPassword.text.toString().isNotBlank()
    }

    private fun insertInRoom() {
        //init viewModel and other observers
        checkViewModelStatus()

        if (binding.edtUsername.text.toString()
                .isNotBlank() && binding.edtPassword.text.toString().isNotBlank()
        ) {
            //inserting user if fields are not empty
            viewModel.insertUser(
                User(
                    0,
                    binding.edtUsername.text.toString(),
                    binding.edtPassword.text.toString()
                )
            )
        } else {
            //if either fields are empty fields
            Toast.makeText(applicationContext, "Empty Fields!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun insertInSqlite() {

    }

    private fun createSQLiteDB(){

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.btnLogin.id -> {
                if(validateFields()){
                    //Room
                    insertInRoom()

                    //Sqlite
//                    insertInSqlite()
                }else{
                    //if either fields are empty fields
                    Toast.makeText(applicationContext, "Empty Fields!!", Toast.LENGTH_SHORT).show()
                }
            }
            binding.btnGetPass.id -> {
                //init viewModel and other observers
                checkViewModelStatus()
                viewModel.getUser(binding.edtUsername.text.toString())

                //for sqlite

            }
            binding.btnInsert.id -> {
                //init viewModel and other observers
                checkViewModelStatus()

                //inserting 1000 users
                viewModel.insertUsers()
            }
            binding.btnEncryptDB.id -> {
                lifecycleScope.launch(Dispatchers.Default) {
                    SQLCipherUtils.encrypt(applicationContext)
                }
            }
        }
    }
}