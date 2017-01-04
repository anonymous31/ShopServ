
CREATE OR REPLACE FUNCTION call_sort_and_insrt_seller()
RETURNS VOID AS 
$$
DECLARE
exist TEXT;
BEGIN
	PERFORM sort_keys();
	PERFORM sort_unsok();
	
        INSERT INTO person(person_status_fk,nick,passwd,salt,email,reg_date,style,person_status_name,is_client,is_employee)
	VALUES(1,'seller01','579d9ec9d0c3d687aaa91289ac2854e4','123','test@test.com',CURRENT_TIMESTAMP,'white','active',true,true);

END
$$ 
LANGUAGE 'plpgsql' SECURITY DEFINER;

CREATE OR REPLACE FUNCTION insrt_amount() RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
	UPDATE catalog SET prod_amount=prod_amount+1 WHERE id=NEW.catalog_fk;

	UPDATE catalog_attribute AS ca SET refcount=refcount+1
	WHERE ca.attribute_value_fk IN (NEW.brand_fk,NEW.resolution_fk,NEW.screen_fk,NEW."Resp time_fk",NEW.color_fk,NEW."Battery Life_fk")
	AND ca.catalog_fk=NEW.catalog_fk;

	RETURN NEW;
END;
$$;
CREATE TRIGGER tr_insrt_prod
AFTER INSERT ON product
FOR EACH ROW
EXECUTE PROCEDURE insrt_amount();




CREATE OR REPLACE FUNCTION insrt_or_append(i_pids INTEGER[],i_key TEXT)
RETURNS VOID AS 
$$
DECLARE
exist TEXT;
BEGIN
        SELECT key INTO exist FROM tbtest2 WHERE key = i_key;
	IF CHAR_LENGTH(exist)>0 THEN
		UPDATE tbtest2 SET value = value || i_pids WHERE key = i_key;
	ELSE
		INSERT INTO tbtest2(key,value) VALUES (i_key, i_pids);
	END IF;

END
$$ 
LANGUAGE 'plpgsql' SECURITY DEFINER;

CREATE OR REPLACE FUNCTION insrt_or_append2(i_pids INTEGER[],i_key TEXT)
RETURNS VOID AS 
$$
DECLARE
exist TEXT;
BEGIN
        SELECT key INTO exist FROM tbtest2 WHERE key = i_key;
	IF CHAR_LENGTH(exist)>0 THEN
		UPDATE tbtest2 SET value = i_pids || value,  total_amount=total_amount+1  WHERE key = i_key;
	ELSE
		INSERT INTO tbtest2(key,value,total_amount) VALUES (i_key, i_pids,1);
	END IF;
END
$$ 
LANGUAGE 'plpgsql' SECURITY DEFINER;

CREATE OR REPLACE FUNCTION sort_keys()
RETURNS VOID AS 
$$
DECLARE
tb_row tbtest2%ROWTYPE;
BEGIN
	FOR tb_row IN
	SELECT *
	FROM tbtest2
	LOOP
		IF tb_row.key LIKE '%price:asc%' THEN
			UPDATE tbtest2 SET value = 
				array(SELECT p.id pid
				FROM product p 
				WHERE p.id IN (SELECT unnest(tb_row.value)  )
				ORDER BY price ASC )
				,total_amount = icount(tb_row.value)
			 WHERE key = tb_row.key;
		END IF;

		IF tb_row.key LIKE '%price:desc%' THEN
			UPDATE tbtest2 SET value = 
				array(SELECT p.id pid
				FROM product p 
				WHERE p.id IN (SELECT unnest(tb_row.value)  )
				ORDER BY price DESC )
				,total_amount = icount(tb_row.value)
			 WHERE key = tb_row.key;
		END IF;
		
		IF tb_row.key NOT LIKE '%price:desc%' AND tb_row.key NOT LIKE '%price:asc%' THEN 
			UPDATE tbtest2 SET total_amount = icount(tb_row.value)
			 WHERE key = tb_row.key;
		END IF;

	END LOOP;
	
END
$$ 
LANGUAGE 'plpgsql' SECURITY DEFINER;


CREATE OR REPLACE FUNCTION sort_unsok()
RETURNS VOID AS 
$$
DECLARE
tb_row1 tbtest2%ROWTYPE;
BEGIN
	SELECT * INTO tb_row1 FROM tbtest2 WHERE key = 'unsubmordersdesc';
	UPDATE tbtest2 SET value = sort_desc(tb_row1.value)
	,total_amount = icount(tb_row1.value)
 	WHERE key = 'unsubmordersdesc';
END
$$ 
LANGUAGE 'plpgsql' SECURITY DEFINER;

CREATE OR REPLACE FUNCTION remove_order(ordId INTEGER)
RETURNS VOID AS 
$$
DECLARE
tb_row tbtest2%ROWTYPE;
BEGIN
	
	UPDATE tbtest2 SET value = ( value - ordId ), total_amount=total_amount-1  WHERE key = 'unsubmordersdesc';
END
$$ 
LANGUAGE 'plpgsql' SECURITY DEFINER;



INSERT INTO person_status(name)
VALUES('active');
INSERT INTO person_status(name)
VALUES('blocked');


INSERT INTO position_type(name)
VALUES('manager');
INSERT INTO position_type(name)
VALUES('seller');
INSERT INTO position_type(name)
VALUES('cleaner');


