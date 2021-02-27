package com.learning.workmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val  MESSAGE_STATUS = "message_status"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mWorkManager = WorkManager.getInstance()
        val mRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java).build()

        sendBtn.setOnClickListener {
            mWorkManager.enqueue(mRequest)
        }

        mWorkManager.getWorkInfoByIdLiveData(mRequest.id).observe(this, Observer {
            if (it != null) {
                val state = it.state
                statusTv.append(state.toString() + "\n")
            }
        })

    }

}