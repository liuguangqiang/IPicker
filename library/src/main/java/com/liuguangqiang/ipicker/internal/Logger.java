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

package com.liuguangqiang.ipicker.internal;

import android.util.Log;

/**
 * Created by Eric on 16/9/12.
 */

public class Logger {

    private static final String TAG = "IPicker";

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

}
