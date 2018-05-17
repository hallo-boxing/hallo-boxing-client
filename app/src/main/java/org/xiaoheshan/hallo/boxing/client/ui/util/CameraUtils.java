package org.xiaoheshan.hallo.boxing.client.ui.util;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 简介
 *
 * @author : _Chf
 * @since : 08-05-2018
 */
public abstract class CameraUtils {

    private static final String PATH = Environment.getExternalStorageDirectory() +
            File.separator + Environment.DIRECTORY_DCIM + File.separator;

    static {
        File file = new File(PATH);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static File generateImageFile() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return new File(PATH + "IMG_" + dateFormat.format(new Date()) + ".jpg");
    }

}
