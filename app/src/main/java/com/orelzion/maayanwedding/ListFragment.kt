package com.orelzion.maayanwedding

import android.app.Fragment
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import com.orelzion.maayanwedding.data.Attendee
import com.orelzion.maayanwedding.data.AttendeeManager
import com.orelzion.maayanwedding.viewmodel.AttendeeViewHolder
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * Created by orelzion on 31/07/2017.
 */
class ListFragment : Fragment(), MainActivity.OnDataUpdated {

    lateinit var adapter: AttendeeAdapter
    var isInSearchMode = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        AttendeeManager.instance.addListener(this)

        attendeesList.layoutManager = LinearLayoutManager(view.context,
                LinearLayoutManager.VERTICAL,
                false)
        adapter = AttendeeAdapter()
        attendeesList.adapter = adapter


        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.START,
                ItemTouchHelper.START or ItemTouchHelper.END) {
            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val attendee = AttendeeManager.instance.attendees[viewHolder.adapterPosition]

                AlertDialog.Builder(view.context)
                        .setMessage(getString(R.string.delete_message, attendee.name))
                        .setPositiveButton(android.R.string.yes, { _, _ ->
                            AttendeeManager.instance.myRef.child(attendee.uuid).removeValue()
                        })
                        .setNegativeButton(R.string.cancel, { _, _ ->
                            adapter.resetToAttendeeList()
                        })
                        .show()
            }


        })
        itemTouchHelper.attachToRecyclerView(attendeesList)

        add.setOnClickListener {
            val addAttendee = AddAttendeeView(view.context)
            AlertDialog.Builder(view.context)
                    .setView(addAttendee)
                    .setPositiveButton(R.string.add, { _, _ ->
                        val attendee = addAttendee.attendee
                        AttendeeManager.instance.myRef.child(attendee.uuid).setValue(attendee)
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        AttendeeManager.instance.removeListener(this)
    }

    override fun onUpdated() {
        if (!isInSearchMode) {
            adapter.resetToAttendeeList()
        }
    }

    inner class AttendeeAdapter : RecyclerView.Adapter<AttendeeViewHolder>() {

        var attList = emptyList<Attendee>()


        fun resetToAttendeeList() {
            attList = AttendeeManager.instance.attendees
            attList = attList.sortedBy { it.name }
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendeeViewHolder {
            return AttendeeViewHolder.newInstance(parent.context, parent)
        }

        override fun onBindViewHolder(holder: AttendeeViewHolder?, position: Int) {
            holder?.bind(attList[position], object : AttendeeViewHolder.AttendeeListener {
                override fun onChecked(attendeePosition: Int, checked: Boolean) {
                    val attendee = attList[attendeePosition]
                    AttendeeManager.instance.myRef.child(attendee.uuid).setValue(attendee.copy(
                            hasArrived = checked))
                }

                override fun onEdit(attendeePosition: Int) {
                    val attendee = attList[attendeePosition]
                    val editAttendee = EditAttendeeView(this@ListFragment.activity)
                    editAttendee.updateWithAttendee(attendee)
                    AlertDialog.Builder(this@ListFragment.activity)
                            .setView(editAttendee)
                            .setPositiveButton(R.string.save, { _, _ ->
                                val attendee = editAttendee.getUpdatedAttendee()
                                AttendeeManager.instance.myRef.child(attendee.uuid).setValue(
                                        attendee)
                            })
                            .setNegativeButton(R.string.cancel, null)
                            .show()
                }
            })
        }

        override fun getItemCount(): Int {
            return attList.size
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.list_menu, menu)
        // Retrieve the SearchView and plug it into SearchManager
        val searchView = MenuItemCompat.getActionView(menu.findItem(R.id.action_search)) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {

                if (newText.isEmpty()) {
                    isInSearchMode = false
                    adapter.resetToAttendeeList()
                    return true
                }

                isInSearchMode = true

                val attendee = AttendeeManager.instance.attendees.filter { it.name.contains(newText) }
                if (attendee != null) {
                    adapter.attList = attendee
                } else {
                    adapter.attList = emptyList()
                }
                adapter.notifyDataSetChanged()
                return true
            }

        })
    }
}