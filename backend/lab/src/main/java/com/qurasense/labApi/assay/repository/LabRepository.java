package com.qurasense.labApi.assay.repository;


import java.util.List;

import com.qurasense.labApi.assay.model.Assay;

public interface LabRepository {
    Assay getAssayById(String id);

//    Assay getAssayByName(String name);

    List<Assay> getAssays();

    void saveAssay(Assay assay);
}
