// Copyright (c) 2017, Qurasense. All rights reserved.
part of app_facade;

abstract class AssayApi {
  Future<Assay> getAssayById(String id);

  Future<String> saveAssay(Assay assay);

  Future<Iterable<Assay>> getAssays();
}

