package com.qurasense.healthApi.health.controllers;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;

import com.qurasense.healthApi.health.report.ReportService;
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

    @RequestMapping(value = "/report/healths", method = RequestMethod.GET)
    @ResponseBody
    public void sendExcelToClient(HttpServletResponse response) throws IOException {
        try (OutputStream outputStream = response.getOutputStream()) {
            XSSFWorkbook workbook = reportService.exportHealths();
            workbook.write(outputStream);
        } finally {
            response.getOutputStream().close();
        }
    }

}
