package io.pavelshackih.giphy.presentation.main

import androidx.recyclerview.widget.DiffUtil
import io.pavelshackih.giphy.model.data.db.GiphyRow

object GiphyRowDiffCallback : DiffUtil.ItemCallback<GiphyRow>() {
    override fun areItemsTheSame(oldItem: GiphyRow, newItem: GiphyRow): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GiphyRow, newItem: GiphyRow): Boolean {
        return oldItem == newItem
    }
}