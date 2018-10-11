package com.qurasense.healthApi.sample.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.qurasense.common.repository.datastore.DatastoreCustomRepository;
import com.qurasense.healthApi.sample.model.Sample;
import com.qurasense.healthApi.sample.model.SampleStatus;
import com.qurasense.healthApi.sample.model.StripSample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Repository
@Profile({"emulator", "cloud"})
public class SampleRepositoryImpl extends DatastoreCustomRepository<Sample> implements SampleRepository {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<Sample> fetchAll(String userId) {
        logger.info("fetch all samples, user id:{}", userId);
        return ofy().load().type(Sample.class).filter("customerId =", userId).order("-createTime").list();
    }

    @Override
    public StripSample find(String sampleId) {
        return ofy().load().type(StripSample.class).id(sampleId).now();
    }

    @Override
    public List<Sample> fetchByIds(List<String> sampleIds) {
        return new ArrayList<>(ofy().load().type(Sample.class).ids(sampleIds.toArray(new String[]{})).values());
    }

    @Override
    public void updatePullPicture(String blobName, String sampleId) {
        StripSample stripSample = find(sampleId);
        stripSample.setStripRemovePictureBlobName(blobName);
        stripSample.setStripRemovePictureTime(new Date());
        save(stripSample);
    }

    @Override
    public void updateRemovePicture(String blobName, String sampleId) {
        StripSample stripSample = find(sampleId);
        stripSample.setPadRemovePictureBlobName(blobName);
        stripSample.setPadRemovePictureTime(new Date());
        save(stripSample);
    }

    @Override
    public StripSample findLatestNotFinished(String userId) {
        return ofy().load().type(StripSample.class)
                .filter("createUserId =", userId)
                .filter("status =", SampleStatus.INPROGRESS)
                .order("status")
                .order("-createTime")
                .first()
                .now();
    }

}
