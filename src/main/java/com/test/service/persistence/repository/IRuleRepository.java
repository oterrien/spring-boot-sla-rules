package com.test.service.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.test.model.Rule;
import org.springframework.stereotype.Repository;

@Repository
public interface IRuleRepository extends JpaRepository<Rule, Long> {
}
