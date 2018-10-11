// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'dart:convert';
import 'package:http/http.dart';
import 'package:flutter/services.dart';

import 'package:app_facade/app_facade.dart';

class SecurityServiceImpl extends SecurityServiceBase {

  SecurityServiceImpl(Client http, Map<String, String> storage, UrlBuilder urlBuilder):super(http, storage, urlBuilder);

}