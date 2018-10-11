// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

class ErrorInfo {

  String message;

  ErrorInfo(String errorJson) {
    Map<String, dynamic> map = JSON.decode(errorJson);
    message = map['message'];
  }

}