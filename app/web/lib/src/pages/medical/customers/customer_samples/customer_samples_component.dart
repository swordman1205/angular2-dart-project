import 'dart:async';

import 'package:angular/angular.dart';
import 'package:app_facade/app_facade.dart';

class CustomerSamplesComponent implements OnInit {
  final SampleApi _sampleApi;
  final GatewayApi _gatewayApi;

  List<StripSample> samples = [];

  CustomerSamplesComponent(this._sampleApi, this._gatewayApi);

  @override
  Future<Null> ngOnInit() async {
    var customerId = "123";
    // _routeParams.get('id');
    if (customerId != null) {
      loadSamples(customerId);
    }
  }

  Future<Null> loadSamples(String customerId) async {
    samples = await _sampleApi.getSamples(customerId);
  }

//  Future<Null> downloadSample(String userId, String sampleId) async {
//  }

  Future<Null> downloadSamplesZip() async {
    var list = samples.map((s) => new UserSampleId(s.customerId, s.id)).toList();
    await _gatewayApi.downloadSamplesZip(list);
  }
}