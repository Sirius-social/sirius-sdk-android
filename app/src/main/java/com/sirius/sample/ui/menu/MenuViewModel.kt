package com.sirius.sample.ui.menu

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.sirius.sample.base.providers.ResourcesProvider
import com.sirius.sample.base.ui.BaseViewModel
import com.sirius.sample.transform.EventTransform
import com.sirius.sample.models.ui.ItemActions
import com.sirius.sample.repository.EventRepository
import com.sirius.sample.repository.UserRepository

import javax.inject.Inject



open class MenuViewModel @Inject constructor(
    val userRepository: UserRepository,
    resourcesProvider: ResourcesProvider,
    val eventRepository: EventRepository
) : BaseViewModel(resourcesProvider) {


    val goToShowQrScreenLiveData = MutableLiveData<Boolean>()
    val emptyVisibilityLiveData = MutableLiveData<Int>()
    val actionsListVisibilityLiveData = MutableLiveData<Int>()
    val adapterListLiveData : MutableLiveData<List<ItemActions>> = MutableLiveData(listOf())
    val eventLiveData = eventRepository.eventStoreLiveData



    fun onConnectClick(v: View) {
        goToShowQrScreenLiveData.postValue(true)
    }


    fun showHideEmpty(show : Boolean){
        if(show){
            emptyVisibilityLiveData.postValue(View.VISIBLE)
            actionsListVisibilityLiveData.postValue(View.GONE)
        }else{
            emptyVisibilityLiveData.postValue(View.GONE)
            actionsListVisibilityLiveData.postValue(View.VISIBLE)
        }

    }


    private fun createList() : List<ItemActions>{
        val   list = eventRepository.loadAllUnacceptedEventActions().map {
             EventTransform.eventToItemActions(it)
        }
        return list

    }

    fun updateList(){
        val list =  createList()
        showHideEmpty(list.isEmpty())
        adapterListLiveData.postValue(list)
    }

    override fun setupViews() {
        super.setupViews()
        updateList()
    }


}


