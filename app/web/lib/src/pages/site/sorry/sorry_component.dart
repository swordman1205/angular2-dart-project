// Copyright (c) 2017, Qurasense. All rights reserved.

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:app_web/src/pages/site/widgets/site-content/site_content_component.dart';
import 'package:app_web/src/utils/contact_utils.dart';

@Component(
  selector: 'sorry',
  host: const {'class': 'page-component'},
  styleUrls: const ['sorry_component.css'],
  templateUrl: 'sorry_component.html',
  directives: const [
    CORE_DIRECTIVES,
    ROUTER_DIRECTIVES,
    formDirectives,
    materialDirectives,
    SiteContentComponent,
  ],
)
class SorryComponent {
  String errorMessage = "";
  bool submitted = false;
  ControlGroup emailForm;

  String emailValue = '';

  SorryComponent() {
    emailForm = new FormBuilder().group({
      "email": ["", ContactUtils.validateEmail],
    });
  }

  subscribe() {
    submitted = true;
    if (!emailForm.valid) return;
    errorMessage = "";
  }
}
