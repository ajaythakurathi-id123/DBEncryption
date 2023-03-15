package com.skycore.db_encryption.repository.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.skycore.db_encryption.misc.Constants
import com.skycore.db_encryption.repository.cache.dao.CommentDao
import com.skycore.db_encryption.repository.cache.dao.UserDao
import com.skycore.db_encryption.repository.cache.entity.Comment
import com.skycore.db_encryption.repository.cache.entity.User
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteDatabaseHook
import net.sqlcipher.database.SupportFactory

@Database(entities = [User::class, Comment::class], version = 1, exportSchema = false)
abstract class DatabaseHandler : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun commentDao(): CommentDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: DatabaseHandler? = null

        fun getDatabase(context: Context): DatabaseHandler {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database

            //LATER -- Make sure your passphrase is NOT hardcoded and is secured, like using the Android KeyStore
            //encrypting DB using pass phrase
            val passphrase: ByteArray =
                SQLiteDatabase.getBytes(Constants.DB_PASS_PHRASE.toCharArray())
            val factory = SupportFactory(passphrase, object : SQLiteDatabaseHook {
                override fun preKey(database: SQLiteDatabase?) = Unit

                override fun postKey(database: SQLiteDatabase?) {
                    database?.rawExecSQL(
                        "PRAGMA cipher_memory_security = OFF"
                    )
                }
            })

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseHandler::class.java,
                    getDBName(context)
                )
                    .openHelperFactory(factory)
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private fun getDBName(context: Context): String {
            val dbPref = context.getSharedPreferences(Constants.PREF_DB, Context.MODE_PRIVATE)
            return dbPref.getString(Constants.DB_NAME_PREF_KEY, null)
                ?: Constants.DB_NAME_UNENCRYPTED
        }
    }
}