import 'package:angular/angular.dart';
import 'package:app_facade/app_facade.dart';

@Pipe('enum')
class EnumPipe extends PipeTransform {

  String transform(String value, String enumName) {
    List<EnumWrapper> values = Enums.find(enumName);
    return values.firstWhere((ew) => ew.name == value, orElse: () => null)?.caption;
  }

}