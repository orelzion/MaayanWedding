package com.orelzion.maayanwedding.viewmodel

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orelzion.maayanwedding.AddAttendeeView
import com.orelzion.maayanwedding.EditAttendeeView
import com.orelzion.maayanwedding.R
import com.orelzion.maayanwedding.data.Attendee
import com.orelzion.maayanwedding.data.AttendeeManager
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


    fun bind(attendee: Attendee, onChecked: OnChecked? = null) {
        itemView.name.text = "${attendee.name} (${attendee.count})"
        itemView.tableNumber.text = attendee.tableNum.toString()
        itemView.hasArrived.isChecked = attendee.hasArrived

        //in some cases, it will prevent unwanted situations
        itemView.hasArrived.setOnCheckedChangeListener(null);


        itemView.hasArrived.setOnCheckedChangeListener { _, isChecked ->
            onChecked?.onChecked(attendee, isChecked)
        }

        itemView.edit.setOnClickListener { null }
        itemView.edit.setOnClickListener {
            val editAttendee = EditAttendeeView(it.context)
            editAttendee.updateWithAttendee(attendee)
            AlertDialog.Builder(it.context)
                    .setView(editAttendee)
                    .setPositiveButton(R.string.save, { _, _ ->
                        val attendee = editAttendee.getUpdatedAttendee()
                        AttendeeManager.instance.myRef.child(attendee.uuid).setValue(attendee)
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show()
        }
    }



    interface OnChecked {
        fun onChecked(attendee: Attendee, checked: Boolean)
    }

}