import 'package:angular/angular.dart';
import 'package:app_facade/app_facade.dart';

@Pipe('age')
class AgePipe extends PipeTransform {

  String transform(DateTime value) {
    return TimeUtils.durationInYears(value)?.toString();
  }

}