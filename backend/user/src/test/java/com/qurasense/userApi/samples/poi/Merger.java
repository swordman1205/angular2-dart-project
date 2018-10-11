package com.qurasense.userApi.samples.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Merger {

    public static void main(String[] args) throws IOException, InvalidFormatException {
        File output = new File("C:\\Work\\qurasense\\tmp\\NewXlsfileWhereDataWillBeMerged.xlsx");
        XSSFWorkbook workbook = MergeMultipleXlsFilesInDifferentSheet.mergeExcelFiles(output, "C:\\Work\\qurasense\\tmp\\sheetmerge");
        FileOutputStream fileOutputStream = new FileOutputStream(output);
        try {
            workbook.write(fileOutputStream);
        } finally {
            fileOutputStream.flush();
            fileOutputStream.close();
        }
    }

}
