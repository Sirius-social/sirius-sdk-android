package com.sirius.sample.ui.chats

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.sirius.sample.base.providers.ResourcesProvider
import com.sirius.sample.base.ui.BaseViewModel
import com.sirius.sample.models.ui.ItemContacts
import com.sirius.sample.models.ui.message.BaseItemMessage
import com.sirius.sample.repository.EventRepository
import com.sirius.sample.repository.UserRepository
import com.sirius.sample.transform.EventTransform
import java.util.*

import javax.inject.Inject



open class ChatsViewModel @Inject constructor(
    val userRepository: UserRepository,
    resourcesProvider: ResourcesProvider,
    val eventRepository: EventRepository
) : BaseViewModel(resourcesProvider) {



    val adapterListLiveData : MutableLiveData<List<BaseItemMessage>> = MutableLiveData(listOf())

    var item: ItemContacts? =null


    private fun createList() : MutableList<BaseItemMessage>{
        val list :  MutableList<BaseItemMessage> = mutableListOf()
       /* if(no pairwise){
            add invitation
        }else{
            fetch all event for pairwise
        }*/
        val event =  EventTransform.itemContactsToEvent(item,eventRepository)
        val message = EventTransform.eventToBaseItemMessage(event)
        list.add(message)
        return list

    }


    fun onChooseIdClick(v: View) {

    }

    fun onFilterClick(v: View){

    }

    override fun setupViews() {
        super.setupViews()
        val list :  MutableList<BaseItemMessage> = createList()
        //TODO add date to list
        adapterListLiveData.postValue(list)
        setTitle(item?.title)
    }



}


