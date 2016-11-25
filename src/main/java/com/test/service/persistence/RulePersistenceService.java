package com.test.service.persistence;

import com.test.model.Rule;
import com.test.service.persistence.repository.IRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RulePersistenceService {

    @Autowired
    private IRuleRepository ruleRepository;

    public Rule get(Long id) {
        return ruleRepository.findOne(id);
    }

    public List<Rule> getAllSortedByPriority() {

        Sort sort = new Sort(Sort.Direction.ASC, "priority");
        return ruleRepository.findAll(sort);
    }

}
