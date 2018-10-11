package com.qurasense.gateway.zip;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipBuilder {

    private final ByteArrayOutputStream baos;
    private final ZipOutputStream zipOut;

    private ZipBuilder() throws FileNotFoundException {
        baos = new ByteArrayOutputStream();
        zipOut = new ZipOutputStream(baos);
    }

    public static ZipBuilder create() throws FileNotFoundException {
        return new ZipBuilder();
    }

    public void addFile(byte[] fileBytes, ZipSampleController.SampleAndUserId sampleAndUserId, String fileName) throws IOException {
        String fullName = String.format("sample-%s-user-%s/%s", sampleAndUserId.getSampleId(),
                sampleAndUserId.getUserId(), fileName);
        ZipEntry zipEntry = new ZipEntry(fullName);
        zipOut.putNextEntry(zipEntry);
        zipOut.write(fileBytes);
    }

    public void addFile(byte[] fileBytes, String fileName) throws IOException {
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        zipOut.write(fileBytes);
    }

    public byte[] build() throws IOException {
        try {
            zipOut.flush();
        } finally {
            zipOut.close();
            baos.close();
        }
        return baos.toByteArray();
    }
}
