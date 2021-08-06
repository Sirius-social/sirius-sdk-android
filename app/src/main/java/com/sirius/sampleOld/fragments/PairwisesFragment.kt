package com.sirius.sampleOld.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

import com.sirius.sdk_android.helpers.PairwiseHelper
import com.sirius.sample.R

class PairwisesFragment : Fragment() {

    var list : RecyclerView? =null
    var adapter = PairwisesListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pairwises, container,false)
        list  = view.findViewById(R.id.contactsRecyclerView)
        adapter.setOnAdapterItemClick {
           // PairwiseHelper.getInstance().sendMessageTo("TEST 0095",it)
        }
        list!!.adapter = adapter
        updateAdapter()
        return view
    }


    fun updateAdapter(){
        val list = PairwiseHelper.getInstance().getAllPairwise()
        adapter.setDataList(list)
        adapter.notifyDataSetChanged()
    }


}