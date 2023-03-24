/*
 * Copyright (C) 2023 The Android Open Source Project
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

package com.android.tv.settings.device.eco;

import android.annotation.ArrayRes;
import android.annotation.BoolRes;
import android.annotation.DrawableRes;
import android.annotation.IntegerRes;
import android.annotation.NonNull;
import android.annotation.Nullable;
import android.annotation.StringRes;
import android.content.Context;
import android.content.res.Resources;
import android.os.PowerManager;
import android.provider.DeviceConfig;
import android.util.ArraySet;

import com.android.tv.settings.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Provides available energy modes, and allows to update the current energy mode.
 */
public final class EnergyModesHelper {
    public static final String NAMESPACE_LOW_POWER_STANDBY = "low_power_standby";
    public static final String KEY_ENABLE_POLICY = "enable_policy";

    private final Context mContext;

    /** Describes an Energy Mode. */
    public static final class EnergyMode {
        @StringRes
        public final int identifierRes;
        public final boolean ecoHighlighted;
        public final boolean enableLowPowerStandby;
        @BoolRes
        public final int enabledRes;
        @StringRes
        public final int titleRes;
        @StringRes
        public final int subtitleRes;
        @DrawableRes
        public final int iconRes;
        @StringRes
        public final int infoTextRes;
        @ArrayRes
        public final int featuresRes;
        @StringRes
        public final int ecoHintRes;
        @DrawableRes
        public final int ecoHintIconRes;

        @ArrayRes
        public final int baseExemptPackagesRes;
        @ArrayRes
        public final int vendorExemptPackagesRes;
        @IntegerRes
        public final int baseAllowedReasonsRes;
        @IntegerRes
        public final int vendorAllowedReasonsRes;
        @ArrayRes
        public final int baseAllowedFeaturesRes;
        @ArrayRes
        public final int vendorAllowedFeaturesRes;

        public EnergyMode(@StringRes int identifierRes, boolean ecoHighlighted,
                boolean enableLowPowerStandby, @BoolRes int enabledRes, @StringRes int titleRes,
                @StringRes int subtitleRes, @DrawableRes int iconRes, @StringRes int infoTextRes,
                @ArrayRes int featuresRes, @StringRes int ecoHintRes,
                @DrawableRes int ecoHintIconRes, @ArrayRes int baseExemptPackagesRes,
                @ArrayRes int vendorExemptPackagesRes, @IntegerRes int baseAllowedReasonsRes,
                @IntegerRes int vendorAllowedReasonsRes, @ArrayRes int baseAllowedFeaturesRes,
                @ArrayRes int vendorAllowedFeaturesRes) {
            this.ecoHighlighted = ecoHighlighted;
            this.enableLowPowerStandby = enableLowPowerStandby;
            this.enabledRes = enabledRes;
            this.titleRes = titleRes;
            this.subtitleRes = subtitleRes;
            this.iconRes = iconRes;
            this.infoTextRes = infoTextRes;
            this.featuresRes = featuresRes;
            this.ecoHintRes = ecoHintRes;
            this.ecoHintIconRes = ecoHintIconRes;
            this.identifierRes = identifierRes;
            this.baseExemptPackagesRes = baseExemptPackagesRes;
            this.vendorExemptPackagesRes = vendorExemptPackagesRes;
            this.baseAllowedReasonsRes = baseAllowedReasonsRes;
            this.vendorAllowedReasonsRes = vendorAllowedReasonsRes;
            this.baseAllowedFeaturesRes = baseAllowedFeaturesRes;
            this.vendorAllowedFeaturesRes = vendorAllowedFeaturesRes;
        }
    }

    public static EnergyMode MODE_LOW_ENERGY = new EnergyMode(
            R.string.energy_mode_low_identifier,
            true,
            true,
            R.bool.energy_mode_low_enabled,
            R.string.energy_mode_low_title,
            R.string.energy_mode_low_subtitle,
            R.drawable.ic_settings_eco_leaf,
            R.string.energy_mode_low_info,
            R.array.energy_mode_low_features,
            R.string.energy_mode_low_eco_hint,
            R.drawable.ic_tips_and_updates,
            R.array.energy_mode_low_baseExemptPackages,
            R.array.energy_mode_low_vendorExemptPackages,
            R.integer.energy_mode_low_baseAllowedReasons,
            R.integer.energy_mode_low_vendorAllowedReasons,
            R.array.energy_mode_low_baseAllowedFeatures,
            R.array.energy_mode_low_vendorAllowedFeatures);

