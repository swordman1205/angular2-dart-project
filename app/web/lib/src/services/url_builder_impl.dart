import 'package:angular/di.dart';
import 'package:app_facade/app_facade.dart';

@Injectable()
class EmulatorUrlBuilder implements UrlBuilder {
  @override
  String buildUrl(String aPath) {
    return "http://localhost:8080$aPath";
  }
}

@Injectable()
class ProductionUrlBuilder implements UrlBuilder {
  @override
  String buildUrl(String aPath) {
    return aPath;
  }
}