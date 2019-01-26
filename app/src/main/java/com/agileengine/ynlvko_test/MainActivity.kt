package com.agileengine.ynlvko_test

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.agileengine.ynlvko_test.images.image_details.ImageDetailsFragment
import com.agileengine.ynlvko_test.images.image_list.ImageListFragment

class MainActivity : AppCompatActivity(), Navigator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            setupImageListFragment()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            supportFragmentManager.popBackStack()
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showImageDetails(imagePosition: Int) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .addToBackStack(null)
            .add(R.id.fragmentContainer, ImageDetailsFragment.newInstance(imagePosition))
            .commit()
    }

    private fun setupImageListFragment() {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, ImageListFragment.newInstance())
            .commit()
    }
}
