package com.orelzion.maayanwedding

import android.app.Fragment
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import com.orelzion.maayanwedding.data.Attendee
import com.orelzion.maayanwedding.data.AttendeeManager
import com.orelzion.maayanwedding.viewmodel.TableViewHolder
import kotlinx.android.synthetic.main.fragment_table.*

/**
 * Created by orelzion on 31/07/2017.
 */
class TableFragment : Fragment(), MainActivity.OnDataUpdated {

    lateinit var adapter: TablesAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        tablesList.layoutManager = LinearLayoutManager(view.context,
                LinearLayoutManager.VERTICAL,
                false)
        adapter = TablesAdapter()
        tablesList.adapter = adapter

        AttendeeManager.instance.addListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        AttendeeManager.instance.removeListener(this)
    }

    inner class TablesAdapter : RecyclerView.Adapter<TableViewHolder>() {


        var attList = emptyList<List<Attendee>>()

        fun resetToAttendeeList() {
            attList = AttendeeManager.instance.attendees.groupBy { it.tableNum }.values.toList()
            attList = attList.sortedBy { it.first().tableNum.first() }

            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
            return TableViewHolder.newInstance(parent)
        }

        override fun onBindViewHolder(holder: TableViewHolder?, position: Int) {
            holder?.bind(attList[position])
        }

        override fun getItemCount(): Int {
            return attList.size
        }
    }

    override fun onUpdated() {
        adapter.resetToAttendeeList()
    }

    private var isInSearchMode = false

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

                val attendee = AttendeeManager.instance.attendees.groupBy {
                    it.tableNum.contains(newText.toInt())
                }.filter { it.key }

                if (attendee != null) {
                    adapter.attList = attendee.values.toList()
                } else {
                    adapter.attList = emptyList()
                }
                adapter.notifyDataSetChanged()
                return true
            }

        })
    }
}