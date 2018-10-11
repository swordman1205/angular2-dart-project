// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'dart:html';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
//import 'dart:js';


@Component(
  selector: 'camera-button',
  styleUrls: const ['camera_button_component.css'],
  template: '''
    <span>
      <form enctype="multipart/form-data">
          <label class="cameraButton">{{label}}
            <input #cameraInput type="file" accept="image/*;capture=camera"  name="file">
            <!-- file field -->
            <!--<input #cameraInput type="file" accept="image/*;capture=camera"  name="file" id="{{'input' + cameraFormId}}">--> 
            <!-- below directly open camera -->
            <!-- <input type="file" accept="image/*" capture="camera" name="file" id="{{'input' + cameraFormId}}"> -->
          </label>
      </form>
    </span>
    ''',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
  ],
)
class CameraButtonComponent implements AfterViewInit {
  @Input()
  String label;

  @ViewChild("cameraInput")
  ElementRef cameraInputRef;

  final _imageResultRequest = new StreamController<CameraResult>();
  @Output()
  Stream<CameraResult> get imageResult => _imageResultRequest.stream;

  String _lastSelectedHash;

  @override
  ngAfterViewInit() {
    FileUploadInputElement uploadInput = cameraInputRef.nativeElement;
    uploadInput.onChange.listen((e) {
      File file = (e.target as dynamic).files[0];
      String fileName = file.name;
      String fileSize = file.size.toString();
      String hash = '$fileName$fileSize';
      if (_lastSelectedHash != hash) {
        _lastSelectedHash = hash;
        var reader = new FileReader();
        reader.onLoad.listen((e) {
          var target = e.target as dynamic;
          var url =  target.result;
          _imageResultRequest.add(new CameraResult(file, url));
        });
        reader.readAsDataUrl(file);
      }
    });
  }
}

class CameraResult {
  File imageFile;
  String previewUrl;
  CameraResult(this.imageFile, this.previewUrl);
}
