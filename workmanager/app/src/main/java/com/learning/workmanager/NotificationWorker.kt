package com.learning.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.learning.workmanager.MainActivity.Companion.MESSAGE_STATUS

class NotificationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    private val WORK_RESULT = "work_result"

    override fun doWork(): Result {
        val taskData = inputData
        val taskDataString = taskData.getString(MESSAGE_STATUS)
        showNotification("WorkManager", taskDataString ?: "Message has been sent.")
        val outputData = Data.Builder().putString(WORK_RESULT,"Job finished").build()
        return Result.success(outputData)
    }

    private fun showNotification(task: String, desc: String){
        val manager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "task_channel"
        val channel_name = "task_name"

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, channel_name, NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(task)
            .setContentText(desc)
            .setSmallIcon(R.mipmap.ic_launcher)

        manager.notify(1, builder.build())
    }
}