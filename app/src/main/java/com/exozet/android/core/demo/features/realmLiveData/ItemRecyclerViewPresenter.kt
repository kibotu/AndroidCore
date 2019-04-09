package com.exozet.android.core.demo.features.realmLiveData

import androidx.recyclerview.widget.RecyclerView
import com.exozet.android.core.demo.R
import kotlinx.android.synthetic.main.recycler_view_item.view.*
import net.kibotu.android.recyclerviewpresenter.Presenter
import net.kibotu.android.recyclerviewpresenter.RecyclerViewModel
import net.kibotu.logger.Logger.logv

class ItemRecyclerViewPresenter : Presenter<RecyclerViewModel<String>>() {

    override val layout: Int = R.layout.recycler_view_item

    override fun bindViewHolder(viewHolder: RecyclerView.ViewHolder, item: RecyclerViewModel<String>, position: Int) {
        logv("bindViewHolder: ${item.model}")
        viewHolder.itemView.itemTextView.text = item.model
    }
}