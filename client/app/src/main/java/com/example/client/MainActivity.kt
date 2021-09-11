package com.example.client

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity(), ConnectFragment.ConnectListener {

    private var TAG = "MainActivity"
    private val fragmentManager: FragmentManager = supportFragmentManager
    private lateinit var imm: InputMethodManager
    private lateinit var connectFragment: Fragment
    private lateinit var mainFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        ExecutorServiceFactory.executorService.submit{
            connectFragment = ConnectFragment.newInstance()
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.FragmentContainer, connectFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ExecutorServiceFactory.executorService.shutdownNow()
    }

    override fun notify(msg: String) {
        Log.d(TAG, msg)
        mainFragment = MainFragment.newInstance(imm)
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_right,
            R.anim.enter_from_right,
            R.anim.exit_to_right)
        fragmentTransaction.replace(R.id.FragmentContainer, mainFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}