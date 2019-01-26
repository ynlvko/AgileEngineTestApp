package com.agileengine.ynlvko_test.images.image_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agileengine.ynlvko_test.Navigator
import com.agileengine.ynlvko_test.R
import com.agileengine.ynlvko_test.images.Image
import com.agileengine.ynlvko_test.views.gone
import com.agileengine.ynlvko_test.views.visible
import kotlinx.android.synthetic.main.fragment_image_list.*

class ImageListFragment : Fragment() {
    private lateinit var viewModel: ImageListViewModel
    private lateinit var navigator: Navigator
    private lateinit var adapter: ImageListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = activity as Navigator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ImageListViewModel.getInstance(requireActivity())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refresh.isEnabled = false
        initAdapter()
        initRecyclerView()

        viewModel.data().observe(this, Observer { viewObject ->
            refresh.isRefreshing = viewObject.progress
            if (viewObject.progress) {
                return@Observer
            }
            if (viewObject.error || viewObject.data == null) {
                showError()
            } else {
                showData(viewObject.data)
            }
        })
    }

    private fun initRecyclerView() {
        rvImages.adapter = adapter
        rvImages.layoutManager = GridLayoutManager(requireContext(), SpanCount)

        rvImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = rvImages.layoutManager as? GridLayoutManager
                    ?: throw IllegalStateException("RecyclerView's layoutManager is not GridLayoutManager")
                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                if (lastVisiblePosition >= layoutManager.itemCount - NextPageThreshold) {
                    viewModel.fetchNextPage()
                }
            }
        })
    }

    private fun initAdapter() {
        adapter = ImageListAdapter { imagePosition ->
            navigator.showImageDetails(imagePosition)
        }
    }

    private fun showError() {
        rvImages.gone()
        tvEmptyView.visible()
    }

    private fun showData(images: List<Image>) {
        if (images.isEmpty()) {
            showError()
        } else {
            rvImages.visible()
            tvEmptyView.gone()
            adapter.updateImages(images)
        }
    }

    companion object {
        fun newInstance() = ImageListFragment()

        private const val SpanCount = 2
        private const val NextPageThreshold = 2
    }
}
