import 'dart:async';
import 'dart:html' as html;

import 'package:angular/di.dart';
import 'package:app_facade/app_facade.dart';

@Injectable()
class SampleApiImpl extends SampleApiBase implements SampleApi {

  ApiClient _client;

  SampleApiImpl(ApiClient client) : _client = client, super(client);

//  @override
//  Future<Null> downloadSamplePullPicture(String userId, String sampleId) async {
//    // TODO why cant this be done thrugh pure HTTP
//    var future = await html.HttpRequest.request(_client.buildUrl("/health_api/$userId/sample/$sampleId/pullPicture"),
//        method: "GET",
//        requestHeaders: _client.getNoContentWithToken(),
//        responseType: "blob");
//    if (future.status == 200) {
//      print("success download");
//      var blob = new html.Blob([future.response], 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
//      var url = html.Url.createObjectUrlFromBlob(blob);
//      var anchor = new html.AnchorElement();
//      anchor.href = url;
//      anchor.download = 'sample.jpg';
//      anchor.click();
//    }
//    else throw "customers download error";
//  }

}