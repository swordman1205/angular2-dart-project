import 'package:angular/angular.dart';

@Pipe('shortId')
class ShortIdPipe extends PipeTransform {

  String transform(String value) {
    if (value == null) {
      return null;
    }
    if (value.length < 8) {
      return value;
    }
    return value.substring(0, 8);
  }

}