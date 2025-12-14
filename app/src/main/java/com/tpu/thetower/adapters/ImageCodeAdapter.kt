package com.tpu.thetower.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tpu.thetower.R

class ImageCodeAdapter(private val items: Array<Int>, private val layoutImage: Int) : RecyclerView.Adapter<ImageCodeAdapter.ImageCodeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageCodeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutImage, parent, false) as ImageView
        return ImageCodeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageCodeViewHolder, position: Int) {
        val loopPos = position % items.size
        holder.bind(items[loopPos])
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    class ImageCodeViewHolder(private val imageView: ImageView) : RecyclerView.ViewHolder(imageView) {
        fun bind(value: Int) {
            imageView.setImageResource(value)
        }
    }
}
