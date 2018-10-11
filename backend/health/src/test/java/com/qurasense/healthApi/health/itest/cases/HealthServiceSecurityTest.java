package com.qurasense.healthApi.health.itest.cases;

import java.time.LocalDate;
import java.util.Date;

import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.healthApi.HealthApiTestApplication;
import com.qurasense.healthApi.health.model.HealthCard;
import com.qurasense.healthApi.health.model.HealthCardType;
import com.qurasense.healthApi.health.model.HealthInfo;
import com.qurasense.healthApi.health.repository.PeriodRepository;
import com.qurasense.healthApi.health.service.HealthService;
import com.qurasense.healthApi.repository.LocalCardsRepository;
import com.qurasense.healthApi.repository.LocalHealthInfoRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HealthApiTestApplication.class)
@ComponentScan(basePackages = {"com.qurasense.userApi"}/*, excludeFilters=
        {@ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value= EmulatorUserRepository.class)}*/)
public class HealthServiceSecurityTest {

    @Autowired
    private HealthService healthService;

    @Autowired
    private LocalHealthInfoRepository healthInfoRepository;

    @Autowired
    private PeriodRepository periodRepository;

    @Autowired
    private LocalCardsRepository cardRepository;

    @Autowired
    private IdGenerationStrategy idGenerationStrategy;

    @After
    public void tearDown() {
        cardRepository.deleteAll();
    }

    @Test
    @WithMockUser(username="1", authorities = "CUSTOMER")
    public void testUserFindHealth() {
        createHealthInfoWithoutSecurity("1");
        healthService.findHealth("1");
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username="2", authorities = "CUSTOMER")
    public void testWrongUserFindHealth() {
        createHealthInfoWithoutSecurity("1");
        healthService.findHealth("1");
    }

    @Test
    @WithMockUser(username="19", authorities = {"MEDICAL"})
    public void testMedicalFindHealth() {
        createHealthInfoWithoutSecurity("1");
        healthService.findHealth("1");
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username="1", authorities = {"LAB_TECH"})
    public void testLabFindHealth() {
        createHealthInfoWithoutSecurity("1");
        healthService.findHealth("1");
    }

    @Test
    @WithMockUser(username="1", authorities = "CUSTOMER")
    public void testUserUpdateHealth() {
        HealthInfo healthInfo = createHealthInfoWithoutSecurity("1");
        healthInfo.setWeight(57l);
        healthService.saveHealth(healthInfo);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username="2", authorities = "CUSTOMER")
    public void testWrongUserUpdateHealth() {
        HealthInfo healthInfo = createHealthInfoWithoutSecurity("1");
        healthInfo.setWeight(57l);
        healthService.saveHealth(healthInfo);
    }

    @Test
    @WithMockUser(username="19", authorities = {"MEDICAL"})
    public void testMedicalUpdateHealth() {
        HealthInfo healthInfo = createHealthInfoWithoutSecurity("1");
        healthInfo.setWeight(57l);
        healthService.saveHealth(healthInfo);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username="1", authorities = {"LAB_TECH"})
    public void testLabUpdateHealth() {
        HealthInfo healthInfo = createHealthInfoWithoutSecurity("1");
        healthInfo.setWeight(57l);
        healthService.saveHealth(healthInfo);
    }

    @Test
    @WithMockUser(username="1", authorities = "CUSTOMER")
    public void testUserSavePeriod() {
        healthService.savePeriod(1l, new Date());
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username="2", authorities = "CUSTOMER")
    public void testWrongUserSavePeriod() {
        healthService.savePeriod(1l, new Date());
    }

    @Test
    @WithMockUser(username="19", authorities = {"MEDICAL"})
    public void testMedicalSavePeriod() {
        healthService.savePeriod(1l, new Date());
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username="1", authorities = {"LAB_TECH"})
    public void testLabSavePeriod() {
        healthService.savePeriod(1l, new Date());
    }

    @Test
    @WithMockUser(username="1", authorities = "CUSTOMER")
    public void testUserGetLastPeriod() {
        createLastPeriod(1l);
        healthService.getLastPeriod(1l);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username="2", authorities = "CUSTOMER")
    public void testWrongUserGetLastPeriod() {
        createLastPeriod(1l);
        healthService.savePeriod(1l, new Date());
    }

    @Test
    @WithMockUser(username="19", authorities = {"MEDICAL"})
    public void testMedicalGetLastPeriod() {
        createLastPeriod(1l);
        healthService.savePeriod(1l, new Date());
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username="1", authorities = {"LAB_TECH"})
    public void testLabGetLastPeriod() {
        createLastPeriod(1l);
        healthService.savePeriod(1l, new Date());
    }

    @Test
    @WithMockUser(username="1", authorities = {"MEDICAL"})
    public void testMedicialGetCards() {
        healthService.getCards("2");
    }

    @Test
    @WithMockUser(username="1", authorities = {"CUSTOMER"})
    public void testCustomerGetCards() {
        healthService.getCards("1");
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username="1", authorities = {"CUSTOMER"})
    public void testWrongCustomerGetCards() {
        healthService.getCards("2");
    }

    @Test
    @WithMockUser(username="19", authorities = {"MEDICAL"})
    public void testMedicalSaveCard() {
        healthService.saveHealthCard(new HealthCard(idGenerationStrategy.generate(null), "1", HealthCardType.ENRICH_PROFILE, LocalDate.now(), null));
    }

    @Test(expected = IllegalArgumentException.class)
    @WithMockUser(username="19", authorities = {"MEDICAL"})
    public void testMedicialSaveCardTwice() {
        healthService.saveHealthCard(new HealthCard(idGenerationStrategy.generate(null), "1", HealthCardType.ENRICH_PROFILE, LocalDate.now(), null));
        healthService.saveHealthCard(new HealthCard(idGenerationStrategy.generate(null), "1", HealthCardType.ENRICH_PROFILE, LocalDate.now().plusDays(1), "test"));
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username="19", authorities = {"LAB_TECH"})
    public void testSaveCard() {
        healthService.saveHealthCard(new HealthCard(null, "1", HealthCardType.ENRICH_PROFILE, LocalDate.now(), null));
    }

    private HealthInfo createHealthInfoWithoutSecurity(String userId) {
        HealthInfo healthInfo = new HealthInfo();
        healthInfo.setUserId(userId);
        healthInfo.setId(idGenerationStrategy.generate(null));

        healthInfoRepository.save(healthInfo);
        return healthInfo;
    }

    private void createLastPeriod(Long userId) {
        periodRepository.savePeriod(userId, new Date(), new Date());
    }

}
