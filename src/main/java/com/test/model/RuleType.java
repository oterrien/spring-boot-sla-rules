package com.test.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@javax.persistence.Entity
@Table(name = "RULE_TYPE")
public class RuleType{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long  id;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "CLASS_NAME")
    private String className;

    @OneToMany(mappedBy = "ruleType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Rule> rules;
}