INSERT  INTO catalog (catalog_level, upper_catalog_fk,title,image,info,longinfo,urlname)
VALUES (1,0,'mobile phones','img/arvutid.png','','','phones');
INSERT  INTO catalog (catalog_level, upper_catalog_fk,title,image,info,longinfo,urlname)
VALUES (2,1,'cell phone','img/lauaarvutid.png','','','cell');
INSERT  INTO catalog (catalog_level, upper_catalog_fk,title,image,info,longinfo,urlname)
VALUES (2,1,'smartphone','img/serverid.png','','','smartphone');
INSERT  INTO catalog (catalog_level, upper_catalog_fk,title,image,info,longinfo,urlname)
VALUES (2,1,'tablet','img/sülearvutid.png','','','tablet');
INSERT  INTO catalog (catalog_level, upper_catalog_fk,title,image,info,longinfo,urlname)
VALUES (1,0,'computer parts','img/komponendid.png','','','parts');
INSERT  INTO catalog (catalog_level, upper_catalog_fk,title,image,info,longinfo,urlname)
VALUES (2,5,'motherboards','img/emaplaadid.png','','','motherboards');
INSERT  INTO catalog (catalog_level, upper_catalog_fk,title,image,info,longinfo,urlname)
VALUES (2,5,'gpu','img/graafikakaardid.png','','','gpu');
INSERT  INTO catalog (catalog_level, upper_catalog_fk,title,image,info,longinfo,urlname)
VALUES (2,5,'audio cards','img/helikaardid.png','','','audio');
INSERT  INTO catalog (catalog_level, upper_catalog_fk,title,image,info,longinfo,urlname)
VALUES (1,0,'monitors','img/monitorid.png','','','monitors');



INSERT INTO attribute(name)
VALUES ('brand');
INSERT INTO attribute(name)
VALUES ('resolution');
INSERT INTO attribute(name)
VALUES ('screen');
INSERT INTO attribute(name)
VALUES ('Resp time');
INSERT INTO attribute(name)
VALUES ('color');
INSERT INTO attribute(name)
VALUES ('Battery Life');



INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'Apple',1);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'Asus',2);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'Datagate',3);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'Dell',4);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'Asrock',5);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'Creative',6);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'Gigabyte',7);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'AOC',8);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'NEC',9);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'Acer',10);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'Samsung',11);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'BenQ',12);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'LG',13);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'PHILIPS',14);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'AsusTest',15);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'Google',16);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'Garmin',17);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'Microsoft',18);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'Nokia',19);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'SONY ERICSSON',20);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'T-Mobile',21);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'Motorola',22);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (1,'HP',23);

/*23*/

INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (2,'400×800',1);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (2,'1024x768',2);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (2,'1280x720',3);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (2,'1280x800',4);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (2,'1920x1080',5);
/*28*/

INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (3,'10''''',1);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (3,'15''''',2);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (3,'17''''',3);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (3,'19''''',4);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (3,'22''''',5);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (3,'27''''',6);
/*34*/
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (4,'5 ms',1);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (4,'6 ms',2);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (4,'7 ms',3);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (4,'8 ms',4);

/*38*/




INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (5,'black',1);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (5,'white',2);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (5,'red',3);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (5,'green',4);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (5,'blue',5);
/*43*/
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (6,'1 hour',1);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (6,'2 hour',2);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (6,'4 hour',3);
INSERT INTO attribute_value(attribute_fk,attributeValue,orderby)
VALUES (6,'666 hour',4);
/*47*/



INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,1,1);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,1,2);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,1,3);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,1,4);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,1,5);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,1,6);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,1,7);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,1,8);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,1,9);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,1,10);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,1,11);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,1,12);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,1,13);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,1,14);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,1,15);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,2,25);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,2,26);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,2,27);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,2,28);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,3,29);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,3,30);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,3,31);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,3,32);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,3,33);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,3,34);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,4,35);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,4,36);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,4,37);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (9,4,38);

INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,1,10);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,1,11);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,1,12);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,1,13);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,1,14);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,1,15);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,1,16);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,1,17);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,1,18);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,1,19);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,1,20);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,1,21);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,1,22);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,1,23);


INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,2,24);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,2,25);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,2,26);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,2,27);

INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,5,39);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,5,40);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,5,41);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,5,42);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,5,43);

INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,6,44);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,6,45);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,6,46);
INSERT INTO catalog_attribute(catalog_fk,attribute_fk,attribute_value_fk)
VALUES (3,6,47);



INSERT INTO shipment_type(ship_date)
VALUES('AT SHOP');
INSERT INTO shipment_type(ship_date)
VALUES('1-2 days');
INSERT INTO shipment_type(ship_date)
VALUES('3-5 days');

INSERT INTO shop(address,name)
VALUES('Sõpruse pst 201/203','Magistrali kaubanduskeskus');
INSERT INTO shop(address,name)
VALUES('Saku 15','KESKLINNA ESINDUS');
INSERT INTO shop(address,name)
VALUES('Peterburi tee 62a / J. Smuuli tee 43','Vesse Maksimarket');
INSERT INTO shop(address,name)
VALUES('on demand','on demand');



INSERT INTO order_status(name)
VALUES('Unpaid');
INSERT INTO order_status(name)
VALUES('Submitted');
INSERT INTO order_status(name)
VALUES('Rejected');
