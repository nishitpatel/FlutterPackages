
package com.np.otp_auto_read

import android.os.Build
import android.os.Bundle
import android.content.Intent
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import com.np.otp_auto_read.smsutils.SMSUtils

class OtpAutoReadPlugin : FlutterActivity() {

    private val CHANNEL = "com.hdfcsec.smartnow/nativechannel"
    var smsResult: MethodChannel.Result? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        configureMethodChannel(flutterEngine)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun configureMethodChannel(@NonNull flutterEngine: FlutterEngine) {
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method.lowercase()) {
                "initotpread" -> {
                    smsResult = result
                    SMSUtils.checkUserConsent(this@OtpAutoReadPlugin, result)
                }
                "deinitotpread" -> {
                    SMSUtils.unRegisterReciever(this@OtpAutoReadPlugin)
                    result.success("SUCCESS")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        try {
            if (requestCode == SMSUtils.REQ_USER_CONSENT) {
                if (resultCode == RESULT_OK && intent != null) {
                    val message: String? = intent.getStringExtra("com.google.android.gms.auth.api.phone.EXTRA_SMS_MESSAGE")
                    val otp = SMSUtils.getOtpFromMessage(message)
                    smsResult?.success(otp)
                }
            }
        } catch (ex: Exception) {
            Log.e("EF::onActivityResult : ", ex.toString())
        }
    }
}
