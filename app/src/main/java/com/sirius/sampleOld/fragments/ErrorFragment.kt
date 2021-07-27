package com.sirius.sampleOld.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sirius.sample.R


class ErrorFragment : Fragment() {


    var hintText : TextView? = null

    companion object  {

        fun newInstance(error: String?):ErrorFragment {
            val args = Bundle()
            args.putString("error",error)
            val fragment = ErrorFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_error, container,false)
        hintText =  view.findViewById(R.id.hintText)
        val errorText =  arguments?.getString("error")
        hintText?.text = errorText
        return view
    }



}