package com.test.controller;

import com.test.model.Entity;
import com.test.rule.Element;
import com.test.service.RuleService;
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
    private RuleService ruleService;

    @RequestMapping(method = RequestMethod.POST, value = "/loadAndApplyRule/{status}")
    @ResponseBody
    public ResponseEntity<List<Element<Entity>>> loadAndApplyRule(@PathVariable(name = "status") Element.Status status) {

        return new ResponseEntity<>(ruleService.loadAndApplyRules(status), HttpStatus.FOUND);
    }


}
