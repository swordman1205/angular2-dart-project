# Testing strategy
We use the following testing methods,

 * **Unit testing:**
 * **Integration testing:** datastores and external services
 * **Component testing:** in or out of process?
 * **Contract testing:** ensuring consistency across boundaries
 * **End-to-end testing:**

Out test pipeline is as follows.

 * For each project run unit tests and integration tests suites.
 * Deploy solution if all tests are passed in dev environment
 * Run contract tests (testing/service_tests)
 * Run end-to-end tests (testing/website_tests).
 * If all tests passes then deploy application in test environment

## Unit testing
We do unit testing in both Java and Dart projects.

### Java projects
We use JUnit 5 for unit testing and each project must use the following layout
```
src/test/java
src/test/java/com.qurasense.moduleName
src/test/java/com.qurasense.moduleName.utest
src/test/java/com.qurasense.moduleName.utest.cases
```

Thus all unit tests are placed in the **utest.cases** folder in each projects.
The **utest** folder may also contain specific spring setup or mock setup needed
to execute the tests. Further each **utest** folder will also contain a test suite
 named **ModuleNameUnitTestSuite**
```java
@RunWith(JUnitPlatform.class)
@SelectPackages("com.qurasense.moduleName.utest.cases")
public class ModuleNameUnitTestSuite {
}
```

### Dart projects
We use the standard **test** package and each project uses this layout
```
test
test/all_tests.dart
test/utest/all_unit_tests.dart
test/utest/case_1.dart
```

 * **all_tests.dart** is the entry point for executing all tests (unit and integration tests)
 * The **utest** folder contains all the unit tests cases
 * **all_unit_tests.dart** executes all unit tests


## Integration testing
For now we only do integration tests in each Java projects

### Java projects
Integration tests of java projects involves.

 * Testing REST services with only this project running

We use JUnit 5 for integration testing and each project must use the following layout
```
src/test/java
src/test/java/com.qurasense.moduleName
src/test/java/com.qurasense.moduleName.itest
src/test/java/com.qurasense.moduleName.itest.cases
```

Thus all integration tests are placed in the **itest.cases** folder in each projects.
The **itest** folder may also contain specific spring setup or such as a SpringBoot test
application and other tooling needed to execute the tests. Further each **itest** folder
will also contain a test suite named **ModuleNameIntegrationTestSuite**

```java
@RunWith(JUnitPlatform.class)
@SelectPackages("com.qurasense.moduleName.itest.cases")
public class ModuleNameIntegrationTestSuite {
}
```

## End-to-End Testing
We have to types of end-to-end tests, common for both is that they tests the application
as a whole in a  setup where all micro-services are deployed and able to communicate to each other.
The only limit is that we do not communicate with external systems (such as email/sms) but the inter
communication between our own micro-services will be tested.

 * **Service testing:** Smoke tests services
 * **GUI testing:** Smoke tests GUI

### Service testing
Service tests are placed in the **testing/service_tests** project

### GUI testing
GUI tests are placed in the **testing/gui_tests** project

# Links

 * https://martinfowler.com/articles/microservice-testing/#agenda