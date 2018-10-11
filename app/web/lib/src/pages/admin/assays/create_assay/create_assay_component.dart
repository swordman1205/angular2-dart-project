// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'package:app_facade/app_facade.dart';
import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';

@Component(
  selector: 'create-assay',
  //styleUrls: const ['assays_component.css'],
  templateUrl: 'create_assay_component.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    formDirectives,
    ROUTER_DIRECTIVES,
  ],
  pipes: const [COMMON_PIPES],
)
class CreateAssayComponent implements OnInit {
  final AssayApi _assayApi;
  ControlGroup form;

  final _resultEmitter = new StreamController<bool>();
  @Output()
  Stream<bool> get onAction => _resultEmitter.stream;

  CreateAssayComponent(this._assayApi);

  @override
  ngOnInit() {
    form = new FormBuilder().group({
      "name": [null, Validators.compose([
        Validators.required,
        Validators.minLength(2)])],
      "range": new FormBuilder().group({
        "min": [null, Validators.required],
        "max": [null, Validators.required],
      }),
    });
  }

  onSubmit() async {
    var assay = prepareAssay();
    var id = await _assayApi.saveAssay(assay);
    assay.id = id;
    print("created assay ${assay.name} with id $id");
    _resultEmitter.add(true);
  }

  Assay prepareAssay() {
    var formModel = form.value;
    var assay = new Assay();
    assay.name = formModel["name"];
    assay.rangeMin = formModel["range"]["min"];
    assay.rangeMax = formModel["range"]["max"];
    return assay;
  }
}

