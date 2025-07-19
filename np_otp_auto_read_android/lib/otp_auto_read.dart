
import 'package:flutter/services.dart';

class OtpAutoRead {
  static const MethodChannel _channel = MethodChannel('com.hdfcsec.smartnow/nativechannel');

  static Future<void> start() async {
    await _channel.invokeMethod('initotpread');
  }

  static Future<void> stop() async {
    await _channel.invokeMethod('deinitotpread');
  }

  static void setListener(Function(String) onOtpReceived) {
    _channel.setMethodCallHandler((call) async {
      if (call.method == 'onOtpReceived') {
        onOtpReceived(call.arguments as String);
      }
    });
  }
}
