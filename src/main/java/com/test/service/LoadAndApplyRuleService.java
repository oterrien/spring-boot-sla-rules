package com.test.service;

import com.test.model.Rule;
import com.test.model.RuleParameter;
import com.test.rule.Element;
import com.test.rule.IRule;
import com.test.rule.Parameter;
import com.test.service.persistence.RulePersistenceService;
import com.test.service.persistence.repository.IRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
public abstract class LoadAndApplyRuleService<T> {

    @Autowired
    private RulePersistenceService rulePersistenceService;

    @Autowired
    private IRepository<T> entityRepository;

    protected LoadAndApplyRuleService(IRepository<T> entityRepository) {
        this.entityRepository = entityRepository;
    }

    public List<Element<T>> loadAndApplyRules(Element.Status status) {

        List<Element<T>> result = applyRules(entityRepository.findAll(), status);
        log.warn("Number of elements " + status + " : " + result.size());
        return result;
    }

    public List<Element<T>> loadAndApplyRules(Element.Status status, int pageSize, int numberOfThreads) {

        AtomicInteger currentPage = new AtomicInteger(0);
        AtomicInteger numberOfPages = new AtomicInteger(1);

        List<Element<T>> result = Collections.synchronizedList(new ArrayList<>());
        result.addAll(createSupplier(status, pageSize, currentPage.get(), numberOfPages).get());

        if (numberOfPages.get() > 1) {
            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
            try {
                Collection<Future<List<Element<T>>>> taskResult = new ArrayList<>();
                int totalPage = numberOfPages.get();

                while (currentPage.getAndIncrement() < totalPage) {
                    final int curPage = currentPage.get();
                    taskResult.add(executorService.submit(() -> createSupplier(status, pageSize, curPage, null).get()));
                }

                taskResult.parallelStream().allMatch(f -> waitAndGet(f, result));

            } finally {
                executorService.shutdown();
            }
        }
        log.warn("Number of elements " + status + " : " + result.size());
        return result;
    }

    private Supplier<List<Element<T>>> createSupplier(Element.Status status, int pageSize, int currentPage, AtomicInteger numberOfPages){
        return () -> {
            log.info("#####-Load and filter page #" + currentPage);
            PageRequest pageRequest = new PageRequest(currentPage, pageSize);
            Page<T> page = entityRepository.findAll(pageRequest);
            if (numberOfPages != null){
                numberOfPages.set(page.getTotalPages());
            }
            return applyRules(page.getContent(), status);
        };

    }

    private boolean waitAndGet(Future<List<Element<T>>> future, List<Element<T>> results) {
        try {
            results.addAll(future.get());
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Element<T>> applyRules(List<T> entities, Element.Status status) {

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

    private List<IRule> getAllSortedByPriority() {

        return rulePersistenceService.getAllSortedByPriority().
                stream().
                map(this::transform).
                collect(Collectors.toList());
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
                    withParameter(transform(rule.getRuleParameter()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Parameter> transform(List<RuleParameter> ruleParameters) {
        return ruleParameters.stream().map(this::transform).collect(Collectors.toList());
    }

    private Parameter transform(RuleParameter ruleParameter) {

        return Parameter.of(ruleParameter.getField(), ruleParameter.getValue(), ruleParameter.getClause(), ruleParameter.getType(), ruleParameter.isKey());
    }

}
