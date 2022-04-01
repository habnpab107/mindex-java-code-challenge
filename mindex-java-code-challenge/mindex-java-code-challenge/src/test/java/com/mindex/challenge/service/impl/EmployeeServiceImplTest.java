package com.mindex.challenge.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String reportingStructureUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        reportingStructureUrl = "http://localhost:" + port + "/employee/reportingStructure/{id}";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    @Test
    public void testReportingStructure_JL() {
    	
    	String employeeId_JL = "16a596ae-edd3-4847-99fe-c4518e82c86f";
    	
        // Read JL employee
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, employeeId_JL).getBody();
        assertNotNull(readEmployee.getEmployeeId());
        assertEquals(employeeId_JL, readEmployee.getEmployeeId());

        // Read checks
        ReportingStructure reportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, readEmployee.getEmployeeId()).getBody();
        assertEquals(4, reportingStructure.getNumberOfReports());
    }

    @Test
    public void testReportingStructure_PM() {
    	
    	String employeeId_PM = "b7839309-3348-463b-a7e3-5de1c168beb3";
    	
        // Read PM employee
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, employeeId_PM).getBody();
        assertNotNull(readEmployee.getEmployeeId());
        assertEquals(employeeId_PM, readEmployee.getEmployeeId());

        // Read checks
        ReportingStructure reportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, readEmployee.getEmployeeId()).getBody();
        assertEquals(0, reportingStructure.getNumberOfReports());
    }
    

    @Test
    public void testReportingStructure_RS() {
    	
    	String employeeId_RS = "03aa1462-ffa9-4978-901b-7c001562cf6f";
    	
        // Read RS employee
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, employeeId_RS).getBody();
        assertNotNull(readEmployee.getEmployeeId());
        assertEquals(employeeId_RS, readEmployee.getEmployeeId());

        // Read checks
        ReportingStructure reportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, readEmployee.getEmployeeId()).getBody();
        assertEquals(2, reportingStructure.getNumberOfReports());
    }
    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
