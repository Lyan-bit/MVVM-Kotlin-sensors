package com.example.har.fragments

import android.content.Context
import android.os.*
import android.widget.*
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.PermissionChecker
import androidx.core.content.ContextCompat
import com.example.har.R
import com.example.har.viewModel.AccelerometerClassificationViewModel

class AccelerometerClassificationFragment : Fragment(), View.OnClickListener {
	lateinit var root: View
	lateinit var myContext: Context
	private lateinit var model: AccelerometerClassificationViewModel

	private lateinit var accelerometerSensorValue : TextView
	val permissionRequestCode = 100
	val permissionGranted = PackageManager.PERMISSION_GRANTED
	val activityPermission = Manifest.permission.ACTIVITY_RECOGNITION
	lateinit var resultTextView: TextView

	private lateinit var buttonStart : Button
	private lateinit var buttonStop : Button
	private var start = true

	companion object {
		fun newInstance(c: Context): AccelerometerClassificationFragment {
			val fragment = AccelerometerClassificationFragment ()
			val args = Bundle()
			fragment.arguments = args
			fragment.myContext = c
			return fragment
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		root = inflater.inflate(R.layout.accelerometerclassification_layout , container, false)
		super.onViewCreated(root, savedInstanceState)

		model = AccelerometerClassificationViewModel.getInstance(myContext)
		model.setHandler()

		//UI components declaration
		accelerometerSensorValue  = root.findViewById(R.id.accelerometerSensorValue)

		buttonStart = root.findViewById(R.id.buttonStart)
		buttonStart.setOnClickListener(this)

		buttonStop = root.findViewById(R.id.buttonStop)
		buttonStop.setOnClickListener(this)

		resultTextView = root.findViewById(R.id.result)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(
				arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), permissionRequestCode)
		}
		return root
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == permissionRequestCode && resultCode == Activity.RESULT_CANCELED) {
			Toast.makeText(requireContext(), "Steps can't count!", Toast.LENGTH_SHORT).show()
		}
	}

	private fun isPermissionGranted(): Boolean{
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PermissionChecker.checkSelfPermission(
			requireContext(),
			activityPermission
		) == permissionGranted
		else return true

	}

	private fun requestactivityPermission(){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
			requestPermissions(arrayOf(activityPermission), permissionRequestCode)
		}
	}

	override fun onClick(v: View?) {

		when (v?.id) {
			R.id.buttonStart-> {
				requestactivityPermission()
				start()
			}
			R.id.buttonStop-> {
				stop()
			}
		}
	}

	private fun start () {
		if(!isPermissionGranted()){
			requestactivityPermission()
			return
		}
		model.startSensors()
		start = true

		viewLifecycleOwner.lifecycleScope.launchWhenStarted {
			while (start) {
				delay(100)
				accelerometerSensorValue.text = model.listSensors().getAccelerometer()
				resultTextView.text = model.res
			}
		}
	}

	private fun stop () {
		start = false
		model.stopSensors()
		accelerometerSensorValue.text = ""
	}
}
