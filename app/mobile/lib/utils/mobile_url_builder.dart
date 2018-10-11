import 'package:app_facade/app_facade.dart';

class MobileUrlBuilder extends UrlBuilder {

  String prefix;

  MobileUrlBuilder(this.prefix);

  @override
  String buildUrl(String aPath) {
    if (aPath.startsWith("/")) {
      return "$prefix$aPath";
    } else {
      return "$prefix/$aPath";
    }
  }
}