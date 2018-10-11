import 'package:app_facade/app_facade.dart';


main() {
  var now = new DateTime.now();

  var time  = TimeUtils.formatTime12(now);
  print("time is $time");

  var recreated = TimeUtils.parseTime12(time);
  print(recreated);
}

