package com.agileengine.ynlvko_test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.agileengine.ynlvko_test.images.image_list.ImageListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            setupImageListFragment()
        }
    }

    private fun setupImageListFragment() {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, ImageListFragment.newInstance())
            .commit()
    }
}
