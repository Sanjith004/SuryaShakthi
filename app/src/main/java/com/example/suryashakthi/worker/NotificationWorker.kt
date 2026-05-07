package com.example.suryashakthi.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Background worker to drive user engagement.
 * Sends a reminder to log daily energy data.
 */
@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val CHANNEL_ID = "engagement_notifications"
        const val NOTIFICATION_ID = 1001
    }

    override suspend fun doWork(): Result {
        return try {
            pushEngagementNotification()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun pushEngagementNotification() {
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Surya Shakti Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Solar production and savings alerts"
            }
            manager.createNotificationChannel(channel)
        }

        // Feature: Peak Suggestion (Randomly simulate weather check)
        val isHighSun = java.util.Random().nextBoolean()
        val title = if (isHighSun) "Peak Sun Alert!" else "Surya Shakti: Data Check"
        val message = if (isHighSun) 
            "High Sun: Ideal time for heavy appliances. Maximize your savings!" 
            else "Ready to see your solar impact today? Log your readings now."

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_menu_day)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        manager.notify(NOTIFICATION_ID, notification)
    }
}
