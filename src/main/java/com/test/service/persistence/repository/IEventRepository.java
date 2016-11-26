package com.test.service.persistence.repository;

import com.test.model.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEventRepository extends IRepository<Entity> {
}
