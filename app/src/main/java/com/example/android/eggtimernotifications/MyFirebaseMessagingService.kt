package com.example.android.eggtimernotifications

import android.app.NotificationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.android.eggtimernotifications.util.sendNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * The "Firebase Cloud Messaging" (FCM) part of this lesson is given here
 * */
class MyFirebaseMessagingService : FirebaseMessagingService()  {

    /**
     * In order to handle FCM messages, you need to handle the data payload in the onMessageReceived() function of
     * MyFirebaseMessagingService. The data payload is stored in the data property of the remoteMessage object.
     * Both remoteMessage object and the data property can be null. Check if data property of remoteMessage object
     * has some value and print the data to the log. Add a log to print data property of the remoteMessage object
     * after checking remoteMessage and data is not empty. To test your code, you can use the Notifications composer
     * again, this time setting Custom data key, value pairs under Additional options in Step 4. Create a new message
     * and select the same topic. Set eggs and 3 as custom data. Make sure your app is running and in foreground.
     * Send the message and observe data message log in the logcat.  If your app is in the background, the FCM message
     * will trigger an automatic notification and onMessageReceived function will receive the remoteMessage object only
     * when the user clicks the notification.
     * */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        // check if the message contains a data payload
        remoteMessage.data.let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
        }

        /**
         * You want to make sure the user sees a notification popping up as a reminder.
         * You will add code which sends a notification using the notifications framework.
         * Check notification property and the remoteObject is not null and call the
         * sendNotification function. sendNotification functions gets an instance of
         * NotificationManager and calls the sendNotification extension function you previously
         * wrote. If you run the app again and send a notification by using Notifications
         * composer, you should see a notification regardless of if the app is in foreground
         * or background.
         * */
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            sendNotification(it.body!!)
        }
    }

    /**
     * MyFirebaseMessagingService class extends Firebase Messaging Service. Override the onNewToken() function and
     * add a log to print the token. This code will print the registration token to logcat, everytime a new token is
     * received. In most cases you may want to store this token. Let’s pretend sendRegistrationToServer function does
     * that. Call this function with the token after printing the log.
     *
     * onNewToken function will be called when a new token is generated. Now if you run the egg timer app and observe
     * logcat by filtering token(View > Tool Windows > Logcat), you should see a log line showing your token. This is
     * the token you need in order to send a message to this device. This function is only called when a new token is
     * created. If you do not see the log with the token, your app may already have received the token before. In that
     * case, uninstalling the app will help you to receive a new token.
     * */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?)  {
        // todo
    }

    private fun sendNotification(messageBody: String) {
        val notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.sendNotification(messageBody, applicationContext)
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}