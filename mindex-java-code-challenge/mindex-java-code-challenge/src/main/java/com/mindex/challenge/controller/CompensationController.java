package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    private CompensationService compensationService;
    
    @Autowired
    private EmployeeService employeeService;

    /*
     * Compensation CREATE
     * fields employeeId (String; must exist), salary (double), effectiveDate (java.util.Date)
     * 
     */
    @PostMapping("/compensation")
    public Compensation create(@RequestBody Compensation compensation) {
        LOG.debug("Received Compensation create request for [{}]", compensation);
        
        // flag null employee
        if (compensation.getEmployeeId() == null) throw new RuntimeException("EmployeeId required.");

        // flag invalid employee
        Employee e = employeeService.read(compensation.getEmployeeId());
        
        // date check - the parser catches format, not sure if there are any others required?
        
        // salary check - parser checks that value is double, anything else? don't want richard pryor posting a high value salary,eh?        

        // create record
        return compensationService.create(compensation);
    }

    /*
     * Compensation GET
     * param: employeeId (String; must exist)
     * 
     */
    @GetMapping("/compensation/{id}")
    public Compensation read(@PathVariable String id) {
        LOG.debug("Received Compensation create request for id [{}]", id);
        
        // flag null employee
        if (id == null) throw new RuntimeException("EmployeeId required.");

        // flag invalid employee
        Employee e = employeeService.read(id);

        return compensationService.read(id);
    }
}