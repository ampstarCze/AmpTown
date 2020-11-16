package com.example.amptown;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class accelerometerControls {

    Context context;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener accelerometerListener;

    public float xAccel;

   public  accelerometerControls(Context context)
   {
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accelerometer ==null)
        {
            return;
        }

        accelerometerListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
               xAccel = event.values[0];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }     ;
        sensorManager.registerListener(accelerometerListener,accelerometer,SensorManager.SENSOR_DELAY_GAME);
   }

}
