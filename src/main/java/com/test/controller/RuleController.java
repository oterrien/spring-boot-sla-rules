package com.test.controller;

import com.test.model.Rule;
import com.test.service.persistence.RulePersistenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rules")
@Slf4j
public class RuleController {

    @Autowired
    private RulePersistenceService rulePersistenceService;

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public ResponseEntity<Rule> get(@PathVariable(name = "id") Long id) {

        return new ResponseEntity<>(rulePersistenceService.get(id), HttpStatus.FOUND);
    }
}
