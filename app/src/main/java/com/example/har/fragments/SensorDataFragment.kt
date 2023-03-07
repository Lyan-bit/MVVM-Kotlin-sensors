package com.example.har.fragments

import android.content.Context
import android.os.*
import android.widget.*
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.har.R
import com.example.har.viewModel.SensorDataViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SensorDataFragment : Fragment(), View.OnClickListener {
	lateinit var root: View
	lateinit var myContext: Context

	private lateinit var sensorvm: SensorDataViewModel

	private lateinit var accelerometerSensorValue : TextView
	private lateinit var buttonStart : Button
	private lateinit var buttonStop : Button
	lateinit var resultTextView : TextView
	private var start = true

	companion object {
		fun newInstance(c: Context): SensorDataFragment {
			val fragment = SensorDataFragment()
			val args = Bundle()
			fragment.arguments = args
			fragment.myContext = c
			return fragment
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		root = inflater.inflate(R.layout.sensordata_layout, container, false)
		super.onViewCreated(root, savedInstanceState)

		sensorvm = SensorDataViewModel.getInstance(myContext)
		sensorvm.setHandler()

		accelerometerSensorValue = root.findViewById(R.id.accelerometerSensorValue)

		buttonStart = root.findViewById(R.id.buttonStart)
		buttonStart.setOnClickListener(this)

		buttonStop = root.findViewById(R.id.buttonStop)
		buttonStop.setOnClickListener(this)

		resultTextView = root.findViewById(R.id.result)

		return root
	}

	override fun onClick(v: View?) {

		when (v?.id) {
			R.id.buttonStart -> {
				start(v)
			}
			R.id.buttonStop -> {
				stop(v)
			}
		}
	}

	private fun start (_v: View) {
		sensorvm.startSensors()
		start = true

		viewLifecycleOwner.lifecycleScope.launch {
			while (start) {
				delay(100)
				accelerometerSensorValue.text = sensorvm.currentSensor
				resultTextView.text = sensorvm.res
			}
		}


	}

	private fun stop (_v: View) {
		start = false
		sensorvm.stopSensors()
		accelerometerSensorValue.text = ""
	}
}

