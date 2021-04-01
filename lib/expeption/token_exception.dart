import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class TokenException implements Exception {
  final PlatformException exception;

  TokenException({@required this.exception});

  @override
  String toString() {
    return "Cannot get token.\n${exception.message}";
  }
}