package com.agileengine.ynlvko_test.images.image_details

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.agileengine.ynlvko_test.R
import com.agileengine.ynlvko_test.core.ServiceLocator
import com.agileengine.ynlvko_test.images.Image
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import kotlinx.android.synthetic.main.fragment_image_details.*

class ImageDetailsFragment : Fragment() {
    private var imagePosition = -1
    private var image: Image? = null
    private var selectedFilter: Transformation? = null
    private lateinit var viewModel: ImageDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imagePosition = arguments?.getInt(ArgImagePosition)
            ?: throw IllegalStateException("Can't load ImageId for ImageDetailsFragment")
        viewModel = ImageDetailsViewModel.getInstance(this, imagePosition)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.data().observe(this, Observer { image ->
            this.image = image
            displayImage()
        })
        fabShare.setOnClickListener(::share)
        fabFilters.setOnClickListener(::showFiltersPopup)
    }

    private fun displayImage() {
        image?.let { image ->
            val picassoRequest = Picasso.get().load(image.fullUrl)
            val selectedFilter = selectedFilter
            if (selectedFilter != null) {
                picassoRequest.transform(selectedFilter)
            }
            picassoRequest.into(ivImage)
        }
    }

    private fun share(v: View) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, viewModel.data().value?.fullUrl)
            type = "text/plain"
        }
        if (requireActivity().packageManager
                .resolveActivity(sendIntent, PackageManager.MATCH_DEFAULT_ONLY) != null
        ) {
            startActivity(sendIntent)
        }
    }

    private fun showFiltersPopup(v: View) {
        val popupMenu = PopupMenu(requireActivity(), fabFilters)
        val imageFilters = ServiceLocator.getInstance(requireContext()).getImageFilters(requireActivity())
        imageFilters.keys.forEach {
            popupMenu.menu.add(it)
        }
        popupMenu.setOnMenuItemClickListener {
            selectedFilter = imageFilters[it.title]
            displayImage()
            true
        }
        popupMenu.show()
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
