// Copyright (c) 2017, Qurasense. All rights reserved.

part of app_facade;

class AssayApiMock implements AssayApi {
  Map<String, Assay> _assays = {};

  @override
  Future<Assay> getAssayById(String id) async {
    return _assays.values.firstWhere((a) => a.id == id, orElse: () => null);
  }

  @override
  Future<Iterable<Assay>> getAssays() async {
    return _assays.values;
  }

  @override
  Future<String> saveAssay(Assay assay) async {
    if(assay.id == null) {
      assay.id = _nextUid();
    }
    _assays[assay.name] = assay;
    return assay.id;
  }
}