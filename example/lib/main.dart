import 'package:flutter/material.dart';
import 'package:flutter_recaptcha_plugin/expeption/token_exception.dart';
import 'package:flutter_recaptcha_plugin/flutter_recaptcha_plugin.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _token = 'Unknown';
  final String _testApiKey = "6LdJ_pcaAAAAALsvmFBxP_Nrn45xoVwBm2SiGN2A";

  void getToken() async {
    String _requestToken;
    try {
      _requestToken =
          await FlutterRecaptchaPlugin.getRecaptchaToken(apiKey: _testApiKey);
    } on TokenException catch (e) {
      print(e);
      _requestToken = e.exception.message;
    }
    setState(() {
      _token = _requestToken;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text(
            'Token is:\n$_token',
            textAlign: TextAlign.center,
          ),
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: () {
            getToken();
          },
          child: Text(
            "Get token",
            textAlign: TextAlign.center,
          ),
        ),
      ),
    );
  }
}
