package com.exozet.android.core.demo.features.realmLiveData

import androidx.recyclerview.widget.RecyclerView
import com.exozet.android.core.demo.R
import kotlinx.android.synthetic.main.recycler_view_item.view.*
import net.kibotu.android.recyclerviewpresenter.Adapter
import net.kibotu.android.recyclerviewpresenter.Presenter
import net.kibotu.android.recyclerviewpresenter.PresenterModel
import net.kibotu.logger.Logger.logv

class StringPresenter : Presenter<StringModel>() {

    override val layout: Int = R.layout.recycler_view_item

    override fun bindViewHolder(viewHolder: RecyclerView.ViewHolder, item: PresenterModel<StringModel>, position: Int, payloads: MutableList<Any>?, adapter: Adapter) {
        logv { "bindViewHolder: ${item.model}" }

        with(viewHolder.itemView) {
            itemTextView.text = item.model.value
        }
    }
}