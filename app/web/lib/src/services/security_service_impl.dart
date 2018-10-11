// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:html' as html;

import 'package:angular/di.dart';
import 'package:app_facade/app_facade.dart';
import 'package:http/http.dart';

@Injectable()
class SecurityServiceImpl extends SecurityServiceBase {

  SecurityServiceImpl(Client http, html.Storage sessionStorage, UrlBuilder urlBuilder) : super(http, sessionStorage, urlBuilder);

}