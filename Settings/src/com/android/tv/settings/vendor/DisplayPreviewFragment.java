/*
 * Copyright (C) 2020 The Android Open Source Project
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

package com.android.tv.settings.vendor;

import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.view.Display;

import androidx.annotation.Keep;
import androidx.annotation.VisibleForTesting;
import androidx.preference.Preference;

import com.android.tv.settings.MainFragment;
import com.android.tv.settings.R;
import com.android.tv.settings.SettingsPreferenceFragment;
import com.android.tv.settings.customization.CustomizationConstants;
import com.android.tv.settings.customization.Partner;
import com.android.tv.settings.customization.PartnerPreferencesMerger;
import com.android.tv.settings.util.ResolutionSelectionUtils;

import java.util.Objects;

/** A vendor sample of display preview settings. */
@Keep
public class DisplayPreviewFragment extends SettingsPreferenceFragment implements
        DisplayManager.DisplayListener {
    private DisplayManager mDisplayManager;
    private Display.Mode mCurrentMode = null;
    private static final String KEY_RESOLUTION_TITLE = "resolution_selection";
    private static final String KEY_MATCH_CONTENT_FRAME_RATE = "match_content_frame_rate";
    private static final String KEY_RESOLUTION_SELECTION = "resolution_selection";
    private static final String KEY_ADVANCED_DISPLAY_SETTINGS = "advanced_display_settings";
    private static final String KEY_ADVANCED_PICTURE_SETTINGS = "advanced_sound_settings";
    private static final String KEY_VENDOR_PICTURE = "picture_vendor_settings";
    private static final String KEY_VENDOR_SOUND = "sound_vendor_settings";

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.preview_display_vendor, null);
        if (Partner.getInstance(getContext()).isCustomizationPackageProvided()) {
            PartnerPreferencesMerger.mergePreferences(
                    getContext(),
                    getPreferenceScreen(),
                    CustomizationConstants.DISPLAY_PREVIEW_SCREEN
            );
        }
        mDisplayManager = getDisplayManager();
        Display display = mDisplayManager.getDisplay(Display.DEFAULT_DISPLAY);
        if (display.getSystemPreferredDisplayMode() != null) {
            mDisplayManager.registerDisplayListener(this, null);
            mCurrentMode = mDisplayManager.getGlobalUserPreferredDisplayMode();
            updateResolutionTitleDescription(ResolutionSelectionUtils.modeToString(
                    mCurrentMode, getContext()));
        } else {
            removeResolutionPreference();
        }
        updatePictureAndSound();
    }

    @VisibleForTesting
    DisplayManager getDisplayManager() {
        return getContext().getSystemService(DisplayManager.class);
    }

    @Override
    public void onDisplayAdded(int displayId) {}

    @Override
    public void onDisplayRemoved(int displayId) {}

    @Override
    public void onDisplayChanged(int displayId) {
        Display.Mode newMode = mDisplayManager.getGlobalUserPreferredDisplayMode();
        if (!Objects.equals(mCurrentMode, newMode)) {
            updateResolutionTitleDescription(
                    ResolutionSelectionUtils.modeToString(newMode, getContext()));
            mCurrentMode = newMode;
        }
    }

    private void updateResolutionTitleDescription(String summary) {
        Preference titlePreference = findPreference(KEY_RESOLUTION_TITLE);
        if (titlePreference != null) {
            titlePreference.setSummary(summary);
        }
    }

    private void removeResolutionPreference() {
        Preference resolutionPreference = findPreference(KEY_RESOLUTION_TITLE);
        if (resolutionPreference != null) {
            getPreferenceScreen().removePreference(resolutionPreference);
        }
    }

    private void updatePictureAndSound() {
        final Preference pictureSettingPreference = findPreference(KEY_VENDOR_PICTURE);
        final Preference soundSettingPreference = findPreference(KEY_VENDOR_SOUND);

        final boolean isVendorPictureIntentHandled =
                isVendorPrefIntentHandled(pictureSettingPreference);
        final boolean isVendorSoundIntentHandled =
                isVendorPrefIntentHandled(soundSettingPreference);

        final boolean hideBuiltinSettings =
                isVendorPictureIntentHandled || isVendorSoundIntentHandled;
        String[] builtinSettingsKeys =
                new String[]{KEY_MATCH_CONTENT_FRAME_RATE, KEY_RESOLUTION_SELECTION,
                        KEY_ADVANCED_DISPLAY_SETTINGS, KEY_ADVANCED_PICTURE_SETTINGS};

        if (hideBuiltinSettings) {
            for (String builtinSettingsKey : builtinSettingsKeys) {
                Preference pref = findPreference(builtinSettingsKey);
                if (pref != null) pref.setVisible(false);
            }

            // Finish full-screen TvSettings activity after navigate to vendor's one-panel activity
            // to keep the background playback playing
            Preference.OnPreferenceClickListener handleIntentAndFinishActivityCallback =
                    preference -> {
                        Intent intent = preference.getIntent();
                        if (intent != null) {
                            getContext().startActivity(intent);
                            getActivity().finish();
                        }
                        return true;
                    };

            if (isVendorPictureIntentHandled) {
                pictureSettingPreference.setVisible(true);
                pictureSettingPreference.setOnPreferenceClickListener(
                        handleIntentAndFinishActivityCallback);
            }
            if (isVendorSoundIntentHandled) {
                soundSettingPreference.setVisible(true);
                soundSettingPreference.setOnPreferenceClickListener(
                        handleIntentAndFinishActivityCallback);
            }
        }
    }

    private boolean isVendorPrefIntentHandled(Preference pref) {
        if (pref != null) {
            Intent intent = pref.getIntent();
            return intent != null
                    && MainFragment.systemIntentIsHandled(getContext(), intent) != null;
        }
        return false;
    }
}
