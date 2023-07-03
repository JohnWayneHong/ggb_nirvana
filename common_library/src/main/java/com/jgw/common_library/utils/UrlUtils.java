package com.jgw.common_library.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.jgw.common_library.provider.CameraFileProvider;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pql on 2017/12/12.
 */

public class UrlUtils {

    public static Uri getProviderUri(Context context, File file) {
        if (file == null) {
            return null;
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = CameraFileProvider.getUriForFile(context, CameraFileProvider.getProviderName(context), file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public static List<Uri> getProviderList(Context context, List<String> paths) {
        if (paths == null || paths.isEmpty()) return null;
        ArrayList<Uri> shareImageUriList = new ArrayList<>();
        int size = paths.size();

        for (int i = 0; i < size; i++) {
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = getImageContentUri(context, paths.get(i));
            } else {
                uri = Uri.fromFile(new File(paths.get(i)));
            }
            if (uri != null) {
                shareImageUriList.add(uri);
            }
        }
        return shareImageUriList;
    }

    public static Uri getImageContentUri(Context context, String filePath) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                    new String[]{filePath}, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                    Uri baseUri = Uri.parse("content://media/external/images/media");
                    uri = Uri.withAppendedPath(baseUri, "" + id);
                }
                cursor.close();
            }
            if (uri == null) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            }
        } else {
            uri = Uri.fromFile(new File(filePath));
        }
        return uri;
    }
}
