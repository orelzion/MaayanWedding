package com.orelzion.maayanwedding

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import com.orelzion.maayanwedding.data.Attendee
import kotlinx.android.synthetic.main.view_add_attendee.view.*

/**
 * Created by orelzion on 30/07/2017.
 */
class AddAttendeeView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER
        inflate(context, R.layout.view_add_attendee, this)
    }

    val attendee: Attendee
        get() {
            val tables = tableNumber.text.toString().split("-").mapNotNull { it.toIntOrNull() }
            return Attendee(name.text.toString(), count.text.toString().toInt(), tables, tableCapcity.text.toString().toInt())
        }
}