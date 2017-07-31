package com.orelzion.maayanwedding.viewmodel

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orelzion.maayanwedding.R
import com.orelzion.maayanwedding.data.Attendee
import kotlinx.android.synthetic.main.view_holder_table.view.*

/**
 * Created by orelzion on 31/07/2017.
 */
class TableViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun newInstance(parent: ViewGroup): TableViewHolder {
            return TableViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_holder_table,
                    parent,
                    false))
        }
    }

    fun bind(attendees: List<Attendee>) {
        itemView.tableNumbers.text = attendees[0].tableNum.toString()
        itemView.invitedCount.text = attendees.sumBy { it.count }.toString()
        val attended = attendees.filter { it.hasArrived }.sumBy { it.count }
        itemView.attended.text = attended.toString()
        val factor = if (attendees[0].tableNum.contains(44) || attendees[0].tableNum.contains(38)) 24 else 12
        itemView.available.text = ((attendees[0].tableNum.size * factor) - attended).toString()
    }
}