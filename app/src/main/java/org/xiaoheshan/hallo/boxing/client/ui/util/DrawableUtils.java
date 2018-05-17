package org.xiaoheshan.hallo.boxing.client.ui.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * 简介
 *
 * @author : _Chf
 * @since : 06-05-2018
 */
public abstract class DrawableUtils {

    public static Drawable getDrawable(Context context, int resourceId) {
        Drawable drawable = context.getDrawable(resourceId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

}
