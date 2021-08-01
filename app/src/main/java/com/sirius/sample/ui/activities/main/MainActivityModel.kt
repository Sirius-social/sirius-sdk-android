package com.sirius.sample.ui.activities.main




import com.sirius.sample.base.providers.ResourcesProvider
import com.sirius.sample.base.ui.BaseActivityModel
import com.sirius.sample.repository.EventRepository
import javax.inject.Inject

class MainActivityModel @Inject constructor(
    resourceProvider: ResourcesProvider,
    val eventRepository: EventRepository

) : BaseActivityModel(resourceProvider) {

   val invitationStartLiveData = eventRepository.invitationStartLiveData
   val invitationStopLiveData = eventRepository.invitationStopLiveData
   val eventStoreLiveData = eventRepository.eventStoreLiveData
   val eventStartLiveData = eventRepository.eventStartLiveData
   val eventStopLiveData = eventRepository.eventStopLiveData


    override fun onViewCreated() {
        super.onViewCreated()
    }

}