package com.qurasense.healthApi.health.report;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.qurasense.common.MessagesRetriever;
import com.qurasense.healthApi.health.model.HealthInfo;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HealthExcelExporter {

    private final Logger logger = LoggerFactory.getLogger(HealthExcelExporter.class);

    @Autowired
    private MessagesRetriever messagesRetriever;

    private static List<String> COLUMNS = Arrays.asList(
            "User Id",
            "Average period length",
            "Average cycle length",
            "Sexual activity",
            "Birth controls",
            "Weight",
            "Height");

    public XSSFWorkbook export(List<HealthInfo> healthInfos) throws IOException {
        logger.info("Start build report");

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("healthInfos");
        int rowNum = 0;
        //header
        XSSFRow headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(indexOfColumn("User Id")).setCellValue("User Id");
        headerRow.createCell(indexOfColumn("Average period length")).setCellValue("Average period length");
        headerRow.createCell(indexOfColumn("Average cycle length")).setCellValue("Average cycle length");
        headerRow.createCell(indexOfColumn("Sexual activity")).setCellValue("Sexual activity");
        headerRow.createCell(indexOfColumn("Birth controls")).setCellValue("Birth controls");
        headerRow.createCell(indexOfColumn("Weight")).setCellValue("Weight");
        headerRow.createCell(indexOfColumn("Height")).setCellValue("Height");

        //body
        for (HealthInfo health: healthInfos) {
            XSSFRow row = sheet.createRow(rowNum++);
            row.createCell(indexOfColumn("User Id")).setCellValue(health.getUserId());
            row.createCell(indexOfColumn("Average period length")).setCellValue(
                    messagesRetriever.getCaption(health.getAverageCycleLength()));
            row.createCell(indexOfColumn("Typical cycle length")).setCellValue(
                    health.getTypicalCycleLength());
            row.createCell(indexOfColumn("Sexual activity")).setCellValue(
                    messagesRetriever.getCaption(health.getSexualActivity()));
            String bc = health.getBirthControls()
                    .stream()
                    .map((b) -> messagesRetriever.getCaption(b))
                    .collect(Collectors.joining(","));

            row.createCell(indexOfColumn("Birth controls")).setCellValue(bc);
            row.createCell(indexOfColumn("Weight")).setCellValue(MetricUtils.weightToString(health.getWeight(), health.getWeightUnit()));
            row.createCell(indexOfColumn("Height")).setCellValue(MetricUtils.heightToString(health.getHeight(), health.getHeightUnit()));
        }
        for (short i = 0; i < COLUMNS.size(); i++) {
            sheet.autoSizeColumn(i);
        }
        logger.info("Report builded, rowNums: {}", rowNum);
        return workbook;
    }

    private int indexOfColumn(String aCaption) {
        return COLUMNS.indexOf(aCaption);
    }

}
