package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.examples.recyclerViewExample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.aytplayersample.R

/** Videos to play inside the list */
private val videoIds = listOf(
  "6JYIGclVQdw",
  "LvetJ9U_tVY",
  "6JYIGclVQdw",
  "LvetJ9U_tVY",
  "6JYIGclVQdw",
  "LvetJ9U_tVY",
  "6JYIGclVQdw",
  "LvetJ9U_tVY",
  "6JYIGclVQdw",
  "LvetJ9U_tVY",
  "6JYIGclVQdw",
  "LvetJ9U_tVY"
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