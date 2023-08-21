/*
 * Copyright (C) 2022 LibreMoblieOS Foundation.
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

package com.android.tv.settings.privacy;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.hardware.usb.IUsbManager;
import android.hardware.usb.UsbManager;
import com.android.settingslib.core.AbstractPreferenceController;

import vendor.lineage.trust.V1_0.IUsbRestrict;

import java.util.NoSuchElementException;

public class TrustRestrictUsbPreferenceController extends AbstractPreferenceController {

    private Context mContext;
    private String mKey;

    private IUsbRestrict mUsbRestrictor = null;
    private boolean mIsUsb1_3 = false;

    public TrustRestrictUsbPreferenceController(Context context, String key) {
        super(context);

        mContext = context;
        mKey = key;

        try {
            mUsbRestrictor = IUsbRestrict.getService();
        } catch (NoSuchElementException | RemoteException e) {
            // ignore, the hal is not available
        }

        IUsbManager usbMgr = IUsbManager.Stub.asInterface(ServiceManager.getService(
                Context.USB_SERVICE));
        try {
            int version = usbMgr.getUsbHalVersion();

            if (version >= UsbManager.USB_HAL_V1_3)
                mIsUsb1_3 = true;
        } catch (RemoteException e) {
            // ignore, the hal is not available
        }
    }

    @Override
    public String getPreferenceKey() {
        return mKey;
    }

    @Override
    public boolean isAvailable() {
        return (mIsUsb1_3 || mUsbRestrictor != null);
    }
}
