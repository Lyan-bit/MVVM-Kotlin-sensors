package com.example.har.viewModel

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.ViewModel
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.util.ArrayList

class ClassificationViewModel (myContext: Context): ViewModel()  {
    private val assetManager: AssetManager = myContext.assets
    var res = ""

    companion object {
        private var instance: SensorDataViewModel? = null
        fun getInstance(context: Context): SensorDataViewModel {
            return instance ?: SensorDataViewModel(context)
        }
    }

    //classification
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
            byteBuffer.putFloat(j[0])
            byteBuffer.putFloat(j[1])
            byteBuffer.putFloat(j[2])
        }
        val outputVal: ByteBuffer = ByteBuffer.allocateDirect(12)
        outputVal.order(ByteOrder.nativeOrder())

        tflite.run(byteBuffer, outputVal)
        outputVal.rewind()

        val result = FloatArray(3)
        for (i in 0..2) {
            result[i] = outputVal.float
        }

        res = if (result[0] > result[1] && result[0] > result[2])
            "Stationary"
        else if (result[1] > result[0] && result[1] > result[2])
            "Walking"
        else
            "Running"
        // res = "Stationary: \t" + round(result[0]) +"\n"+"Walking: \t" + round(result[1]) +"\n"+"Running: \t" + round(result[2])
        Log.i("res", res)
        return res

    }
}