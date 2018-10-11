package com.qurasense.labApi.assay.service;

import java.util.List;

import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.labApi.assay.model.Assay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssayServiceImpl implements AssayService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EntityRepository repository;

    @Autowired
    private IdGenerationStrategy idGenerationStrategy;

    @Override
    public String createAssay(Assay assay) {
        /*if(repository.findByName(Assay.class, assay.getName()) != null) {
            throw new RuntimeException("biomarker with name " + assay.getName() + " already exists");
        }*/
        if (assay.getId() == null) {
            assay.setId(idGenerationStrategy.generate(null));
        }
        repository.save(assay);
        return assay.getId();
    }

    @Override
    public void updateAssay(Assay assay) {
        throw new UnsupportedOperationException("TODO not implemented");
    }

    @Override
    public List<Assay> getAssays() {
        return repository.findAll(Assay.class);
    }

    @Override
    public Assay findAssay(String id) {
        return repository.findById(Assay.class, id);
    }

}
