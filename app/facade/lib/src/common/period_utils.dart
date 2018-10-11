part of app_facade;

class PeriodUtils {
  static DateTime calculateNextPeriod(HealthInfo healthInfo) {
    var now = TimeUtils.atDayStart(new DateTime.now());
    DateTime nextPeriod = healthInfo.firstDateOfLastPeriod;
    var periodLength = new Duration(days: healthInfo.typicalCycleLength);
    do {
      nextPeriod = nextPeriod.add(periodLength);
    } while (nextPeriod.isBefore(now));
    return nextPeriod;
  }
}