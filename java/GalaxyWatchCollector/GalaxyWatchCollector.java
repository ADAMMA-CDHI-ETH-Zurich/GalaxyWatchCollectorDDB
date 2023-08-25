package GalaxyWatchCollector;/*
 * Copyright (C) 2021 Samsung Electronics Co., Ltd. All rights reserved.
 *
 * Mobile Communication Division,
 * IT & Mobile Communications, Samsung Electronics Co., Ltd.
 *
 * This software and its documentation are confidential and proprietary
 * information of Samsung Electronics Co., Ltd.  No part of the software and
 * documents may be copied, reproduced, transmitted, translated, or reduced to
 * any electronic medium or machine-readable form without the prior written
 * consent of Samsung Electronics.
 *
 * Samsung Electronics makes no representations with respect to the contents,
 * and assumes no responsibility for any errors that might appear in the
 * software and documents. This publication and the contents hereof are subject
 * to change without notice.
 */

import static android.content.Context.POWER_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.galaxywatchddbguard.GalaxyWatchAccelerometerSample;
import com.example.galaxywatchddbguard.GalaxyWatchDDBGuard;
import com.example.galaxywatchddbguard.GalaxyWatchHeartRateSample;

import java.util.ArrayList;
import java.util.List;

import AndroidPermissions.GenericPermission;
import AndroidPermissions.LocationPermission;
import JavaCLAID.CLAID;
import JavaCLAID.Channel;
import JavaCLAID.Module;
import JavaCLAID.Reflector;
import JavaCLAIDDataTypes.AccelerometerSample;
import JavaCLAIDDataTypes.AccelerometerSampleList;
import JavaCLAIDDataTypes.HeartRateSample;

public class GalaxyWatchCollector extends Module {

    private final String TAG = GalaxyWatchCollector.class.getSimpleName();



    private boolean enableAccelerometer;
    private boolean enableHeartRate;

    Channel<AccelerometerSample> accelerometerSampleChannel;
    Channel<HeartRateSample> heartRateSampleChannel;

    GalaxyWatchDDBGuard galaxyWatchDDBGuard = new GalaxyWatchDDBGuard();

    public void reflect(Reflector r)
    {
        r.reflect("enableAccelerometer", this.enableAccelerometer);
        r.reflect("enableHeartRate", this.enableHeartRate);
    }

    public void initialize()
    {
        this.accelerometerSampleChannel = this.publish(AccelerometerSample.class, "AccelerometerData");
        this.heartRateSampleChannel = this.publish(HeartRateSample.class, "HeartRateData");
        Context context = (Context) CLAID.getContext();

        galaxyWatchDDBGuard.initialize(context,
                enableAccelerometer,
                enableHeartRate,
                acclerometerData -> onAccelerometerData(acclerometerData),
                heartRateData -> onHeartRateData(heartRateData)
                );
    }

    public void onAccelerometerData(GalaxyWatchAccelerometerSample data)
    {
        AccelerometerSample sample = new AccelerometerSample();
        sample.setData(data.x, data.y, data.z);
        this.accelerometerSampleChannel.postWithTimestamp(sample, data.timestamp);
    }

    public void onHeartRateData(GalaxyWatchHeartRateSample data)
    {
        HeartRateSample sample = new HeartRateSample();
        sample.set_hr(data.hr);
        sample.set_hrIbi(data.hrIbi);
        sample.set_status(data.status);
        this.heartRateSampleChannel.postWithTimestamp(sample, data.timestamp);
    }


}
