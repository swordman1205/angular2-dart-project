// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'package:app_facade/app_facade.dart';
import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_web/src/directives/row_hover_directive.dart';
import 'package:app_web/src/pages/admin/assays/create_assay/create_assay_component.dart';

@Component(
  selector: 'assays',
  host: const {'class': 'page-component'},
  styleUrls: const ['assays_component.css'],
  templateUrl: 'assays_component.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    formDirectives,
    ROUTER_DIRECTIVES,
    RowHoverDirective,
    CreateAssayComponent,
  ],
  pipes: const [COMMON_PIPES],
)
class AssaysComponent implements OnInit {
  final AssayApi _assayApi;
  String action;
  Iterable<Assay> assays;

  AssaysComponent(this._assayApi);

  void createAssay() {
    action = "CREATE";
  }

  @override
  Future<Null> ngOnInit() async {
    assays = await _assayApi.getAssays();
    action = null;
  }

  Future<Null> onAction(bool result) async {
    print("executed $action with result $result");
    if(result) {
      assays = await _assayApi.getAssays();
    }
    action = null;
  }
}

