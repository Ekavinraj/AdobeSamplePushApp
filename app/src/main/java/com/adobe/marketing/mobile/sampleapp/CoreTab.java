/*
 Copyright 2020 Adobe
 All Rights Reserved.

 NOTICE: Adobe permits you to use, modify, and distribute this file in
 accordance with the terms of the Adobe license agreement accompanying
 it.
 */
package com.adobe.marketing.mobile.sampleapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adobe.marketing.mobile.AdobeCallback;
import com.adobe.marketing.mobile.AdobeCallbackWithError;
import com.adobe.marketing.mobile.AdobeError;
import com.adobe.marketing.mobile.Identity;
import com.adobe.marketing.mobile.MobileCore;
import com.adobe.marketing.mobile.MobilePrivacyStatus;
import com.adobe.marketing.mobile.VisitorID;
import com.adobe.marketing.mobile.edge.identity.AuthenticatedState;
import com.adobe.marketing.mobile.edge.identity.IdentityItem;
import com.adobe.marketing.mobile.edge.identity.IdentityMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoreTab extends Fragment implements NavigationAware {

    boolean userIsViewingThisFragment = true;
    private static final String LOG_TAG = "Core Tab";

    Toast toast = null;

    public void showToast(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(MainApp.getAppContext(), message, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_core_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Create references to all our components
        Button rdoOptIn = getView().findViewById(R.id.rdoOptIn);
        Button rdoOptOut = getView().findViewById(R.id.rdoOptOut);
        Button rdoOptUnknown = getView().findViewById(R.id.rdoOptUnknown);
        Button btnEmitStateEvent = getView().findViewById(R.id.btn_emitStateEvent);
        Button btnEmitActionEvent = getView().findViewById(R.id.btn_emitActionEvent);
        Button btnGetPrivacy = getView().findViewById(R.id.btn_getPrivacy);
        Button btnCollectPII = getView().findViewById(R.id.btn_collectPII);
        Button btnUpdateConfig = getView().findViewById(R.id.btn_updateConfig);
        Button btnSetAdvertisingId = getView().findViewById(R.id.btn_setAdvertisingId);
        Button btnSetPushId = getView().findViewById(R.id.btn_setPushId);
        Button btnGetECID = getView().findViewById(R.id.btn_getECID);
        Button btnGetUrlVar = getView().findViewById(R.id.btn_getUrlVar);
        Button btnSyncId = getView().findViewById(R.id.btn_syncId);
        Button btnGetSdkId = getView().findViewById(R.id.btn_getSdkId);
        Button btnAppendUrl = getView().findViewById(R.id.btn_appendUrl);
        final TextView txtCurrentPrivacy = getView().findViewById(R.id.text_currentPrivacy);


        btnCollectPII.setOnClickListener(v -> MobileCore.collectPii(Collections.singletonMap("name", "kavinraj Yinzcam Push POC")));
        btnUpdateConfig.setOnClickListener(v -> MobileCore.updateConfiguration(Collections.singletonMap("analytics.batchLimit", 3)));
        btnSetAdvertisingId.setOnClickListener(v -> MobileCore.setAdvertisingIdentifier("advertisingIdentifier"));
        btnSetPushId.setOnClickListener(v -> MobileCore.setPushIdentifier("9516258b6230afdd93cf0cd07b8dd845"));
        btnGetECID.setOnClickListener(v -> Identity.getExperienceCloudId(new AdobeCallbackWithError<String>() {
            @Override
            public void fail(AdobeError adobeError) {
                Log.i(LOG_TAG, "getExperienceCloudId failed with error: " + adobeError.getErrorName());
            }

            @Override
            public void call(String s) {
                Log.i(LOG_TAG, "getExperienceCloudId returned: " + s);
            }
        }));

        btnGetUrlVar.setOnClickListener(v -> Identity.getUrlVariables(new AdobeCallbackWithError<String>() {
            @Override
            public void fail(AdobeError adobeError) {
                Log.i(LOG_TAG, "getUrlVariables failed with error: " + adobeError.getErrorName());
            }

            @Override
            public void call(String s) {
                Log.i(LOG_TAG, "getUrlVariables returned: " + s);
            }
        }));

        btnGetSdkId.setOnClickListener(v -> MobileCore.getSdkIdentities(new AdobeCallbackWithError<String>() {
            @Override
            public void fail(AdobeError adobeError) {
                Log.i(LOG_TAG, "getSdkIdentities failed with error: " + adobeError.getErrorName());
            }

            @Override
            public void call(String s) {
                Log.i(LOG_TAG, "getSdkIdentities returned: " + s);
            }
        }));

        btnSyncId.setOnClickListener(v -> Identity.syncIdentifier("idType1", "1234567", VisitorID.AuthenticationState.AUTHENTICATED));
        btnAppendUrl.setOnClickListener(v -> Identity.appendVisitorInfoForURL("https://example.com", new AdobeCallbackWithError<String>() {
            @Override
            public void fail(AdobeError adobeError) {
                Log.i(LOG_TAG, "appendVisitorInfoForURL failed with error: " + adobeError.getErrorName());
            }

            @Override
            public void call(String s) {
                Log.i(LOG_TAG, "appendVisitorInfoForURL returned: " + s);
            }
        }));

        btnGetPrivacy.setOnClickListener(v -> MobileCore.getPrivacyStatus(mobilePrivacyStatus -> getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtCurrentPrivacy.setText("Current Privacy: " + mobilePrivacyStatus.getValue());
            }
        })));

        /*btnEmitActionEvent.setOnClickListener(v -> {
            String eventName = "sampleAction";

            Map<String, String> additionalContextData = new HashMap<String, String>();
            additionalContextData.put("exampleCustomKey", "test88u7gigya89");
            MobileCore.trackAction(eventName, additionalContextData);

            showToast("Analytics action \"" + eventName + "\" triggered");

        });*/
        btnEmitActionEvent.setOnClickListener(v -> {
            // Define the name of the action event
            String eventName = "sampleAction";
            // Generate a random 12-digit alphanumeric value
            String randomValue = RandomStringUtils.randomAlphanumeric(12);
            // Create a map to store additional context data
            Map<String, String> additionalContextData = new HashMap<String, String>();
            // Add the random value as a custom key-value pair to the context data
            additionalContextData.put("exampleCustomKey", randomValue);
            // Track the action event with the provided name and context data
            MobileCore.trackAction(eventName, additionalContextData);
            // Display a toast message indicating the action event was triggered
            showToast("Analytics action \"" + eventName + "\" triggered with random key: " + randomValue);
        });

        btnEmitStateEvent.setOnClickListener(v -> {
            String eventName = "sampleState";

            Map<String, String> additionalContextData = new HashMap<>();
            additionalContextData.put("exampleCustomKey", "exampleValue");
            MobileCore.trackState(eventName, additionalContextData);

            showToast("Analytics state \"" + eventName + "\" triggered");

        });

        rdoOptIn.setOnClickListener(v -> MobileCore.setPrivacyStatus(MobilePrivacyStatus.OPT_IN));
        rdoOptOut.setOnClickListener(v -> MobileCore.setPrivacyStatus(MobilePrivacyStatus.OPT_OUT));
        rdoOptUnknown.setOnClickListener(v -> MobileCore.setPrivacyStatus(MobilePrivacyStatus.UNKNOWN));
    }

    @Override
    public void OnNavigateTo() {
        userIsViewingThisFragment = true;
    }

    @Override
    public void OnNavigateAway() {
        userIsViewingThisFragment = false;
    }
}
