package com.fanhl.spreadertablayout.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_recycler_view.*
import kotlinx.android.synthetic.main.item_view.view.*

class RecyclerViewActivity : AppCompatActivity() {
    private val adapter by lazy { RecyclerViewAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        initData()
    }

    private fun initData() {
        recycler_view.adapter = adapter
        adapter.setNewData(List(3) { it })

        spreader_tab_layout.setupWithRecyclerView(recycler_view)
    }
}

class RecyclerViewAdapter : BaseQuickAdapter<Int, BaseViewHolder>(R.layout.item_view) {
    override fun convert(helper: BaseViewHolder?, item: Int?) {
        helper?.itemView?.apply {
            tv_title.text = "RecyclerView\n$item"
        }
    }
}