package com.exozet.android.core.demo.features.realmLiveData

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.exozet.android.core.base.BaseFragment
import com.exozet.android.core.demo.R
import kotlinx.android.synthetic.main.fragment_viewmodel_sample.*
import net.kibotu.android.recyclerviewpresenter.PresenterAdapter
import net.kibotu.android.recyclerviewpresenter.RecyclerViewModel


class ViewModelSampleFragment : BaseFragment() {

    override val layout: Int = R.layout.fragment_viewmodel_sample

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PresenterAdapter<RecyclerViewModel<String>>()
        itemRecyclerView.adapter = adapter

        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.getItems().observe(this, Observer {
            adapter.clear()
            for (item in it) {
                adapter.append(RecyclerViewModel(item.name ?: ""), ItemRecyclerViewPresenter::class.java)
            }
        })

        // addButton.setOnClickListener { v -> viewModel.addItem() }
    }
}