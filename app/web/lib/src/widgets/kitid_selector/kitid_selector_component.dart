// Copyright (c) 2017, Qurasense. All rights reserved.

import 'dart:async';

import 'package:angular/angular.dart';
import 'package:angular_components/angular_components.dart';

@Component(
  selector: 'kitid-selector',
  styles: const ['''
  :host {
      width: 100%;
  }
  material-input {
    max-width: 80px;
  }
  .is-valid { }

  .is-invalid { }
  @media screen and (max-width: 991px) {
    material-input {
      max-width: 100%;
    }
  }
  '''],
  template: ''' 
  <template [ngIf]="kitId != null">
    <div [class.is-valid]="kitId.valid" [class.is-invalid]="!kitId.valid">
        <material-input [(ngModel)]="kitId.id1" label="aaaa"></material-input>
        <material-input [(ngModel)]="kitId.id2" label="bbbb"></material-input>
        <material-input [(ngModel)]="kitId.id3" label="cccc"></material-input>
        <material-input [(ngModel)]="kitId.id4" label="dddd"></material-input>
        <!--div [hidden]="kitId.valid" class="invalid-feedback">
            KitId is required
        </div-->
    </div>
  </template>
  ''',
  directives: const [
    CORE_DIRECTIVES,
    materialDirectives,
  ],
)
class KitIdSelectorComponent implements OnInit, AfterViewChecked {
  KitId kitId;
  String lastValidKitId;

  @Input()
  void set value(String val) {
    kitId = new KitId.fromString(val);
  }

  // output event
  final _valueChanged = new StreamController<String>();
  @Output()
  Stream<String> get valueChanged => _valueChanged.stream;

  @override
  Future<Null> ngOnInit() async {
    if(kitId == null) {
      value = "";
    }
  }

  @override
  ngAfterViewChecked() {
    if(kitId == null) {
      return;
    }
    if(kitId.valid) {
      var currentKitId = kitId.value;
      if (lastValidKitId != currentKitId) {
        // only emit changed values
        _valueChanged.add(currentKitId);
        lastValidKitId = currentKitId;
      }
    }
  }
}

class KitId {
  String id1, id2, id3, id4;

  factory KitId.fromString(String kit) {
    var ids = kit.trim().split("-");
    var defaultIds = ["", "", "", ""];
    var safeLength = ids.length > defaultIds.length ? defaultIds.length : ids.length;
    for(int i=0; i<safeLength; i++) {
      defaultIds[i] = ids[i];
    }
    var kitId = new KitId(defaultIds[0], defaultIds[1], defaultIds[2], defaultIds[3]);
    return kitId;
  }

  KitId(this.id1, this.id2, this.id3, this.id4);

  bool get valid {
    var validValues = [id1,id2,id3,id4].map((String value) => value != null && value.length >= 1);
    return !validValues.contains(false);
  }

  String get value => [id1,id2,id3,id4].join("-");
}
