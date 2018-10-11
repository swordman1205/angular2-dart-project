// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'dart:html' as html;

import 'package:angular/di.dart';
import 'package:app_facade/app_facade.dart';

@Injectable()
class HealthApiImpl extends HealthApiBase implements HealthApi {
  final ApiClient _client;

  HealthApiImpl(ApiClient client, SecurityService securityService) :
        _client = client,
        super(client, securityService);

}