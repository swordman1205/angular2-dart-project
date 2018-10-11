package com.qurasense.healthApi.sample.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String store(String userId, MultipartFile file);

    Resource loadAsResource(String filename);

    void delete(String filename);

    public static class StorageFileNotFoundException extends RuntimeException {
    }

}