package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.recyclerViewExample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.utils.VideoIdsProvider
import com.pierfrancescosoffritti.aytplayersample.R

/** Videos to play inside the list */
private val videoIds = listOf(
  VideoIdsProvider.getNextVideoId(),
  VideoIdsProvider.getNextVideoId(),
  VideoIdsProvider.getNextVideoId(),
  VideoIdsProvider.getNextVideoId(),
  VideoIdsProvider.getNextVideoId(),
  VideoIdsProvider.getNextVideoId(),
  VideoIdsProvider.getNextVideoId(),
  VideoIdsProvider.getNextVideoId(),
  VideoIdsProvider.getNextVideoId(),
  VideoIdsProvider.getNextVideoId(),
  VideoIdsProvider.getNextVideoId(),
  VideoIdsProvider.getNextVideoId(),
)

class RecyclerViewActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_recycler_view_example)

    val recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
      setHasFixedSize(true)
      layoutManager = LinearLayoutManager(this@RecyclerViewActivity)
    }

    recyclerView.adapter = RecyclerViewAdapter(videoIds, lifecycle)
  }
}