package com.example.voiceasistance

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.speech.RecognizerIntent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.example.voiceasistance.databinding.ActivityMainBinding
import java.util.Locale
import java.util.Objects


class MainActivity : AppCompatActivity() {
    lateinit var iv_mic: ImageView
    lateinit var tv_Speech_to_text: TextView
    private val REQUEST_CODE_SPEECH_INPUT = 1
    lateinit var binding: ActivityMainBinding
    var mp: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        iv_mic = findViewById(R.id.iv_mic);
        tv_Speech_to_text = findViewById(R.id.tv_speech_to_text);

        iv_mic.setOnClickListener(View.OnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")
            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                Toast
                    .makeText(
                        this@MainActivity, " " + e.message,
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
        })
    }
    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        @Nullable data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                val result = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS
                ) as ArrayList<String>
                tv_Speech_to_text.text =
                    Objects.requireNonNull(result)!!!![0]


                binding.tvSpeechToText.text = Objects.requireNonNull(result)[0]

                if (binding.tvSpeechToText.text=="silent") {
                    vibratorFun()
                }else if (binding.tvSpeechToText.text=="vibration"){
                    vibratorFun()
                }else if (binding.tvSpeechToText.text=="sound"){
                    mp = MediaPlayer.create(this, R.raw.lofi)
                    mp!!.start()
                }else if (binding.tvSpeechToText.text=="play sound"){
                    mp = MediaPlayer.create(this, R.raw.night)
                    mp!!.start()
                }else{
                    Toast.makeText(this, "Not Match", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun vibratorFun() {
        var vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator!!.vibrate(
                VibrationEffect.createOneShot(1000, VibrationEffect.PARCELABLE_WRITE_RETURN_VALUE)
            )
        } else {
            //deprecated in API 26
            vibrator!!.vibrate(1000)
        }
    }
    }

