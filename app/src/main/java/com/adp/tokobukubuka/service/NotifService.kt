package com.adp.tokobukubuka.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class NotifService : Service() {

    override fun onBind(intent: Intent): IBinder? {

        //Saving action implementation

        return null
    }

    companion object {

        private const val NOTIFICATION_ID_EXTRA = "notificationId"
        private const val IMAGE_URL_EXTRA = "imageUrl"
    }
}