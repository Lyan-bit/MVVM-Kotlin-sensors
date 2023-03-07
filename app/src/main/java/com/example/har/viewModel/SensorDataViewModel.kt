package com.example.har.viewModel

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.lifecycle.ViewModel
import com.example.har.AccelerometerSensorManager
import com.example.har.MySensorEvent
import com.example.har.SensorType
import java.util.ArrayList

class SensorDataViewModel (myContext: Context): ViewModel()  {

    private var classificationvm = ClassificationViewModel (myContext)
    private var accelerometer : AccelerometerSensorManager = AccelerometerSensorManager(myContext)
    var currentSensor = ""
    var res = ""

    companion object {
        private var instance: SensorDataViewModel? = null
        fun getInstance(context: Context): SensorDataViewModel {
            return instance ?: SensorDataViewModel(context)
        }
    }

    //sensors
    fun startSensors () {
        if (!accelerometer.sensorExists()){
            currentSensor = "No Accelerometer Sensor"
        }
        else {
            accelerometer.startSensor()
        }
    }

    fun stopSensors () {
        accelerometer.stopSensor()
    }

    fun setHandler() {
        accelerometer.setHandler(handler)
    }

    // Handle messages
    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        /*
         * handleMessage() defines the operations to perform when
         * the Handler receives a new Message to process.
         */
        override fun handleMessage(inputMessage: Message) {
            // Gets the image task from the incoming Message object.
            val sensorEvent = inputMessage.obj as MySensorEvent
            val accelerometerArray: ArrayList<FloatArray> = ArrayList()

            if (sensorEvent.type == SensorType.Accelerometer){
                currentSensor = sensorEvent.value
                accelerometerArray.add(sensorEvent.data)
                res = classificationvm.classify(accelerometerArray)
                accelerometerArray.clear()
            }
        }
    }
}