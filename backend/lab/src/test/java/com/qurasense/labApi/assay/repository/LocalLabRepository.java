package com.qurasense.labApi.assay.repository;

import java.util.List;

import com.qurasense.common.repository.local.LocalCustomRepository;
import com.qurasense.labApi.assay.model.Assay;
import org.springframework.stereotype.Repository;

@Repository
public class LocalLabRepository extends LocalCustomRepository<Assay> implements LabRepository {
    @Override
    protected Class<Assay> getEntityClass() {
        return Assay.class;
    }

    @Override
    public Assay getAssayById(String id) {
        return findById(id);
    }

    @Override
    public List<Assay> getAssays() {
        return findAll();
    }

    @Override
    public void saveAssay(Assay assay) {
        save(assay);
    }

}
