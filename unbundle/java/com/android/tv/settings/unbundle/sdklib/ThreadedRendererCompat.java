/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.view.ThreadedRenderer;

public class ThreadedRendererCompat {
    public static final String DEBUG_SHOW_NON_RECTANGULAR_CLIP_PROPERTY =
            ThreadedRenderer.DEBUG_SHOW_NON_RECTANGULAR_CLIP_PROPERTY;
    public static final String DEBUG_OVERDRAW_PROPERTY = ThreadedRenderer.DEBUG_OVERDRAW_PROPERTY;

    public static final String DEBUG_SHOW_LAYERS_UPDATES_PROPERTY =
            ThreadedRenderer.DEBUG_SHOW_LAYERS_UPDATES_PROPERTY;

    public static final String DEBUG_DIRTY_REGIONS_PROPERTY =
            ThreadedRenderer.DEBUG_DIRTY_REGIONS_PROPERTY;

    public static final String PROFILE_PROPERTY = ThreadedRenderer.PROFILE_PROPERTY;
}
