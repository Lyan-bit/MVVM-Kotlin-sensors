package com.example.har

enum class SensorType(i: Int) {
    Accelerometer(1),
}

class MySensorEvent {
    var type = SensorType.Accelerometer
    var value = ""
    var data : FloatArray = FloatArray(3)
}