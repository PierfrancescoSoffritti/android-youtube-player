package com.pierfrancescosoffritti.androidyoutubeplayer.ui.menu;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

public class MenuItem {

    private final String text;
    @DrawableRes private final int icon;
    private final View.OnClickListener onClickListener;

    public MenuItem(@NonNull String text, @DrawableRes final int icon, @Nullable View.OnClickListener onClickListener) {
        this.text = text;
        this.icon = icon;
        this.onClickListener = onClickListener;
    }

    @NonNull
    public String getText() {
        return text;
    }

    public @DrawableRes int getIcon() {
        return icon;
    }

    @Nullable
    public View.OnClickListener getOnClickListener() {
        return onClickListener;
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