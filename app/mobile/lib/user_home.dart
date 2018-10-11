// Copyright (c) 2017, Qurasense. All rights reserved.

import 'package:flutter/material.dart';

class UserHome extends StatefulWidget {

  static String routeName = '/user';

  @override
  UserHomeState createState() => new UserHomeState();
}

class UserHomeState extends State<UserHome> {

  @override
  Widget build(BuildContext context) {
    return new Material(
      child:  new Column(
          children: <Widget>[
            new Text('Hello user')
          ]
      ),
    );
  }
}