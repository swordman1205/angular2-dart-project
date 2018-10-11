package com.qurasense.healthApi.sample.repository;

import java.util.List;

import com.qurasense.healthApi.sample.model.Sample;
import com.qurasense.healthApi.sample.model.StripSample;

public interface SampleRepository {
    List<Sample> fetchAll(String userId);

    StripSample find(String sampleId);

    List<Sample> fetchByIds(List<String> sampleIds);

    void updatePullPicture(String blobName, String sampleId);

    void updateRemovePicture(String blobName, String sampleId);

    StripSample findLatestNotFinished(String userId);

}
