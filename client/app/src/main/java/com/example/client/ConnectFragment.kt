package com.example.client

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class ConnectFragment : Fragment() {

    private val TAG = "ConnectFragment"
    private var listener: ConnectListener? = null

    fun interface ConnectListener {
        fun notify(msg: String)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        view.findViewById<View>(R.id.ConnectButton).setOnClickListener {
            view.findViewById<View>(R.id.linearProgressIndicator).apply {
                visibility = View.VISIBLE
            }
            TCP.connect("192.168.1.83", 5555).thenApply {
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
        inflater: LayoutInflater,
        container: ViewGroup?,
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