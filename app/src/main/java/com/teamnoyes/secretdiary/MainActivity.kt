package com.teamnoyes.secretdiary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {
    private val firstNumberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.firstNumberPicker)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val secondNumberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.secondNumberPicker)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val thirdNumberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.thirdNumberPicker)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val openButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.openButton)
    }

    private val changePWButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.changePWButton)
    }

    private var changePWMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstNumberPicker
        secondNumberPicker
        thirdNumberPicker

        openButton.setOnClickListener {
            if (changePWMode) {
                Toast.makeText(this, "비밀번호 변경 중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // 다른 앱들과 공유하지 않음
            val passwordPreference = getSharedPreferences("password", Context.MODE_PRIVATE)

            val passwordFromUser =
                "${firstNumberPicker.value}${secondNumberPicker.value}${thirdNumberPicker.value}"

            if (passwordPreference.getString("password", "000") == passwordFromUser) {
                startActivity(Intent(this, DiaryActivity::class.java))
            } else {
                showErrorAlertDialog()
            }
        }

        changePWButton.setOnClickListener {
            val passwordPreference = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser =
                "${firstNumberPicker.value}${secondNumberPicker.value}${thirdNumberPicker.value}"

            if (changePWMode) {
                // 번호 저장

                // commit true로 설정 false일 시 apply로 설정 됨
                passwordPreference.edit(true) {
                    putString("password", passwordFromUser)
                }

                changePWMode = false
                changePWButton.setBackgroundColor(Color.BLACK)
            } else {
                // changePWMode 활성화 :: 비밀번호 맞는지 확인

                if (passwordPreference.getString("password", "000") == passwordFromUser) {
                    changePWMode = true
                    Toast.makeText(this, "변경할 패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    changePWButton.setBackgroundColor(Color.RED)

                } else {
                    showErrorAlertDialog()
                }
            }
        }
    }

    private fun showErrorAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("실패")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("확인") { _, _ -> } // 사용되지 않으므로 _로 이름 정의
            .create()
            .show()
    }
}