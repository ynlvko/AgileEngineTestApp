package com.agileengine.ynlvko_test.images.image_list

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.agileengine.ynlvko_test.images.Image

class ImageListAdapter : RecyclerView.Adapter<ImageItemVH>() {
    private var imageList = listOf<Image>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageItemVH {
        return ImageItemVH(TextView(parent.context))
    }

    override fun getItemCount() = imageList.size

    override fun onBindViewHolder(holder: ImageItemVH, position: Int) {

    }

    fun updateImages(images: List<Image>) {
        imageList = images
        notifyDataSetChanged()
    }
}

class ImageItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

}
