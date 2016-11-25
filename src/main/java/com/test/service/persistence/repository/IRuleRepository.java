package com.test.service.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.test.model.Rule;

public interface IRuleRepository extends JpaRepository<Rule, Long> {
}
