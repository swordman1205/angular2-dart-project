// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'dart:html';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/widgets/camera_button/camera_button_component.dart';
import 'package:app_web/src/widgets/choice_selector/choice_selector_component.dart';
import 'package:app_web/src/widgets/date_selector/date_selector_component.dart';
import 'package:app_web/src/widgets/loader_button/busy_button_component.dart';
import 'package:app_web/src/widgets/time_selector/time_picker_component.dart';
import 'package:app_web/src/widgets/wizard/wizard_component.dart';
import 'package:http/http.dart';
import 'package:http_parser/http_parser.dart';


@Component(
  selector: 'sample-step2',
  host: const {'class': 'wizard-component'},
  styleUrls: const [''],
  templateUrl: 'sample_step2_component.html',
  directives: const [
    CORE_DIRECTIVES,
    formDirectives,
    materialDirectives,
    CameraButtonComponent,
    TimeSelectorComponent,
    ChoiceSelectorComponent,
    DateSelectorComponent,
    BusyButtonComponent,
    ROUTER_DIRECTIVES,
  ],
)
class SampleStep2Component extends WizardComponent implements OnInit {
  final SampleApi _sampleApi;
  final Router _router;
  final SecurityService _securityService;

  List<EnumWrapper> howMuchBleedOptions = Enums.bleedIntensity;
  List<EnumWrapper> howMuchBleedSelectedOptions = new List();

  String padRemoveDate;
  String padRemoveTime;

  bool showLostPadModal = false;
  bool showRemovePictureQuestion = false;
  bool showPullQuestion = false;
  bool showThankYouInfo = false;

  CameraResult removePicture;
  CameraResult pullPicture;

  Future removeFuture;
  Future pullFuture;

  SampleStep2Component(this._sampleApi, this._router, this._securityService);

  @Input()
  StripSample sampleData;

  @override
  ngOnInit() {
    if (sampleData.bleedIntensity != null) {
      howMuchBleedSelectedOptions.add(Enums.bleedIntensity.firstWhere((ew) => ew.name == sampleData.bleedIntensity));
    }

    if (sampleData.bleedIntensity != null) {
      var enumWrapper = Enums.bleedIntensity.firstWhere((ew) => ew.name == sampleData.bleedIntensity);
      howMuchBleedSelectedOptions = [enumWrapper];
    }
    navigateToStep('is-pad-saturated');
  }

  void onStepChange(String stepName) {
    if (stepName == 'time-pad-remove') {
      //update padRemoveTime each time we open step
      if(sampleData.stepNumber < 3) {
        sampleData.padRemoveTime = new DateTime.now();
      }
      padRemoveDate = TimeUtils.formatDate(sampleData.padRemoveTime);
      padRemoveTime = TimeUtils.formatTime12(sampleData.padRemoveTime);
    }
  }

  @override
  Future<Null> confirm() async {
    if (howMuchBleedSelectedOptions.isNotEmpty) {
      sampleData.bleedIntensity = howMuchBleedSelectedOptions.first.name;
    }
    sampleData.padRemoveTime = DateTime.parse(padRemoveDate);
    sampleData.padRemoveTime = TimeUtils.adjustTime12(padRemoveTime, sampleData.padRemoveTime);
    if (sampleData.stepNumber < 3) {
      sampleData.stepNumber = 3;
    }
    await _sampleApi.saveStripSample(sampleData);
    super.confirm();
  }

  @override
  Future<bool> isStepCompleted(String stepName) async {
    bool valid = false;
    switch(stepName) {
      case "remove-take-picture":
        valid = sampleData.padRemovePictureBlobName != null;
        break;
      case "pull-sample-out-of-pad":
        valid = sampleData.stripRemovePictureBlobName != null;
        break;
      case "time-pad-remove":
        valid = sampleData.padRemoveTime != null && sampleData.padRemoveTime.year != -1;
        break;
      default:
        valid = true;
        break;
    }
    return valid;
  }

  void previewRemovePicture(CameraResult removePictureCameraResult) {
    removePicture = removePictureCameraResult;
  }
  
  Future<Null> uploadRemovePicture() async {
    var fileReader = new FileReader();
    var currentUserId = await _securityService.getCurrentUserId();
    var completer = new Completer();
    removeFuture = completer.future;

    fileReader.onLoad.listen((e) {
      var target = e.target as dynamic;
      List<int> result = target.result;
      MultipartFile mf = new MultipartFile.fromBytes(
          "file",
          result,
          filename: removePicture.imageFile.name,
          contentType: new MediaType.parse(removePicture.imageFile.type));
      Future<String> uploadRemovePictureFuture = _sampleApi.uploadRemovePicture(currentUserId, sampleData.id, mf);
      uploadRemovePictureFuture.then((blobName) {
        sampleData.padRemovePictureBlobName = blobName;
        completer.complete(null);
        navigateToNext();
      });
    });
    fileReader.readAsArrayBuffer(removePicture.imageFile);
  }

  void previewPullPicture(CameraResult pullPictureCameraResult) {
    pullPicture = pullPictureCameraResult;
  }

  Future<Null> uploadPullPicture() async {
    var fileReader = new FileReader();
    var currentUserId = await _securityService.getCurrentUserId();
    var completer = new Completer();
    pullFuture = completer.future;

    fileReader.onLoad.listen((e) {
      var target = e.target as dynamic;
      List<int> result = target.result;
      MultipartFile mf = new MultipartFile.fromBytes(
          "file",
          result,
          filename: pullPicture.imageFile.name,
          contentType: new MediaType.parse(pullPicture.imageFile.type));
      Future<String> uploadRemovePictureFuture = _sampleApi.uploadPullPicture(currentUserId, sampleData.id, mf);
      uploadRemovePictureFuture.then((blobName) {
        sampleData.stripRemovePictureBlobName = blobName;
        completer.complete();
        navigateToNext();
      });
    });
    fileReader.readAsArrayBuffer(pullPicture.imageFile);
  }

  void toFeedback() {
    _router.navigate([
      '../Lostpad'
    ]);
  }
}

