package com.sirius.sample.ui.contacts

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.sirius.sample.base.providers.ResourcesProvider
import com.sirius.sample.base.ui.BaseViewModel
import com.sirius.sample.models.ui.ItemContacts
import com.sirius.sample.models.ui.ItemTags
import com.sirius.sample.repository.UserRepository
import com.sirius.sample.transform.PairwiseTransform
import com.sirius.sdk_android.SiriusSDK
import com.sirius.sdk_android.helpers.PairwiseHelper
import java.util.*

import javax.inject.Inject


open class ContactsViewModel @Inject constructor(
    val userRepository: UserRepository,
    resourcesProvider: ResourcesProvider
) : BaseViewModel(resourcesProvider) {


    val adapterListLiveData: MutableLiveData<List<ItemContacts>> = MutableLiveData(listOf())
    val onChatClickLiveData: MutableLiveData<ItemContacts?> = MutableLiveData()
    val onAddTagBtnClickLiveData: MutableLiveData<ItemContacts?> = MutableLiveData()
    val onMoreBtnClickLiveData: MutableLiveData<ItemContacts?> = MutableLiveData()
    val onDetailsClickLiveData: MutableLiveData<ItemContacts?> = MutableLiveData()
    val onTagsClickLiveData: MutableLiveData<ItemTags?> = MutableLiveData()


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

    private fun createList(): List<ItemContacts> {
        val pairwises = PairwiseHelper.getInstance().getAllPairwise()
        val list = pairwises.map {
            PairwiseTransform.pairwiseToItemContacts(it)
        }
        return list
    }


    fun onFilterClick(v: View) {

    }

    override fun setupViews() {
        super.setupViews()
        val list: List<ItemContacts> = createList()
        //TODO add date to list
        adapterListLiveData.postValue(list)
    }


}


