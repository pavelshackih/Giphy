package io.pavelshackih.giphy.presentation.main

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.request.transition.TransitionFactory
import io.pavelshackih.giphy.GlideRequests
import io.pavelshackih.giphy.R
import io.pavelshackih.giphy.model.data.db.GiphyRow

class ImageViewHolder(view: View, private val glide: GlideRequests) :
    RecyclerView.ViewHolder(view) {

    private val imageView: ImageView = view.findViewById(R.id.image)

    fun bind(model: GiphyRow?) {
        if (model == null) {
            imageView.setImageResource(0)
        } else {
            glide.load(model.url)
                .transition(GenericTransitionOptions.with(CustomTransition()))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        }
    }

    private class CustomTransition : TransitionFactory<Drawable> {

        override fun build(
            dataSource: DataSource,
            isFirstResource: Boolean
        ): Transition<Drawable>? {
            return if (dataSource !== DataSource.REMOTE) {
                null
            } else {
                DrawableCrossFadeFactory.Builder().build().build(dataSource, isFirstResource)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(parent: ViewGroup, glide: GlideRequests): ImageViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder, parent, false)
            return ImageViewHolder(view, glide)
        }
    }
}