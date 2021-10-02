package com.example.client

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.progressindicator.LinearProgressIndicator
import java.util.concurrent.CompletableFuture.supplyAsync
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ConnectFragment : Fragment() {

    private lateinit var connectButton: View
    private lateinit var linearProgressIndicator: LinearProgressIndicator
    private val TAG = "ConnectFragment"
    private var listener: ConnectListener? = null
    private val executorService: ExecutorService = Executors.newCachedThreadPool()

    fun interface ConnectListener {
        fun notify(msg: String)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        connectButton = view.findViewById(R.id.ConnectButton)
        linearProgressIndicator = view.findViewById(R.id.linearProgressIndicator)
        linearProgressIndicator.visibility = View.INVISIBLE

        connectButton.setOnClickListener {
            linearProgressIndicator.visibility = View.VISIBLE
            supplyAsync({ Thread.sleep(3000)}, executorService)
                .thenApply{
                    listener?.notify("ConnectFragment notify to switch")
                }
            Log.d(TAG, "click listener complete!")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ConnectListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement FragmentAListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_connect, container, false)
    }

    companion object {
        /**
         * @return A new instance of fragment ConnectFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = ConnectFragment()
    }
}