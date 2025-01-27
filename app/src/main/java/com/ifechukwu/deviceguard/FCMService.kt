package com.ifechukwu.deviceguard

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ifechukwu.deviceguard.utils.Constants
import org.koin.android.ext.android.inject
import timber.log.Timber


/**
 * @Author: ifechukwu.udorji
 * @Date: 12/30/2024
 */
class FCMService: FirebaseMessagingService() {
    private val deviceController: DeviceController by inject()
    private val sessionManager: SessionManager by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendRegistrationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.i("Remote message received")

        // Check if message contains a notification payload
        remoteMessage.notification?.let { notification ->
            sendNotification(notification.title, notification.body)
        }

        // Check if message contains a data payload
        remoteMessage.data.isNotEmpty().let {
            // Handle data payload if needed
            handleDataMessage(remoteMessage.data)
        }
    }

    private fun sendNotification(title: String?, messageBody: String?) {
        val channelId = getString(R.string.notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // Create intent for notification tap action
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "DeviceGuard Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun handleDataMessage(data: Map<String, String>) {
        val command = data["command"]?.uppercase()

        if (command == null) {
            Timber.e("Invalid command received")
            return
        }

        when(command) {
            Constants.COMMAND_LOCK -> {
                Timber.i("Lock command received")
                deviceController.lockDevice()

            }
            Constants.COMMAND_WIPE -> {
                Timber.i("Wipe data command received")
                deviceController.wipeDevice()

            }
            Constants.COMMAND_DISABLE_ADMIN -> {
                Timber.i("Disable admin command received")
                deviceController.disableAdmin()
            }
            Constants.COMMAND_REBOOT -> {
                Timber.i("Reboot command received")
                deviceController.rebootDevice()
            }
            Constants.COMMAND_RESET_PASSWORD -> {
                Timber.i("Reset password command received")
                val newPassword = data["password"]
                if (newPassword != null) {
                    deviceController.resetPassword(newPassword)
                }
            }
            Constants.COMMAND_HIDE_APP -> {
                Timber.i("Hide app command received")
                val isHidden = data["is_hidden"]?.toBoolean() ?: false
                deviceController.hideApplication(isHidden)
            }
            else -> {
                Timber.e("Invalid command received")
            }
        }
    }

    private fun sendRegistrationToServer(token: String) {
        sessionManager.saveFirebaseToken(token)
        Timber.d("New token: $token")
    }
}