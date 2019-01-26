package com.agileengine.ynlvko_test.images.image_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.agileengine.ynlvko_test.R
import com.agileengine.ynlvko_test.images.ImagesViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_image_details.*

class ImageDetailsFragment : Fragment() {
    private var imagePosition = -1
    private lateinit var viewModel: ImagesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imagePosition = arguments?.getInt(ArgImagePosition)
            ?: throw IllegalStateException("Can't load ImageId for ImageDetailsFragment")
        viewModel = ImagesViewModel.getInstance(requireActivity())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.data().observe(this, Observer {
            Picasso.get().load(it[imagePosition].url).into(ivImage)
        })
    }

    companion object {
        private const val ArgImagePosition = "ArgImagePosition"

        fun newInstance(imagePosition: Int): ImageDetailsFragment {
            return ImageDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ArgImagePosition, imagePosition)
                }
            }
        }
    }
}
