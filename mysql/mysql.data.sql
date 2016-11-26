insert into REJECTION_CODE(CODE) values ('Rule1');
insert into REJECTION_CODE(CODE) values ('Rule2');
insert into REJECTION_CODE(CODE) values ('Rule3');
commit;

insert into RULE_TYPE(TYPE, CLASS_NAME) values ('GenericRule', 'com.test.rule.GenericRule');
commit;

insert into RULE(PRIORITY, NAME, DESCRIPTION, REJECTION_CODE_ID, RULE_TYPE_ID) values 
(0, 'Rule_1', 'Rule_1',
	(select ID from REJECTION_CODE where CODE = 'Rule1'),
	(select ID from RULE_TYPE where TYPE = 'GenericRule'));
insert into RULE(PRIORITY, NAME, DESCRIPTION, REJECTION_CODE_ID, RULE_TYPE_ID) values 
(1, 'Rule_2', 'Rule_2',
	(select ID from REJECTION_CODE where CODE = 'Rule2'),
	(select ID from RULE_TYPE where TYPE = 'GenericRule'));
commit;

insert into RULE_PARAMETER(RULE_ID, FIELD, VALUE, CLAUSE, TYPE, IS_KEY) values
((select id from rule where name = 'Rule_1'),
	'currency', 'EUR', 'EQUALS', 'STRING', false);
insert into RULE_PARAMETER(RULE_ID, FIELD, VALUE, CLAUSE, TYPE, IS_KEY) values
((select id from rule where name = 'Rule_1'),
	'seller', 'Seller_1', 'EQUALS', 'STRING', true);
insert into RULE_PARAMETER(RULE_ID, FIELD, VALUE, CLAUSE, TYPE, IS_KEY) values
((select id from rule where name = 'Rule_2'),
	'seller', 'Seller_2', 'EQUALS', 'STRING', true);
insert into RULE_PARAMETER(RULE_ID, FIELD, VALUE, CLAUSE, TYPE, IS_KEY) values
((select id from rule where name = 'Rule_2'),
	'debtor', 'Debtor_2', 'DIFFERENT', 'STRING', false);
commit;

truncate table ENTITY;
insert into ENTITY(DEBTOR, COUNTRY, CURRENCY, SELLER, INVOICE_DATE, NUMBER_OF_INVOICES) values
('Debtor_1', 'FR', 'EUR', 'Seller_1', '2016-03-20', 1);
insert into ENTITY(DEBTOR, COUNTRY, CURRENCY, SELLER, INVOICE_DATE, NUMBER_OF_INVOICES) values
('Debtor_1', 'FR', 'EUR', 'Seller_2', '2016-10-09', 2);
insert into ENTITY(DEBTOR, COUNTRY, CURRENCY, SELLER, INVOICE_DATE, NUMBER_OF_INVOICES) values
('Debtor_2', 'UK', 'GBP', 'Seller_1', '2016-10-17', 3);
insert into ENTITY(DEBTOR, COUNTRY, CURRENCY, SELLER, INVOICE_DATE, NUMBER_OF_INVOICES) values
('Debtor_2', 'FR', 'GBP', 'Seller_2', '2016-04-16', 4);
commit;
