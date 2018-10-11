// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/directives/row_hover_directive.dart';
import 'package:app_web/src/pages/admin/organizations/create_organization/create_organization_component.dart';
import 'package:app_web/src/pipes/enum_pipe.dart';

@Component(
  selector: 'organizations',
  host: const {'class': 'page-component'},
  styleUrls: const ['organizations_component.css'],
  templateUrl: 'organizations_component.html',
  directives: const [
    RowHoverDirective,
    CORE_DIRECTIVES,
    materialDirectives,
    ROUTER_DIRECTIVES,
    formDirectives,
    CreateOrganizationComponent,
  ],
  pipes: const [COMMON_PIPES, EnumPipe],
)
class OrganizationsComponent implements OnInit {
  final AccountApi _accountApi;
  String action;
  Iterable<Organization> organizations;

  OrganizationsComponent(this._accountApi);

  @override
  ngOnInit() async {
    organizations = await _accountApi.getOrganizations();
  }

  void createOrganization() {
    action = "CREATE";
  }

  Future<Null> onAction(bool result) async {
    print("executed $action with result $result");
    if(result) {
      organizations = await _accountApi.getOrganizations();
    }
    action = null;
  }
}
