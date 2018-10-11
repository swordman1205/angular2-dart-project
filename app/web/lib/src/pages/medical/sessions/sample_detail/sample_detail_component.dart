import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/directives/row_hover_directive.dart';
import 'package:app_web/src/pages/medical/customers/customer_trial/customer_trial_component.dart';
import 'package:app_web/src/pipes/age_pipe.dart';
import 'package:app_web/src/pipes/enum_pipe.dart';

@Component(
    selector: 'sample-detail',
    host: const {'class': 'page-component'},
    styleUrls: const ['sample_detail_component.css'],
    templateUrl: 'sample_detail_component.html',
    directives: const [
      CORE_DIRECTIVES,
      formDirectives,
      materialDirectives,
      materialNumberInputDirectives,
      ROUTER_DIRECTIVES,
      RowHoverDirective,
    ],
    pipes: const [COMMON_PIPES, AgePipe, EnumPipe]
)
class SampleDetailComponent implements OnInit {

  final RouteParams _routeParams;
  final TrialApi _trialApi;
  final SampleApi _sampleApi;
  final UserApi _userApi;
  final SecurityService _securityService;
  final UrlBuilder _urlBuilder;

  SampleDevice sample;
  Iterable<Assay> assays = [];

  SampleDetailComponent(this._routeParams, this._trialApi, this._sampleApi,
      this._userApi, this._securityService, this._urlBuilder);

  @override
  ngOnInit() async {
    var sampleId = _routeParams.get('id');
    if (sampleId != null) {
      sample = await _sampleApi.getSampleById(sampleId);
    }
  }

  bool isStrip(SampleDevice sample) {
    return sample is StripSample;
  }

  String createRemoveUrl() {
    return _urlBuilder.buildUrl("/health_api/${sample.customerId}/sample/${sample.id}/removePicture?access_token=${_securityService.getAccessToken()}");
  }

  String createPullUrl() {
    return _urlBuilder.buildUrl("/health_api/${sample.customerId}/sample/${sample.id}/pullPicture?access_token=${_securityService.getAccessToken()}");
  }

}