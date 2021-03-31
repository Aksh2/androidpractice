package com.learning.activitylifecyclecalls

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class ActivityTwo : AppCompatActivity() {
    private val TAG = "state"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two)
        Log.d(TAG,"ActivityTwo: onCreate()")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG,"ActivityTwo: onRestart()")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"ActivityTwo: onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"ActivityTwo: onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"ActivityTwo: onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"ActivityTwo: onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"ActivityTwo: onDestroy()")
    }
}