// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';
import 'package:app_facade/app_facade.dart';
import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';
import 'package:angular_forms/angular_forms.dart';
import 'package:angular_router/angular_router.dart';

@Component(
  selector: 'create-organization',
  //styleUrls: const ['assays_component.css'],
  templateUrl: 'create_organization_component.html',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
    formDirectives,
    ROUTER_DIRECTIVES,
  ],
  pipes: const [COMMON_PIPES],
)
class CreateOrganizationComponent implements OnInit {
  final TrialApi _trialApi;
  final AccountApi _accountApi;
  ControlGroup form;

  EnumWrapper selectedType;
  CreateOrganizationComponent(this._trialApi, this._accountApi);
  List<EnumWrapper> types = Enums.organizationType;

  final _resultEmitter = new StreamController<bool>();
  @Output()
  Stream<bool> get onAction => _resultEmitter.stream;

  @override
  ngOnInit() {
    form = new FormBuilder().group({
      "name": [null, Validators.compose([
        Validators.required,
        Validators.minLength(2)])],
    });
  }

  onSubmit() async {
    var organization = prepareOrganization();
    var id = await _accountApi.saveOrganization(organization);
    organization.id = id;
    print("created organization ${organization.name} with id $id");
    _resultEmitter.add(true);
  }

  Organization prepareOrganization() {
    var formModel = form.value;
    var organization = new Organization();
    organization.name = formModel["name"];
    organization.type = selectedType.name;
    return organization;
  }

}

