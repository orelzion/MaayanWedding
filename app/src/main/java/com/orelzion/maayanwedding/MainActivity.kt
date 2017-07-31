package com.orelzion.maayanwedding

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dision.android.rtlviewpager.RTLPagerAdapter
import com.dision.android.rtlviewpager.Tab
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val resources = resources
        val configuration = resources.configuration
        val displayMetrics = resources.displayMetrics
        configuration.setLocale(Locale("iw", "IL"))
        applicationContext.createConfigurationContext(configuration)

        setContentView(R.layout.activity_main)

        val tabs = arrayOf(
                object : Tab(0, getString(R.string.attendees)) {

                    override fun getFragment(): Fragment {
                        return ListFragment()
                    }
                },
                object : Tab(1, getString(R.string.tables)) {

                    override fun getFragment(): Fragment {
                        return TableFragment()
                    }
                }
        )

        pager.adapter = RTLPagerAdapter(fragmentManager, tabs, true)
        pager.setRtlOriented(true)
        tabLayout.setupWithViewPager(pager)
    }

    interface OnDataUpdated {
        fun onUpdated()
    }
}
