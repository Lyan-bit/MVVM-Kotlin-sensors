package com.example.har.viewModel

import android.content.Context
import android.content.res.AssetManager
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.lifecycle.ViewModel
import com.example.har.AccelerometerSensorManager
import com.example.har.MySensorEvent
import com.example.har.SensorType
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.util.*
import kotlin.Comparator

class SensorDataViewModel (myContext: Context): ViewModel()  {

    private val assetManager: AssetManager = myContext.assets
    private var accelerometer: AccelerometerSensorManager = AccelerometerSensorManager(myContext)
    var currentSensor = ""
    var res = ""

    companion object {
        private var instance: SensorDataViewModel? = null
        fun getInstance(context: Context): SensorDataViewModel {
            return instance ?: SensorDataViewModel(context)
        }
    }

    fun classify(x: ArrayList<FloatArray>): String {
        lateinit var tflite : Interpreter
        lateinit var tflitemodel : ByteBuffer

        try{
            tflitemodel = loadModelFile(assetManager, "converted_HARmodel.tflite")
            tflite = Interpreter(tflitemodel)
        } catch(ex: Exception){
            ex.printStackTrace()
        }

        val byteBuffer = ByteBuffer.allocateDirect(4 * 26 * 3 )
        for (j in x) {
            byteBuffer.putFloat(j[1]/4000)
            byteBuffer.putFloat(j[1]/4000)
            byteBuffer.putFloat(j[1]/4000)
        }

        val labelsList : List<String> = listOf ("Stationary","Walking","Running")
        val outputVal: ByteBuffer = ByteBuffer.allocateDirect(12)
        outputVal.order(ByteOrder.nativeOrder())
        tflite.run(byteBuffer, outputVal)
        outputVal.rewind()

        val output = FloatArray(3)
        for (i in 0..2) {
            output[i] = outputVal.float
        }

        res = getSortedResult(output, labelsList)[0].toString()
        return res
    }

    data class Recognition(
        var id: String = "",
        var title: String = "",
        var confidence: Float = 0F
    )  {
        override fun toString(): String {
            return "$title ($confidence%)"
        }
    }

    private fun getSortedResult(labelProbArray: FloatArray, labelList: List<String>): List<Recognition> {

        val pq = PriorityQueue(
            labelList.size,
            Comparator<Recognition> {
                    (_, _, confidence1), (_, _, confidence2)
                -> confidence1.compareTo(confidence2) * -1
            })

        for (i in labelList.indices) {
            val confidence = labelProbArray[i]
            pq.add(
                Recognition("" + i,
                    if (labelList.size > i) labelList[i] else "Unknown", confidence*100))
        }
        val recognitions = ArrayList<Recognition>()
        val recognitionsSize = Math.min(pq.size, labelList.size)

        if (pq.isNotEmpty()) {
            for (i in 0 until recognitionsSize) {
                recognitions.add(pq.poll())
            }
        }
        else {
            recognitions.add(Recognition("0", "Unknown",100F))
        }
        return recognitions
    }

    private fun loadModelFile(assetManager: AssetManager, modelPath: String): ByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            startOffset, declaredLength)
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
                res = classify(accelerometerArray)
                accelerometerArray.clear()
            }
        }
    }
}
