package com.qurasense.healthApi.sample.storage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Profile("emulator")
public class LocalStorageService implements StorageService {

    private Map<String, Resource> inMemoryFiles = new HashMap<>();

    @Override
    public String store(String userId, MultipartFile file) {
        try {
            UUID uuid = UUID.randomUUID();
            ByteArrayResource byteArrayResource = new ByteArrayResource(file.getBytes());
            inMemoryFiles.put(uuid.toString(), byteArrayResource);
            return uuid.toString();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    @Override
    public Resource loadAsResource(String filename) {
        return inMemoryFiles.get(filename);
    }

    @Override
    public void delete(String filename) {
        inMemoryFiles.remove(filename);
    }
}
