// Copyright (c) 2017, Qurasense. All rights reserved.
import 'dart:html';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_web/src/pages/site/register/signup/signup_component.dart';
import 'package:app_web/src/widgets/trial_process/trial_process_component.dart';

@Component(
  selector: 'register',
  host: const {'class': 'page-component'},
  styleUrls: const ['register_component.css'],
  styles: const [''' 
  '''],
  templateUrl: 'register_component.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    ROUTER_DIRECTIVES,
    TrialProcessComponent
  ],
)
@RouteConfig(const [
  const Route(path: '/...', name: 'Signup', component: SignupComponent)
])
class RegisterComponent implements OnInit, OnDestroy {
  ngOnInit() {
    String className = querySelector('body').className;
    if (className.isEmpty) {
      querySelector('body').className = 'register-container';
    } else {
      querySelector('body').className = '${className} register-container';
    }
  }

  ngOnDestroy() {
    String className = querySelector('body').className;
    if (className == 'register-container') {
      querySelector('body').className = '';
    } else {
      if (className.endsWith('register-container')) {
        querySelector('body').className = className.replaceAll(' register-container', '');
      } else {
        querySelector('body').className = className.replaceAll('register-container ', '');
      }
    }
  }
}
