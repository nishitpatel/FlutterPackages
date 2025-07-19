
import 'package:flutter/material.dart';
import 'package:otp_auto_read/otp_auto_read.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'OTP Auto Read Demo',
      home: Scaffold(
        appBar: AppBar(title: const Text('OTP Auto Read Example')),
        body: const OtpPage(),
      ),
    );
  }
}

class OtpPage extends StatefulWidget {
  const OtpPage({super.key});
  @override
  State<OtpPage> createState() => _OtpPageState();
}

class _OtpPageState extends State<OtpPage> {
  String _otp = '';

  @override
  void initState() {
    super.initState();
    OtpAutoRead.setListener((otp) {
      setState(() {
        _otp = otp;
      });
    });
    OtpAutoRead.start();
  }

  @override
  void dispose() {
    OtpAutoRead.stop();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Text('Received OTP: $_otp', style: const TextStyle(fontSize: 24)),
    );
  }
}
