CREATE TABLE phonesbook (
  ID int(7) not null,
  Name VARCHAR(20)  NOT NULL,
  Surname VARCHAR(20) NOT NULL,
  Phone VARCHAR(20) NOT NULL,
  CONSTRAINT ID_cont_PK PRIMARY KEY (ID)
);

INSERT INTO phonesbook  VALUES ('1', 'Bob', 'Bobov', '79001231231');
INSERT INTO phonesbook  VALUES ('2', 'Sam', 'Samov', '79007778889');
INSERT INTO phonesbook  VALUES ('3', 'Clarck', 'Clarckov', '79001313131');


DELIMITER $$
DROP PROCEDURE IF EXISTS `getPhone` $$
CREATE PROCEDURE `getPhone`
   (IN  sname VARCHAR(25), OUT sphone VARCHAR(25))
BEGIN
   SELECT phone INTO sphone
   FROM phonesbook
   WHERE NAME = SNAME;
END $$

DROP FUNCTION IF EXISTS concatHello;
DELIMITER $$
CREATE FUNCTION concatHello(text TEXT)
  RETURNS TEXT
  LANGUAGE SQL
BEGIN
  RETURN CONCAT('Hello, ', text);
END;
$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS `hello` $$
CREATE PROCEDURE `hello`
   (IN  text VARCHAR(25), OUT result VARCHAR(25))
BEGIN
   SELECT concatHello(text) INTO result
END $$

