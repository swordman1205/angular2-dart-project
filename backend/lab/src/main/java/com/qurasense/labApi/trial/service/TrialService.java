package com.qurasense.labApi.trial.service;

import java.util.List;

import com.qurasense.labApi.trial.model.Trial;
import org.springframework.security.access.prepost.PreAuthorize;

public interface TrialService {
    @PreAuthorize("hasAuthority('ADMIN')")
    String createTrial(Trial trial);

    @PreAuthorize("hasAuthority('ADMIN')")
    void updateTrial(Trial trial);

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MEDICAL')")
    List<Trial> getTrials();

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MEDICAL')")
    Trial findTrialById(String id);

}
