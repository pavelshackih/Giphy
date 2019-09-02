package io.pavelshackih.giphy.presentation.main

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.pavelshackih.giphy.GlideApp
import io.pavelshackih.giphy.R
import io.pavelshackih.giphy.databinding.ActivityMainBinding
import io.pavelshackih.giphy.model.domain.NetworkState
import io.pavelshackih.giphy.model.domain.Status
import io.pavelshackih.giphy.util.asComponent

class MainActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivityMainBinding

    private val model: MainViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val component = this@MainActivity.asComponent()
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(component.mainInteractor()) as T
            }
        }
    }

    private var networkState: NetworkState? = null
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = setContentView(this, R.layout.activity_main)

        initToolbar()
        initSnackBar()
        initAdapter()
        initSwipeToRefresh()
    }

    private fun initToolbar() {
        setSupportActionBar(dataBinding.toolbar)
    }

    private fun initSnackBar() {
        model.networkState.observe(this, Observer { newNetworkState ->
            if (newNetworkState != null && networkState != newNetworkState) {
                if (newNetworkState.status == Status.FAILED) {
                    val string = getString(R.string.default_error)
                    snackbar =
                        Snackbar.make(dataBinding.coordinator, string, Snackbar.LENGTH_INDEFINITE)
                            .setAction(getString(R.string.retry_button)) { model.onRetry() }
                    snackbar?.show()
                } else {
                    snackbar?.dismiss()
                    snackbar = null
                }
                networkState = newNetworkState
            }
        })
    }

    private fun initAdapter() {
        val glide = GlideApp.with(this)
        val adapter = ImagesAdapter(glide)
        val spanCount = resources.getInteger(R.integer.span_count)
        val gridLayoutManager = GridLayoutManager(this, spanCount)
        dataBinding.recyclerView.layoutManager = gridLayoutManager
        dataBinding.recyclerView.itemAnimator = DefaultItemAnimator()
        dataBinding.recyclerView.adapter = adapter
        model.images.observe(this, Observer { pagedList ->
            adapter.submitList(pagedList)
        })
    }

    private fun initSwipeToRefresh() {
        model.refreshState.observe(this, Observer {
            dataBinding.swipeRefresh.isRefreshing = it == NetworkState.LOADING
        })
        dataBinding.swipeRefresh.setOnRefreshListener {
            model.onSwipeRefresh()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu?.findItem(R.id.app_bar_search)
        val searchView = searchItem?.actionView as? SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Please provide search query.", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    searchView.isIconified = true
                    searchView.clearFocus()
                    model.onSearch(query)
                    searchItem.collapseActionView()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return true
    }
}
