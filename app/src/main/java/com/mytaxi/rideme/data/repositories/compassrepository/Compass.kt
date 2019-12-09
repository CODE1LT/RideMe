package com.mytaxi.rideme.data.repositories.compassrepository

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.Surface
import android.view.WindowManager
import com.mytaxi.rideme.app.Application
import com.mytaxi.rideme.logger.Logger
import javax.inject.Inject

class Compass(private val context: Context) : SensorEventListener {

    companion object {
        private const val TAG = "Compass"
        private const val ROTATION_VECTOR_SMOOTHING_FACTOR = 1f
        private const val GEOMAGNETIC_SMOOTHING_FACTOR = 1f
        private const val GRAVITY_SMOOTHING_FACTOR = 0.3f
    }

    @Inject
    lateinit var logger: Logger

    private var newAzimuthListener: CompassListener? = null

    private val mSensorManager: SensorManager = context
        .getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var mRotationVectorSensor: Sensor?
    private var mMagnetometerSensor: Sensor?
    private var mAccelerometerSensor: Sensor?

    private var mUseRotationVectorSensor = false

    private var mRotationVector = FloatArray(5)
    private var mGeomagnetic = FloatArray(3)
    private var mGravity = FloatArray(3)

    private var mAzimuthSensibility: Float = 0.toFloat()
    private var mLastAzimuthDegrees: Float = 0.toFloat()

    init {
        Application.appComponent.inject(this)
        mMagnetometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    }

    fun setListener(compassListener: CompassListener) {
        newAzimuthListener = compassListener
    }

    fun start() {
        start(1f)
    }

    private fun start(azimuthSensibility: Float) {
        mAzimuthSensibility = azimuthSensibility
        if (mRotationVectorSensor != null) {
            mSensorManager.registerListener(this, mRotationVectorSensor, SensorManager.SENSOR_DELAY_GAME)
        }
        if (mMagnetometerSensor != null) {
            mSensorManager.registerListener(this, mMagnetometerSensor, SensorManager.SENSOR_DELAY_GAME)
        }
        if (mAccelerometerSensor != null) {
            mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    fun stop() {
        mAzimuthSensibility = 0f
        mSensorManager.unregisterListener(this)
    }

    // SensorEventListener
    override fun onSensorChanged(event: SensorEvent) {
        makeCalculations(event)
    }

    private fun makeCalculations(event: SensorEvent) {
        synchronized(this) {
            // Get the orientation array with Sensor.TYPE_ROTATION_VECTOR if possible (more precise), otherwise with Sensor.TYPE_MAGNETIC_FIELD and Sensor.TYPE_ACCELEROMETER combined
            val orientation = FloatArray(3)
            if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                // Only use rotation vector sensor if it is working on this device
                if (!mUseRotationVectorSensor) {
                    logger.d(TAG,"Using Sensor.TYPE_ROTATION_VECTOR (more precise compass data)")
                    mUseRotationVectorSensor = true
                }
                // Smooth values
                mRotationVector = exponentialSmoothing(
                    event.values, mRotationVector,
                    ROTATION_VECTOR_SMOOTHING_FACTOR
                )
                // Calculate the rotation matrix
                val rotationMatrix = FloatArray(9)
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                // Calculate the orientation
                SensorManager.getOrientation(rotationMatrix, orientation)
            } else if (!mUseRotationVectorSensor && (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD || event.sensor.type == Sensor.TYPE_ACCELEROMETER)) {
                if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    mGeomagnetic = exponentialSmoothing(
                        event.values, mGeomagnetic,
                        GEOMAGNETIC_SMOOTHING_FACTOR
                    )
                }
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    mGravity = exponentialSmoothing(
                        event.values, mGravity,
                        GRAVITY_SMOOTHING_FACTOR
                    )
                }
                // Calculate the rotation and inclination matrix
                val rotationMatrix = FloatArray(9)
                val inclinationMatrix = FloatArray(9)
                SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix, mGravity, mGeomagnetic)
                // Calculate the orientation
                SensorManager.getOrientation(rotationMatrix, orientation)
            } else {
                return
            }

            // Calculate azimuth, pitch and roll values from the orientation[] array
            // Correct values depending on the screen rotation
            val screenRotation =
                (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation
            var mAzimuthDegrees = Math.toDegrees(orientation[0].toDouble()).toFloat()
            if (screenRotation == Surface.ROTATION_0) {
                val mRollDegrees = Math.toDegrees(orientation[2].toDouble()).toFloat()
                if (mRollDegrees >= 90 || mRollDegrees <= -90) {
                    mAzimuthDegrees += 180f
                }
            } else if (screenRotation == Surface.ROTATION_90) {
                mAzimuthDegrees += 90f
            } else if (screenRotation == Surface.ROTATION_180) {
                mAzimuthDegrees += 180f
                val mRollDegrees = (-Math.toDegrees(orientation[2].toDouble())).toFloat()
                if (mRollDegrees >= 90 || mRollDegrees <= -90) {
                    mAzimuthDegrees += 180f
                }
            } else if (screenRotation == Surface.ROTATION_270) {
                mAzimuthDegrees += 270f
            }

            // Force azimuth value between 0° and 360°.
            mAzimuthDegrees = (mAzimuthDegrees + 360) % 360

            // Notify the compass listener if needed
            if (Math.abs(mAzimuthDegrees - mLastAzimuthDegrees) >= mAzimuthSensibility || mLastAzimuthDegrees == 0f) {
                mLastAzimuthDegrees = mAzimuthDegrees
                newAzimuthListener?.onNewAzimuth(mAzimuthDegrees)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    private fun exponentialSmoothing(newValue: FloatArray, lastValue: FloatArray?, alpha: Float): FloatArray {
        val output = FloatArray(newValue.size)
        if (lastValue == null) {
            return newValue
        }
        for (i in newValue.indices) {
            output[i] = lastValue[i] + alpha * (newValue[i] - lastValue[i])
        }
        return output
    }

}