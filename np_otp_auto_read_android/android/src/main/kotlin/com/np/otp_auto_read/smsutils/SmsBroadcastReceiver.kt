
package com.np.otp_auto_read.smsutils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class SmsBroadcastReceiver(private val listener: SmsBroadcastReceiverListener) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val extras = intent?.extras ?: return
        listener.onSuccess(extras)
    }
}
