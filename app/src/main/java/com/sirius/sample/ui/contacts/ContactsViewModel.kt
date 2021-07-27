package com.sirius.sample.ui.contacts

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.sirius.sample.base.providers.ResourcesProvider
import com.sirius.sample.base.ui.BaseViewModel
import com.sirius.sample.models.ui.ItemContacts
import com.sirius.sample.models.ui.ItemTags
import com.sirius.sample.repository.UserRepository
import java.util.*

import javax.inject.Inject



open class ContactsViewModel @Inject constructor(
    val userRepository: UserRepository,
    resourcesProvider: ResourcesProvider
) : BaseViewModel(resourcesProvider) {



    val adapterListLiveData : MutableLiveData<List<ItemContacts>> = MutableLiveData(listOf())
    val onChatClickLiveData : MutableLiveData<ItemContacts?> = MutableLiveData()
    val onAddTagBtnClickLiveData : MutableLiveData<ItemContacts?> = MutableLiveData()
    val onMoreBtnClickLiveData : MutableLiveData<ItemContacts?> = MutableLiveData()
    val onDetailsClickLiveData : MutableLiveData<ItemContacts?> = MutableLiveData()
    val onTagsClickLiveData : MutableLiveData<ItemTags?> = MutableLiveData()


    fun onChatsClick(item: ItemContacts) {
        onChatClickLiveData.postValue(item)
    }

    fun onAddTagBtnClick(item: ItemContacts) {
        onAddTagBtnClickLiveData.postValue(item)
    }

    fun onMoreActionBtnClick(item: ItemContacts) {
        onMoreBtnClickLiveData.postValue(item)
    }

    fun onDetailsClick(item: ItemContacts) {
        onDetailsClickLiveData.postValue(item)
    }

    fun onTagsClick(item: ItemTags) {
        onTagsClickLiveData.postValue(item)
    }

    private fun createList() : MutableList<ItemContacts>{
        val list :  MutableList<ItemContacts> = mutableListOf()
        list.add(ItemContacts("Hoth departemnt", Date(),false))
        list.add(ItemContacts("Jedy academu", Date(),false))
        list.add(ItemContacts("USCIS", Date(1620761306000),true))
        list.add(ItemContacts("Airport milan", Date(1620761306000),true))
        return list

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


