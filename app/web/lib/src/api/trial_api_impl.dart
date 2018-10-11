// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'package:angular/di.dart';
import 'package:app_facade/app_facade.dart';

@Injectable()
class TrialApiImpl extends TrialApiBase {
  TrialApiImpl(ApiClient client) : super(client);
}