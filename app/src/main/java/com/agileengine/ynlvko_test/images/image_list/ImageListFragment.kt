package com.agileengine.ynlvko_test.images.image_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.agileengine.ynlvko_test.R
import com.agileengine.ynlvko_test.gone
import com.agileengine.ynlvko_test.images.ImagesViewModel
import com.agileengine.ynlvko_test.visible
import kotlinx.android.synthetic.main.fragment_image_list.*

class ImageListFragment : Fragment() {
    private lateinit var viewModel: ImagesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ImagesViewModel.getInstance(requireActivity())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        rvImages.adapter = initAdapter()
        rvImages.layoutManager = GridLayoutManager(requireContext(),
            SpanCount
        )

        viewModel.progress().observe(this, Observer { inProgress ->
            refresh.isRefreshing = inProgress
        })
    }

    private fun initAdapter(): ImageListAdapter {
        val adapter = ImageListAdapter()
        viewModel.data().observe(this, Observer {
            adapter.updateImages(it)
            if (it.isEmpty()) {
                rvImages.gone()
                tvEmptyView.visible()
            } else {
                rvImages.visible()
                tvEmptyView.gone()
            }
        })
        return adapter
    }

    companion object {
        fun newInstance() = ImageListFragment()

        private const val SpanCount = 2
    }
}
