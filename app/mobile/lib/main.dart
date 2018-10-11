// Copyright (c) 2017, Qurasense. All rights reserved.

library stocks;

import 'package:app_facade/app_facade.dart';
import 'package:app_mobile/login_form.dart';
import 'package:app_mobile/user_home.dart';
import 'package:app_mobile/user_types.dart';
import 'package:app_mobile/utils/dependency_injection.dart';
import 'package:flutter/material.dart';

class UserApp extends StatefulWidget {
  @override
  UserAppState createState() => new UserAppState();
}

class UserAppState extends State<UserApp> {

  UserConfiguration _configuration = new UserConfiguration();

  @override
  void initState() {
    super.initState();
  }

  void configurationUpdater(UserConfiguration value) {
    setState(() {
      _configuration = value;
    });
  }

  ThemeData get theme {
    return new ThemeData(
    brightness: Brightness.light,
    primarySwatch: Colors.purple
    );
  }


  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: 'Users',
      theme: theme,
      routes: <String, WidgetBuilder>{
        LoginForm.routeName : (BuildContext context) => new LoginForm(),
        UserHome.routeName: (BuildContext context) => new UserHome()
      },
    );
  }
}

void main() {
  //first call init
  new Injector(backendType: BackendType.MOCK);
  runApp(new UserApp());
}
