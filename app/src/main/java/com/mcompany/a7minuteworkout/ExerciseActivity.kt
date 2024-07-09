package com.mcompany.a7minuteworkout

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mcompany.a7minuteworkout.databinding.ActivityExerciseBinding
import com.mcompany.a7minuteworkout.databinding.CustomDialogBinding
import java.util.Locale

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding : ActivityExerciseBinding? = null
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration : Long = 5

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseTimerDuration : Long= 10

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts : TextToSpeech? = null
    private var player : MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarExercise?.setNavigationOnClickListener {
            customDialog()
        }

        tts = TextToSpeech(this, this)
        exerciseList = Constants.defaultExerciseList()


        setRestView()
        setupExerciseStatusAdapter()




    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

        customDialog()


    }

    private fun customDialog(){

        pauseTimer()

        val customDialog = Dialog(this, R.style.CustomDialogTheme)
        val dialogBinding = CustomDialogBinding.inflate(layoutInflater)

        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)

        dialogBinding.tvCancel.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }

        dialogBinding.tvHard.setOnClickListener {
            resetExercise()

            customDialog.dismiss()
        }

        dialogBinding.tvKnow.setOnClickListener {
            customDialog.dismiss()
        }

        dialogBinding.textViewQuit.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()


        }

        customDialog.show()




    }



    private fun setupExerciseStatusAdapter(){

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)

        binding?.recyclerview?.adapter = exerciseAdapter
        binding?.recyclerview?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    }

    private fun setRestView(){

        try {
            //val soundURI = Uri.parse("android.resource://com.mcompany.a7minuteworkout/"+R.raw.start)

            player = MediaPlayer.create(applicationContext, R.raw.start)
            player?.isLooping = false
            player?.start()

        } catch (e: Exception){
            e.printStackTrace()
        }


        binding?.frameRestBar?.visibility = View.VISIBLE
        binding?.textViewTitle?.visibility = View.VISIBLE
        binding?.textViewNextWorkout?.visibility = View.VISIBLE

        binding?.textViewTitleWorkout?.visibility = View.INVISIBLE
        binding?.frameExerciseBar?.visibility = View.INVISIBLE

        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0

        }





        setRestProgressBar()


    }

    private fun setExerciseTimer(){

        try {
            //val soundURI = Uri.parse("android.resource://com.mcompany.a7minuteworkout/"+R.raw.start)

            player = MediaPlayer.create(applicationContext, R.raw.start)
            player?.isLooping = false
            player?.start()

        } catch (e: Exception){
            e.printStackTrace()
        }

        binding?.constraintLayout2?.visibility = View.INVISIBLE
        binding?.textViewNextWorkout?.visibility = View.INVISIBLE

        binding?.frameRestBar?.visibility = View.INVISIBLE
        binding?.textViewTitle?.visibility = View.INVISIBLE


        binding?.textViewTitleWorkout?.visibility = View.VISIBLE
        binding?.frameExerciseBar?.visibility = View.VISIBLE

        if (exerciseProgress != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0

        }

        binding?.imageViewExercise?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.textViewTitleWorkout?.text = exerciseList!![currentExercisePosition].getName()


        speakOut(exerciseList!![currentExercisePosition].getName())


        setExerciseProgressBar()






    }

    private fun setRestProgressBar(){

        binding?.progressBarTimer?.progress = restProgress


        restTimer = object : CountDownTimer(restTimerDuration*1000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBarTimer?.progress = 10 - restProgress
                binding?.textViewTime?.text = (10 - restProgress).toString()


                if (millisUntilFinished <= 4000) {
                    speakOut((millisUntilFinished/1000).toString())
                }


            }

            override fun onFinish() {
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setSelected(true)
                exerciseAdapter?.notifyDataSetChanged()


                setExerciseTimer()

            }


        }.start()




    }

    private fun setExerciseProgressBar(){

        binding?.exerciseBarTimer?.progress = exerciseProgress



        exerciseTimer = object : CountDownTimer(exerciseTimerDuration*1000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.exerciseBarTimer?.progress = 30 - exerciseProgress
                binding?.textViewExerciseTime?.text = (30 - exerciseProgress).toString()

                if (millisUntilFinished <= 4000) {
                    speakOut((millisUntilFinished/1000).toString())
                }
            }

            override fun onFinish() {


                exerciseList!![currentExercisePosition].setSelected(false)
                exerciseList!![currentExercisePosition].setCompleted(true)
                exerciseAdapter?.notifyDataSetChanged()

                if (currentExercisePosition < exerciseList?.size!!-1){
                    binding?.textViewTitle?.text = "REST"
                    binding?.textViewTitle?.setTextColor(Color.WHITE)
                    binding?.constraintLayout2?.visibility = View.VISIBLE
                    binding?.textViewNextWorkout?.visibility = View.VISIBLE

                    binding?.imageViewExercise?.setImageResource(exerciseList!![currentExercisePosition +1].getImage())


                    binding?.textViewNextWorkout?.text = exerciseList!![currentExercisePosition + 1].getName()


                    setRestView()


                } else{
                    speakOut("You have finished the exercise!")
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)




                }
            }


        }.start()




    }

    private fun speakOut(text: String){

        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")


    }

    private fun resetExercise() {

        currentExercisePosition = -1
        exerciseList?.forEach { it.setCompleted(false); it.setSelected(false) }
        setupExerciseStatusAdapter()
        setRestView()


    }

    private fun pauseTimer(){

        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0

        }

        if (exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS){

            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_NOT_SUPPORTED ||
                result == TextToSpeech.LANG_MISSING_DATA){

                Toast.makeText(this, "Invalid language!", Toast.LENGTH_SHORT).show()



            }
        } else{

            Toast.makeText(this, "Retry again!", Toast.LENGTH_SHORT).show()

        }

    }

    override fun onDestroy() {
        super.onDestroy()

        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0

        }

        if (exerciseProgress != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0

        }

        if (tts != null){
            tts?.stop()
            tts?.shutdown()
        }

        if (player != null){
            player!!.stop()
        }

        binding = null
    }
}
