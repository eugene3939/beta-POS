package com.example.myposbeta

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myposbeta.databinding.ActivityLoginBinding
import com.example.myposbeta.dbHelper.UserDBHelper

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbrw: SQLiteDatabase
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 使用 View Binding 初始化綁定
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        createTransactionDB() //創建TransactionDB
//        createProductDB() //創建ProductDB
        createUserDB()  //創建UserDB

        // 登入按鈕
        binding.btnLogin.setOnClickListener {
            // 取得帳號、密碼
            val acc = binding.edtAccount.text.toString()
            val pas = binding.edtPassword.text.toString()

            val loginQuery = "SELECT * FROM UserTable WHERE account = '$acc' AND password = '$pas';"
            val loginCursor = dbrw.rawQuery(loginQuery, null)

            // 檢查是否有查詢結果
            if (loginCursor.moveToFirst()) {
                val userName = loginCursor.getString(loginCursor.getColumnIndex("uName")) //取得用戶名稱
                val intent = Intent(this, MainActivity::class.java)
                Toast.makeText(this, "Welcome: $userName", Toast.LENGTH_SHORT).show()
                Log.d("用戶登入成功", "用戶名稱: $userName")
                startActivity(intent)
            } else {
                // 資料庫中未包含 User 的資料
                Toast.makeText(this, "登入失敗", Toast.LENGTH_SHORT).show()
                Log.d("登入失敗提示: ", loginQuery)
            }

            loginCursor?.close() // 確保在使用完畢後關閉 Cursor
        }
    }

    private fun createDatabase(dbHelper: SQLiteOpenHelper, tableName: String, defaultData: List<String>) {
        dbrw = dbHelper.writableDatabase

        // 檢查資料表是否為空
        val isEmptyQuery = "SELECT COUNT(*) FROM $tableName;"
        val countCursor = dbrw.rawQuery(isEmptyQuery, null)

        if (countCursor.moveToFirst()) {
            val count = countCursor.getInt(0)

            // 資料表為空，新增預設資料進table
            if (count == 0) {
                Log.d("$tableName 為空", "還沒有放資料")

                for (data in defaultData) {
                    dbrw.execSQL(data)
                }

                Log.d("成功新增", "${defaultData.size} 組預設資料")
            } else {
                Log.d("$tableName 不為空", "他一共有 $count 組rows.")
            }
        } else {
            Log.e("$tableName 有其他問題", "Error in counting rows.")
        }

        countCursor.close()
    }

    private fun createUserDB() {
        val dbHelper = UserDBHelper(this)
        val defaultUserData = listOf(
            "INSERT INTO UserTable(uName, account, password) VALUES('Eugene', 1, 1);",
            "INSERT INTO UserTable(uName, account, password) VALUES('Oscar', 3, 3);"
        )
        createDatabase(dbHelper, "UserTable", defaultUserData)
    }

    override fun onDestroy() {
        // 在 Activity 銷毀時關閉資料庫連接
        dbrw.close()
        super.onDestroy()
    }
}


