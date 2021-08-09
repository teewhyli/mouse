package com.example.client

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layout: View = findViewById(R.id.touch_board)
//        val controller = layout.windowInsetsController
//
//        controller?.show(WindowInsets.Type.ime())

        val editText: EditText = findViewById(R.id.edit_board_input)
        editText.requestFocus()
//        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)

        layout.setOnTouchListener { v, event ->
            println(event)
            return@setOnTouchListener true
        }
    }
}