    public static EnergyMode MODE_MODERATE_ENERGY = new EnergyMode(
            R.string.energy_mode_moderate_identifier,
            false,
            true,
            R.bool.energy_mode_moderate_enabled,
            R.string.energy_mode_moderate_title,
            R.string.energy_mode_moderate_subtitle,
            R.drawable.ic_settings_energy_mode_moderate,
            R.string.energy_mode_moderate_info,
            R.array.energy_mode_moderate_features,
            R.string.energy_mode_moderate_eco_hint,
            0,
            R.array.energy_mode_moderate_baseExemptPackages,
            R.array.energy_mode_moderate_vendorExemptPackages,
            R.integer.energy_mode_moderate_baseAllowedReasons,
            R.integer.energy_mode_moderate_vendorAllowedReasons,
            R.array.energy_mode_moderate_baseAllowedFeatures,
            R.array.energy_mode_moderate_vendorAllowedFeatures);

    public static EnergyMode MODE_HIGH_ENERGY = new EnergyMode(
            R.string.energy_mode_high_identifier,
            false,
            true,
            R.bool.energy_mode_high_enabled,
            R.string.energy_mode_high_title,
            R.string.energy_mode_high_subtitle,
            R.drawable.ic_settings_energy_mode_high,
            R.string.energy_mode_high_info,
            R.array.energy_mode_high_features,
            R.string.energy_mode_high_eco_hint,
            R.drawable.ic_bolt,
            R.array.energy_mode_high_baseExemptPackages,
            R.array.energy_mode_high_vendorExemptPackages,
            R.integer.energy_mode_high_baseAllowedReasons,
            R.integer.energy_mode_high_vendorAllowedReasons,
            R.array.energy_mode_high_baseAllowedFeatures,
            R.array.energy_mode_high_vendorAllowedFeatures);

    public static EnergyMode MODE_UNRESTRICTED = new EnergyMode(
            R.string.energy_mode_unrestricted_identifier,
            false,
            false,
            R.bool.energy_mode_unrestricted_enabled,
            R.string.energy_mode_high_title,
            R.string.energy_mode_high_subtitle,
            R.drawable.ic_settings_energy_mode_high,
            R.string.energy_mode_high_info,
            R.array.energy_mode_high_features,
            R.string.energy_mode_high_eco_hint,
            R.drawable.ic_bolt,
            0, 0, 0, 0, 0, 0);

    public static EnergyMode[] ENERGY_MODES =
            new EnergyMode[]{MODE_LOW_ENERGY, MODE_MODERATE_ENERGY, MODE_HIGH_ENERGY,
                    MODE_UNRESTRICTED};

    public EnergyModesHelper(Context context) {
        mContext = context;
    }

    /**
     * Returns whether this device supports Low Power Standby.
     *
     * If false, energy modes are not supported.
     */
    public static boolean isLowPowerStandbySupported(Context context) {
        final PowerManager powerManager = context.getSystemService(PowerManager.class);
        return powerManager.isLowPowerStandbySupported();
    }

    private boolean areEnergyModesEnabled() {
        boolean customPoliciesEnabled = DeviceConfig.getBoolean(NAMESPACE_LOW_POWER_STANDBY,
                KEY_ENABLE_POLICY, false);

        return isLowPowerStandbySupported(mContext) && customPoliciesEnabled;
    }

    /** Returns whether Energy Modes should be shown and used on this device */
    public boolean areEnergyModesAvailable() {
        return !getEnergyModes().isEmpty();
    }

    /** Returns all enabled energy modes in the order they should be presented. */
    @NonNull
    public List<EnergyMode> getEnergyModes() {
        ArrayList<EnergyMode> enabledModes = new ArrayList<>();
        if (!areEnergyModesEnabled()) {
            return enabledModes;
        }

        if (isEnergyModeEnabled(MODE_LOW_ENERGY)) {
            enabledModes.add(MODE_LOW_ENERGY);
        }

        if (isEnergyModeEnabled(MODE_MODERATE_ENERGY)) {
            enabledModes.add(MODE_MODERATE_ENERGY);
        }

        if (isEnergyModeEnabled(MODE_UNRESTRICTED)) {
            enabledModes.add(MODE_UNRESTRICTED);
        } else if (isEnergyModeEnabled(MODE_HIGH_ENERGY)) {
            enabledModes.add(MODE_HIGH_ENERGY);
        }

        return enabledModes;
    }

