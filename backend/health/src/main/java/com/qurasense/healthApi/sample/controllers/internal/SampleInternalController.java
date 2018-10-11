package com.qurasense.healthApi.sample.controllers.internal;

import com.qurasense.common.shared.SampleShare;
import com.qurasense.healthApi.sample.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/internal")
@ApiIgnore
public class SampleInternalController {

    @Autowired
    private SampleService sampleService;

    @RequestMapping(value = "/sample", method = RequestMethod.GET)
    public SampleShare getSampleShare(@RequestParam String sampleId) {
        return sampleService.findSampleShare(sampleId);
    }

}
