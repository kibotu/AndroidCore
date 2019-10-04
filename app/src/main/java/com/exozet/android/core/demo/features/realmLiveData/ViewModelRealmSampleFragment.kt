package com.exozet.android.core.demo.features.realmLiveData

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.exozet.android.core.base.BaseFragment
import com.exozet.android.core.demo.R
import com.exozet.android.core.extensions.onClick
import com.exozet.android.core.uid.PrimaryKeyGenerator
import kotlinx.android.synthetic.main.fragment_viewmodel_sample.*
import net.kibotu.android.recyclerviewpresenter.PresenterAdapter
import net.kibotu.android.recyclerviewpresenter.PresenterModel


class ViewModelRealmSampleFragment : BaseFragment() {

    override val layout: Int = R.layout.fragment_viewmodel_sample

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PresenterAdapter()
        adapter.registerPresenter(StringPresenter())
        list.adapter = adapter

        val vm = ViewModelProvider(this).get(RepositoryViewModel::class.java)

        vm.strings.observe(this, Observer {

            val primaryKeyGenerator = PrimaryKeyGenerator()

            adapter.submitList(it.map { PresenterModel(it, R.layout.recycler_view_item, uuid = primaryKeyGenerator.newUID().toString()) })

            list.smoothScrollToPosition(it.lastIndex.coerceAtLeast(0))
        })

        add.onClick { vm.addItem() }

        remove.onClick { vm.deleteAll() }
    }
}