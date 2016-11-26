package com.test.service;

import com.test.model.Entity;
import com.test.service.persistence.repository.IEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntityLoadAndApplyRuleService extends LoadAndApplyRuleService<Entity>{

    public EntityLoadAndApplyRuleService(@Autowired IEventRepository entityRepository) {
        super(entityRepository);
    }
}
