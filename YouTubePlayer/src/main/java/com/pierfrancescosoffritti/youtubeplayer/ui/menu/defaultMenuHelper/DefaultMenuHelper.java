package com.pierfrancescosoffritti.youtubeplayer.ui.menu.defaultMenuHelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.pierfrancescosoffritti.youtubeplayer.R;
import com.pierfrancescosoffritti.youtubeplayer.ui.menu.MenuHelper;
import com.pierfrancescosoffritti.youtubeplayer.ui.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class DefaultMenuHelper implements MenuHelper {

    private final Context context;
    private final List<MenuItem> menuItems;

    @Nullable private PopupWindow popupWindow;

    public DefaultMenuHelper(@NonNull Context context, List<MenuItem> defaultMenuItems) {
        this.context = context;

        menuItems = new ArrayList<>(defaultMenuItems);
    }

    @Override
    public void showMenu(View anchorView) {
        PopupWindow window = popupWindow == null ? createPopupWindow() : popupWindow;
        window.showAsDropDown(anchorView, - context.getResources().getDimensionPixelSize(R.dimen._8dp) * 0, - context.getResources().getDimensionPixelSize(R.dimen._8dp) * 4);
    }

    @Override
    public void addItem(MenuItem menuItem) {
        menuItems.add(menuItem);
    }

    @Override
    public void removeItem(int itemIndex) {
        menuItems.remove(itemIndex);
    }

    private PopupWindow createPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(inflater == null)
            throw new RuntimeException("can't access LAYOUT_INFLATER_SERVICE");

        View view = inflater.inflate(R.layout.player_menu, null);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        setUpRecyclerView(recyclerView);

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
