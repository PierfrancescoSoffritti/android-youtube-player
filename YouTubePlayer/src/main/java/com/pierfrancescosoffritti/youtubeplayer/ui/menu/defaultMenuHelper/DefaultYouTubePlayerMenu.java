package com.pierfrancescosoffritti.youtubeplayer.ui.menu.defaultMenuHelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.pierfrancescosoffritti.youtubeplayer.R;
import com.pierfrancescosoffritti.youtubeplayer.ui.PlayerUIController;
import com.pierfrancescosoffritti.youtubeplayer.ui.menu.YouTubePlayerMenu;
import com.pierfrancescosoffritti.youtubeplayer.ui.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class DefaultYouTubePlayerMenu implements YouTubePlayerMenu {

    private final Context context;
    private final List<MenuItem> menuItems;

    public DefaultYouTubePlayerMenu(@NonNull Context context) {
        this.context = context;

        this.menuItems = new ArrayList<>();
    }

    @Override
    public void showMenu(View anchorView) {
        PopupWindow popupWindow = createPopupWindow();
        popupWindow.showAsDropDown(anchorView, 0, - context.getResources().getDimensionPixelSize(R.dimen._8dp) * 4);

        if(menuItems.size() == 0)
            Log.e(PlayerUIController.class.getName(), "The menu is empty");
    }

    @Override
    public void addItem(MenuItem menuItem) {
        menuItems.add(menuItem);
    }

    @Override
    public void removeItem(int itemIndex) {
        menuItems.remove(itemIndex);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    private PopupWindow createPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(inflater == null)
            throw new RuntimeException("can't access LAYOUT_INFLATER_SERVICE");

        View view = inflater.inflate(R.layout.player_menu, null);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        setUpRecyclerView(recyclerView);

        PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);

        return popupWindow;
    }

    private void setUpRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        MenuAdapter adapter = new MenuAdapter(context, menuItems);
        recyclerView.setAdapter(adapter);
    }
}
