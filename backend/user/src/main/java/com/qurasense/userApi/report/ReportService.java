package com.qurasense.userApi.report;

import com.qurasense.userApi.model.CustomerInfo;
import com.qurasense.userApi.repository.CustomerRepository;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerExcelExporter customerExcelExporter;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MEDICAL')")
    public XSSFWorkbook exportCustomers() throws IOException {
        List<CustomerInfo> customers = customerRepository.getCustomers();
        return customerExcelExporter.export(customers);
    }
}
