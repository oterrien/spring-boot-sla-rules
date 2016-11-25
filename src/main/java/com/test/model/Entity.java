package com.test.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@javax.persistence.Entity
@Table(name = "ENTITY")
public class Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "SELLER")
    private String seller;

    @Column(name = "DEBTOR")
    private String debtor;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "INVOICE_DATE")
    private String invoiceDate;

    @Column(name = "NUMBER_OF_INVOICES")
    private Integer numberOfInvoices;

}
