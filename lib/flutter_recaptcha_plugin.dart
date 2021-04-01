import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

import 'expeption/token_exception.dart';

class FlutterRecaptchaPlugin {
  static const MethodChannel _channel =
      const MethodChannel('flutter_recaptcha_plugin');

  static Future<String> getRecaptchaToken({@required String apiKey}) async {
    try {
      final String _token =
          await _channel.invokeMethod("verify", {"key": apiKey});
      return _token;
    } on PlatformException catch (err) {
      throw TokenException(exception: err);
    }
  }
}
