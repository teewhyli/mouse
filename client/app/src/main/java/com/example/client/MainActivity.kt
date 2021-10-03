package com.example.client

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity(), ConnectFragment.ConnectListener {

    private var TAG = "MainActivity"
    private val fragmentManager: FragmentManager = supportFragmentManager
    private lateinit var imm: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        fragmentManager.beginTransaction().apply {
            replace(R.id.FragmentContainer, ConnectFragment.newInstance())
            addToBackStack(null)
            commit()
        }
    }

    override fun notify(msg: String) {
        Log.d(TAG, msg)

        fragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_right,
                R.anim.enter_from_right,
                R.anim.exit_to_right)
            replace(R.id.FragmentContainer, MainFragment.newInstance(imm))
            addToBackStack(null)
            commit()
        }
    }
}