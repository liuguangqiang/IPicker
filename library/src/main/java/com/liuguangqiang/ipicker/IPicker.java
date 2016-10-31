/*
 * Copyright 2016 Eric Liu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liuguangqiang.ipicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.liuguangqiang.ipicker.events.IPickerEvent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Eric on 16/9/18.
 */
public class IPicker {

    /**
     * Limit the count of picture selected.
     */
    private static int limit = 1;

    private static boolean cropEnable = false;

    private IPicker() {
    }

    /**
     * Limit the count of picture selected.
     *
     * @param limit
     */
    public static void setLimit(int limit) {
        IPicker.limit = limit;
    }

    public static void setCropEnable(boolean cropEnable) {
        IPicker.cropEnable = cropEnable;
    }

    /**
     * Finish the IPicker and post a event to observer.
     *
     * @param path
     */
    public static void finish(String path) {
        List<String> paths = new ArrayList<>();
        paths.add(path);
        finish(paths);
    }

    /**
     * Finish the IPicker and post a event to observer.
     *
     * @param paths
     */
    public static void finish(List<String> paths) {
        EventBus.getDefault().post(new IPickerEvent(paths));
    }

    /**
     * Open the IPicker to select photos or take pictures.
     *
     * @param context
     */
    public static void open(Context context) {
        open(context, null);
    }

    /**
     * Open the IPicker to select photos or take pictures.
     *
     * @param context
     * @param selected
     */
    public static void open(Context context, ArrayList<String> selected) {
        Intent intent = new Intent(context, IPickerActivity.class);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        Bundle bundle = new Bundle();
        bundle.putInt(IPickerActivity.ARG_LIMIT, limit);
        bundle.putBoolean(IPickerActivity.ARG_CROP_ENABLE, cropEnable);
        if (selected != null && !selected.isEmpty()) {
            bundle.putStringArrayList(IPickerActivity.ARG_SELECTED, selected);

        }
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
