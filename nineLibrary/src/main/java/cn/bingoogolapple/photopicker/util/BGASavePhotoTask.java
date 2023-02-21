/**
 * Copyright 2016 bingoogolapple
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.bingoogolapple.photopicker.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateUtils;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bingoogolapple.photopicker.R;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/25 下午6:49
 * 描述:
 */
public class BGASavePhotoTask extends BGAAsyncTask<Void, Void> {
    private Context mContext;
    private SoftReference<Bitmap> mBitmap;
    private File mNewFile;

    public BGASavePhotoTask(Callback<Void> callback, Context context, File newFile) {
        super(callback);
        mContext = context.getApplicationContext();
        mNewFile = newFile;
    }

    public void setBitmapAndPerform(Bitmap bitmap) {
        mBitmap = new SoftReference<>(bitmap);

        if (Build.VERSION.SDK_INT >= 11) {
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            execute();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        FileOutputStream fos = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                saveImageToGallery2(mBitmap.get(),mContext);
            }else {
                fos = new FileOutputStream(mNewFile);
                mBitmap.get().compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
            }
            // 通知图库更新
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mNewFile)));

            BGAPhotoPickerUtil.showSafe(mContext.getString(R.string.bga_pp_save_img_success_folder, mNewFile.getParentFile().getAbsolutePath()));
        } catch (Exception e) {
            BGAPhotoPickerUtil.showSafe(R.string.bga_pp_save_img_failure);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    BGAPhotoPickerUtil.showSafe(R.string.bga_pp_save_img_failure);
                }
            }
            recycleBitmap();
        }
        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        recycleBitmap();
    }

    private void recycleBitmap() {
        if (mBitmap != null && mBitmap.get() != null && !mBitmap.get().isRecycled()) {
            mBitmap.get().recycle();
            mBitmap = null;
        }
    }

    /**
     * android 11及以上保存图片到相册
     * @param context
     * @param image
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public static boolean saveImageToGallery2(Bitmap image, Context context){
        boolean isSuccess;

        Long mImageTime = System.currentTimeMillis();
        String imageDate = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date(mImageTime));
        String SCREENSHOT_FILE_NAME_TEMPLATE = "GGBScreen_%s.png";//图片名称，以"GGBScreen"+时间戳命名
        String mImageFileName = String.format(SCREENSHOT_FILE_NAME_TEMPLATE, imageDate);

        final ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES
                + File.separator + "GGBScreen"); //Environment.DIRECTORY_SCREENSHOTS:截图,图库中显示的文件夹名。"dh"
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, mImageFileName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
        values.put(MediaStore.MediaColumns.DATE_ADDED, mImageTime / 1000);
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, mImageTime / 1000);
        values.put(MediaStore.MediaColumns.DATE_EXPIRES, (mImageTime + DateUtils.DAY_IN_MILLIS) / 1000);
        values.put(MediaStore.MediaColumns.IS_PENDING, 1);

        ContentResolver resolver = context.getContentResolver();
        final Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        try {
            // First, write the actual data for our screenshot
            try (OutputStream out = resolver.openOutputStream(uri)) {
                if (!image.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                    throw new IOException("Failed to compress");
                }
            }
            // Everything went well above, publish it!
            values.clear();
            values.put(MediaStore.MediaColumns.IS_PENDING, 0);
            values.putNull(MediaStore.MediaColumns.DATE_EXPIRES);
            resolver.update(uri, values, null, null);

            isSuccess = true;
        }catch (IOException e){
            resolver.delete(uri, null);

            isSuccess = false;
        }
        return isSuccess;

    }

}
