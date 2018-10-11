// Copyright (c) 2017, Qurasense. All rights reserved.

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
@Component(
  selector: 'site-content',
  host: const {'class': 'page'},
  styleUrls: const ['site_content_component.css'],
  templateUrl: 'site_content_component.html',
  directives: const [
    CORE_DIRECTIVES,
    ROUTER_DIRECTIVES,
    materialDirectives,
  ],
)
class SiteContentComponent {

}
