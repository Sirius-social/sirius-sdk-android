package com.sirius.sample.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.sirius.sample.R

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container,false)
        val connectBtn : ImageView = view.findViewById(R.id.connectBtn);
        connectBtn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.addToBackStack("1")?.replace(R.id.mainFrame, ShowQrFragment())?.commit()
        }
        return view
    }



}