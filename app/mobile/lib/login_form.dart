// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'dart:convert';

import 'package:app_facade/app_facade.dart';
import 'package:app_mobile/utils/dependency_injection.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:flutter/services.dart';

class LoginForm extends StatefulWidget {
  const LoginForm({ Key key }) : super(key: key);

  static GlobalKey<FormFieldState<String>> emailFieldKey = new GlobalKey<FormFieldState<String>>();
  static GlobalKey<FormFieldState<String>> passwordFieldKey = new GlobalKey<FormFieldState<String>>();
  static GlobalKey<FormFieldState<String>> loginButtonKey = new GlobalKey<FormFieldState<String>>();
  static GlobalKey<FormFieldState<String>> snackBarKey = new GlobalKey<FormFieldState<String>>();
  static const String routeName = '/';

  @override
  LoginFormState createState() => new LoginFormState();
}

class LoginData {
  String email = '';
  String password = '';
}

class LoginFormState extends State<LoginForm> {
  final GlobalKey<ScaffoldState> _scaffoldKey = new GlobalKey<ScaffoldState>();

  LoginData loginData = new LoginData();

  UserApi _userApi;

  void showInSnackBar(String value) {
    print(value);
    _scaffoldKey.currentState.showSnackBar(new SnackBar(
        key: LoginForm.snackBarKey,
        content: new Text(value)
    ));
  }

  bool _autovalidate = false;
  bool _formWasEdited = false;
  final GlobalKey<FormState> _formKey = new GlobalKey<FormState>();


  @override
  void initState() {
    super.initState();
    _userApi = new Injector().userApi;
  }

  Future<Null> _handleSubmitted() async {
    final FormState form = _formKey.currentState;
    if (!form.validate()) {
      _autovalidate = true;  // Start validating on every change.
      showInSnackBar('Please fix the errors in red before submitting.');
    } else {
      form.save();
      login();
    }
  }

  Future<Null> login() async {
    try {
      await _userApi.login(loginData.email, loginData.password);
      Navigator.popAndPushNamed(context, '/user');
    } catch (e) {
      showInSnackBar(e.toString());
    }
  }

  String _validateEmail(String value) {
    _formWasEdited = true;
    if (value.isEmpty)
      return 'Name is required.';
//    final RegExp nameExp = new RegExp(r'^[A-Za-z ]+$');
//    if (!nameExp.hasMatch(value))
//      return 'Please enter only alphabetical characters.';
    return null;
  }

  String _validatePassword(String value) {

    _formWasEdited = true;
    final FormFieldState<String> passwordField = LoginForm.passwordFieldKey.currentState;
    if (passwordField.value == null || passwordField.value.isEmpty)
      return 'Please choose a password.';
    if (passwordField.value != value)
      return 'Passwords don\'t match';
    return null;
  }

  Future<bool> _warnUserAboutInvalidData() async {
    final FormState form = _formKey.currentState;
    if (form == null || !_formWasEdited || form.validate())
      return true;

    return await showDialog<bool>(
      context: context,
      child: new AlertDialog(
        title: const Text('This form has errors'),
        content: const Text('Really leave this form?'),
        actions: <Widget> [
          new FlatButton(
            child: const Text('YES'),
            onPressed: () { Navigator.of(context).pop(true); },
          ),
          new FlatButton(
            child: const Text('NO'),
            onPressed: () { Navigator.of(context).pop(false); },
          ),
        ],
      ),
    ) ?? false;
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      key: _scaffoldKey,
      appBar: new AppBar(
        title: const Text('Qurasense'),
      ),
      body: new Form(
          key: _formKey,
          autovalidate: _autovalidate,
          onWillPop: _warnUserAboutInvalidData,
          child: new ListView(
            padding: const EdgeInsets.symmetric(horizontal: 16.0),
            children: <Widget>[
              new TextFormField(
                key: LoginForm.emailFieldKey,
//                initialValue: "_admin@qurasense.com",
                decoration: const InputDecoration(
                  icon: const Icon(Icons.person),
                  hintText: 'Your email',
                  labelText: 'Email *',
                ),
                onSaved: (String value) { loginData.email = value; },
                validator: _validateEmail,
              ),
              new TextFormField(
//                initialValue: "secret",
                key: LoginForm.passwordFieldKey,
                decoration: const InputDecoration(
                  icon: const Icon(Icons.security),
                  hintText: 'Your password',
                  labelText: 'Password *',
                  ),
                obscureText: true,
                onSaved: (String value) { loginData.password = value; },
                validator: _validatePassword,
              ),
              new Container(
                key: LoginForm.loginButtonKey,
                padding: const EdgeInsets.all(20.0),
                alignment: const FractionalOffset(0.5, 0.5),
                child: new RaisedButton(
                  child: const Text('SUBMIT'),
                  onPressed: _handleSubmitted,
                ),
              ),
              new Container(
                padding: const EdgeInsets.only(top: 20.0),
                child: new Text('* indicates required field', style: Theme.of(context).textTheme.caption),
              ),
            ],
          )
      ),
    );
  }
}