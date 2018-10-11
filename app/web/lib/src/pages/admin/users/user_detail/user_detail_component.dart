// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';
import 'package:app_web/src/widgets/account/account_component.dart';

@Component(
  selector: 'user-detail',
  host: const {},
  styleUrls: const [''],
  styles: const ['''
  '''],
  templateUrl: 'user_detail_component.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    AccountComponent,
  ],
)
class UserDetailComponent implements OnInit {
  final RouteParams _routeParams;
  final UserApi _userApi;
  User user;

  UserDetailComponent(this._routeParams, this._userApi);

  @override
  Future<Null> ngOnInit() async {
    var _id = _routeParams.get('id');
    if (_id != null) user = await (_userApi.getUser(_id));
  }
}