    private boolean isEnergyModeEnabled(EnergyMode mode) {
        if (mode == null) {
            return false;
        }

        if (mode == MODE_HIGH_ENERGY && isEnergyModeEnabled(MODE_UNRESTRICTED)) {
            // unrestricted mode overrides high energy mode
            return false;
        }

        Resources resources = mContext.getResources();
        boolean baseEnabled = resources.getBoolean(mode.enabledRes);
        String identifier = mContext.getString(mode.identifierRes);
        return DeviceConfig.getBoolean(NAMESPACE_LOW_POWER_STANDBY,
                "policy_" + identifier + "_enabled", baseEnabled);
    }

    /** Returns an energy mode by its identifier, or null if not found. */
    @Nullable
    public EnergyMode getEnergyMode(@StringRes int identifierRes) {
        for (EnergyMode energyMode : ENERGY_MODES) {
            if (energyMode.identifierRes == identifierRes) {
                return energyMode;
            }
        }

        return null;
    }

    /** Returns an energy mode by its identifier, or null if not found. */
    @Nullable
    public EnergyMode getEnergyMode(String identifier) {
        for (EnergyMode energyMode : ENERGY_MODES) {
            if (mContext.getString(energyMode.identifierRes).equals(identifier)) {
                return energyMode;
            }
        }

        return null;
    }

    /** Returns the description of the energy mode, incl. list of features */
    public String getSummary(EnergyMode mode) {
        StringBuilder summary = new StringBuilder();
        summary.append(mContext.getString(mode.infoTextRes));

        String featuresList = getFeaturesList(mode);
        if (featuresList != null) {
            summary.append("\n\n");
            summary.append(mContext.getString(R.string.energy_mode_enables));
            summary.append("\n");
            summary.append(featuresList);
        }

        return summary.toString();
    }

    /** Returns the list of features */
    @Nullable
    public String getFeaturesList(EnergyMode mode) {
        String[] features = mContext.getResources().getStringArray(mode.featuresRes);
        if (features.length == 0) {
            return null;
        }

        StringBuilder featureList = new StringBuilder();
        for (int i = 0; i < features.length; i++) {
            featureList.append("\u2022 ");
            featureList.append(features[i]);
            if (i < features.length - 1) {
                featureList.append("\n");
            }
        }

        return featureList.toString();
    }

    @Nullable
    private String[] getDeviceConfigStringArray(String key) {
        String string = DeviceConfig.getString(NAMESPACE_LOW_POWER_STANDBY, key, null);
        if (string == null) {
            return null;
        }
        return string.split(",");
    }

    private PowerManager.LowPowerStandbyPolicy getPolicy(EnergyMode mode) {
        final Resources resources = mContext.getResources();
        final String identifier = mContext.getString(mode.identifierRes);

        if (!mode.enableLowPowerStandby) {
            return new PowerManager.LowPowerStandbyPolicy(
                    identifier,
                    Collections.emptySet(),
                    0,
                    Collections.emptySet());
        }

        final String[] baseExemptPackages = resources.getStringArray(mode.baseExemptPackagesRes);
        final String[] deviceConfigExemptPackagesOverrides = getDeviceConfigStringArray(
                "policy_" + identifier + "_exempt_packages");
        final String[] vendorExemptPackages = resources.getStringArray(
                mode.vendorExemptPackagesRes);
        final Set<String> exemptPackages = combine(
                deviceConfigExemptPackagesOverrides != null
                        ? deviceConfigExemptPackagesOverrides
                        : baseExemptPackages,
                        vendorExemptPackages);

        final int baseAllowedReasons = resources.getInteger(mode.baseAllowedReasonsRes);
        final int deviceConfigAllowedReasonOverride = DeviceConfig.getInt(
                NAMESPACE_LOW_POWER_STANDBY, "policy_" + identifier + "_allowed_reasons", -1);
        final int vendorAllowedReasons = resources.getInteger(mode.vendorAllowedReasonsRes);
        final int allowedReasons = (
                (deviceConfigAllowedReasonOverride != -1
                        ? deviceConfigAllowedReasonOverride
                        : baseAllowedReasons) | vendorAllowedReasons);

        final String[] baseAllowedFeatures = resources.getStringArray(mode.baseAllowedFeaturesRes);
        final String[] deviceConfigAllowedFeaturesOverrides = getDeviceConfigStringArray(
                "policy_" + identifier + "_allowed_features");
        final String[] vendorAllowedFeatures = resources.getStringArray(
                mode.vendorAllowedFeaturesRes);
        final Set<String> allowedFeatures = combine(
                deviceConfigAllowedFeaturesOverrides != null
                        ? deviceConfigAllowedFeaturesOverrides
                        : baseAllowedFeatures,
                vendorAllowedFeatures);

        return new PowerManager.LowPowerStandbyPolicy(
                identifier,
                exemptPackages,
                allowedReasons,
                allowedFeatures);
    }

