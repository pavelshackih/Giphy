package io.pavelshackih.giphy.presentation.main

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.pavelshackih.giphy.GlideRequests
import io.pavelshackih.giphy.model.data.db.GiphyRow

class ImagesAdapter(private val glide: GlideRequests) :
    PagedListAdapter<GiphyRow, RecyclerView.ViewHolder>(GiphyRowDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageViewHolder.newInstance(parent, glide)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImageViewHolder) {
            holder.bind(getItem(position))
        }
    }
}