package com.test.service;

import com.test.model.Rule;
import com.test.model.RuleParameter;
import com.test.rule.IRule;
import com.test.rule.Parameter;
import com.test.service.persistence.RulePersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RuleService {

    @Autowired
    private RulePersistenceService rulePersistenceService;

    public IRule get(long id) {

        return Optional.
                ofNullable(rulePersistenceService.get(id)).
                map(this::transform).
                orElse(null);
    }

    private IRule transform(Rule rule) {

        try {
            String className = rule.getRuleType().getClassName();
            Class clazz = Class.forName(className);

            return IRule.class.cast(clazz.newInstance()).
                    withPriority(rule.getPriority()).
                    withName(rule.getName()).
                    withDescription(rule.getDescription()).
                    withRejectionCode(rule.getRejectionCode().getCode()).
                    withParameter(rule.getRuleParameter().stream().map(this::transform).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Parameter transform(RuleParameter ruleParameter) {

        return Parameter.of(ruleParameter.getField(), ruleParameter.getValue(), ruleParameter.getClause(), ruleParameter.getType(), ruleParameter.isKey());
    }

}
