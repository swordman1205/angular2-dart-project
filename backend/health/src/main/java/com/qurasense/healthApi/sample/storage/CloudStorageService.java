package com.qurasense.healthApi.sample.storage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.annotation.PostConstruct;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("cloud")
public class CloudStorageService implements StorageService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Storage storage;
    private String bucketName;
    private BucketInfo bucketInfo;

    @PostConstruct
    protected void init() {
        // Instantiates a client
        storage = StorageOptions.getDefaultInstance().getService();
        // Creates the new bucket
        bucketName = String.format("%s-sample-image", StorageOptions.getDefaultInstance().getProjectId());
        bucketInfo = BucketInfo.of(bucketName);
        logger.info("sample image bucket name: {}", bucketName);
//        always 403 if bucket was created this way
//        try {
//            Bucket bucket = storage.create(bucketInfo);
//        } catch (Exception e) {
//            //bucket already exist
//        }
    }

    @Override
    public String store(String userId, MultipartFile file) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("YYYYMMddHHmmssSSS");
        DateTime dt = DateTime.now(DateTimeZone.UTC);
        String dtString = dt.toString(dtf);
        final String fileName = String.format("%s_%s_%s", userId, dtString, file.getOriginalFilename());

        // the inputstream is closed by default, so we don't need to close it here
        BlobInfo blobInfo = null;
        try {
            blobInfo = storage.create(
                    BlobInfo
                            .newBuilder(bucketInfo, fileName)
                            // Modify access list to allow all users with link to read file
//                                .setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER))))
                            .build(),
                    file.getBytes());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
        logger.info("correctly saved:" + blobInfo.getMediaLink());

        return blobInfo.getBlobId().getName();
    }

    @Override
    public Resource loadAsResource(String filename) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ReadChannel reader = storage.reader(BlobId.of(bucketName, filename))) {
            ByteBuffer bytes = ByteBuffer.allocate(64 * 1024);
            while (reader.read(bytes) > 0) {
                bytes.flip();
                baos.write(bytes.array());
                bytes.clear();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return new ByteArrayResource(baos.toByteArray());
    }

    @Override
    public void delete(String filename) {
        storage.delete(BlobId.of(bucketName, filename));
    }

}
