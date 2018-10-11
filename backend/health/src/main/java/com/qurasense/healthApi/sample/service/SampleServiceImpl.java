package com.qurasense.healthApi.sample.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.messaging.AbstractMessagePublisher;
import com.qurasense.common.messaging.broadcast.message.SampleRegistered;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.common.shared.SampleShare;
import com.qurasense.healthApi.sample.model.Sample;
import com.qurasense.healthApi.sample.model.StripSample;
import com.qurasense.healthApi.sample.repository.SampleRepository;
import com.qurasense.healthApi.sample.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Service
public class SampleServiceImpl implements SampleService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private IdGenerationStrategy idGenerationStrategy;

    @Autowired
    private EntityRepository entityRepository;

    @javax.annotation.Resource(name = "broadcastMessagePublisher")
    private AbstractMessagePublisher broadcastMessagePublisher;

    @Override
    public String savePullPicture(String userId, String sampleId, MultipartFile file) {
        StopWatch sw = new StopWatch();
        sw.start();
        String blobName = storageService.store(userId, file);
        sampleRepository.updatePullPicture(blobName, sampleId);
        sw.stop();
        logger.info("updated sample pull picture name for user: {}, sample: {}. Timing: {}", userId, sampleId, sw.prettyPrint());
        return blobName;
    }

    @Override
    public String saveRemovePicture(String userId, String sampleId, MultipartFile file) {
        StopWatch sw = new StopWatch();
        sw.start();
        String blobName = storageService.store(userId, file);
        sampleRepository.updateRemovePicture(blobName, sampleId);
        sw.stop();
        logger.info("updating sample remove picture name for user: {}, sample: {}. Timing: {}", userId, sampleId, sw.prettyPrint());
        return blobName;
    }

    @Override
    public String saveSample(Sample sample) {
        if (Objects.isNull(sample.getId())) {
            sample.setId(idGenerationStrategy.generate(null));
            sample.setCreateTime(new Date());

            entityRepository.save(sample);
            broadcastMessagePublisher.publishMessage(new SampleRegistered(sample.getCustomerId(), sample.getId()));
            return sample.getId();
        } else {
            if (sample.getCreateTime() == null) {
                Sample loadedSample = entityRepository.findById(Sample.class, sample.getId());
                sample.setCreateTime(loadedSample.getCreateTime());
            }
            entityRepository.save(sample);
            return sample.getId();
        }
    }

    @Override
    public List<Sample> fetchSamples(String userId) {
        return sampleRepository.fetchAll(userId);
    }

    @Override
    public StripSample findSample(String sampleId) {
        return sampleRepository.find(sampleId);
    }

    @Override
    public SampleShare findSampleShare(String sampleId) {
        StripSample stripSample = findSample(sampleId);
        SampleShare result = new SampleShare();
        result.setKitId(stripSample.getKitId());
        return result;
    }

    @Override
    public Resource getSamplePullPicture(String aSampleId) {
        StripSample stripSample = sampleRepository.find(aSampleId);
        return storageService.loadAsResource(stripSample.getStripRemovePictureBlobName());
    }

    @Override
    public Resource getSampleRemovePicture(String aSampleId) {
        StripSample stripSample = sampleRepository.find(aSampleId);
        return storageService.loadAsResource(stripSample.getPadRemovePictureBlobName());
    }

    @Override
    public List<Sample> getSamples(String userId) {
        return sampleRepository.fetchAll(userId);
    }

    @Override
    public List<Sample> getSamples(List<String> sampleIds) {
        return sampleRepository.fetchByIds(sampleIds);
    }

    @Override
    public List<StripSample> getSamples() {
        return ofy().load().type(StripSample.class).list();
    }

    @Override
    public StripSample findLatestNotFinished(String userId) {
        return sampleRepository.findLatestNotFinished(userId);
    }

}
