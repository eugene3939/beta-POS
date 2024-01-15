package com.example.myposbeta.dbHelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TranslationDBHelper(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "TransactionDB.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS TransactionTable (" //建立ProductTable
                + "tId INTEGER PRIMARY KEY AUTOINCREMENT,"  //交易id
                + "tDate DATE,"     //交易日期
                + "tDescription TEXT);")  //交易描述

//        Log.d("進入table", "link start")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS TransactionTable;")
        onCreate(db)
    }
}