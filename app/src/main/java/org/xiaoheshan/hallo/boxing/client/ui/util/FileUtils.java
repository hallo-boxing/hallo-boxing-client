package org.xiaoheshan.hallo.boxing.client.ui.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * 简介
 *
 * @author : _Chf
 * @since : 11-05-2018
 */
public abstract class FileUtils {

    public static File fromUri(Uri uri, ContentResolver contentResolver) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = contentResolver.query(uri, proj, null, null, null);
        if (cursor == null) {
            return new File(uri.getPath());
        }
        if(cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return new File(res);
    }
}
