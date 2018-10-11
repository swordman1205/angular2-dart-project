package com.qurasense.labApi.trial.service;

import java.util.Date;
import java.util.List;

import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.labApi.trial.model.Trial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TrialServiceImpl implements TrialService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EntityRepository repository;

    @Autowired
    private IdGenerationStrategy idGenerationStrategy;

    @Override
    public String createTrial(Trial trial) {
//        if(repository.findByName(Trial.class, trial.getName()) != null) {
//            throw new RuntimeException("trial with name " + trial.getName() + " already exists");
//        }
        if (trial.getId() == null) {
            trial.setId(idGenerationStrategy.generate(null));
            trial.setCreateTime(new Date());
            trial.setCreateUserId(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        repository.save(trial);
        return trial.getId();
    }

    @Override
    public void updateTrial(Trial trial) {
        throw new UnsupportedOperationException("TODO not implemented");
    }

    @Override
    public List<Trial> getTrials() {
        return repository.findAll(Trial.class);
    }

    @Override
    public Trial findTrialById(String id) {
        return repository.findById(Trial.class,id);
    }

}
