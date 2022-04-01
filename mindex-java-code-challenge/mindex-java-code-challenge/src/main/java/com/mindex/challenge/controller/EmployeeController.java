package com.mindex.challenge.controller;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee create request for id [{}]", id);

        return employeeService.read(id);
    }

    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee create request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }
    
    /*
     * Reporting Structure for given id
     * returns Employee record (as in /employee/{id} but with numberOfReports - determined on the fly from recursively
     * walking through the DirectReports tree.  Assumes that there are no data issues.
     * 
     * 1. This may be more ideal in its own Controller
     * 2. The getNumberOfReports method would be better off in a Service 
     */
    @GetMapping("/employee/reportingStructure/{id}")
    public ReportingStructure reports(@PathVariable String id) {
        LOG.debug("Received employee reportingStructure request for id [{}]", id);

        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setEmployee(employeeService.read(id));
        reportingStructure.setNumberOfReports(this.getNumberOfReports(0, reportingStructure.getEmployee().getDirectReports()));
        return reportingStructure;
    }

	private int getNumberOfReports(int previousCount, List<Employee> directReports) {
		//LOG.debug("getNumberOfReports - previousCount: [{}], directReports [{}]", previousCount, directReports);
		if (directReports == null || directReports.isEmpty()) {
			return previousCount;
		}

		for (Employee employee : directReports) {
			
			Employee directReportEmployee = employeeService.read(employee.getEmployeeId());
			
			previousCount = getNumberOfReports(previousCount + 1, directReportEmployee.getDirectReports());
		}
		return previousCount;
	}
}
