package com.qurasense.gateway;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipSample {

    public static void main(String[] args) throws IOException {
        try (FileOutputStream fos = new FileOutputStream("multiCompressed.zip");
                ZipOutputStream zipOut = new ZipOutputStream(fos);) {
            byte[] file1 = "file1 content".getBytes();
            ZipEntry zipEntry1 = new ZipEntry("sample1/some1.txt");
            zipOut.putNextEntry(zipEntry1);
            zipOut.write(file1);

            byte[] file2 = "file2 content".getBytes();
            ZipEntry zipEntry2 = new ZipEntry("sample1/some2.txt");
            zipOut.putNextEntry(zipEntry2);
            zipOut.write(file2);
        }


    }

}
