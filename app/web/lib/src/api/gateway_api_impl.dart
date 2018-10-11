// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'dart:convert';
import 'dart:html' as html;

import 'package:angular/di.dart';
import 'package:app_facade/app_facade.dart';

@Injectable()
class GatewayApiImpl extends GatewayApiBase implements GatewayApi {
  final ApiClient _client;

  GatewayApiImpl(ApiClient client) : _client = client, super(client);

  @override
  Future<Null> downloadSamplesZip(List<UserSampleId> userAndSampleIds) async {
    var encoded = JSON.encode(userAndSampleIds);
    // TODO cant we use https://stackoverflow.com/a/47015631 - i.e. just orginary POST with responseType headers
    var future = await html.HttpRequest.request(_client.buildUrl("/aggregate/sample"),
        method: "POST",
        requestHeaders: _client.getJsonContentTypeWithToken(),
        responseType: "blob",
        sendData: encoded);
    if (future.status == 200) {
      print("success samples download");
      var blob = new html.Blob([future.response], 'application/zip');
      var url = html.Url.createObjectUrlFromBlob(blob);
      var anchor = new html.AnchorElement();
      anchor.href = url;
      anchor.download = 'samples.zip';
      anchor.click();
    }
    else throw "samples download error";
  }

  @override
  Future<Null> downloadCustomersAndHealths() async {
    // TODO cant we use https://stackoverflow.com/a/47015631 - i.e. just orginary POST with responseType headers
    var future = await html.HttpRequest.request(_client.buildUrl("/aggregate/customersHealths"),
        method: "POST",
        requestHeaders: _client.getJsonContentTypeWithToken(),
        responseType: "blob");
    if (future.status == 200) {
      print("success samples download");
      var blob = new html.Blob([future.response], 'application/zip');
      var url = html.Url.createObjectUrlFromBlob(blob);
      var anchor = new html.AnchorElement();
      anchor.href = url;
      anchor.download = 'customersHealths.zip';
      anchor.click();
    }
    else throw "customers healths download error";
  }
}