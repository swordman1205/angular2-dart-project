package com.qurasense.healthApi.sample.service;

import java.util.List;

import com.qurasense.common.shared.SampleShare;
import com.qurasense.healthApi.sample.model.Sample;
import com.qurasense.healthApi.sample.model.StripSample;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

public interface SampleService {

    @PreAuthorize("hasAuthority('MEDICAL') or (hasAuthority('CUSTOMER') and isCurrentUser(#userId))")
    String savePullPicture(String userId, String sampleId, MultipartFile file);

    @PreAuthorize("hasAuthority('MEDICAL') or (hasAuthority('CUSTOMER') and isCurrentUser(#userId))")
    String saveRemovePicture(String userId, String sampleId, MultipartFile file);

    @PreAuthorize("hasAuthority('MEDICAL') or (hasAuthority('CUSTOMER') and isCurrentUser(#userId))")
    List<Sample> fetchSamples(String userId);

    @PostAuthorize("hasAuthority('MEDICAL') or (hasAuthority('CUSTOMER') and isCurrentUser(returnObject.customerId))")
    StripSample findSample(String sampleId);

    @PostAuthorize("hasAuthority('MEDICAL')")
    Resource getSamplePullPicture(String aSampleId);

    @PostAuthorize("hasAuthority('MEDICAL')")
    Resource getSampleRemovePicture(String aSampleId);

    @PreAuthorize("hasAuthority('MEDICAL') or (hasAuthority('CUSTOMER') and isCurrentUser(#userId))")
    List<Sample> getSamples(String userId);

    List<Sample> getSamples(List<String> sampleIds);

    @PreAuthorize("hasAuthority('MEDICAL')")
    List<StripSample> getSamples();

    @PreAuthorize("hasAuthority('MEDICAL') or (hasAuthority('CUSTOMER') and isCurrentUser(#userId))")
    StripSample findLatestNotFinished(String userId);

    @PreAuthorize("hasAuthority('CUSTOMER') and isCurrentUser(#sample.customerId)")
    String saveSample(Sample sample);

    SampleShare findSampleShare(String sampleId);


}
