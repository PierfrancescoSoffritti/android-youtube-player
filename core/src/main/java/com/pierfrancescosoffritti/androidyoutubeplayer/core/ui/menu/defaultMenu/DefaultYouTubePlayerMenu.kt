package com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.menu.defaultMenu

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow

import com.pierfrancescosoffritti.androidyoutubeplayer.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.menu.MenuItem
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.menu.YouTubePlayerMenu

import java.util.ArrayList

internal class DefaultYouTubePlayerMenu(private val context: Context) : YouTubePlayerMenu {
    private val menuItems = ArrayList<MenuItem>()
    private var popupWindow: PopupWindow? = null

    override val itemCount: Int
        get() = menuItems.size

    override fun show(anchorView: View) {
        popupWindow = createPopupWindow()
        popupWindow?.showAsDropDown(anchorView, -context.resources.getDimensionPixelSize(R.dimen.ayp_8dp) * 12, -context.resources.getDimensionPixelSize(R.dimen.ayp_8dp) * 12)

        if (menuItems.size == 0)
            Log.e(YouTubePlayerMenu::class.java.name, "The menu is empty")
    }

    override fun dismiss() {
        popupWindow?.dismiss()
    }

    override fun addItem(menuItem: MenuItem): YouTubePlayerMenu {
        menuItems.add(menuItem)
        return this
    }

    override fun removeItem(itemIndex: Int): YouTubePlayerMenu {
        menuItems.removeAt(itemIndex)
        return this
    }

    override fun removeItem(menuItem: MenuItem): YouTubePlayerMenu {
        menuItems.remove(menuItem)
        return this
    }

    private fun createPopupWindow(): PopupWindow {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.ayp_player_menu, null)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = MenuAdapter(context, menuItems)
        recyclerView.setHasFixedSize(true)

        val popupWindow = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        popupWindow.contentView = view
        popupWindow.isFocusable = true
        popupWindow.width = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT

        return popupWindow
    }
}
