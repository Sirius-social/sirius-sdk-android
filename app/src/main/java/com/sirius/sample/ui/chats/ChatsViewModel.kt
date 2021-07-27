package com.sirius.sample.ui.chats

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.sirius.sample.base.providers.ResourcesProvider
import com.sirius.sample.base.ui.BaseViewModel
import com.sirius.sample.models.ui.ItemContacts
import com.sirius.sample.repository.UserRepository
import java.util.*

import javax.inject.Inject



open class ChatsViewModel @Inject constructor(
    val userRepository: UserRepository,
    resourcesProvider: ResourcesProvider
) : BaseViewModel(resourcesProvider) {



    val adapterListLiveData : MutableLiveData<List<ItemContacts>> = MutableLiveData(listOf())



    private fun createList() : MutableList<ItemContacts>{
        val list :  MutableList<ItemContacts> = mutableListOf()
        list.add(ItemContacts("Hoth departemnt", Date(),false))
        list.add(ItemContacts("Jedy academu", Date(),false))
        list.add(ItemContacts("USCIS", Date(1620761306000),true))
        list.add(ItemContacts("Airport milan", Date(1620761306000),true))
        return list

    }


    fun onChooseIdClick(v: View) {

    }

    fun onFilterClick(v: View){

    }

    override fun setupViews() {
        super.setupViews()
        val list :  MutableList<ItemContacts> = createList()
        //TODO add date to list
        adapterListLiveData.postValue(list)
    }



}


