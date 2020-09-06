package com.example.hardwaresensorapp

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt
import kotlin.random.Random

class MainActivity : AppCompatActivity(), SensorEventListener {

    lateinit var sensorManager: SensorManager
    lateinit var proxSensor: Sensor
    lateinit var accelSensor: Sensor
    var accelFlag = false
    var proxyFlag = false

    val colors =
        arrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW)

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {

        if (proxyFlag) {
            if (event!!.values[0] > 0) {
                flProxIndicator.setBackgroundColor(colors[Random.nextInt(6)])
            }
        }

        if (accelFlag) {
            val bgColor = Color.rgb(
                accel2Color(event!!.values[0]),
                accel2Color(event!!.values[1]),
                accel2Color(event!!.values[2])
            )

            flAccelIndicator.setBackgroundColor(bgColor)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService<SensorManager>()!!
        proxSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    }


    override fun onResume() {
        super.onResume()

        accelBtn.setOnClickListener {
            accelFlag = true
            proxyFlag = false
            sensorManager.unregisterListener(this)
            sensorManager.registerListener(
                this, accelSensor, 1000 * 1000
            )
        }

        proximityBtn.setOnClickListener {
            accelFlag = false
            proxyFlag = true
            sensorManager.unregisterListener(this)
            sensorManager.registerListener(
                this, proxSensor, 1000 * 1000
            )
        }
    }

    override fun onPause() {
        sensorManager.unregisterListener(this)
        super.onPause()
    }

    private fun accel2Color(accel: Float): Int {
        return (((accel + 12) / 24) * 255).roundToInt()
    }

}