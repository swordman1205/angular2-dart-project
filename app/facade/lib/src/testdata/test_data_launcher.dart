import 'dart:async';

import 'package:app_facade/app_facade.dart';
import 'package:app_facade/src/testdata/test_data_creator.dart';
import 'package:args/args.dart';

Future<Null> main(List<String> arguments) async {
  print("executing test launcher at: ${new DateTime.now()}");
  final parser = new ArgParser();
  parser.addOption("url", abbr: 'u');
  parser.addOption("backend", abbr: 'b', defaultsTo: "PRODUCTION");
  ArgResults argResults = parser.parse(arguments);
  BackendType backendType = BackendType.values.firstWhere((b) => b.toString().endsWith(argResults["backend"]));
  print("url: ${argResults["url"]}, backend type: ${backendType}");

  if (argResults.wasParsed("url")) {
    TestDataCreator.createTestData(backendType,argResults["url"]);
  } else if (backendType == BackendType.MOCK) {
    TestDataCreator.createTestData(backendType);
  } else {
    throw "-u [url] is mandatory for EMULATOR and PRODUCTION backend";
  }
}