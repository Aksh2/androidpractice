package com.learning.activitylifecyclecalls

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG = "state"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG,"MainActivity: onCreate()")
        activityTwoBtn.setOnClickListener {
            val intent = Intent(this, ActivityTwo::class.java)
            startActivity(intent)
        }

    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG,"MainActivity: onRestart()")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"MainActivity: onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"MainActivity: onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"MainActivity: onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"MainActivity: onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"MainActivity: onDestroy()")
    }
}