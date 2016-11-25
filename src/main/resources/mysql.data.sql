insert into REJECTION_CODE(CODE) values ('Rule1');
insert into REJECTION_CODE(CODE) values ('Rule2');
insert into REJECTION_CODE(CODE) values ('Rule3');
commit;

insert into RULE_TYPE(TYPE, CLASS_NAME) values ('GenericRule', 'com.test.rule.GenericRule');
commit;

insert into RULE(PRIORITY, NAME, DESCRIPTION, REJECTION_CODE_ID, RULE_TYPE_ID) values 
(0, 'Rule de Test', 'Cette regle pour tester le bon fonctionnement de JPA',
(select ID from REJECTION_CODE where CODE = 'Rule1'),
(select ID from RULE_TYPE where TYPE = 'GenericRule'));
commit;

insert into RULE_PARAMETER(RULE_ID, FIELD, VALUE, CLAUSE, TYPE, IS_KEY)
values((select id from rule where name = 'Rule de Test'),
'currency', 'EUR', 'EQUALS', 'STRING', false);
insert into RULE_PARAMETER(RULE_ID, FIELD, VALUE, CLAUSE, TYPE, IS_KEY)
values((select id from rule where name = 'Rule de Test'),
'seller', 'Seller_1', 'EQUALS', 'STRING', true);
commit;
