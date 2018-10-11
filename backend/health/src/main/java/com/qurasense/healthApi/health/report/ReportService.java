package com.qurasense.healthApi.health.report;

import java.io.IOException;
import java.util.List;

import com.qurasense.healthApi.health.model.HealthInfo;
import com.qurasense.healthApi.health.repository.HealthInfoRepository;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Service
public class ReportService {

    @Autowired
    private HealthInfoRepository healthInfoRepository;

    @Autowired
    private HealthExcelExporter healthExcelExporter;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MEDICAL')")
    public XSSFWorkbook exportHealths() throws IOException {
        List<HealthInfo> list = ofy().load().type(HealthInfo.class).list();
        return healthExcelExporter.export(list);
    }
}
