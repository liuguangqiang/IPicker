package com.liuguangqiang.ipicker.internal;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.liuguangqiang.ipicker.entities.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * A helper for querying all images from sd card.
 * <p>
 * Created by Eric on 16/9/12.
 */
public class ImageMedia {

    /**
     * Return a collection of image path.
     *
     * @param context
     * @return
     */
    public static List<Photo> queryAll(Context context) {
        List<Photo> photos = new ArrayList<>();

        // which image properties are we querying
        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DATA,
        };

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cur = MediaStore.Images.Media.query(
                context.getContentResolver(),
                images,                                             // Uri
                projection,                                         // Which columns to return
                null,                                               // Which rows to return (all rows)
                null,                                               // Selection arguments (none)
                MediaStore.Images.Media.DATE_TAKEN + " DESC"        // Ordering
        );

        if (cur.moveToFirst()) {
            int dataColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATA);
            do {
                // Get the field values
                photos.add(new Photo(cur.getString(dataColumn)));
            } while (cur.moveToNext());
        }

        return photos;
    }

}
