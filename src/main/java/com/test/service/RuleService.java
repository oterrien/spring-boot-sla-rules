package com.test.service;

import com.test.model.Entity;
import com.test.model.Rule;
import com.test.model.RuleParameter;
import com.test.rule.Element;
import com.test.rule.IRule;
import com.test.rule.Parameter;
import com.test.service.persistence.RulePersistenceService;
import com.test.service.persistence.repository.IEntityRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class RuleService {

    @Autowired
    private RulePersistenceService rulePersistenceService;

    @Autowired
    private IEntityRepository entityRepository;

    public List<Element<Entity>> loadAndApplyRules(Element.Status status) {

        return applyRules(entityRepository.findAll(), status);
    }

    public List<Element<Entity>> loadAndApplyRules(Element.Status status, int pageSize) {

        //region Simulate PAGINATION
        List<Entity> entities = entityRepository.findAll();
        AtomicLong count = new AtomicLong(0);
        Map<Long, List<Entity>> map = entities.
                stream().
                map(e -> new EntityGroup<>(e, count.incrementAndGet() / pageSize)).
                collect(Collectors.groupingBy(EntityGroup::getPageIndex, Collectors.mapping(EntityGroup::getEntity, Collectors.toList())));
        // endregion

       /* Callable<List<Entity>> callable = new Callable<List<Entity>>() {
            @Override
            public List<Entity> call() throws Exception {
                return null;
            }
        };



        Future<List<Element<Entity>> future;*/

        return applyRules(entityRepository.findAll(), status);
    }

    @Data
    @AllArgsConstructor
    private static class EntityGroup<T> {

        private T entity;
        private Long pageIndex;
    }

    public <T> List<Element<T>> applyRules(List<T> entities, Element.Status status) {

        // chain all rules sorted by priority
        Consumer<Element> ruleApplier = getAllSortedByPriority().
                stream().
                map(p -> (Consumer<Element>) p).
                reduce(Consumer::andThen)
                .get();

        return entities.
                parallelStream().
                map(Element::new).
                peek(ruleApplier).
                filter(p -> p.getStatus() == status).
                collect(Collectors.toList());
    }

    public List<IRule> getAllSortedByPriority() {

        return rulePersistenceService.getAllSortedByPriority().
                stream().
                map(this::transform).
                collect(Collectors.toList());
    }

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
