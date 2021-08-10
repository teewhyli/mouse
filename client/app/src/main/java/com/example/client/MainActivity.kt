package com.example.client

import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.*
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.util.*


class MainActivity : AppCompatActivity(), Callback {

    private lateinit var layout: View;
    private lateinit var extFloatingButton: ExtendedFloatingActionButton;
    private lateinit var imm: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layout = findViewById(R.id.touch_board)
        extFloatingButton = findViewById(R.id.extended_fab)
        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        extFloatingButton.setOnClickListener { showSoftKeyboard() }

        layout.setOnTouchListener { _, event ->
            println(event)
            return@setOnTouchListener true
        }
    }

    private fun showSoftKeyboard() {
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
    }

    private fun hideSoftKeyboard() {
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (event.isShiftPressed){
            println("shifted")
        }
        return when (keyCode) {
            KEYCODE_ENTER -> {
                hideSoftKeyboard()
                true
            }
            KEYCODE_SHIFT_LEFT -> { true }
            else -> {
                println(event)
                println(keyCode)
                super.onKeyUp(keyCode, event)
            }
        }
    }
}