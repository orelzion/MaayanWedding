package com.orelzion.maayanwedding.data

import java.util.*

/**
 * Created by orelzion on 30/07/2017.
 */
data class Attendee(val name: String = "", val count: Int = 0, val tableNum: List<Int> = emptyList(), val hasArrived: Boolean = false, val uuid: String = UUID.randomUUID().toString()) {

    override fun equals(other: Any?): Boolean {
        return (other as? Attendee)?.uuid?.equals(this.uuid) ?: false
    }
}