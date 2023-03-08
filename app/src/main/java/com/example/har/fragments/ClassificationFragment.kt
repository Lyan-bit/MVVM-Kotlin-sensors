package com.example.har.fragments

import android.os.Bundle
import android.widget.*
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.content.Context
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.har.R
import com.example.har.viewModel.ClassificationViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class ClassificationFragment : Fragment(), View.OnClickListener {
    lateinit var root: View
    lateinit var myContext: Context
    lateinit var classificationvm: ClassificationViewModel

	lateinit var resultTextView : TextView
    lateinit var buttonDiagnose: Button
    lateinit var buttonCancel: Button

    companion object {
        fun newInstance(c: Context): ClassificationFragment {
            val fragment = ClassificationFragment()
            val args = Bundle()
            fragment.arguments = args
            fragment.myContext = c
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.classification_layout, container, false)
        super.onViewCreated(root, savedInstanceState)
        classificationvm = ClassificationViewModel(myContext)

        resultTextView = root.findViewById(R.id.result)

        buttonDiagnose = root.findViewById(R.id.diagnoseOK)
        buttonDiagnose.setOnClickListener(this)

        buttonCancel = root.findViewById(R.id.diagnoseCancel)
        buttonCancel.setOnClickListener(this)

		return root
	}

    private fun diagnoseOK () {
        val one = floatArrayOf(-2.3350e-01f, 5.2750e-02f, -3.8000e-02f)
        val two = floatArrayOf(-2.2800e-01f, 1.0975e-01f, -3.9500e-02f)
        val three = floatArrayOf(-2.2350e-01f, 9.0500e-02f, -6.5000e-02f)
        val four = floatArrayOf(-2.1200e-01f, 8.3500e-02f, -7.5500e-02f)
        val five = floatArrayOf(-2.1325e-01f, 8.4500e-02f, -7.5000e-02f)
        val six = floatArrayOf(-2.1875e-01f, 6.9000e-02f, -7.6500e-02f)
        val seven = floatArrayOf(-2.1200e-01f, 5.3500e-02f, -7.4750e-02f)
        val eight = floatArrayOf(-2.1275e-01f, 7.4000e-02f, -8.8750e-02f)
        val nine = floatArrayOf(-2.1425e-01f, 7.1500e-02f, -8.3500e-02f)
        val ten = floatArrayOf(-2.1325e-01f, 6.4750e-02f, -9.3750e-02f)
        val eleven = floatArrayOf(-2.2050e-01f,  7.5500e-02f, -9.9500e-02f)
        val twelve = floatArrayOf(-2.1750e-01f,  7.2750e-02f, -1.0150e-01f)
        val thirteen = floatArrayOf(-2.2800e-01f,  7.0250e-02f, -8.4000e-02f)
        val fourteen = floatArrayOf(-2.2025e-01f,  5.3750e-02f, -8.9000e-02f)
        val fifteen = floatArrayOf(-2.0375e-01f,  8.8750e-02f, -9.1250e-02f)
        val sixteen = floatArrayOf(-1.8900e-01f,  6.7250e-02f, -9.5500e-02f)
        val seventeen = floatArrayOf(-1.9750e-01f,  5.3000e-02f, -1.1850e-01f)
        val eighteen = floatArrayOf(-2.1300e-01f,  1.0775e-01f, -1.0150e-01f)
        val twenty = floatArrayOf(-2.0300e-01f,  5.8750e-02f, -1.0075e-01f)
        val twenty1 = floatArrayOf(-2.0200e-01f,  6.8750e-02f, -1.1825e-01f)
        val twenty2 = floatArrayOf(-1.9300e-01f,  6.6250e-02f, -1.1575e-01f)
        val twenty3 = floatArrayOf(-2.2050e-01f,  8.4250e-02f, -1.0525e-01f)
        val twenty4 = floatArrayOf(-2.2025e-01f,  6.9250e-02f, -1.1225e-01f)
        val twenty5 = floatArrayOf(-2.1125e-01f,  8.9500e-02f, -1.1550e-01f)
        val twenty6 = floatArrayOf(-2.0500e-01f,6.4000e-02f, -1.2600e-01f)

        val data: ArrayList<FloatArray> = ArrayList()
        data.add(one)
        data.add(two)
        data.add(three)
        data.add(four)
        data.add(five)
        data.add(six)
        data.add(seven)
        data.add(eight)
        data.add(ten)
        data.add(eleven)
        data.add(twelve)
        data.add(thirteen)
        data.add(fourteen)
        data.add(fifteen)
        data.add(sixteen)
        data.add(seventeen)
        data.add(eighteen)
        data.add(twenty)
        data.add(twenty1)
        data.add(twenty2)
        data.add(twenty3)
        data.add(twenty4)
        data.add(twenty5)
        data.add(twenty6)


        viewLifecycleOwner.lifecycleScope.launch {
                delay(100)
                resultTextView.text = classificationvm.classify(data)
        }
    }

    private fun diagnoseCancel () {
        resultTextView.text = ""
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.diagnoseOK -> {
                diagnoseOK()
            }
            R.id.diagnoseCancel -> {
                diagnoseCancel()
            }
        }

    }
}

