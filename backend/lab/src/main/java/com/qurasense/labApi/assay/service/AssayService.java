package com.qurasense.labApi.assay.service;

import java.util.List;

import com.qurasense.labApi.assay.model.Assay;
import org.springframework.security.access.prepost.PreAuthorize;

public interface AssayService {
    @PreAuthorize("hasAuthority('ADMIN')")
    String createAssay(Assay assay);

    @PreAuthorize("hasAuthority('ADMIN')")
    void updateAssay(Assay assay);

    @PreAuthorize("hasAuthority('ADMIN')")
    List<Assay> getAssays();

    @PreAuthorize("hasAuthority('ADMIN')")
    Assay findAssay(String id);
}
