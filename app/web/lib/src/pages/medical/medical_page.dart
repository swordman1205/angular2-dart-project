// Copyright (c) 2017, Qurasense. All rights reserved.

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_web/src/pages/medical/account/medical_account_component.dart';
import 'package:app_web/src/pages/medical/customers/customer_detail/customer_detail_component.dart';
import 'package:app_web/src/pages/medical/customers/customers_component.dart';
import 'package:app_web/src/pages/medical/sessions/sample_detail/sample_detail_component.dart';
import 'package:app_web/src/pages/medical/sessions/session_detail/session_detail_component.dart';
import 'package:app_web/src/pages/medical/sessions/sessions_component.dart';

@Component(
  selector: 'medical',
  host: const {'class': 'page'},
  styleUrls: const ['../page_internal.css', 'medical_page.css'],
  styles: const ['''
    .page-header {
      background-color: #FD5755;
    }
  '''],
  templateUrl: 'medical_page.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    ROUTER_DIRECTIVES,
  ]
)
@RouteConfig(const [
  const Route(path: '/account', name: 'Account', component: MedicalAccountComponent),
  const Route(path: '/customers', name: 'Customers', component: CustomersComponent),
  const Route(path: '/customers/:id', name: 'CustomerDetail', component: CustomerDetailComponent),
  const Route(path: '/sessions', name: 'Sessions', component: SessionsComponent),
  const Route(path: '/sessions/:id', name: 'SessionDetail', component: SessionDetailComponent),
  const Route(path: '/samples/:id', name: 'SampleDetail', component: SampleDetailComponent),
])
class MedicalPage {

}
