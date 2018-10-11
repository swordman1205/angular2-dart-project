package com.qurasense.healthApi.sample.controllers;

import java.util.List;
import java.util.Objects;

import com.qurasense.healthApi.sample.model.Sample;
import com.qurasense.healthApi.sample.model.StripSample;
import com.qurasense.healthApi.sample.model.TubeSample;
import com.qurasense.healthApi.sample.service.SampleService;
import com.qurasense.healthApi.sample.storage.StorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@Api(value="customer sample", description="customer sample")
public class SampleController {

    @Autowired
    private SampleService sampleService;

    @GetMapping("/{userId}/sample/{sampleId}/removePicture")
    @ResponseBody
    @ApiIgnore
    public ResponseEntity<Resource> downloadRemovePicture(@PathVariable("userId") String userId,
            @PathVariable("sampleId") String sampleId) {
        Resource file = sampleService.getSampleRemovePicture(sampleId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"remove-" + sampleId + ".jpg\"").body(file);
    }

    @GetMapping("/{userId}/sample/{sampleId}/pullPicture")
    @ResponseBody
    @ApiIgnore
    public ResponseEntity<Resource> downloadPullPicture(@PathVariable("userId") String userId,
            @PathVariable("sampleId") String sampleId) {
        Resource file = sampleService.getSamplePullPicture(sampleId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"pull-" + sampleId + ".jpg\"").body(file);
    }

    @PostMapping("/sample/stripSample")
    @ResponseBody
    public String saveStripSample(@RequestBody StripSample stripSample) {
        return sampleService.saveSample(stripSample);
    }

    @PostMapping("/sample/tubeSample")
    @ResponseBody
    public String saveTubeSample(@RequestBody TubeSample tubeSample) {
        return sampleService.saveSample(tubeSample);
    }

    @ApiOperation(value = "create remove picture")
    @PostMapping("/{userId}/sample/{sampleId}/removePicture")
    @ResponseBody
    public String saveRemovePicture(@PathVariable("userId") String userId, @PathVariable("sampleId") String sampleId,
            @RequestParam("file") MultipartFile file) {
        return sampleService.saveRemovePicture(userId, sampleId, file);
    }

    @ApiOperation(value = "save pull picture")
    @PostMapping("/{userId}/sample/{sampleId}/pullPicture")
    @ResponseBody
    public String savePullPicture(@PathVariable("userId") String userId, @PathVariable("sampleId") String sampleId,
            @RequestParam("file") MultipartFile file) {
        return sampleService.savePullPicture(userId, sampleId, file);
    }

    @ApiOperation(value = "get customer sample list", response = StripSample.class, responseContainer = "List")
    @GetMapping("/samples")
    @ResponseBody
    public List<Sample> getSamples(@RequestParam("customerId") String userId) {
        return sampleService.getSamples(userId);
    }

    @ApiOperation(value = "get customer sample list", response = StripSample.class, responseContainer = "List")
    @GetMapping("/samplesById")
    @ResponseBody
    public List<Sample> getSamples(@RequestParam("sampleId") List<String> sampleIds) {
        return sampleService.getSamples(sampleIds);
    }

    @ApiOperation(value = "get customer sample list", response = StripSample.class, responseContainer = "List")
    @GetMapping("/samples/{sampleId}")
    @ResponseBody
    public StripSample getSample(@PathVariable("sampleId") String sampleId) {
        return sampleService.findSample(sampleId);
    }

    @ExceptionHandler(StorageService.StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageService.StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "get latest not finished sample for user")
    @GetMapping("/{userId}/sample/latestNotFinished")
    @ResponseBody
    public ResponseEntity<StripSample> getLatestNotFinished(@PathVariable("userId") String userId) {
        StripSample latestNotFinished = sampleService.findLatestNotFinished(userId);
        if (Objects.nonNull(latestNotFinished)) {
            return ResponseEntity.ok(latestNotFinished);
        }
        return ResponseEntity.notFound().build();
    }
}