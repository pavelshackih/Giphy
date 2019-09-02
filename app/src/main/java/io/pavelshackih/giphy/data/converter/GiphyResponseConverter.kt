package io.pavelshackih.giphy.data.converter

import android.net.Uri
import io.pavelshackih.giphy.model.data.db.GiphyRow
import io.pavelshackih.giphy.model.data.network.GiphyItem
import io.pavelshackih.giphy.model.data.network.GiphyResponse

class GiphyResponseConverter {

    fun convert(searchQuery: String, source: GiphyResponse): List<GiphyRow> {
        return source.data.mapIndexed { index, item ->
            GiphyRow(
                0,
                item.id,
                index + source.pagination.offset + 1,
                searchQuery,
                parseUrl(item)
            )
        }
    }

    private fun parseUrl(item: GiphyItem): String {
        val image = item.images[FIXED_WIDTH]
        val url = image?.url ?: throw IllegalStateException("Can't get url from $item")
        val parcedUrl: Uri = Uri.parse(url)
        val param = parcedUrl.getQueryParameter("rid")
        return "https://i.giphy.com/media/${item.id}/$param"
    }

    companion object {
        private const val FIXED_WIDTH = "fixed_width"
    }
}