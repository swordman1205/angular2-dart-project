package com.qurasense.userApi.report;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.qurasense.common.DateUtils;
import com.qurasense.common.MessagesRetriever;
import com.qurasense.userApi.model.CustomerInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerExcelExporter {

    private final Logger logger = LoggerFactory.getLogger(CustomerExcelExporter.class);

    @Autowired
    private MessagesRetriever messagesRetriever;

    private static List<String> COLUMNS = Arrays.asList(
            "User Id",
            "Customer Id",
            "Full name",
            "Email",
            "Birth date",
            "Phone",
            "Address",
            "City",
            "State",
            "Zip",
            "Country",
            "Contact Day",
            "Contact Times",
            "Status");

    public XSSFWorkbook export(List<CustomerInfo> customers) throws IOException {
        logger.info("Start build report");

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("customers");
        int rowNum = 0;
        //header
        XSSFRow headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(indexOfColumn("User Id")).setCellValue("User Id");
        headerRow.createCell(indexOfColumn("Customer Id")).setCellValue("Customer Id");
        headerRow.createCell(indexOfColumn("Full name")).setCellValue("Full name");
        headerRow.createCell(indexOfColumn("Email")).setCellValue("Email");
        headerRow.createCell(indexOfColumn("Birth date")).setCellValue("Birth date");
        headerRow.createCell(indexOfColumn("Phone")).setCellValue("Phone");
        headerRow.createCell(indexOfColumn("Address")).setCellValue("Address");
        headerRow.createCell(indexOfColumn("City")).setCellValue("City");
        headerRow.createCell(indexOfColumn("State")).setCellValue("State");
        headerRow.createCell(indexOfColumn("Zip")).setCellValue("Zip");
        headerRow.createCell(indexOfColumn("Country")).setCellValue("Country");
        headerRow.createCell(indexOfColumn("Contact Day")).setCellValue("Contact Day");
        headerRow.createCell(indexOfColumn("Contact Times")).setCellValue("Contact Times");
        headerRow.createCell(indexOfColumn("Status")).setCellValue("Status");

        //body
        for (CustomerInfo customer: customers) {
            XSSFRow row = sheet.createRow(rowNum++);
            row.createCell(indexOfColumn("User Id")).setCellValue(customer.getUserId());
            row.createCell(indexOfColumn("Customer Id")).setCellValue(customer.getId());
            row.createCell(indexOfColumn("Full name")).setCellValue(customer.getFullName());
            row.createCell(indexOfColumn("Email")).setCellValue(customer.getEmail());
            row.createCell(indexOfColumn("Birth date")).setCellValue(customer.getDateOfBirth().toString());
            row.createCell(indexOfColumn("Phone")).setCellValue(customer.getPhone());
            row.createCell(indexOfColumn("Address")).setCellValue(customer.getAddressLine());
            row.createCell(indexOfColumn("City")).setCellValue(customer.getCity());
            row.createCell(indexOfColumn("State")).setCellValue(customer.getState());
            row.createCell(indexOfColumn("Zip")).setCellValue(customer.getZip());
            row.createCell(indexOfColumn("Country")).setCellValue(customer.getCountry());
            row.createCell(indexOfColumn("Contact Day")).setCellValue(messagesRetriever.getCaption(customer.getContactDay()));
            row.createCell(indexOfColumn("Contact Times")).setCellValue(customer.getContactTimes().stream()
                    .map(messagesRetriever::getCaption)
                    .collect(Collectors.joining(","))
            );

            String customerStatusString = StringUtils.EMPTY;
            if (Objects.nonNull(customer.getCustomerStatus())) {
                customerStatusString = String.format("%s(%s)",
                        messagesRetriever.getCaption(customer.getCustomerStatus().getType()),
                        DateUtils.formatDateTime(customer.getCustomerStatus().getTime()));
            }
            row.createCell(indexOfColumn("Status")).setCellValue(customerStatusString);
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
