package com.qurasense.userApi.controllers;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;

import com.qurasense.userApi.report.ReportService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ReportService reportService;

    @RequestMapping(value = "/report/customers", method = RequestMethod.GET, produces = {"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"})
    @ResponseBody
    public void sendExcelToClient(HttpServletResponse response) throws IOException {
        try (OutputStream outputStream = response.getOutputStream()) {
            XSSFWorkbook workbook = reportService.exportCustomers();
            workbook.write(outputStream);
        } finally {
            response.getOutputStream().close();
        }
    }

}
