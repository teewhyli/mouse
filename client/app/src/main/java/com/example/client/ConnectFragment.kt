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
import java.lang.RuntimeException
import java.util.concurrent.CompletableFuture.supplyAsync
import java.util.concurrent.ExecutorService


/**
 * A simple [Fragment] subclass.
 * Use the [ConnectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConnectFragment : Fragment() {

    private lateinit var connectButton: View
    private lateinit var linearProgressIndicator: LinearProgressIndicator
    private lateinit var es: ExecutorService
    private val TAG = "ConnectFragment"
    private var listener: ConnectListener? = null

    fun interface ConnectListener {
        fun notify(something: String)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        connectButton = view.findViewById(R.id.ConnectButton)
        linearProgressIndicator = view.findViewById(R.id.linearProgressIndicator)
        linearProgressIndicator.visibility = View.INVISIBLE

        connectButton.setOnClickListener {
            linearProgressIndicator.visibility = View.VISIBLE
            supplyAsync({ Thread.sleep(1000)}, es)
                .thenApply{
                    listener?.notify("ha")
                }
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connect, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param ExecutorService executorService.
         * @return A new instance of fragment ConnectFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(executorService: ExecutorService) =
            ConnectFragment().apply {
            es = executorService
        }
    }
}