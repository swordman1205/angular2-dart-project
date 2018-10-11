package com.qurasense.healthApi.repository;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.qurasense.common.repository.local.LocalCustomRepository;
import com.qurasense.healthApi.sample.model.Sample;
import com.qurasense.healthApi.sample.model.SampleStatus;
import com.qurasense.healthApi.sample.model.StripSample;
import com.qurasense.healthApi.sample.repository.SampleRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LocalSampleRepository extends LocalCustomRepository<StripSample> implements SampleRepository {

    @Override
    protected Class<StripSample> getEntityClass() {
        return StripSample.class;
    }

    @Override
    public List<Sample> fetchAll(String userId) {
        return getValues().stream().filter(s-> Objects.equals(userId, s.getCreateUserId())).collect(Collectors.toList());
    }

    @Override
    public StripSample find(String sampleId) {
        return findById(sampleId);
    }

    @Override
    public List<Sample> fetchByIds(List<String> sampleIds) {
        return sampleIds.stream().map(this::findById).collect(Collectors.toList());
    }

    @Override
    public void updatePullPicture(String blobName, String sampleId) {
        StripSample stripSample = find(sampleId);
        stripSample.setStripRemovePictureBlobName(blobName);
        stripSample.setStripRemovePictureTime(new Date());
    }

    @Override
    public void updateRemovePicture(String blobName, String sampleId) {
        StripSample stripSample = find(sampleId);
        stripSample.setPadRemovePictureBlobName(blobName);
        stripSample.setPadRemovePictureTime(new Date());
    }

    @Override
    public StripSample findLatestNotFinished(String userId) {
        return getValues().stream()
                .filter(s->Objects.equals(s.getCreateUserId(), userId))
                .filter(s->Objects.equals(s.getStatus(), SampleStatus.INPROGRESS))
                .sorted((s1, s2)->s1.getCreateTime().compareTo(s2.getCreateTime()))
                .findFirst()
                .orElse(null);
    }

}
