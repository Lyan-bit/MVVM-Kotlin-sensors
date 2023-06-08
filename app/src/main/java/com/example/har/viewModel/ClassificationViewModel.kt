package com.example.har.viewModel

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.lifecycle.ViewModel
import com.example.har.MySensorEvent
import com.example.har.SensorType
import com.example.har.StepCounterSensorManager
import java.util.*

class ClassificationViewModel (myContext: Context): ViewModel()  {
      private var stepCounter: StepCounterSensorManager = StepCounterSensorManager(myContext)
    var currentSensor = ""
    var res = ""

    companion object {
        private var instance: ClassificationViewModel? = null
        fun getInstance(context: Context): ClassificationViewModel {
            return instance ?: ClassificationViewModel(context)
        }
    }

    fun collectSensor(): String {
        var result = ""
        return result
    }

    fun startSensors () {
        if (!stepCounter.sensorExists()){
            currentSensor = "No StepCounter Sensor"
        }
        else {
            stepCounter.startSensor()
        }
    }

    fun stopSensors () {
        if (stepCounter.sensorExists()){
            stepCounter.stopSensor()
        }
    }

    fun setHandler() {
        stepCounter.setHandler(handler)
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

                if (sensorEvent.type == SensorType.STEP_COUNTER){
                    currentSensor = sensorEvent.value
                    accelerometerArray.add(sensorEvent.data)
                    res =  sensorEvent.value
                    accelerometerArray.clear()
                }
        }
    }
}
