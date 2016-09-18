package com.liuguangqiang.ipicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 16/9/18.
 */
public class IPicker {

    private static OnSelectListener listener;

    /**
     * Limit the count of picture selected.
     */
    private static int limit = 1;

    private IPicker() {
    }

    /**
     * Limit the count of picture selected.
     *
     * @param size
     */
    public static void setLimit(int size) {
        limit = size;
    }

    public static void setListener(OnSelectListener onSelectListener) {
        listener = onSelectListener;
    }

    public static void finish(String path) {
        List<String> paths = new ArrayList<>();
        paths.add(path);
        finish(paths);
    }

    public static void finish(List<String> paths) {
        if (listener != null) {
            listener.onSelected(paths);
        }
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
        if (selected != null && !selected.isEmpty()) {
            bundle.putStringArrayList(IPickerActivity.ARG_SELECTED, selected);

        }
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public interface OnSelectListener {

        void onSelected(List<String> paths);

    }

}
