package com.mcompany.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.mcompany.a7minuteworkout.databinding.ActivityBmiactivityBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object{
        private const val METRIC_UNITS = "METRIC_UNITS"
        private const val US_UNITS = "US_UNITS"
    }

    private var currectVisible : String = METRIC_UNITS

    private var bmiBinding : ActivityBmiactivityBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bmiBinding = ActivityBmiactivityBinding.inflate(layoutInflater)
        setContentView(bmiBinding?.root)

        setSupportActionBar(bmiBinding?.toolbarBMI)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "BMI"
        }

        bmiBinding?.toolbarBMI?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        bmiBinding?.radiButtonMetric?.isChecked = true

        makeVisibleMetric()

        bmiBinding?.radioGroup?.setOnCheckedChangeListener { _, checkedId : Int ->

            if(checkedId == R.id.radiButtonMetric){
                makeVisibleMetric()
            } else{
                bmiBinding?.radiButtonMetric?.isChecked = false
                makeVisibleUS()
            }


        }

        bmiBinding?.buttonCalculate?.setOnClickListener {
            calculateUnits()
        }

    }

    private fun calculateUnits(){

        if (currectVisible == METRIC_UNITS){
            if (validate()){
                val weight : Float = bmiBinding?.editTextWeight?.text.toString().toFloat()
                val height : Float = bmiBinding?.editTextHeight?.text.toString().toFloat() / 100

                val bmi = weight / (height * height)

                displayBMIMetrics(bmi)

            } else{
                Toast.makeText(this, "Enter your weight and height!", Toast.LENGTH_SHORT).show()
            }

        }
        else{
            if(validateUS()){

                val inch : Float = bmiBinding?.editTextInch?.text.toString().toFloat()
                val feet : Float = bmiBinding?.editTextFeet?.text.toString().toFloat()
                val pound : Float = bmiBinding?.editTextPound?.text.toString().toFloat()

                val height = inch + (feet * 12)

                val bmi = 703 * pound / (height * height)

                displayBMIMetrics(bmi)

            }

            else{
                Toast.makeText(this, "Enter fields!", Toast.LENGTH_SHORT).show()
            }


        }


    }

    private fun makeVisibleMetric(){

        currectVisible = METRIC_UNITS

        bmiBinding?.linearEditTextsMetric?.visibility = View.VISIBLE
        bmiBinding?.linearEditTextsUS?.visibility = View.INVISIBLE
        bmiBinding?.linearBMIResult?.visibility = View.INVISIBLE

        bmiBinding?.editTextWeight?.text!!.clear()
        bmiBinding?.editTextHeight?.text!!.clear()


    }

    private fun makeVisibleUS(){

        currectVisible = US_UNITS

        bmiBinding?.linearEditTextsMetric?.visibility = View.INVISIBLE
        bmiBinding?.linearEditTextsUS?.visibility = View.VISIBLE
        bmiBinding?.linearBMIResult?.visibility = View.INVISIBLE

        bmiBinding?.editTextPound?.text!!.clear()
        bmiBinding?.editTextFeet?.text!!.clear()
        bmiBinding?.editTextInch?.text!!.clear()


    }

    private fun displayBMIMetrics(bmi: Float){

        val bmiLabel: String
        val bmiDescription: String

        when {
            bmi < 15.0 -> {
                bmiLabel = "Very severely underweight"
                bmiDescription =
                    "You are at a risk of serious health problems. Seek medical attention immediately."
            }

            bmi in 15.0..16.0 -> {
                bmiLabel = "Severely underweight"
                bmiDescription =
                    "You are at a risk of serious health problems. Seek medical attention."
            }

            bmi in 16.0..18.5 -> {
                bmiLabel = "Underweight"
                bmiDescription = "You are at a risk of health problems. Consider gaining weight."
            }

            bmi in 18.5..24.9 -> {
                bmiLabel = "Normal weight"
                bmiDescription = "Congratulations! You are at a healthy weight."
            }

            bmi in 25.0..29.9 -> {
                bmiLabel = "Overweight"
                bmiDescription = "You are at a risk of health problems. Consider losing weight."
            }

            bmi in 30.0..34.9 -> {
                bmiLabel = "Obese Class I"
                bmiDescription =
                    "You are at a high risk of health problems. Seek medical attention."
            }

            bmi in 35.0..39.9 -> {
                bmiLabel = "Obese Class II"
                bmiDescription =
                    "You are at a very high risk of health problems. Seek medical attention."
            }

            else -> {
                bmiLabel = "Obese Class III"
                bmiDescription =
                    "You are at a very high risk of serious health problems. Seek medical attention immediately."
            }

        }

        val bmiValue  = BigDecimal(bmi.toDouble()).setScale(1, RoundingMode.HALF_EVEN).toString()



        bmiBinding?.linearBMIResult?.visibility = View.VISIBLE

        bmiBinding?.textViewBMIValue?.text = bmiValue
        bmiBinding?.textViewBMILabel?.text = bmiLabel
        bmiBinding?.textViewBMIDescription?.text = bmiDescription




    }

    private fun validate() : Boolean{

        var isValid  = true

        if (bmiBinding?.editTextWeight?.text.toString().isEmpty()){
            isValid = false
        }

        else if (bmiBinding?.editTextHeight?.text.toString().isEmpty()){
            isValid = false
        }


        return isValid

    }

    private fun validateUS() : Boolean{

        var isValid  = true

        if (bmiBinding?.editTextFeet?.text.toString().isEmpty()){
            isValid = false
        }

        else if (bmiBinding?.editTextInch?.text.toString().isEmpty()){
            isValid = false
        }

        else if (bmiBinding?.editTextPound?.text.toString().isEmpty()){
            isValid = false
        }



        return isValid

    }


}