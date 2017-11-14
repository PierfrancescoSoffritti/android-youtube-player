package com.pierfrancescosoffritti.youtubeplayer.ui;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pierfrancescosoffritti.youtubeplayer.R;

import java.util.Arrays;

class MenuHelper {

    private Context context;

    MenuHelper(Context context) {
        this.context = context;
    }

    void showMenu(View anchorView) {
        PopupWindow window = createPopupDisplay();
        window.showAsDropDown(anchorView, - context.getResources().getDimensionPixelSize(R.dimen._8dp) * 0, - context.getResources().getDimensionPixelSize(R.dimen._8dp) * 4);
    }

    private PopupWindow createPopupDisplay() {
//        final PopupWindow popupWindow = new PopupWindow(context);


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(inflater == null)
            throw new RuntimeException("can't access LAYOUT_INFLATER_SERVICE");

        View view = inflater.inflate(R.layout.player_menu, null);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        setUpRecyclerView(recyclerView);

        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);

        return popupWindow;
    }

    private void setUpRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        MenuItem[] dataset = new MenuItem[1];
        dataset[0] = new MenuItem("Video quality", R.drawable.ic_settings_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        MyAdapter adapter = new MyAdapter(dataset);
        recyclerView.setAdapter(adapter);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private MenuItem[] menuItems;

        class ViewHolder extends RecyclerView.ViewHolder {
            final View root;
            final TextView textView;

            ViewHolder(View menuItemView) {
                super(menuItemView);
                root = menuItemView;
                textView = menuItemView.findViewById(R.id.text);
            }
        }

        MyAdapter(MenuItem[] menuItems) {
            this.menuItems = menuItems;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.root.setOnClickListener(menuItems[position].onClickListener);
            holder.textView.setText(menuItems[position].text);
            holder.textView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, menuItems[position].icon), null, null, null);
        }

        @Override
        public int getItemCount() {
            return menuItems.length;
        }
    }

    class MenuItem {
        final String text;
        @DrawableRes final int icon;
        final View.OnClickListener onClickListener;

        MenuItem(String text, @DrawableRes final int icon, View.OnClickListener onClickListener) {
            this.text = text;
            this.icon = icon;
            this.onClickListener = onClickListener;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MenuItem menuItem = (MenuItem) o;

            return icon == menuItem.icon && text.equals(menuItem.text);
        }

        @Override
        public int hashCode() {
            int result = text.hashCode();
            result = 31 * result + icon;
            return result;
        }
    }
}
