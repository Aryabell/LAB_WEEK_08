package com.example.lab_week_08

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat

class SecondNotificationService : Service() {

    private val channelId = "SECOND_NOTIFICATION_CHANNEL"
    private val notificationId = 2  // beda dari yang pertama biar gak tabrakan

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "SecondNotificationService Started", Toast.LENGTH_SHORT).show()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Second Notification Service")
            .setContentText("Running countdown for another task...")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notification: Notification = notificationBuilder.build()
        startForeground(notificationId, notification)

        // Ubah durasi countdown agar gak bentrok dengan NotificationService
        val countDownTime: Long = 5000 // 5 detik
        val interval: Long = 1000 // 1 detik

        object : CountDownTimer(countDownTime, interval) {
            override fun onTick(millisUntilFinished: Long) {
                Toast.makeText(
                    applicationContext,
                    "Second Service running: ${millisUntilFinished / 1000}s left",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFinish() {
                Toast.makeText(
                    applicationContext,
                    "SecondNotificationService Done!",
                    Toast.LENGTH_LONG
                ).show()
                stopForeground(true)
                stopSelf()
            }
        }.start()

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Second Notification Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
