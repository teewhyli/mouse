package com.example.client

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import android.os.*
import android.util.Log
import android.view.inputmethod.InputMethodManager
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity(), ConnectFragment.ConnectListener {

    private val es: ExecutorService = Executors.newCachedThreadPool()
    private var TAG = "MainActivity"
    private val fragmentManager: FragmentManager = supportFragmentManager
    private lateinit var imm: InputMethodManager
    private lateinit var connectFragment: Fragment
    private lateinit var mainFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        es.submit{
            connectFragment = ConnectFragment.newInstance(es)
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.FragmentContainer, connectFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        es.shutdownNow()
    }

    override fun notify(something: String) {
        Log.d(TAG, "ConnectFragment notify to switch")
        mainFragment = MainFragment.newInstance(imm, es)
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