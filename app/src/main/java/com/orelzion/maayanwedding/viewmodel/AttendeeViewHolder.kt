package com.orelzion.maayanwedding.viewmodel

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orelzion.maayanwedding.R
import com.orelzion.maayanwedding.data.Attendee
import kotlinx.android.synthetic.main.view_holder_attendee.view.*

/**
 * Created by orelzion on 30/07/2017.
 */
class AttendeeViewHolder private constructor(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun newInstance(context: Context, parent: ViewGroup): AttendeeViewHolder {
            return AttendeeViewHolder(LayoutInflater.from(context).inflate(R.layout.view_holder_attendee,
                    parent,
                    false))
        }
    }


    fun bind(attendee: Attendee, attendeeListener: AttendeeListener? = null) {
        itemView.name.text = "${attendee.name} (${attendee.count})"
        itemView.tableNumber.text = attendee.tableNum.toString()
        itemView.hasArrived.isChecked = attendee.hasArrived

        itemView.hasArrived.setOnCheckedChangeListener { _, isChecked ->
            attendeeListener?.onChecked(adapterPosition, isChecked)
        }

        itemView.edit.setOnClickListener {
            attendeeListener?.onEdit(adapterPosition)
        }
    }

    interface AttendeeListener {
        fun onChecked(attendeePosition: Int, checked: Boolean)
        fun onEdit(attendeePosition: Int)
    }

}