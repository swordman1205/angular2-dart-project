package com.qurasense.labApi.assay.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;

import com.qurasense.labApi.assay.model.Assay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"emulator", "cloud"})
public class LabRepositoryImpl implements LabRepository {
    private final Logger logger = LoggerFactory.getLogger(getClass());

//    @Autowired
//    private Datastore datastore;

    private List<Assay> assays = new ArrayList<>();

    @PostConstruct
    protected void init() {
        // labKeyFactory = datastore.newKeyFactory().setKind(HEALTH_INFO_KIND);
    }

    @Override
    public Assay getAssayById(String id) {
        return assays.stream().filter(a-> Objects.equals(a.getId(), id)).findFirst().get();
    }

//    @Override
//    public Assay getAssayByName(String name) {
//        return assays.stream().filter(a-> Objects.equals(a.getName(), name)).findFirst().orElse(null);
//    }

    @Override
    public List<Assay> getAssays() {
        return assays;
    }

    @Override
    public void saveAssay(Assay assay) {
        if (assay.getId() != null) {
            assays.removeIf(a->Objects.equals(assay.getId(), a.getId()));
        }
        assays.add(assay);
    }
}
