import 'package:app_facade/app_facade.dart';
import "package:test/test.dart";

main() {
  test('time utils format and parse shouldn`t differ more than one minute', () {
    var now = new DateTime.now();
    print(now);

    var time  = TimeUtils.formatDateTime(now);
    expect(time, isNotNull);

    var recreated = TimeUtils.parseDateTime(time);
    print(recreated);
    Duration duration = recreated.difference(now);
    print(duration);
    expect(duration.inMinutes, equals(0));
  });
}

