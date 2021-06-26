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

package com.example.android.eggtimernotifications.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.eggtimernotifications.R
import com.example.android.eggtimernotifications.databinding.FragmentEggTimerBinding
import com.google.firebase.messaging.FirebaseMessaging

class EggTimerFragment : Fragment() {

    private val TOPIC = "breakfast"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentEggTimerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_egg_timer, container, false
        )

        val viewModel = ViewModelProvider(this).get(EggTimerViewModel::class.java)

        binding.eggTimerViewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        /**
         * Next, to create a channel, you need to call the new createChannel() extension function. This function takes
         * two parameters, the channel id and the channel name. You need to look up your channel id and channel name
         * from the string resources. You need to pass the channel id to the notification builder. Setting a wrong value
         * as the channel id will make the notification fail.
         * */
        createChannel(
            getString(R.string.egg_notification_channel_id),
            getString(R.string.egg_notification_channel_name)
        )

        /**
         * It is a good idea to create a new notification channel for the FCM since your users may want to enable/disable
         * egg timer or FCM push notifications separately. Open EggTimerFragment.kt and in onCreateView() create a new
         * channel with breakfast channel id and the breakfast channel name from string resources.
         * */
        createChannel(
            getString(R.string.breakfast_notification_channel_id),
            getString(R.string.breakfast_notification_channel_name)
        )

        /**
         * You need to call this function to subscribe to a topic when the app starts.
         * */
        subscribeTopic()

        return binding.root
    }

    /**
     * Pass the unique channel id to the constructor of NotificationChannel. Next pass the notification channel name which users
     * will also see in their settings screen. As the last parameter, pass the importance level for the notification channel.
     * Importance levels will be covered later in this lesson so for now you can use NotificationManager.IMPORTANCE_LOW.
     * On the notificationChannel object set enableLights to true. This setting will enable the lights when a notification is shown.
     * Again On the notificationChannel object set lightColor to red in order to display a red light when a notification is shown.
     * On the notificationChannel object set enableVibration to true in order to enable vibration. On the notificationChannel object
     * set channel description to "Time for breakfast".
     *
     * In order to change the importance level of your app’s notification channel, change the importance level from IMPORTANCE_LOW
     * to IMPORTANCE_HIGH. Changing this will change the behavior of your notification to make a sound and appear as a heads-up
     * notification even when the app is in the foreground.
     * */
    // TODO: Step 1.6 START create a channel
    private fun createChannel(channelId: String, channelName: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            /**
             * Add the setShowBadge(false) to the channel creation code for the egg timer to disable badges.
             * */
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Time for breakfast"

            /**
             * Get an instance of NotificationManager by calling getSystemService. Call createNotificationChannel on NotificationManager
             * and pass notificationChannel object which you created in the previous step.
             * */
            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // TODO: Step 1.6 END create a channel
    }

    /**
     * Create a function named subscribe.to topic. You need get an instance of FirebaseMessaging and call subscribeToTopic() function with
     * the topic name. You also need to add an addOnCompleteListener to get notified back from FCM on if your subscription is succeeded
     * or failed.
     * */
    private fun subscribeTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
            .addOnCompleteListener { task ->
                var msg = getString(R.string.message_subscribed)
                if (!task.isSuccessful) {
                    msg = getString(R.string.message_subscribe_failed)
                }
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        fun newInstance() = EggTimerFragment()
    }
}

