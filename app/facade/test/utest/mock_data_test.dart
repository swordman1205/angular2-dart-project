import 'package:app_facade/app_facade.dart';
import 'package:app_facade/src/testdata/test_data_creator.dart';
import "package:test/test.dart";

main() {
  group('mock data - ', () {
    TestDataInjector injector;

    test('create test data', () async {
      injector = await TestDataCreator.createTestData(BackendType.MOCK);
      expect(injector, isNotNull);

      expect(injector.accountApi, isNotNull);
      expect(injector.assayApi, isNotNull);
      expect(injector.consentApi, isNotNull);
      expect(injector.gatewayApi, isNotNull);
      expect(injector.healthApi, isNotNull);
      expect(injector.inventoryApi, isNotNull);
      expect(injector.sampleApi, isNotNull);
      expect(injector.trialApi, isNotNull);
      expect(injector.userApi, isNotNull);
    });

    test('assert assays', () async {
      var assays = (await injector.assayApi.getAssays()).toList();
      expect(assays, hasLength(1));
    });

    test('assert organizations', () async {
      var organizations = (await injector.accountApi.getOrganizations()).toList();
      expect(organizations, hasLength(3));
    });

    test('assert users', () async {
      var users = (await injector.userApi.getUsers()).where((u) => u.role != 'CUSTOMER').toList();
      expect(users, hasLength(4));

      expect(await injector.userApi.checkEmailExist("_admin@qurasense.com"), isTrue);
      expect(await injector.userApi.checkEmailExist("_joel@usspecialtylabs.com"), isTrue);
      expect(await injector.userApi.checkEmailExist("_jdonovan@coastphlebotomy.com"), isTrue);
      expect(await injector.userApi.checkEmailExist("_sara@qurasense.com"), isTrue);
    });

    test('assert trials', () async {
      var trials = (await injector.trialApi.getTrials()).toList();
      expect(trials, hasLength(1));

      var trial = trials.first;
      expect(trial.id, equals(TestDataHolder.TRIAL_SANDIEGO), reason: "San Diego trial is only trial");
    });

    test('assert customers', () async {
      var customers = (await injector.userApi.getUsers()).where((u) => u.role == 'CUSTOMER').toList();
      expect(customers, hasLength(2));
      for (var cu in customers) {
        expect(await injector.healthApi.getHealthRecordByCustomerUserId(cu.id), isNotNull);
        expect(await injector.userApi.getCustomerInfo(cu.id), isNotNull);
      }

      expect(await injector.userApi.checkEmailExist("_marie@qurasense.com"), true);
      expect(await injector.userApi.checkEmailExist("_lise@qurasense.com"), true);
    });

    test('assert trial participants', () async {
      var participants = await injector.trialApi.getTrialParticipants(TestDataHolder.TRIAL_SANDIEGO);
      expect(participants, hasLength(1));

      var participant = participants.first;
      expect(participant.customerId, equals(TestDataHolder.CUSTOMER_MARIE), reason: "maria is particpating in san diego trial");
      expect(participant.status, equals("APPROVED"));
      expect(participant.nurseId, equals(TestDataHolder.USER_NURSE));
    });

    test('assert samples', () async {
      var samples = await injector.sampleApi.getSamples(TestDataHolder.CUSTOMER_MARIE);
      expect(samples, hasLength(1));

      var sample = samples.first as StripSample;
      expect(sample.customerId, equals(TestDataHolder.CUSTOMER_MARIE));
      expect(sample.status, equals("FINISHED"));
    });

    test('assert trial sessions', () async {
      var sessions = await injector.trialApi.getTrialSessions(TestDataHolder.TRIAL_SANDIEGO);
      expect(sessions, hasLength(1));

      var session = sessions.first;
      expect(session.customerId, equals(TestDataHolder.CUSTOMER_MARIE));
      expect(session.status, equals("ACTIVE"));
      expect(session.compensated, equals(false));
      expect(session.sampleIds, hasLength(1));

      var sampleId = session.sampleIds.first;
      expect(sampleId, equals(TestDataHolder.SAMPLE_MARIE));
    });

    test('nurse approve session for pickup', () async {
//      var activeSessions = await injector.trialApi.getSessionsForNurse(TestDataHolder.USER_NURSE, status:"ACTIVE");
//      expect(activeSessions, hasLength(1));
//
//      var session = activeSessions.first;
//      var res = await injector.trialApi.updateSessionsStatus(session.id, "APPROVED");
//      expect(res, isTrue);
    });

    test('nurse pickup samples', () async {

    });

    test('nurse deliver samples to lab', () async {
      // TODO sample ownership change
    });

    test('lab lists samples for analysis', () async {
      // TODO sample ownership change
    });

    test('lab reports sample analysis', () async {
      // TODO sample ownership change
    });
  });
}

