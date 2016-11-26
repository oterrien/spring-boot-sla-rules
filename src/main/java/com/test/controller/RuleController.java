package com.test.controller;

import com.test.model.Entity;
import com.test.rule.Element;
import com.test.service.EntityLoadAndApplyRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rules")
@Slf4j
public class RuleController {

    @Autowired
    private EntityLoadAndApplyRuleService ruleService;

    @RequestMapping(method = RequestMethod.POST, value = "/loadAndApplyRule")
    @ResponseBody
    public ResponseEntity<List<Element<Entity>>> loadAndApplyRule(@RequestParam(name = "status", defaultValue = "ACCEPTED") Element.Status status,
                                                                  @RequestParam(name = "pageSize", defaultValue = "0") int pageSize,
                                                                  @RequestParam(name = "numberOfThreads", defaultValue = "10") int numberOfThreads) {

        List<Element<Entity>> result = pageSize > 0 ? ruleService.loadAndApplyRules(status, pageSize, numberOfThreads) : ruleService.loadAndApplyRules(status);
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }
}
