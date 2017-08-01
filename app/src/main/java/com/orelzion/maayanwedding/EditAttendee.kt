package com.orelzion.maayanwedding

import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import com.orelzion.maayanwedding.data.Attendee
import kotlinx.android.synthetic.main.view_edit_attendee.view.*

/**
 * Created by shani on 30/07/2017.
 */

class EditAttendeeView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

     var attendeeToEdit:Attendee? = null

    init {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER
        inflate(context, R.layout.view_edit_attendee, this)
    }

    fun updateWithAttendee(attendee: Attendee) {
        attendeeToEdit = attendee
        name.setText(attendee.name)
        count.setText(attendee.count.toString())
        val tables = attendee.tableNum.joinToString(separator = "-")
        tableNumber.setText(tables)
        tableCapcity.setText(attendee.tableSize.toString())
    }

    fun getUpdatedAttendee():Attendee {
        val tables = tableNumber.text.toString().split("-").mapNotNull { it.toIntOrNull() }
        var att = attendeeToEdit!!.copy(name = name.text.toString(), count = count.text.toString().toInt(), tableNum = tables, tableSize = tableCapcity.text.toString().toInt())
        return  att
    }

}