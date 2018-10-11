// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

class EnumWrapper {
  String name;
  String caption;
  EnumWrapper(this.name, this.caption);

  bool operator ==(o) => o is EnumWrapper && name == o.name;
  int get hashCode => name.hashCode;

  @override
  String toString() {
    return "$name:$caption";
  }

}