    private Set<String> combine(String[] a, String[] b) {
        ArraySet<String> result = new ArraySet<>();
        if (a != null) {
            result.addAll(new ArraySet<>(a));
        }
        if (b != null) {
            result.addAll(new ArraySet<>(b));
        }
        return result;
    }

    /** Sets the given energy mode in the system. */
    public void setEnergyMode(@NonNull EnergyMode energyMode) {
        PowerManager.LowPowerStandbyPolicy policy = getPolicy(energyMode);
        PowerManager powerManager = mContext.getSystemService(PowerManager.class);
        powerManager.setLowPowerStandbyEnabled(energyMode.enableLowPowerStandby);
        powerManager.setLowPowerStandbyPolicy(policy);
    }

    /**
     * Returns the default energy mode.
     *
     * This energy mode is used if the current Low Power Standby policy doesn't match any valid
     * and enabled energy modes.
     */
    @Nullable
    public EnergyMode getDefaultEnergyMode() {
        if (!areEnergyModesAvailable()) {
            return null;
        }
        return getEnergyModes().get(0);
    }

    /**
     * Returns true if going from the current energy mode to the given energy mode requires
     * the user to confirm the change.
     */
    public boolean requiresConfirmation(EnergyMode currentMode, EnergyMode newMode) {
        int currentModeIndex = getEnergyModeIndex(currentMode);
        int newModeIndex = getEnergyModeIndex(newMode);

        if (currentModeIndex == -1) {
            return newModeIndex > 0;
        }

        return newModeIndex > currentModeIndex;
    }

    private int getEnergyModeIndex(EnergyMode mode) {
        if (mode == null) {
            return -1;
        }
        for (int i = 0; i < ENERGY_MODES.length; i++) {
            if (mode == ENERGY_MODES[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Makes sure a valid energy mode is set, if energy modes are enabled, and returns the current
     * energy mode.
     */
    @Nullable
    public EnergyMode updateEnergyMode() {
        if (!areEnergyModesAvailable()) {
            return null;
        }

        PowerManager powerManager = mContext.getSystemService(PowerManager.class);
        final PowerManager.LowPowerStandbyPolicy currentPolicy =
                powerManager.getLowPowerStandbyPolicy();
        if (currentPolicy == null) {
            return null;
        }

        final EnergyMode matchingEnergyMode = getEnergyMode(currentPolicy.getIdentifier());
        EnergyMode targetEnergyMode = matchingEnergyMode;
        if (matchingEnergyMode == null || !isEnergyModeEnabled(matchingEnergyMode)) {
            if (matchingEnergyMode == MODE_HIGH_ENERGY && isEnergyModeEnabled(MODE_UNRESTRICTED)) {
                targetEnergyMode = MODE_UNRESTRICTED;
            } else if (matchingEnergyMode == MODE_UNRESTRICTED && isEnergyModeEnabled(
                    MODE_HIGH_ENERGY)) {
                targetEnergyMode = MODE_HIGH_ENERGY;
            } else {
                targetEnergyMode = getDefaultEnergyMode();
            }
        }

        setEnergyMode(targetEnergyMode);
        return targetEnergyMode;
    }

    @Nullable
    private EnergyMode getCurrentEnergyMode() {
        if (!areEnergyModesAvailable()) {
            return null;
        }

        PowerManager powerManager = mContext.getSystemService(PowerManager.class);
        final PowerManager.LowPowerStandbyPolicy currentPolicy =
                powerManager.getLowPowerStandbyPolicy();
        if (currentPolicy == null) {
            return null;
        }

        final EnergyMode matchingEnergyMode = getEnergyMode(currentPolicy.getIdentifier());
        if (isEnergyModeEnabled(matchingEnergyMode)) {
            return matchingEnergyMode;
        }

        if (matchingEnergyMode == MODE_HIGH_ENERGY && isEnergyModeEnabled(MODE_UNRESTRICTED)) {
            return MODE_UNRESTRICTED;
        } else if (matchingEnergyMode == MODE_UNRESTRICTED && isEnergyModeEnabled(
                MODE_HIGH_ENERGY)) {
            return MODE_HIGH_ENERGY;
        }

        return getDefaultEnergyMode();
    }
}

