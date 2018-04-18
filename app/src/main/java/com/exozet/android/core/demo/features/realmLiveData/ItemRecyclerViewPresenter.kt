package com.exozet.myboilerplateapp

import android.view.ViewGroup
import com.exozet.android.core.demo.R
import kotlinx.android.synthetic.main.recycler_view_item.view.*
import net.kibotu.android.recyclerviewpresenter.BaseViewHolder
import net.kibotu.android.recyclerviewpresenter.Presenter
import net.kibotu.android.recyclerviewpresenter.PresenterAdapter
import net.kibotu.logger.Logger

class ItemRecyclerViewPresenter (presenterAdapter: PresenterAdapter<ItemViewModel>) :  Presenter<ItemRecyclerViewPresenter.ItemViewModel, BaseViewHolder>(presenterAdapter){

    override fun bindViewHolder(viewHolder: BaseViewHolder, item: ItemViewModel, position: Int) {
        Logger.d("bindViewHolder: ${item.label}")
        viewHolder.itemView.itemTextView.text = item.label
    }

    override fun getLayout(): Int = R.layout.recycler_view_item

    override fun createViewHolder(layout: Int, parent: ViewGroup): BaseViewHolder = BaseViewHolder(layout, parent)

    data class ItemViewModel(
            var label: String? = null
    )
}