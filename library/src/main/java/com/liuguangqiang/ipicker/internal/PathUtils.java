/*
 *  Copyright 2015 GoIn Inc. All rights reserved.
 */

package com.liuguangqiang.ipicker.internal;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Eric on 15/8/13.
 */
public class PathUtils {

  private static File checkAndMkdirs(File file) {
    if (!file.exists()) {
      file.mkdirs();
    }
    return file;
  }

  private static boolean isExternalStorageWritable() {
    String state = Environment.getExternalStorageState();
    return Environment.MEDIA_MOUNTED.equals(state);
  }

  /**
   * 有 sdcard 的时候，小米是 /storage/sdcard0/Android/data/com.avoscloud.chat/cache/ 无 sdcard 的时候，小米是
   * /data/data/com.avoscloud.chat/cache 依赖于包名。所以不同应用使用该库也没问题，要有点理想。
   */
  private static File getAvailableCacheDir(Context context) {
    if (isExternalStorageWritable()) {
      return context.getExternalCacheDir();
    } else {
      // 只有此应用才能访问。拍照的时候有问题，因为拍照的应用写入不了该文件
      return context.getCacheDir();
    }
  }

  /**
   * 可能文件会被清除掉，需要检查是否存在
   */
  public static String getChatFilePath(Context context, String id) {
    String path = new File(getAvailableCacheDir(context), id).getAbsolutePath();
    return path;
  }

  /**
   * 录音保存的地址
   */
  public static String getRecordPathByCurrentTime(Context context) {
    return new File(getAvailableCacheDir(context), "record_" + System.currentTimeMillis()).getAbsolutePath();
  }

  /**
   * 拍照保存的地址
   */
  public static String getPicturePathByCurrentTime(Context context) {
    String path = new File(getAvailableCacheDir(context), "picture_" + System.currentTimeMillis()).getAbsolutePath();
    return path;
  }

}
