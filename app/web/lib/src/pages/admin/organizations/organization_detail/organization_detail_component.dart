// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/pipes/enum_pipe.dart';
import 'package:app_web/src/widgets/account/account_component.dart';

@Component(
  selector: 'organization-detail',
  host: const {},
  styleUrls: const [''],
  styles: const ['''
  '''],
  templateUrl: 'organization_detail_component.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
  ],
  pipes: const [EnumPipe]
)
class OrganizationDetailComponent implements OnInit {
  final RouteParams _routeParams;
  final AccountApi _accountApi;
  Organization organization;

  OrganizationDetailComponent(this._routeParams, this._accountApi);

  @override
  Future<Null> ngOnInit() async {
    var id = _routeParams.get('id');
    if (id != null) organization = await (_accountApi.getOrganizationById(id));
  }
}
