/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.eggtimernotifications.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.example.android.eggtimernotifications.MainActivity
import com.example.android.eggtimernotifications.R
import com.example.android.eggtimernotifications.receiver.SnoozeReceiver

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

// TODO: Step 1.1 extension function to send messages (GIVEN)
/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    // Create the content intent for the notification, which launches
    // this activity
    // TODO: Step 1.11 create intent
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    /**
     * Next, you need to create a new PendingIntent . The system will use the pending intent to open your app.
     * Create a PendingIntent with applicationContext, notification id, the content intent you created in the previous step
     * and the PendingIntent flag. The PendingIntent flag specifies the option to create a new PendingIntent or use an
     * existing one. You need to set PendingIntent.FLAG_UPDATE_CURRENT as the flag since you don’t want to create a new
     * notification but to update if there is an existing one. This way you will be modifying the current PendingIntent
     * which is associated with the Intent you are supplying.
     * */
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    /**
     * Style Notifications
     * Start with loading the image from resources by using the BitmapFactory
     * */
    val eggImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.cooked_egg
    )
    /**
     * Once you have the image, create a new BigPictureStyle and set your image.
     * You also need to set the bigLargeIcon() to null so the large icon goes away when the notification is expanded
     * */
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(eggImage)
        .bigLargeIcon(null)

    /**
     * In order to use SnoozeReceiver,  create a new intent just after the style in sendNotification() function.
     * You need to create a PendingIntent which will be used by the system to set up a new alarm to post a new
     * notification after 60 secs when the snooze button is tapped by the user. To create a pending
     * intent, call getBroadcast() method on PendingIntent which expects: The application context in which this
     * PendingIntent should start the activity. The request code, which is the request code for this pending intent.
     * If you need to update or cancel this pending intent, you need to use this code to access the pending intent. The
     * snoozeIntent object which is the intent of the activity to be launched the flag value of #FLAG_ONE_SHOT since the
     * intent will be used only once. The quick action and the notification will disappear after the first tap which is
     * why the intent can only be used once.
     * */
    val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
    val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        snoozeIntent,
        FLAGS
    )

    /**
     * To support devices running older versions you need to use NotificationCompat builder instead of notification builder.
     * Get an instance of the NotificationCompat builder, pass in the app context and a channel id. The channel id is a string
     * value from string resources which uses the matching channel. Starting with API level 26, all notifications must be
     * assigned to a channel
     * */
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.egg_notification_channel_id)
    )

    // TODO: Step 1.8 use the new 'breakfast' notification channel

    /**
     * Set the notification icon to represent your app, title and the content text for the message you want to give to the user.
     * You'll see more options to customize your notification further in the lesson but this is the minimum amount of data you
     * need to set in order to send a notification
     * */
        .setSmallIcon(R.drawable.cooked_egg)
        .setContentTitle(applicationContext
            .getString(R.string.notification_title))
        .setContentText(messageBody)

    /**
     * Next, pass the PendingIntent to your notification. You do this by calling setContentIntent() on the NotificationBuilder.
     * Now when you click the notification, the PendingIntent will be triggered, opening up your MainActivity.Also set setAutoCancel()
     * to true, so that when the user taps on the notification, the notification dismisses itself as it takes you to the app.
     * */
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        /**
         * Once you have the image, create a new BigPictureStyle and set your image.
         * You also need to set the bigLargeIcon() to null so the large icon goes away when the notification is expanded
         * */
        .setStyle(bigPicStyle)
        .setLargeIcon(eggImage)

        /**
         * Next, call the addAction() function on the notificationBuilder. This function expects an icon and a text to
         * describe your action to the user. You also need to add the snoozeIntent to notificationBuilder. This intent
         * will be used to trigger the right broadcastReceiver when your action is clicked.
         * */
        .addAction(
            R.drawable.egg_icon,
            applicationContext.getString(R.string.snooze),
            snoozePendingIntent
        )

        /**
         * To support devices running API level 25 or lower, you must also call setPriority() for each notification, using
         * a constant from the NotificationCompat class. To fix this, open NotificationUtils.kt and add PRIORITY_HIGH to
         * notification builder object
         * This time when the notification is delivered you should see a popup appear at the top of the screen regardless
         * of your app running in the foreground or background.
         * */
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    /**
     * Next, you need to call notify() with a unique id for your notification and with the Notification object from your builder.
     * This id represents the current notification instance and is needed for updating or canceling this notification. Since your
     * app will only have one active notification at a given time, you can use the same id for all your notifications. You are
     * already given a constant for this purpose called NOTIFICATION_ID in NotificationUtils.kt. Notice we can directly call notify()
     * since you are performing the call from an extension function on the same class.
     * */
    notify(NOTIFICATION_ID, builder.build())

}

/**
 * Add an extension function on NotificationManager which calls cancelAll.
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}