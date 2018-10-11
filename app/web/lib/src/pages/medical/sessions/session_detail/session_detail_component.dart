import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/directives/row_hover_directive.dart';
import 'package:app_web/src/pages/medical/customers/customer_trial/customer_trial_component.dart';
import 'package:app_web/src/pipes/age_pipe.dart';
import 'package:app_web/src/pipes/enum_pipe.dart';
import 'package:app_web/src/pipes/short_id_pipe.dart';

@Component(
    selector: 'session-detail',
    host: const {'class': 'page-component'},
    styleUrls: const ['session_detail_component.css'],
    templateUrl: 'session_detail_component.html',
    directives: const [
      CORE_DIRECTIVES,
      formDirectives,
      materialDirectives,
      materialNumberInputDirectives,
      ROUTER_DIRECTIVES,
      RowHoverDirective,
    ],
    pipes: const [COMMON_PIPES, AgePipe, EnumPipe, ShortIdPipe])
class SessionDetailComponent implements OnInit {
  final RouteParams _routeParams;
  final TrialApi _trialApi;
  final SampleApi _sampleApi;
  final UserApi _userApi;

  TrialSession session;
  Iterable<SampleDevice> samples = [];
  User customer;
  User nurse;

  SessionDetailComponent(
      this._routeParams, this._trialApi, this._sampleApi, this._userApi);

  @override
  ngOnInit() async {
    var sessionId = _routeParams.get('id');
    if (sessionId != null) {
      session = await _trialApi.getSession(sessionId);
      TrialParticipant trialParticipant =
          await _trialApi.getTrialParticipantById(session.trialParticipantId);
      customer = await _userApi.getUser(trialParticipant.customerId);
      nurse = await _userApi.getUser(trialParticipant.nurseId);
      samples = await _sampleApi.getSampleByIds(session.sampleIds);
    }
  }

  bool isStrip(SampleDevice sample) {
    return sample is StripSample;
  }

  String collectionTime(StripSample sample) {
    if (sample.padStartTime == null || sample.padRemoveTime == null) {
      return null;
    }
    Duration durationBetween =
        TimeUtils.durationBetween(sample.padStartTime, sample.padRemoveTime);
    var durationString = durationBetween.toString();
    return durationString.substring(0, durationString.lastIndexOf(":"));
  }

  void showStripPicture(StripSample sample) {}

  void showPadPicture(StripSample sample) {}
}
