package com.fanhl.spreadertablayout.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TabLayout::class

        tab_layout.tabCount
        tab_layout.selectedTabPosition

        btn_page1.setOnClickListener { spreader_tab_layout.selectedPosition = 0 }
        btn_page2.setOnClickListener { spreader_tab_layout.selectedPosition = 1 }
    }
}
