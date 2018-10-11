// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

class TestDataUrlBuilder implements UrlBuilder {

  String _url;

  TestDataUrlBuilder(this._url);

  String buildUrl(String aPath) {
    return "$_url$aPath";
  }
}