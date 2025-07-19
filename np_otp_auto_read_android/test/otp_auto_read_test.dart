
import 'package:flutter_test/flutter_test.dart';
import 'package:otp_auto_read/otp_auto_read.dart';

void main() {
  test('OTP channel method calls should complete', () async {
    expect(OtpAutoRead.start(), completes);
    expect(OtpAutoRead.stop(), completes);
  });
}
