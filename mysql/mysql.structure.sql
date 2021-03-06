DROP TABLE RULE_PARAMETER;
DROP TABLE RULE;
DROP TABLE REJECTION_CODE;
DROP TABLE RULE_TYPE;
DROP TABLE ENTITY;

CREATE TABLE REJECTION_CODE ( 
	ID INT(10) NOT NULL AUTO_INCREMENT,
	CODE VARCHAR(20) NOT NULL,
	PRIMARY KEY (ID)
);

CREATE TABLE RULE_TYPE ( 
	ID INT(10) NOT NULL AUTO_INCREMENT,
	TYPE VARCHAR(20) NOT NULL,
	CLASS_NAME VARCHAR(200) NOT NULL,
	PRIMARY KEY (ID)
);

CREATE TABLE RULE ( 
	ID INT(10) NOT NULL AUTO_INCREMENT,
	PRIORITY INT(5) NOT NULL DEFAULT 0,
	NAME VARCHAR(20) NOT NULL,
	DESCRIPTION VARCHAR(500) NOT NULL,
	REJECTION_CODE_ID INT(10) NOT NULL,
	RULE_TYPE_ID INT(10) NOT NULL,
	PRIMARY KEY (ID),
	UNIQUE KEY (NAME),
	FOREIGN KEY (REJECTION_CODE_ID) REFERENCES REJECTION_CODE(ID),
	FOREIGN KEY (RULE_TYPE_ID) REFERENCES RULE_TYPE(ID)
);

CREATE TABLE RULE_PARAMETER ( 
	ID INT(10) NOT NULL AUTO_INCREMENT,
	RULE_ID INT(10) NOT NULL,
	FIELD VARCHAR(200) NOT NULL,
	VALUE VARCHAR(200) NOT NULL,
	CLAUSE VARCHAR(200) NOT NULL,
	IS_KEY BOOLEAN NOT NULL DEFAULT 0,
	TYPE VARCHAR(200) NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (RULE_ID) REFERENCES RULE(ID)
);

CREATE TABLE invoice (
	ID INT(10) NOT NULL AUTO_INCREMENT,
	SELLER VARCHAR(20) NOT NULL,
	DEBTOR VARCHAR(20) NOT NULL,
	COUNTRY VARCHAR(20) NOT NULL,
	CURRENCY VARCHAR(20) NOT NULL,
	INVOICE_DATE DATE NOT NULL,
	NUMBER_OF_INVOICES INT(5) NOT NULL DEFAULT 0,
	PRIMARY KEY (ID)
);





