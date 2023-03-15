package com.skycore.db_encryption.misc

import android.content.Context
import android.database.Cursor
import android.util.Log
import net.sqlcipher.database.SQLiteDatabase
import java.io.File

object SQLCipherUtils {
    private const val TAG = "SQLCipherUtils"

    fun encrypt(ctx: Context) {
        //This is necessary, no idea why
        SQLiteDatabase.loadLibs(ctx)

        val unencryptedDatabaseFile: File = ctx.getDatabasePath("unencrypted.db")
        val encryptedDatabaseFile: File = ctx.getDatabasePath("encrypted.db")

        //opening plain/unencrypted DB
        var database = SQLiteDatabase.openOrCreateDatabase(unencryptedDatabaseFile, "", null)

        //executing query to migrate plain DB to encrypted one
        val query: String = String.format(
            "ATTACH DATABASE '%s' AS encrypted KEY '%s'",
            encryptedDatabaseFile.absolutePath, Constants.DB_PASS_PHRASE
        )
        database.rawExecSQL(query)
        database.rawExecSQL("select sqlcipher_export('encrypted')")
        database.rawExecSQL("DETACH DATABASE encrypted")
        database.close()

        //confirming database is copied
        database = SQLiteDatabase.openOrCreateDatabase(
            encryptedDatabaseFile, Constants.DB_PASS_PHRASE,
            null
        )
        val cursor: Cursor = database.rawQuery("select * from tbl_user", arrayOf())
        cursor.moveToFirst()
        val a: String = cursor.getString(0)
        val b: String = cursor.getString(1)
        val c: String = cursor.getString(2)
        Log.d(TAG, "encrypt: a: $a || b: $b || c: $c")

        cursor.close()
        database.close()

        //deleting old/unencrypted DB
        val x = unencryptedDatabaseFile.delete()
        Log.d(TAG, "encrypt: OG delete: $x")

        //updating DB name in shared pref
        setDBName(ctx, Constants.DB_NAME_ENCRYPTED)
    }

    private fun setDBName(context: Context, dbName: String) {
        val dbPref = context.getSharedPreferences(Constants.PREF_DB, Context.MODE_PRIVATE)
        val editor = dbPref.edit()
        editor.putString(Constants.DB_NAME_PREF_KEY, dbName)
        editor.apply()
    }
}