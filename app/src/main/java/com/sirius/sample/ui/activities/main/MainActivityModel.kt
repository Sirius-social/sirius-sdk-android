package com.sirius.sample.ui.activities.main




import com.sirius.sample.base.providers.ResourcesProvider
import com.sirius.sample.base.ui.BaseActivityModel
import javax.inject.Inject

class MainActivityModel @Inject constructor(
    resourceProvider: ResourcesProvider

) : BaseActivityModel(resourceProvider) {

    override fun onViewCreated() {
        super.onViewCreated()

    }

}