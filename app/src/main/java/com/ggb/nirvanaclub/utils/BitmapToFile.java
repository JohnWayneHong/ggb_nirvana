package com.ggb.nirvanaclub.utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BitmapToFile {

    //保存方法
    public static void saveFile(Bitmap bm, String fileName) throws IOException {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            @SuppressLint("SdCardPath") String SAVE_REAL_PATH = "/sdcard/Android/data/"+"com.ggb.nirvanaclub"+"/picture/";

            File foder = new File(SAVE_REAL_PATH);
            if (!foder.exists()) foder.mkdirs();
            File myCaptureFile = new File(SAVE_REAL_PATH, fileName);
            if (!myCaptureFile.exists()) myCaptureFile.createNewFile();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            Log.e("TAG","图片保存文件完毕。。。。");
//        ToastUtil.showSuccess(getApplicationContext(), "已保存在/good/savePic目录下", Toast.LENGTH_SHORT);
            //发送广播通知系统
//            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            Uri uri = Uri.fromFile(myCaptureFile);
//            intent.setData(uri);
        }
    }
}
