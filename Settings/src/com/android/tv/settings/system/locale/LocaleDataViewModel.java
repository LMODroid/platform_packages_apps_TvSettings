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

package com.android.tv.settings.system.locale;

import android.app.ActivityManager;
import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.internal.app.LocaleStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/** ViewModel to provide data for locale selection. */
public class LocaleDataViewModel extends ViewModel {
    private static final String TAG = "LocaleDataViewModel";
    private MutableLiveData<Locale> mCurrentLocale;
    private final Map<LocaleStore.LocaleInfo, ArrayList<LocaleStore.LocaleInfo>> mLocaleMap =
            new HashMap<>();

    public MutableLiveData<Locale> getCurrentLocale() {
        if (mCurrentLocale == null) {
            try {
                mCurrentLocale = new MutableLiveData<>(
                        ActivityManager.getService().getConfiguration()
                                .getLocales().get(0));
            } catch (RemoteException e) {
                Log.e(TAG, "Could not retrieve locale", e);
            }
        }
        return mCurrentLocale;
    }

    public void addLocaleInfoList(LocaleStore.LocaleInfo localeInfo, Context context,
            Set<String> langTagsToIgnore) {
        ArrayList<LocaleStore.LocaleInfo> localeInfoWithCountryList = new ArrayList<>(
                LocaleStore.getLevelLocales(context, langTagsToIgnore, localeInfo, false));
        mLocaleMap.put(localeInfo, localeInfoWithCountryList);
    }

    public ArrayList<LocaleStore.LocaleInfo> getLocaleInfoList(LocaleStore.LocaleInfo localeInfo) {
        return mLocaleMap.get(localeInfo);
    }
}
