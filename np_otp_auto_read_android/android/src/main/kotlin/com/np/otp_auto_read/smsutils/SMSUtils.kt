
package com.np.otp_auto_read.smsutils

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SmsMessage
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import io.flutter.plugin.common.MethodChannel

object SMSUtils {

    const val REQ_USER_CONSENT = 200
    private var smsReceiver: BroadcastReceiver? = null
    private const val TAG = "SMSUtils"

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkUserConsent(activity: Activity, result: MethodChannel.Result) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECEIVE_SMS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            activity.requestPermissions(arrayOf(Manifest.permission.RECEIVE_SMS), 101)
            result.error("PERMISSION_DENIED", "SMS permission denied", null)
            return
        }

        registerReceiver(activity)
        result.success("LISTENING")
    }

    fun registerReceiver(activity: Activity) {
        smsReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == "android.provider.Telephony.SMS_RECEIVED") {
                    val bundle = intent.extras
                    val pdus = bundle?.get("pdus") as? Array<*>
                    val format = bundle?.getString("format")
                    pdus?.forEach { pdu ->
                        val sms = SmsMessage.createFromPdu(pdu as ByteArray, format)
                        val message = sms.messageBody
                        Log.d(TAG, "Received SMS: $message")

                        val regex = Regex("\b\d{6}\b")
                        val otp = regex.find(message)?.value

                        if (otp != null) {
                            MethodChannelHandler.sendOtpToFlutter(otp)
                        }
                    }
                }
            }
        }

        val filter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        filter.priority = IntentFilter.SYSTEM_HIGH_PRIORITY
        activity.registerReceiver(smsReceiver, filter)
    }

    fun getOtpFromMessage(message: String?): String {
        val otpRegex = Regex("\b\d{6}\b")
        return otpRegex.find(message ?: "")?.value ?: ""
    }

    fun unRegisterReciever(activity: Activity) {
        try {
            smsReceiver?.let {
                activity.unregisterReceiver(it)
                smsReceiver = null
                Log.d(TAG, "SMS Receiver unregistered")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error unregistering SMS receiver", e)
        }
    }
}
