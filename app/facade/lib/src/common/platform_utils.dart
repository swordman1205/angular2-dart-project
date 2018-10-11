// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

class PlatformUtils {
  static bool isMobile() {
    return StackTrace.current.toString().contains("app_mobile")
        || StackTrace.current.toString().contains("flutter");
  }
}