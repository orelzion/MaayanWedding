package com.orelzion.maayanwedding.data

import com.google.firebase.database.*
import com.orelzion.maayanwedding.MainActivity

/**
 * Created by orelzion on 31/07/2017.
 */
class AttendeeManager private constructor() {

    var attendees: MutableList<Attendee> = mutableListOf()
    lateinit var myRef: DatabaseReference
    private val listeners = mutableListOf<MainActivity.OnDataUpdated>()

    companion object {
        val instance by lazy { AttendeeManager() }
    }

    init {
        initDataBase()
    }

    private fun initDataBase() {
        val database = FirebaseDatabase.getInstance()
        database.setPersistenceEnabled(true)
        myRef = database.getReference("attendees")

        myRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
                val attendee = dataSnapshot.getValue(Attendee::class.java)
                attendee?.let {
                    val position = attendees.indexOf(attendee)
                    if (position > -1) {
                        attendees[position] = attendee
                        listeners.forEach { it.onUpdated() }
                    }
                }
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                val attendee = dataSnapshot.getValue(Attendee::class.java)
                attendee?.let {
                    attendees.add(attendee)
                    listeners.forEach { it.onUpdated() }
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val attendee = dataSnapshot.getValue(Attendee::class.java)
                attendee?.let {
                    if (attendees.remove(attendee)) {
                        listeners.forEach { it.onUpdated() }
                    }
                }
            }
        })
    }

    fun addListener(onDataUpdated: MainActivity.OnDataUpdated) {
        listeners.add(onDataUpdated)
    }

    fun removeListener(onDataUpdated: MainActivity.OnDataUpdated) {
        listeners.remove(onDataUpdated)
    }
}