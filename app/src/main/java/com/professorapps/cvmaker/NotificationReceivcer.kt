package com.professorapps.cvmaker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.internal.view.SupportMenu

class NotificationReceivcer : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        showNotification(context)
    }

    fun showNotification(context: Context) {
        val builder: NotificationCompat.Builder
        val string = context.resources.getString(R.string.app_name)
        val intent = Intent(context, HomeActivity::class.java)
        intent.putExtras(Bundle())
        intent.setFlags(201326592)
        val activity = PendingIntent.getActivity(context, 0, intent, 134217728)
        val notificationManager = context.getSystemService("notification") as NotificationManager
        val decodeResource = BitmapFactory.decodeResource(context.resources, R.drawable.logo)
        if (Build.VERSION.SDK_INT >= 26) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    "your_name",
                    string,
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
            builder = NotificationCompat.Builder(context).setSmallIcon(R.drawable.logo)
                .setLargeIcon(decodeResource).setLights(SupportMenu.CATEGORY_MASK, 300, 300)
                .setChannelId("your_name").setContentTitle("Hello")
        } else {
            builder =
                NotificationCompat.Builder(context).setSmallIcon(R.drawable.logo).setPriority(1)
                    .setContentTitle("Hello")
        }
        builder.setContentIntent(activity)
        builder.setContentText("We miss you")
        builder.setAutoCancel(true)
        notificationManager.notify(1, builder.build())
    }
}
