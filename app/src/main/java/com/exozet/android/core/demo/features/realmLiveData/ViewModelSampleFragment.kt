package com.exozet.android.core.demo.features.realmLiveData

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.exozet.android.core.base.BaseFragment
import com.exozet.android.core.demo.R
import kotlinx.android.synthetic.main.fragment_viewmodel_sample.*
import net.kibotu.android.recyclerviewpresenter.PresenterAdapter


class ViewModelSampleFragment : BaseFragment() {
    companion object {
        val TAG = ViewModelSampleFragment::class.java.simpleName
    }

    override val layout: Int = R.layout.fragment_viewmodel_sample

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val presenterAdapter = PresenterAdapter<ItemRecyclerViewPresenter.ItemViewModel>()
        itemRecyclerView.adapter = presenterAdapter

        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.getItems().observe(this, Observer {
            it?.let {
                presenterAdapter.clear()
                for (item in it) {
                    presenterAdapter.add(ItemRecyclerViewPresenter.ItemViewModel(item.name), ItemRecyclerViewPresenter::class.java)
                }
            }
        })

        // addButton.setOnClickListener { v -> viewModel.addItem() }
    }
}