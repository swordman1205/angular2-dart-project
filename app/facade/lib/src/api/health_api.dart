// Copyright (c) 2017, Qurasense. All rights reserved.
part of app_facade;

/**
 * Service facade for health_api
 */
abstract class HealthApi {

  Future<Null> submitLastPeriodDate(String userId, DateTime lastPeriodDate);

  Future<DateTime> getLastPeriodDate(String userId);

  Future<HealthInfo> getHealthInfo(String userId);

  Future<Null> saveHealthInfo(HealthInfo healthInfo);

  Future<Null> createCard(HealthCard card);

  Future<Iterable<HealthCard>> getCards(String userId);

  Future<String> saveHealthRecord(HealthRecord healthRecord);

  Future<HealthRecord> getHealthRecordByCustomerUserId(String userId);
}