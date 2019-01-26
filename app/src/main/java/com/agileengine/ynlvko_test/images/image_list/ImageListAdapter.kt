package com.agileengine.ynlvko_test.images.image_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.agileengine.ynlvko_test.R
import com.agileengine.ynlvko_test.images.Image
import com.squareup.picasso.Picasso

class ImageListAdapter(
    private val imageClickListener: ImageClickListener
) : RecyclerView.Adapter<ImageItemVH>() {
    private var imageList = listOf<Image>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageItemVH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_image, parent, false)
        return ImageItemVH(itemView)
    }

    override fun getItemCount() = imageList.size

    override fun onBindViewHolder(holder: ImageItemVH, position: Int) {
        holder.bind(imageList[position])
        holder.itemView.setOnClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                imageClickListener.invoke(holder.adapterPosition)
            }
        }
    }

    fun updateImages(images: List<Image>) {
        imageList = images
        notifyDataSetChanged()
    }
}

typealias ImageClickListener = (Int) -> Unit

class ImageItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView = itemView.findViewById<ImageView>(R.id.ivImage)

    fun bind(image: Image) {
        Picasso.get().load(image.croppedUrl)
            .fit().centerCrop()
            .into(imageView)
    }
}
