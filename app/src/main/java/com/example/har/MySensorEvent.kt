package com.example.har

enum class SensorType(i: Int) {
    Accelerometer(1),
    STEP_COUNTER(2)
}

class MySensorEvent {
    var type = SensorType.Accelerometer
    var value = ""
    var data : FloatArray = FloatArray(3)
    var dataStep_counter : FloatArray = FloatArray(1)

}
