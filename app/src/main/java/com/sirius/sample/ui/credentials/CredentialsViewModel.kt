package com.sirius.sample.ui.credentials

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.sirius.sample.base.providers.ResourcesProvider
import com.sirius.sample.base.ui.BaseViewModel
import com.sirius.sample.models.ui.ItemCredentials
import com.sirius.sample.models.ui.ItemCredentialsDetails
import com.sirius.sample.repository.UserRepository
import com.sirius.sdk_android.helpers.PairwiseHelper
import java.util.*

import javax.inject.Inject



open class CredentialsViewModel @Inject constructor(
    val userRepository: UserRepository,
    resourcesProvider: ResourcesProvider
) : BaseViewModel(resourcesProvider) {

    val adapterListLiveData : MutableLiveData<List<ItemCredentials>> = MutableLiveData(listOf())



    private fun createList() : List<ItemCredentials>{

        val credentilas = PairwiseHelper.getInstance().getAllCredentials()
        val list = credentilas.map {
           val item =  ItemCredentials(it.schema_id ?:"", Date(), false)
            item.detailList = it.getAttributes().map {
                ItemCredentialsDetails(it.name, it.value)
            }
            item
        }
        return list

    }

    fun onFilterClick(v: View){

    }


    override fun setupViews() {
        super.setupViews()
       val list =  createList()
        adapterListLiveData.postValue(list)
    }

}


