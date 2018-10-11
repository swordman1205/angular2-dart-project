// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_router/angular_router.dart';
import 'package:app_facade/app_facade.dart';

@Component(
  selector: 'send-email-profile-card',
  styleUrls: const ['cards.css'],
  styles: const ['''
  '''],
  template: ''' 
    <div class="card mt-30 mt-xs-15">
      <div class="card-title">Hi {{customerUser.fullName}}</div>
      <div class="card-content">
        <p>Thank you for supporting our quest to advance women health. If you have any questions, or concerns, please send us an e-mail or call us.</p>
      </div>
      <a class="btn btn-purple text-center w-init pl-10 pr-10 mr-5" href="mailto:research@qurasense.com">Send e-mail</a>
      <a class="btn btn-purple text-center w-init pl-10 pr-10" href="tel:+14157028935">Call team</a>
    </div>
  ''',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    ROUTER_DIRECTIVES
  ],
)
class SendEmailProfileCardComponent implements OnInit {

  @Input()
  User customerUser;

  @override
  Future<Null> ngOnInit() async { }
}
