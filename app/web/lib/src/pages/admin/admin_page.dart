// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/pages/admin/assays/assay_detail/assay_detail_component.dart';
import 'package:app_web/src/pages/admin/assays/assays_component.dart';
import 'package:app_web/src/pages/admin/communications/communications_component.dart';
import 'package:app_web/src/pages/admin/inventory/inventory_component.dart';
import 'package:app_web/src/pages/admin/organizations/organization_detail/organization_detail_component.dart';
import 'package:app_web/src/pages/admin/organizations/organizations_component.dart';
import 'package:app_web/src/pages/admin/trials/trial_detail/trial_detail_component.dart';
import 'package:app_web/src/pages/admin/trials/trials_component.dart';
import 'package:app_web/src/pages/admin/users/user_detail/user_detail_component.dart';
import 'package:app_web/src/pages/admin/users/users_component.dart';

@Component(
  selector: 'admin',
  host: const {'class': 'page'},
  styleUrls: const ['../page_internal.css', 'admin_page.css'],
  templateUrl: 'admin_page.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    ROUTER_DIRECTIVES
  ],
)
@RouteConfig(const [
  const Route(path: '/assays', name: 'Assays', component: AssaysComponent),
  const Route(path: '/assays/:id', name: 'AssayDetail', component: AssayDetailComponent),
  const Route(path: '/communications', name: 'Communications', component: CommunicationsComponent),
  const Route(path: '/inventory', name: 'Inventory', component: InventoryComponent),
  const Route(path: '/organizations', name: 'Organizations', component: OrganizationsComponent),
  const Route(path: '/organizations/:id', name: 'OrganizationDetail', component: OrganizationDetailComponent),
  const Route(path: '/trials', name: 'Trials', component: TrialsComponent),
  const Route(path: '/trials/:id', name: 'TrialDetail', component: TrialDetailComponent),
  const Route(path: '/users', name: 'Users', component: UsersComponent),
  const Route(path: '/users/:id', name: 'UserDetail', component: UserDetailComponent),
])
class AdminPage implements OnInit {
  final GatewayApi _gatewayApi;
  final SecurityService _securityService;
  User currentUser;
  String version;

  AdminPage(this._gatewayApi, this._securityService);

  @override
  Future<Null> ngOnInit() async {
    currentUser = await _securityService.getCurrentUser();
    version = await _gatewayApi.getVersion();
  }
}
