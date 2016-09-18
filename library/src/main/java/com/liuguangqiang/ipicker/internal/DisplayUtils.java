/*
 *  Copyright 2015 GoIn Inc. All rights reserved.
 */

package com.liuguangqiang.ipicker.internal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;

public class DisplayUtils {

    private static final String SIZE = "DISPLAY_SIZE";
    private static final String SIZE_WIDTH = "DISPLAY_SIZE_WIDTH";
    private static final String SIZE_HEIGHT = "DISPLAY_SIZE_HEIGHT";

    public static int px2dip(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int dip2px(Context context, float dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public static int getWidth() {
        return screenWidth;
    }

    public static int getHeight() {
        return screenHeight;
    }

    public static void initDiplaySize(Activity context) {
        if (screenWidth > 0 && screenHeight > 0) return;

        Display dis = context.getWindowManager().getDefaultDisplay();
        Point outSize = new Point(0, 0);
        dis.getSize(outSize);
        if (outSize != null) {
            screenWidth = outSize.x;
            screenHeight = outSize.y;
        }
    }

}
