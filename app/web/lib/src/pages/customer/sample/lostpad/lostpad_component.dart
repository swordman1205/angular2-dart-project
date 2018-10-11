// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/pages/customer/sample_service.dart';

@Component(
  selector: 'lostpad',
  host: const {'class': 'page-component'},
  styleUrls: const ['lostpad_component.css'],
  templateUrl: 'lostpad_component.html',
  directives: const [
    CORE_DIRECTIVES,
    ROUTER_DIRECTIVES,
    formDirectives,
    materialDirectives,
  ],
)
class LostpadComponent implements OnInit {
  final SampleApi _sampleApi;
  final Router _router;
  final SampleService _sampleService;
  final SecurityService _securityService;

  StripSample sampleData;

  LostpadComponent(this._sampleApi, this._router, this._sampleService, this._securityService);

  @override
  ngOnInit() {
    sampleData = _sampleService.sample;
  }

  Future<Null> submitFeedback() async {
    sampleData.status = 'FINISHED';
    await _sampleApi.saveStripSample(sampleData);
    //navigate to customer home on success
    _router.navigate(["Home"]);
  }
  
}
