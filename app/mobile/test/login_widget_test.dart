import 'package:app_facade/app_facade.dart';
import 'package:app_mobile/login_form.dart';
import 'package:app_mobile/utils/dependency_injection.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  testWidgets('login widget test', (WidgetTester tester) async {
    new Injector(backendType: BackendType.MOCK);

    Widget testWidget = new MediaQuery(
        data: new MediaQueryData(),
        child: new MaterialApp(home: new LoginForm())
    );

    // Tells the tester to build a UI based on the widget tree passed to it
    await tester.pumpWidget(
      testWidget
    );

    await tester.enterText(find.byKey(LoginForm.emailFieldKey), "_admin@qurasense.com");
    await tester.enterText(find.byKey(LoginForm.passwordFieldKey), "secret");
    await tester.tap(find.byKey(LoginForm.loginButtonKey));

    await tester.pump();// begin animation

    var text = find.text('Sorry, only customer can login from mobile device. [Mock]', skipOffstage:  false);
    print('found text is: $text');
    expect(text, findsOneWidget);
  });
}