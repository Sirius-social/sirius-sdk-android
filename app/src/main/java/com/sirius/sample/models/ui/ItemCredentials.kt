package com.sirius.sample.models.ui

import java.util.*

class ItemCredentials {

    constructor() {

    }

    constructor(title: String, date: Date, isActionExist: Boolean) {
        this.title = title
        this.date = date
        this.isActionExist = isActionExist
    }

    var title: String? = null
    var date: Date? = null
    var isActionExist: Boolean = false

}